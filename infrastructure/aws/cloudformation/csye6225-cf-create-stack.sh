#!/bin/bash 


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


echo "
$csye6225VPC
$csye6225InternetGateway,
$csye6225RouteTable"



echo "Generating cloudFormation template $1-csye6225-cf-networking.yml...."

cat >$1-csye6225-cf-networking.yml<<EOF

Resources:
  $csye6225VPC:
    Type: 'AWS::EC2::VPC'
    Properties:
      CidrBlock: $2
  csye6225Subnet1:
    Type: 'AWS::EC2::Subnet'
    Properties:
      CidrBlock: $3
      VpcId:
        Ref: $csye6225VPC
  csye6225Subnet2:
    Type: 'AWS::EC2::Subnet'
    Properties:
      CidrBlock: $4
      VpcId:
        Ref: $csye6225VPC
  csye6225Subnet3:
    Type: 'AWS::EC2::Subnet'
    Properties:
      CidrBlock: $5
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


EOF


echo "Creating cloudformation stack $1..........."

#aws cloudformation create-stack --stack-name $1 --template-body file://$1-csye6225-cf-networking.yml
