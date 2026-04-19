#!/usr/bin/env node
import * as cdk from 'aws-cdk-lib';
import { TaskStack } from '../lib/task-stack';

const app = new cdk.App();

new TaskStack(app, 'TasksApiStack', {
  env: {
    account: process.env.CDK_DEFAULT_ACCOUNT,
    region: process.env.CDK_DEFAULT_REGION || 'us-east-1',
  },
  description: 'Task Management API Stack with Lambda, API Gateway, and DynamoDB',
  tags: {
    Project: 'TaskManagementAPI',
    ManagedBy: 'CDK',
    Environment: 'Production',
  },
});
