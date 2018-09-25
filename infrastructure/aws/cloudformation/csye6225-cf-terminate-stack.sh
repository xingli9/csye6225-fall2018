#!/bin/bash 


#Created on Sep 24, 2018
#@author: Li

display_usage()
{
echo "Usage:$0 [StackName]"
}



if [ $# -lt 1 ];then 
	display_usage
	exit 1
fi


echo "Deleting stack $1......"

aws cloudformation delete-stack --stack-name $1
status=$(aws cloudformation describe-stacks --stack-name $1 | grep StackStatus| cut -d'"' -f4)

while [ -n "$status" ]
do
	echo "StackStatus:$status"
	sleep 3
	status=$(aws cloudformation describe-stacks --stack-name $1 2>&1 | grep StackStatus| cut -d'"' -f4)
done 


echo "Done!!"




