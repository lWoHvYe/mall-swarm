FROM centos
MAINTAINER lWoHvYe <lwohvye@outlook.com>
COPY ["entrypoint.sh","install.sh","set-pass.sh","set-replica.sh","/usr/bin/"]
RUN ["install.sh"]
VOLUME ["/var/lib/mysql"]
EXPOSE 3306
ENTRYPOINT ["entrypoint.sh"]
