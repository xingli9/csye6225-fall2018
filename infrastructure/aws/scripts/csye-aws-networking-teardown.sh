#!/bin/bash


associd1=$(cat $1.log |grep -oP 'assocID1 \K\S+')
associd2=$(cat $1.log |grep -oP 'assocID2 \K\S+')
associd3=$(cat $1.log |grep -oP 'assocID3 \K\S+')



echo "$associd1 $associd2 $associd3"
