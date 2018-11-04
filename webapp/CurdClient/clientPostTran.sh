#!/bin/bash



usage()
{
	echo "
	Usage:$0 [URL] [(PostContent).json]
	Example: $0 csye6225-fall2018-chengl.me:8080/csye6225Webapp-1.0-SNAPSHOT/transaction transaction.json
  
	"
}


if [ $# -lt 2 ];then
	usage
        exit 1
fi

url=$1


receipt=$(cat $2 | grep url | cut -d'"' -f4|sed -n '1 p')


receiptURL="myS3Bucket/$receipt"

curl -v -i -H "Content-Type:multipart/form-data" -F "meta-data=@$2" -F "file-data=@$receiptURL" $url





