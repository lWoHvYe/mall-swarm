#!/bin/bash
password=${MYSQL_ROOT_PASSWORD:-root}
while :;do
    mysqladmin -u root password $password &> /dev/null && break
    sleep 1
done
rm -rf $0
