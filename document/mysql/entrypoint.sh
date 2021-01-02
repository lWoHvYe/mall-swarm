#!/bin/bash
# 如果数据目录中有数据的话就不要初始化
[ -d "/var/lib/mysql/mysql" ] || mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
(nohup set-pass.sh &)
(nohup set-replica.sh &)
# 如果要配置AB复制集群，根据Headless Service，主服务器的 podname 一定是以 “-0” 结尾
# 主从服务器配置文件不一样，启动前要配置不同的配置文件。
# 务必要用ConfigMap传不同的配置文件（master.cnf,slave.cnf）到/etc/mysql目录下。
if [ -n "$POD_NAME" ];then
    pod_seq=$(awk -F"[-]" '{print $2}' <<< $POD_NAME)
    server_id=$[pod_seq+1]
    if [ $pod_seq -eq 0 ];then
        sed -r "s/(server.id).*/\1 = $server_id/" /etc/mysql/master.cnf > /etc/my.cnf
    else
        sed -r "s/(server.id).*/\1 = $server_id/" /etc/mysql/slave.cnf > /etc/my.cnf
    fi
fi
mysqld_safe --user=mysql
