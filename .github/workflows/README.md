# GitHub Actions Workflows

## cdk-deploy.yml - Infrastructure as Code Deployment

A fully automated workflow that builds the Java Lambda and deploys all infrastructure using AWS CDK.

### Key Principle: Infrastructure as Code

**All AWS resources are managed by CDK** - the workflow does NOT create any AWS components directly. This ensures:
- ✅ Reproducible deployments
- ✅ Version-controlled infrastructure
- ✅ Easy rollbacks via git history
- ✅ No manual AWS console changes
- ✅ Drift detection

### Triggers

#### 1. Automatic (Push to master)
```yaml
on:
  push:
    branches: [ master ]
```
- Automatically builds JAR and deploys via CDK
- Updates existing stack or creates new one

#### 2. Pull Requests
```yaml
on:
  pull_request:
    branches: [ master ]
```
- Builds and tests Java code
- Does NOT deploy (validation only)

#### 3. Manual Dispatch
```yaml
on:
  workflow_dispatch:
```
- Trigger manually from GitHub Actions UI
- Useful for hotfixes or re-deployments

### Workflow Jobs

#### Job 1: build-java
**Always runs** for all trigger types

1. ✅ Checkout code
2. ✅ Set up Java 21 with Maven cache
3. ✅ Run `mvn clean package`
4. ✅ Upload JAR artifact

**Duration:** ~2-3 minutes (with cache)

#### Job 2: cdk-deploy
**Runs only for** push to master or manual dispatch

**Infrastructure Deployment:**
1. Download JAR from build job
2. Set up Node.js with npm cache
3. Install CDK dependencies
4. Configure AWS credentials
5. Install AWS CDK CLI
6. Bootstrap CDK (first-time only)
7. Synthesize CloudFormation template
8. Show diff of changes
9. Deploy stack
10. Save outputs (API URL, ARNs, etc.)
11. Run smoke test
12. Display deployment summary

**Duration:** ~3-5 minutes (stack update), ~5-10 minutes (new stack)

### AWS Resources Managed by CDK

The CDK stack (`Infrastructure/lib/task-stack.ts`) creates and manages:

#### 1. DynamoDB Table
```typescript
- Table name: "tasks"
- Partition key: "id" (String)
- Billing: Pay-per-request
- Backup: Point-in-time recovery enabled
- Deletion: RETAIN (data preserved on stack delete)
```

#### 2. Lambda Function
```typescript
- Name: "tasks-api-handler"
- Runtime: Java 21
- Handler: org.example.LambdaHandler::handleRequest
- Memory: 512 MB
- Timeout: 30 seconds
- Logs: Retained for 1 week
- Permissions: Auto-granted DynamoDB read/write
```

#### 3. Lambda Alias
```typescript
- Name: "prod"
- Purpose: Version management and rollback
- Auto-updates: Points to latest version
```

#### 4. API Gateway
```typescript
- Name: "tasks-api"
- Stage: "prod"
- CORS: Enabled
- Logging: INFO level
- Metrics: Enabled
- Routes:
  - GET    /tasks          (List)
  - POST   /tasks          (Create)
  - GET    /tasks/{id}     (Read)
  - PUT    /tasks/{id}     (Update)
  - DELETE /tasks/{id}     (Delete)
```

#### 5. IAM Roles & Policies
```typescript
- Lambda execution role (auto-created by CDK)
- DynamoDB access policies (auto-granted)
- CloudWatch logging permissions (auto-granted)
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `AWS_REGION` | us-east-1 | Deployment region |
| `JAVA_VERSION` | 21 | Java version |
| `NODE_VERSION` | 18 | Node.js for CDK |

### Required Secrets

Configure these in GitHub repository settings → Secrets → Actions:

```
AWS_ACCESS_KEY_ID       - AWS access key with CDK permissions
AWS_SECRET_ACCESS_KEY   - AWS secret access key
AWS_ACCOUNT_ID          - Your AWS account ID (for bootstrap)
```

### Required AWS Permissions

The AWS credentials need the following permissions:
- CloudFormation (full)
- Lambda (full)
- API Gateway (full)
- DynamoDB (full)
- IAM (role creation and policy attachment)
- CloudWatch Logs (full)
- S3 (CDK asset bucket)

**Recommended:** Use AWS PowerUserAccess or AdministratorAccess for CDK deployments.

### Deployment Flow

```
┌─────────────────────────────────────────────────────────┐
│  Push to master / Manual trigger                        │
└──────────────────┬──────────────────────────────────────┘
                   ▼
