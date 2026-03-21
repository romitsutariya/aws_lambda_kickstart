#!/bin/bash

set -e  # Exit on any error

echo "=========================================="
echo "Starting Lambda Deployment Process"
echo "=========================================="

# Step 1: Clean and build the project
echo ""
echo "[1/4] Cleaning and building Maven project..."
mvn clean package

# Check if JAR was created
JAR_FILE="target/lambda_jar_1.0.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    exit 1
fi
echo "✓ JAR file created successfully: $JAR_FILE"

# Step 2: Navigate to terraform directory
echo ""
echo "[2/4] Navigating to terraform directory..."
cd terraform

# Step 3: Terraform plan
echo ""
echo "[3/4] Running terraform plan..."
terraform plan

# Step 4: Ask for confirmation before apply
echo ""
read -p "Do you want to apply these changes? (yes/no): " CONFIRM

if [ "$CONFIRM" == "yes" ]; then
    echo ""
    echo "[4/4] Running terraform apply..."
    terraform apply
    echo ""
    echo "=========================================="
    echo "✓ Deployment completed successfully!"
    echo "=========================================="
else
    echo ""
    echo "Deployment cancelled."
    exit 0
fi
