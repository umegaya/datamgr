FROM tomcat:7

RUN apt-get update
RUN apt-get -y install mysql-client

ADD tool/datamgr.war /usr/local/tomcat/webapps/datamgr.war
ADD tool/migrate /migrate
ADD run.sh run.sh

CMD ["bash", "run.sh"]
