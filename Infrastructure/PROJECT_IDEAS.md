# Simple Project Ideas for AWS Lambda + CDK

## Project 1: Task Manager API
**Goal**: Build a serverless REST API for managing tasks

**Requirements**:
- Lambda function to handle CRUD operations (Create, Read, Update, Delete tasks)
- DynamoDB table to store tasks (id, title, description, status, createdAt)
- API Gateway to expose REST endpoints
- Deploy everything using CDK

**Endpoints**:
- POST /tasks - Create a new task
- GET /tasks - List all tasks
- GET /tasks/{id} - Get specific task
- PUT /tasks/{id} - Update task
- DELETE /tasks/{id} - Delete task

**Tech Stack**: Java 21, AWS Lambda, DynamoDB, API Gateway, CDK

---

## Project 2: URL Shortener Service
**Goal**: Create a simple URL shortener like bit.ly

**Requirements**:
- Lambda to generate short codes and store URL mappings
- DynamoDB table (shortCode, originalUrl, createdAt, clickCount)
- API Gateway for REST endpoints
- Lambda for redirecting short URLs to original URLs

**Endpoints**:
- POST /shorten - Create short URL
- GET /{shortCode} - Redirect to original URL
- GET /stats/{shortCode} - Get click statistics

**Tech Stack**: Java 21, AWS Lambda, DynamoDB, API Gateway, CDK

---

## Project 3: File Upload Service
**Goal**: Serverless file upload and processing system

**Requirements**:
- S3 bucket for file storage
- Lambda to generate pre-signed upload URLs
- Lambda triggered by S3 events to process uploaded files
- SQS queue for processing jobs
- DynamoDB to track file metadata

**Features**:
- Generate secure upload URLs
- Auto-process files when uploaded (validation, metadata extraction)
- Store file info in database
- Send notifications via SQS

**Tech Stack**: Java 21, AWS Lambda, S3, SQS, DynamoDB, CDK

---

## Project 4: Weather Alert System (Simplest)
**Goal**: Scheduled Lambda that checks weather and sends alerts

**Requirements**:
- Lambda function that runs on schedule (EventBridge/CloudWatch)
- Fetch weather from free API (OpenWeatherMap)
- SNS topic to send email alerts
- DynamoDB to store user preferences (location, alert thresholds)

**Features**:
- Check weather every hour
- Send email if temperature > threshold
- Store alert history

**Tech Stack**: Java 21, AWS Lambda, SNS, DynamoDB, EventBridge, CDK

---

## Recommendation: Start with Project 4 (Weather Alert System)
**Why?**: 
- Simple and achievable
- Covers multiple AWS services
- No complex API endpoints
- Easy to test and see results
- Good learning foundation

**Next Steps**:
1. Set up EventBridge rule in CDK
2. Create Lambda handler for weather checking
3. Add SNS topic for notifications
4. Add DynamoDB table for preferences
5. Deploy and test!
