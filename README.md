# ScheduSmart

ScheduSmart ([schedusmart.com](http://schedusmart.com)) is a single-page web application used for generating UWaterloo class schedules.  The app returns optimized results based on the user's provided preferences (morning vs. afternoon classes, days off vs. balanced week...).

The backend is written in Java with the SparkJava web framework.  The frontend is written is AngularJS and designed with Angular Material.  The website is hosted on AWS using the Serverless Application Model (AWS SAM).  The UWaterloo [Open Data API](https://uwaterloo.ca/api/) is used for accessing course information.

Requires UWaterloo API url/key in *src/main/resources/private/config.properties* in the following format:

	uwapikey=[API_KEY]
	uwbaseurl=https://api.uwaterloo.ca/v2/terms

### Hosting Locally

To host ScheduSmart locally, simply run the *deploy-local* bash script.  Use the *-b* option to clean and package with maven before running.

This script hosts the backend using *sam local* (in a Docker instance), and serves the frontend with the python *simplehttpserver* at [http://localhost:8080](http://localhost:8080). 

Requires Maven, the [AWS SAM CLI](https://github.com/awslabs/aws-sam-cli), Docker and Python to be installed.

### Hosting in AWS

To deploy ScheduSmart to AWS, simply run the *deploy-prod* bash script.

This script creates two CloudFormation stacks, comprised of 2 S3 buckets, a Lambda function, and an ApiGateway instance.

Requires Maven and the AWS CLI to be installed.
