import * as cdk from "aws-cdk-lib/core";
import * as apigateway from "aws-cdk-lib/aws-apigateway";
import * as lambda from "aws-cdk-lib/aws-lambda";
import * as dynamodb from "aws-cdk-lib/aws-dynamodb";
import * as logs from "aws-cdk-lib/aws-logs";
import { Construct } from "constructs";

export class TaskStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        // DynamoDB Table
        const tasksTable = new dynamodb.Table(this, "TasksTable", {
            tableName: "tasks",
            partitionKey: {
                name: "id",
                type: dynamodb.AttributeType.STRING
            },
            billingMode: dynamodb.BillingMode.PAY_PER_REQUEST,
            removalPolicy: cdk.RemovalPolicy.RETAIN,
            pointInTimeRecovery: true,
        });

        // Lambda Layer with dependencies (from Maven build)
        const dependenciesLayer = new lambda.LayerVersion(this, "DependenciesLayer", {
            code: lambda.Code.fromAsset("../target/lambda-layer.zip"),
            compatibleRuntimes: [lambda.Runtime.JAVA_21],
            description: "Dependencies layer for Task Management API",
            layerVersionName: "task-api-dependencies",
        });

        // Lambda Function with thin JAR (application code only)
        const tasksLambda = new lambda.Function(this, "TasksLambdaFunction", {
            runtime: lambda.Runtime.JAVA_21,
            handler: "org.task.LambdaHandler::handleRequest",
            code: lambda.Code.fromAsset("../target/lambda-function.jar"),
            functionName: "tasks-api-handler",
            description: "Task Management API Handler - Deployed via CDK",
            layers: [dependenciesLayer],
            environment: {
                TABLE_NAME: tasksTable.tableName,
                ENVIRONMENT: "production",
            },
            timeout: cdk.Duration.seconds(30),
            memorySize: 512,
            logRetention: logs.RetentionDays.ONE_WEEK,
        });

        // Grant Lambda permissions to access DynamoDB
        tasksTable.grantReadWriteData(tasksLambda);

        // Lambda Alias for versioning
        const prodAlias = new lambda.Alias(this, "ProdAlias", {
            aliasName: "prod",
            version: tasksLambda.currentVersion,
            description: "Production alias with auto-versioning",
        });

        // API Gateway
        const api = new apigateway.RestApi(this, "TasksApiGateway", {
            restApiName: "tasks-api",
            description: "Task Management REST API - Deployed via CDK",
            deployOptions: {
                stageName: "prod",
                loggingLevel: apigateway.MethodLoggingLevel.INFO,
                dataTraceEnabled: true,
                metricsEnabled: true,
            },
            defaultCorsPreflightOptions: {
                allowOrigins: apigateway.Cors.ALL_ORIGINS,
                allowMethods: apigateway.Cors.ALL_METHODS,
                allowHeaders: ["Content-Type", "Authorization"],
            },
        });

        // Lambda Integration with Alias
        const integration = new apigateway.LambdaIntegration(prodAlias, {
            proxy: true,
            allowTestInvoke: true,
        });

        // API Routes - /tasks
        const tasksResource = api.root.addResource("tasks");
        tasksResource.addMethod("GET", integration, {
            methodResponses: [{ statusCode: "200" }],
        });
        tasksResource.addMethod("POST", integration, {
            methodResponses: [{ statusCode: "200" }, { statusCode: "400" }],
        });

        // API Routes - /tasks/{task_id}
        const taskIdResource = tasksResource.addResource("{task_id}");
        taskIdResource.addMethod("GET", integration, {
            methodResponses: [{ statusCode: "200" }, { statusCode: "404" }],
        });
        taskIdResource.addMethod("PUT", integration, {
            methodResponses: [{ statusCode: "200" }, { statusCode: "404" }],
        });
        taskIdResource.addMethod("DELETE", integration, {
            methodResponses: [{ statusCode: "200" }, { statusCode: "404" }],
        });

        // CloudFormation Outputs
        new cdk.CfnOutput(this, "ApiUrl", {
            value: api.url,
            description: "API Gateway endpoint URL",
            exportName: "TasksApiUrl",
        });

        new cdk.CfnOutput(this, "ApiId", {
            value: api.restApiId,
            description: "API Gateway ID",
            exportName: "TasksApiId",
        });

        new cdk.CfnOutput(this, "LambdaFunctionName", {
            value: tasksLambda.functionName,
            description: "Lambda function name",
            exportName: "TasksLambdaName",
        });

        new cdk.CfnOutput(this, "LambdaFunctionArn", {
            value: tasksLambda.functionArn,
            description: "Lambda function ARN",
            exportName: "TasksLambdaArn",
        });

        new cdk.CfnOutput(this, "DynamoTableName", {
            value: tasksTable.tableName,
            description: "DynamoDB table name",
            exportName: "TasksTableName",
        });

        new cdk.CfnOutput(this, "ProdAliasArn", {
            value: prodAlias.functionArn,
            description: "Production alias ARN",
            exportName: "TasksProdAliasArn",
        });

        new cdk.CfnOutput(this, "DependenciesLayerArn", {
            value: dependenciesLayer.layerVersionArn,
            description: "Dependencies layer ARN",
            exportName: "TasksDependenciesLayerArn",
        });
    }
}
