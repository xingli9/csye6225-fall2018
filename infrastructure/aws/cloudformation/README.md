CloudFormation Scripts

csye6225-cf-create-stack.sh script generates a aws cloudformation template as ‘[StackName]-csye6225-cf-networking’ in yaml file and uses the template to create a aws VPC (virtual private cloud) with one cidr block and ec2 networking resources including 3 subnets in different availability zone, Internet Gateway, and a routing table which is attached on VPC and associated with three subnets and gateway route. 


Run csye6225-cf-create-stack.sh:

Usage: ./csye6225-cf-create-stack.sh [StackName] [VPC_CidrBlock] [Subnet1] [Subnet2] [Subnet3]

Example: ./csye6225-cf-create-stack.sh Mystack 192.168.0.0/16 192.168.0.0/24 192.168.1.0/24 192.168.2.0/24 

csye6225-cf-terminate-stack.sh run aws cloudformation delete-stack to delete the cloudformation stack, Script will check the stack status and print the current status every two second until make sure the stack is delete_complete.

Run csye6225-cf-terminate-stack.sh:

Usage: ./csye6225-cf-terminate-stack.sh [StackName]


