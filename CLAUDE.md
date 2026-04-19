# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Java 21 AWS Lambda project with a simple string handler. Uses Maven for build and Terraform for infrastructure deployment.

## Build Commands

- **Build project**: `mvn package`
  - Creates shaded JAR at `target/lambda_jar_1.0.jar` for Lambda deployment
- **Run tests**: `mvn test`
- **Run specific test**: `mvn test -Dtest=ClassName#methodName`
- **Clean build**: `mvn clean package`

## Architecture

**Lambda Entry Point**: `LambdaHandler.handleRequest(Object name, Context context)` - Implements `RequestHandler<Object, String>` interface. Simple string handler that formats and returns a greeting message "Hello, {name}!!!".

The architecture has been simplified to a single Lambda handler without service or data access layers.

## AWS Deployment

**Quick Deploy**: `./deploy.sh`
- Automated script that cleans, compiles, packages JAR, runs terraform plan, and applies (with confirmation)

**Manual Steps**:
- Handler: `org.task.LambdaHandler::handleRequest`
- Runtime: java21
- Expects JAR at `../target/lambda_jar_1.0.jar`
- Deploy: `mvn clean package && cd terraform && terraform plan && terraform apply`

**Prerequisites**:
- AWS credentials configured for deployment
- Terraform installed

## Key Dependencies

- AWS Lambda Java Core (1.2.3) for Lambda runtime interfaces
- JUnit 5 for testing
- Maven Shade plugin for creating deployment JAR

## CI/CD

GitHub Actions workflow runs on push/PR to master:
- Builds with Maven on Ubuntu using Java 21
- Runs all tests via `mvn -B package`

print thank you message after every interaction ends