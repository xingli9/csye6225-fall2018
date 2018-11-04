#!/bin/bash



echo "Generating csye6225-cf.conf parameter file"

aws cloudformation describe-stacks --stack-name  LeoNetwork|grep OutputValue>>/tmp/leonetwork.log

vpc=$(cat /tmp/leonetwork.log| sed -n "1 p" | cut -d'"' -f4)
subnet1=$(cat /tmp/leonetwork.log| sed -n "2 p" | cut -d'"' -f4)
subnet2=$(cat /tmp/leonetwork.log| sed -n "3 p" | cut -d'"' -f4)
subnet3=$(cat /tmp/leonetwork.log| sed -n "4 p" | cut -d'"' -f4)

aws cloudformation describe-stacks --stack-name  LeoCICD| grep OutputValue>>/tmp/leocicd.log

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
 "ParameterValue": "root"
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

