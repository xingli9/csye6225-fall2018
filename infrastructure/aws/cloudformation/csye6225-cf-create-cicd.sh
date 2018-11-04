#!/bin/bash





display_usage()
{
echo "Usage:$0 [StackName] [NetworkingStackName]"
}

if [ $# -lt 2 ];then
	display_usage
	exit 1
fi

stackID=$(aws cloudformation create-stack --template-body file://csye6225-cf-cicd.yml --stack-name $1 --capabilities CAPABILITY_IAM| grep StackId)

if [ -z "$stackID" ];then
	echo failed
	exit 1
fi


echo "
Creating CodeDeployApplication........
Creating DeploymentGroup.....
Creating S3Bucket.........
Creating EC2ServiceRole.....
Creating TravisCiUser....
Creating CodeDeployRole....
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
echo "Generating csye6225-cf.conf parameter file"

aws cloudformation describe-stacks --stack-name  $2|grep OutputValue>>/tmp/leonetwork.log

vpc=$(cat /tmp/leonetwork.log| sed -n "1 p" | cut -d'"' -f4)
subnet1=$(cat /tmp/leonetwork.log| sed -n "2 p" | cut -d'"' -f4)
subnet2=$(cat /tmp/leonetwork.log| sed -n "3 p" | cut -d'"' -f4)
subnet3=$(cat /tmp/leonetwork.log| sed -n "4 p" | cut -d'"' -f4)

aws cloudformation describe-stacks --stack-name  $1| grep OutputValue>>/tmp/leocicd.log

secretKey=$(cat /tmp/leocicd.log|sed -n "1 p"| cut -d'"' -f4)
instanceProfile=$(cat /tmp/leocicd.log|sed -n "2 p"| cut -d'"' -f4)
accessKey=$(cat /tmp/leocicd.log|sed -n "3 p"|cut -d'"' -f4)

echo "
$accessKey 
$secretKey"

cat >csye6225-cf.conf<<EOF

[

{
 "ParameterKey": "vpcID",
 "ParameterValue": "$vpc"
},
{
 "ParameterKey": "DBuserName",
 "ParameterValue": "leo"
},
{
 "ParameterKey": "DBpassword",
 "ParameterValue": "leo38377"
},
{
 "ParameterKey": "subnetID1",
 "ParameterValue": "$subnet1"
},
{
 "ParameterKey": "subnetID2",
 "ParameterValue": "$subnet2"
},
{
 "ParameterKey": "subnetID3",
 "ParameterValue": "$subnet3"
},
{
 "ParameterKey": "subnetID",
 "ParameterValue": "$subnet1"
},
{
 "ParameterKey": "KeyName",
 "ParameterValue": "csye6225Leo"
},
{
 "ParameterKey": "HostedZoneName",
 "ParameterValue": "csye6225-fall2018-chengl.me."
},

{
"ParameterKey": "instanceProfile",
"ParameterValue":"$instanceProfile"
}


]


EOF


rm /tmp/leonetwork.log
rm /tmp/leocicd.log


exit 0