┌─────────────────────────────────────────────────────────┐
│  Job 1: Build Java Lambda                              │
│  • Checkout code                                        │
│  • Setup Java 21                                        │
│  • mvn clean package                                    │
│  • Upload lambda_jar_1.0.jar                           │
└──────────────────┬──────────────────────────────────────┘
                   ▼
┌─────────────────────────────────────────────────────────┐
│  Job 2: CDK Deploy (if not PR)                         │
│  • Download JAR                                         │
│  • Setup Node.js                                        │
│  • Install CDK dependencies                             │
│  • Configure AWS credentials                            │
│  • cdk bootstrap (if needed)                           │
│  • cdk synth (generate CloudFormation)                 │
│  • cdk diff (show changes)                             │
│  • cdk deploy (create/update stack)                    │
│    ├─ DynamoDB Table                                   │
│    ├─ Lambda Function                                  │
│    ├─ Lambda Alias (prod)                              │
│    ├─ API Gateway                                      │
│    └─ IAM Roles & Policies                             │
│  • Save outputs (API URL, ARNs)                        │
│  • Smoke test (GET /tasks)                             │
│  • Display summary                                      │
└─────────────────────────────────────────────────────────┘
```

### CDK Advantages Over Manual AWS CLI

#### Before (Manual AWS CLI in Workflow):
```yaml
- Create IAM role manually
- Create Lambda function manually
- Attach policies manually
- Create API Gateway manually
- Link integrations manually
- Create DynamoDB table manually
- No version control of infrastructure
- Hard to reproduce
- Difficult to rollback
```

#### After (CDK):
```yaml
- All infrastructure in code
- Version controlled
- Repeatable deployments
- Easy rollbacks (git revert + redeploy)
- Automatic dependency management
- Change preview (cdk diff)
- Type-safe configuration
```

### Stack Outputs

After deployment, CDK exports these values (saved to `outputs.json`):

```json
{
  "TasksApiStack": {
    "ApiUrl": "https://xxxxx.execute-api.us-east-1.amazonaws.com/prod/",
    "ApiId": "xxxxx",
    "LambdaFunctionName": "tasks-api-handler",
    "LambdaFunctionArn": "arn:aws:lambda:us-east-1:xxx:function:tasks-api-handler",
    "DynamoTableName": "tasks",
    "ProdAliasArn": "arn:aws:lambda:us-east-1:xxx:function:tasks-api-handler:prod"
  }
}
```

### Usage Examples

#### Example 1: Normal Development
```bash
# Make code changes
vim src/main/java/org/example/handler/CreateTaskHandler.java

# Commit and push
git add .
git commit -m "Update create handler"
git push origin master

# ✅ Automatically:
# - Builds JAR
# - Updates Lambda code
# - No infrastructure changes (stack update)
```

#### Example 2: Add New API Route
```bash
# Update CDK stack
vim Infrastructure/lib/task-stack.ts

# Add new route
const searchResource = tasksResource.addResource("search");
searchResource.addMethod("GET", integration);

# Commit and push
git add .
git commit -m "Add search endpoint"
git push origin master

# ✅ Automatically:
# - Builds JAR
# - Updates API Gateway (adds new route)
# - Updates Lambda
```

#### Example 3: Change DynamoDB Settings
```bash
# Update CDK stack
vim Infrastructure/lib/task-stack.ts

# Change billing mode
billingMode: dynamodb.BillingMode.PROVISIONED,
readCapacity: 5,
writeCapacity: 5,

# Commit and push
git push origin master

# ✅ Automatically:
# - Shows diff (cdk diff)
# - Updates DynamoDB table
# - No downtime
```

### Smoke Test

The workflow includes automatic smoke testing:

```bash
# After deployment, tests:
1. Extract API URL from CDK outputs
2. Send GET request to /tasks
3. Verify 200 status code
4. Fail deployment if test fails
```

**Customize in workflow:**
```yaml
- name: Smoke Test
  run: |
    API_URL=$(jq -r '.TasksApiStack.ApiUrl' outputs.json)
    curl -f "${API_URL}tasks" || exit 1
```

### Rollback Strategy

#### Rollback Code Changes
```bash
# Revert last commit
git revert HEAD
git push origin master

# ✅ Triggers deployment with previous code
```

#### Rollback Infrastructure Changes
```bash
# Option 1: Git revert
git revert <commit-with-infrastructure-change>
git push origin master

# Option 2: Manual (if needed)
cd Infrastructure
cdk deploy --previous-version
```

### Local Development

Test CDK changes locally before pushing:

```bash
# Build JAR first
mvn clean package

# Navigate to Infrastructure
cd Infrastructure

