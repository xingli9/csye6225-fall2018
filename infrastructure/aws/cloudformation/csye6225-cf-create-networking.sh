#!/bin/bash 


#Created on Sep 24, 2018
#@author: Li


display_usage()
{
echo "Usage:$0 [StackName] [VPC_CidrBlock] [Subnet1] [Subnet2] [Subnet3]"
}

if [ $# -lt 5 ];then	
	display_usage
	exit 1
fi

echo "Creating $1 
VPC cidr block $2 
$3 
$4 
$5
"
csye6225VPC="$1Csye6225Vpc"
csye6225InternetGateway="$1Csye6225InternetGateway"
csye6225RouteTable="$1Csye6225PublicRouteTable"

echo "Generating cloudFormation template '$1-csye6225-cf-networking.yml'....."


cat >csye6225-cf-networking.yml<<EOF

Resources:
  $csye6225VPC:
    Type: 'AWS::EC2::VPC'
    Properties:
      CidrBlock: $2
  csye6225Subnet1:
    Type: 'AWS::EC2::Subnet'
    Properties:
      CidrBlock: $3
      AvailabilityZone: us-east-1a
      VpcId:
        Ref: $csye6225VPC
  csye6225Subnet2:
    Type: 'AWS::EC2::Subnet'
    Properties:
      CidrBlock: $4
      AvailabilityZone: us-east-1b
      VpcId:
        Ref: $csye6225VPC
  csye6225Subnet3:
    Type: 'AWS::EC2::Subnet'
    Properties:
      CidrBlock: $5
      AvailabilityZone: us-east-1c
      VpcId:
        Ref: $csye6225VPC
  $csye6225InternetGateway:
    Type: 'AWS::EC2::InternetGateway'
  VPCGatewayAttachment:
    Type: 'AWS::EC2::VPCGatewayAttachment'
    Properties:
      VpcId: !Ref $csye6225VPC
      InternetGatewayId: !Ref $csye6225InternetGateway
  $csye6225RouteTable:
    Type: 'AWS::EC2::RouteTable'
    Properties:
      VpcId: !Ref $csye6225VPC
  SubnetRouteAssociation1:
    Type: 'AWS::EC2::SubnetRouteTableAssociation'
    Properties:
      RouteTableId: !Ref $csye6225RouteTable
      SubnetId: !Ref csye6225Subnet1
  SubnetRouteAssociation2:
    Type: 'AWS::EC2::SubnetRouteTableAssociation'
    Properties:
      RouteTableId: !Ref $csye6225RouteTable
      SubnetId: !Ref csye6225Subnet2
  SubnetRouteAssociation3:
    Type: 'AWS::EC2::SubnetRouteTableAssociation'
    Properties:
      RouteTableId: !Ref $csye6225RouteTable
      SubnetId: !Ref csye6225Subnet3
  AddGatewayRoute:
    Type: 'AWS::EC2::Route'
    Properties:
      RouteTableId: !Ref $csye6225RouteTable
      DestinationCidrBlock: 0.0.0.0/0
      GatewayId: !Ref $csye6225InternetGateway

Outputs:
  VPCId:
    Description: vpcID
    Value: !Ref $csye6225VPC
  SubnetID1:
    Description: SubnetID_1
    Value: !Ref csye6225Subnet1
  SubnetID2:
    Description: SubnetID_2
    Value: !Ref csye6225Subnet2
  SubnetID3:
    Description: SubnetID_3
    Value: !Ref csye6225Subnet3

EOF


echo "Creating cloudformation stack $1..........."
echo "Creating Resource $csye6225VPC......."
echo "Creating Resource csye6225Subnet1 csye6225Subnet2 csye6225Subnet3......"
echo "Creating Resource $csye6225InternetGateway........"
echo "Creating Resource $csye6225RouteTable......."

stackID=$(aws cloudformation create-stack --stack-name $1 --template-body file://csye6225-cf-networking.yml| grep StackId) 

if [ -z "$stackID" ];then 
	echo "Falied to create stack $1"
	exit 1
fi


status=$(aws cloudformation describe-stacks --stack-name  $1| grep StackStatus| cut -d'"' -f4)


while [ "$status" != "CREATE_COMPLETE" ]
do

       echo "StackStatus: $status"

       if [ "$status" == "ROLLBACK_COMPLETE" ];then 
	       echo "$1 Stack_Create_Uncomplete !!"
	       exit 1
       fi

       sleep 3
       status=$(aws cloudformation describe-stacks --stack-name  $1 2>&1 | grep StackStatus| cut -d'"' -f4)

done

echo "$1 Stack_Create_Complete !!"

exit 0



