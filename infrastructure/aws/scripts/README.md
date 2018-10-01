AWS cli Shell Script

csye6225-aws-networking-setup.sh script will automatically create a aws VPC(virtual private cloud) with one cidr block and ec2 networking resources including 3 subnets in different availability zone, Internet Gateway, and a routing table which is attached on VPC and associated with three subnets and gateway route. This setup script generates a log file as [StackName].log which contains all the resources ID for feature use or teardown.


Run csye6225-aws-networking-setup.sh :
Usage: ./csye6225-aws-networking-setup.sh [StackName]


csye6225-aws-networking-teardown.sh will delete all networking resources using AWS CLI. Teardown script will read all resources ID form [StackName].log and delete them by using AWC CLI, so make sure you provide correct StackName.



Run csye6225-aws-networking-setup.sh
Usage: ./csye6225-aws-networking-setup.sh [StackName]

