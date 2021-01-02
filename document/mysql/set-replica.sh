#!/bin/bash
# 如果是AB复制集群，则需要通过downwardAPI获取podname，并赋值给POD_NAME变量
# 如果没有进行上面的步骤，则说明是单节点mysql，本脚本直接退出
[ -z "$POD_NAME" ] && exit
pod_name=$(awk -F"[-]" '{print $1}' <<< $POD_NAME)
pod_seq=$(awk -F"[-]" '{print $2}' <<< $POD_NAME)
password=${MYSQL_ROOT_PASSWORD:-root}
# 当数据库服务启动成功后进行判断，结合 Headless Service，主服务器的 podname 一定是以 “-0” 结尾
# 主服务器执行 GRANT 操作，从服务器执行 CHANGE MASTER 操作
while :;do
    mysql -u root -p$password -e "select 1;" &> /dev/null  &&
    if [ $pod_seq -eq 0 ];then
        mysql -u root -p$password -e "grant replication slave,replication client on *.* to $REPLIC_USER@'$SLAVE_HOSTS' identified by '$REPLIC_PASSWORD';" > /dev/null
        mysql -u root -p$password -e "flush privileges;" > /dev/null
    else
        master_log_file=$(mysql -u $REPLIC_USER -p$REPLIC_PASSWORD -h $pod_name-0.$SVC_NAME -e "show master status\G" |awk '/File/{print $2}')
        master_log_pos=$(mysql -u $REPLIC_USER -p$REPLIC_PASSWORD -h $pod_name-0.$SVC_NAME -e "show master status\G" |awk '/Position/{print $2}')
        mysql -u root -p$password -e "change master to \
                                      master_host='${pod_name}-0.$SVC_NAME', \
                                      master_user='$REPLIC_USER', \
                                      master_password='$REPLIC_PASSWORD', \
                                      master_log_file='$master_log_file', \
                                      master_log_pos=$master_log_pos"
        mysql -u root -p$password -e "start slave;" > /dev/null
    fi && break
    sleep 1
done
