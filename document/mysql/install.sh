#!/bin/bash
cd /etc/yum.repos.d || exit
cat > mysql-8.0.repo << eof
[mysql8.0]
name=mysql-8.0-community
baseurl=http://repo.mysql.com/yum/mysql-8.0-community/el/8/x86_64/
enabled=1
gpgcheck=1
gpgkey=http://repo.mysql.com/RPM-GPG-KEY-mysql
eof
yum -y install mysql-community-server
yum clean all
rm -rf $0
