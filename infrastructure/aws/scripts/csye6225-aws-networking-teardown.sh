#!/bin/bash


display_usage()
{
echo "Usage:$0 [StackName]"
}


if [ $# -lt 1 ];then 
	display_usage
	exit 1
fi

vpcid=$(cat $1.log|grep -oP 'vpcid \K\S+')

if [ -z "$vpcid" ];then  
	echo "Stack log file not found!!"
	exit 1
fi
subnet1=$(cat $1.log|grep -oP 'subnet1 \K\S+')
subnet2=$(cat $1.log|grep -oP 'subnet2 \K\S+')
subnet3=$(cat $1.log|grep -oP 'subnet3 \K\S+')
associd1=$(cat $1.log |grep -oP 'assocID1 \K\S+')
associd2=$(cat $1.log |grep -oP 'assocID2 \K\S+')
associd3=$(cat $1.log |grep -oP 'assocID3 \K\S+')
internetGatewayid=$(cat $1.log |grep -oP 'internetGateway \K\S+')
routetableid=$(cat $1.log |grep -oP 'routetable \K\S+')



echo "Disassociating subnets from routeTable............."
aws ec2 disassociate-route-table --association-id $associd1
if [ $? -ne 0 ];then
	echo "Failed..."
	exit 1
fi
aws ec2 disassociate-route-table --association-id $associd2
if [ $? -ne 0 ];then
        echo "Failed..."
	exit 1
fi
aws ec2 disassociate-route-table --association-id $associd3
if [ $? -ne 0 ];then
        echo "Failed..."
	exit 1
fi
echo "Done!!"



echo "Deleting gateway route from routeTable........."
aws ec2 delete-route --route-table-id $routetableid --destination-cidr-block 0.0.0.0/0
if [ $? -ne 0 ];then
        echo "Failed..."
        exit 1
fi
echo Done!!



echo "Deleting routeTable..............."
aws ec2 delete-route-table --route-table-id $routetableid
if [ $? -ne 0 ];then
        echo "Failed..."
        exit 1
fi
echo Done!!



echo "Detaching internetGateway from VPC............."
aws ec2 detach-internet-gateway --internet-gateway-id $internetGatewayid --vpc-id $vpcid
if [ $? -ne 0 ];then
        echo "Failed..."
        exit 1
fi
echo Done!!





echo "Deleting internetGateway..............."
aws ec2 delete-internet-gateway --internet-gateway-id $internetGatewayid
if [ $? -ne 0 ];then
        echo "Failed..."
        exit 1
fi
echo Done!!



echo "Deleting subnets..............."
aws ec2 delete-subnet --subnet-id $subnet1
if [ $? -ne 0 ];then
        echo "Failed..."
        exit 1
fi

aws ec2 delete-subnet --subnet-id $subnet2
if [ $? -ne 0 ];then
        echo "Failed..."
        exit 1
fi

aws ec2 delete-subnet --subnet-id $subnet3
if [ $? -ne 0 ];then
        echo "Failed..."
        exit 1
fi

echo "Done!!"


echo "Deleting VPC....."
aws ec2 delete-vpc --vpc-id $vpcid 
if [ $? -ne 0 ];then
        echo "Failed..."
        exit 1
fi
echo "Done!!"


echo "Deleting $1.log file......."
rm $1.log 

echo "Delete_Complete!!"
