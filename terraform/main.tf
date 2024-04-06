provider "aws" {
  region = "us-east-1"
}

data "aws_iam_policy_document" "assume_role" {
  statement {
    effect = "Allow"
    principals {
      type        = "Service"
      identifiers = ["lambda.amazonaws.com"]
    }
    actions = ["sts:AssumeRole"]
  }
}

resource "aws_iam_role" "iam_for_lambda" {
  name               = "iam_for_lambda"
  assume_role_policy = data.aws_iam_policy_document.assume_role.json
}
resource "aws_lambda_function" "ytHelloLambda" {
  filename      = "../target/lambda_jar_1.0-SNAPSHOT.jar"
  function_name = "lambda_function_name"
  role          = aws_iam_role.iam_for_lambda.arn
  handler       = "org.example.LambdaHandler::handleRequest"

  runtime = "java21"

  environment {
    variables = {
      env = "production"
    }
  }
}

