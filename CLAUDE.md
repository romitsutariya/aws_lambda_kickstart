# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**task-management-serverless** - Production-ready serverless Task Management REST API built with Java 21, AWS Lambda, API Gateway, and DynamoDB. Infrastructure managed as code using AWS CDK with automated CI/CD via GitHub Actions.

## Build Commands

- **Build project**: `mvn package`
  - Creates shaded JAR at `target/lambda_jar_1.0.jar` for Lambda deployment
- **Run tests**: `mvn test`
- **Run specific test**: `mvn test -Dtest=ClassName#methodName`
- **Clean build**: `mvn clean package`

## Architecture

Follows **Clean Architecture** with separation of concerns:
- **Domain Layer** (`org.task.domain`): Task entity, TaskStatus enum
- **Service Layer** (`org.task.service`): DynamoService for database operations
- **Handler Layer** (`org.task.handler`): Individual handlers for each CRUD operation (Create, Get, List, Update, Delete)
- **Router** (`org.task.LambdaHandler`): Main entry point that routes API Gateway requests to appropriate handlers

**Lambda Entry Point**: `org.task.LambdaHandler::handleRequest` - Implements `RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>` for API Gateway proxy integration.

## AWS Deployment

**Infrastructure as Code**: All AWS resources (Lambda, API Gateway, DynamoDB, IAM) managed via AWS CDK.

**Automated Deployment**:
- Push to `master` branch triggers GitHub Actions workflow
- Workflow builds JAR and deploys via `cdk deploy`
- No manual AWS console changes needed

**Manual Local Deployment**:
```bash
mvn clean package
cd Infrastructure
cdk diff      # Preview changes
cdk deploy    # Deploy stack
```

**Prerequisites**:
- AWS credentials configured
- Node.js 25+ and AWS CDK CLI installed (`npm install -g aws-cdk`)
- GitHub Secrets: `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_ACCOUNT_ID`

## Key Dependencies

- AWS Lambda Java Core (1.2.3) & Events (3.16.1) for Lambda runtime
- AWS SDK v2 DynamoDB Enhanced Client for database operations
- Jackson (2.17.0) for JSON serialization with Java 8 date/time support
- JUnit 5 for testing
- Maven Shade plugin for creating deployment JAR
- Checkstyle & Spotless for code quality

## CI/CD

**GitHub Actions Workflow** (`.github/workflows/cdk-deploy.yml`):

**On Push to master**:
1. Build Java Lambda with Maven (Java 21)
2. Deploy via CDK (creates/updates DynamoDB, Lambda, API Gateway)
3. Run smoke tests against deployed API

**On Pull Request**:
- Build and test only (no deployment)

**Stack Outputs**: API URL, Lambda ARN, DynamoDB table name saved as artifacts

print thank you message after every interaction ends