# Install dependencies
npm install

# Synthesize (generates CloudFormation)
cdk synth

# Show what will change
cdk diff

# Deploy to your AWS account
cdk deploy

# Destroy stack (be careful!)
cdk destroy
```

### Monitoring Deployments

#### GitHub Actions
1. Go to repository → Actions tab
2. See all workflow runs
3. Click run for detailed logs
4. Download `cdk-outputs` artifact

#### AWS CloudFormation Console
1. CloudFormation → Stacks
2. Find `TasksApiStack`
3. View events, resources, outputs
4. Monitor deployment progress

#### AWS CDK Toolkit
```bash
# List stacks
cdk list

# Show stack resources
aws cloudformation describe-stack-resources \
  --stack-name TasksApiStack

# Show outputs
aws cloudformation describe-stacks \
  --stack-name TasksApiStack \
  --query 'Stacks[0].Outputs'
```

### Troubleshooting

#### Build Fails
```bash
# Check Maven logs in workflow
# Common issues:
- Dependency resolution
- Test failures
- Compilation errors

# Fix locally:
mvn clean package
```

#### CDK Deploy Fails
```bash
# Check CDK logs in workflow
# Common issues:
- Permission denied
- Resource conflicts
- Stack in progress

# Check stack status:
aws cloudformation describe-stacks \
  --stack-name TasksApiStack
```

#### Smoke Test Fails
```bash
# Check Lambda logs:
aws logs tail /aws/lambda/tasks-api-handler --follow

# Test manually:
curl https://YOUR-API-URL/prod/tasks

# Check API Gateway logs in CloudWatch
```

### Best Practices

1. **Always test locally** before pushing
   ```bash
   mvn clean package
   cd Infrastructure && cdk diff
   ```

2. **Use Pull Requests** for infrastructure changes
   - Review CDK code changes
   - Validate before merge

3. **Monitor CloudWatch** after deployment
   - Lambda logs
   - API Gateway metrics
   - DynamoDB metrics

4. **Use CDK diff** to preview changes
   ```bash
   cdk diff
   ```

5. **Tag important releases**
   ```bash
   git tag -a v1.0.0 -m "Production release"
   git push origin v1.0.0
   ```

6. **Keep CDK dependencies updated**
   ```bash
   cd Infrastructure
   npm update
   ```

### CDK Bootstrap (First Time)

The first deployment requires CDK bootstrap:

```bash
# Bootstrap your AWS account/region
cdk bootstrap aws://ACCOUNT-ID/REGION

# Or let the workflow do it automatically
# (included in cdk-deploy.yml)
```

This creates:
- S3 bucket for CDK assets
- IAM roles for CloudFormation
- ECR repository (if using Docker)

### Cost Optimization

CDK stack uses cost-effective services:

- **DynamoDB:** Pay-per-request (no idle charges)
- **Lambda:** Pay-per-invocation (free tier: 1M requests/month)
- **API Gateway:** Pay-per-request (free tier: 1M requests/month)
- **CloudWatch Logs:** 1 week retention (low cost)

**Estimated monthly cost:** $0-5 for low traffic

### Security Best Practices

1. **Secrets Management**
   - Store AWS credentials as GitHub secrets
   - Never commit credentials to git
   - Rotate keys regularly

2. **IAM Least Privilege**
   - CDK creates minimal IAM roles
   - Only necessary permissions granted

3. **API Security** (Add these to CDK):
   ```typescript
   // Add API key
   const apiKey = api.addApiKey('ApiKey');
   
   // Add usage plan
   const plan = api.addUsagePlan('UsagePlan', {
     apiStages: [{ stage: api.deploymentStage }],
   });
   plan.addApiKey(apiKey);
   ```

4. **Enable WAF** (for production):
   ```typescript
   import * as wafv2 from 'aws-cdk-lib/aws-wafv2';
   // Add WAF rules
   ```

### Migration from Old Workflow

**Removed:**
- Manual IAM role creation
- Manual Lambda creation
- Manual API Gateway setup
- All AWS CLI commands in workflow

**Benefits:**
- Infrastructure versioned in git
- Reproducible across accounts
- Easy to add new resources
- Automatic dependency management
- No workflow-embedded AWS commands

### Future Enhancements

- [ ] Multi-environment support (dev/staging/prod)
- [ ] Custom domain names
- [ ] API authentication (Cognito/API Key)
- [ ] Lambda layers for dependencies
- [ ] X-Ray tracing
- [ ] Alarms and monitoring
- [ ] Automated testing before deploy
- [ ] Blue/green deployments
