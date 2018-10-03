#!/bin/bash 



display_usage()
{

echo "Usage:$0 [StackName]"

}


if [ $# -lt 1 ];then
	display_usage
	exit 1
fi


vpc="$1-csye6225-vpc"
internetGateway="$1-csye6225-vpc"
routeTable="$1-csye6225-route-table"




echo "Creating $vpc....."
vpcid=$(aws ec2 create-vpc --cidr-block 192.168.0.0/16 |grep VpcId |cut -d'"' -f4)
echo $vpcid

cat >$1.log<<EOF
vpcid $vpcid
EOF

if [ -z "$vpcid" ];then
	echo failed to create $vpc....
	exit 1
fi
aws ec2 create-tags --resources $vpcid --tags Key=Name,Value=$vpc



subnetid1=$(aws ec2 create-subnet --vpc-id $vpcid --cidr-block 192.168.0.0/24|grep SubnetId|cut -d'"' -f4)
echo $subnetid1
subnetid2=$(aws ec2 create-subnet --vpc-id $vpcid --cidr-block 192.168.1.0/24|grep SubnetId|cut -d'"' -f4)
echo $subnetid2
subnetid3=$(aws ec2 create-subnet --vpc-id $vpcid --cidr-block 192.168.2.0/24|grep SubnetId|cut -d'"' -f4)
echo $subnetid3

cat<<EOF >> $1.log
subnet1 $subnetid1
subnet2 $subnetid2
subnet3 $subnetid3
EOF


echo "Creating InternetGateway..."
internetGatewayid=$(aws ec2 create-internet-gateway|grep InternetGatewayId |cut -d'"' -f4)
echo $internetGatewayid
aws ec2 create-tags --resources $internetGatewayid --tags Key=Name,Value=$internetGateway

cat<<EOF >> $1.log
internetGateway $internetGatewayid
EOF






echo "Attaching Gateway to VPC...."
aws ec2 attach-internet-gateway --internet-gateway-id $internetGatewayid --vpc-id $vpcid
if [ $? != 0 ];then 
	echo "failed to attach gateway to vpc"
	exit 1
fi
echo "Success!"




echo "Creating RouteTable........."
routetableID=$(aws ec2 create-route-table --vpc-id $vpcid|grep RouteTableId |cut -d'"' -f4)
echo $routetableID
echo "Success!!"
aws ec2 create-tags --resources $routetableID --tags Key=Name,Value=$routeTable

cat<<EOF >> $1.log
routetable $routetableID
EOF





echo "Adding GatewayRoute to RouteTable............"
returnSTR=$(aws ec2 create-route --route-table-id $routetableID --destination-cidr-block 0.0.0.0/0 --gateway-id $internetGatewayid|grep true)
if [ -z "$returnSTR" ];then
	echo "Failed to add gateway route to routeTable!"
	exit 1
fi
echo "Success!!"



echo "Adding subnet route to routeTable..........."
assocID1=$(aws ec2 associate-route-table --route-table-id $routetableID --subnet-id $subnetid1| grep AssociationId |cut -d'"' -f4)
echo $assocID1
assocID2=$(aws ec2 associate-route-table --route-table-id $routetableID --subnet-id $subnetid2| grep AssociationId |cut -d'"' -f4)
echo $assocID2
assocID3=$(aws ec2 associate-route-table --route-table-id $routetableID --subnet-id $subnetid3| grep AssociationId |cut -d'"' -f4)
echo $assocID3

echo "Success!!"


cat<<EOF >> $1.log
assocID1 $assocID1
assocID2 $assocID2
assocID3 $assocID3
EOF



exit 0





