#!/bin/bash


display_usage()
{
echo "Usage:$0 [StackName]"
}

if [ $# -lt 1 ];then
	display_usage
	exit 1
fi

stackID=$(aws cloudformation create-stack --template-body file://csye6225-cf-serverless.yml --stack-name $1 | grep StackId)

if [ -z "$stackID" ];then 
	echo failed
	exit 1
fi


echo " 
Creating SNStopic.........
Creating LambdaInvokePermission.............
"

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
































