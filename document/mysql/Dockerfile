FROM registry.cn-shenzhen.aliyuncs.com/leedon/centos7
MAINTAINER lWoHvYe <lwohvye@outlook.com>
COPY ["entrypoint.sh","install.sh","set-pass.sh","set-replica.sh","/usr/bin/"]
RUN ["install.sh"]
VOLUME ["/var/lib/mysql"]
EXPOSE 3306
ENTRYPOINT ["entrypoint.sh"]
