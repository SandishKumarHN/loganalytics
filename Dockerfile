FROM openjdk:8-jdk

USER root

EXPOSE 8090 9999

ENV JOBSERVER_MEMORY=1G

ENV MESOS_VERSION=1.0.0-2.0.89.ubuntu1404

#MESOS
RUN echo "deb http://repos.mesosphere.io/ubuntu/ trusty main" > /etc/apt/sources.list.d/mesosphere.list \
 && apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF \
 && apt-get -y update \
 && apt-get -y install mesos=${MESOS_VERSION} \
 && apt-get clean \
 && apt-get -y install wget \
 && mkdir -p /database

VOLUME /database

#SPARK
ENV SPARK_HOME=/spark
ENV SPARK_VERSION=2.2.0
ENV HADOOP_VERSION=2.7

RUN wget http://www-eu.apache.org/dist/spark/spark-2.2.0/spark-2.2.0-bin-hadoop2.7.tgz
RUN cp spark-2.2.0-bin-hadoop2.7.tgz /opt/
#ADD conf/spark-2.2.0-bin-hadoop2.7.tgz /opt/

RUN ln -s /opt/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} /spark

COPY conf/server_start.sh /app/server_start.sh
COPY conf/server_stop.sh /app/server_stop.sh
COPY conf/manager_start.sh /app/manager_start.sh
COPY conf/setenv.sh /app/setenv.sh
COPY conf/log4j-stdout.properties /app/log4j-server.properties
COPY conf/docker.conf /app/docker.conf
COPY conf/docker.sh /app/settings.sh
#sbt clean +assembly +packArchiveZip to generate spark-jobserver/job-server-extras/target/scala-2.11/spark-job-server.jar
COPY conf/spark-job-server.jar /app/spark-job-server.jar
COPY conf/h2-1.4.197.jar /app/h2-1.4.197.jar
COPY target/scala-2.11/loganalytics_2.11-0.1.jar /app/loganalytics_2.11-0.1.jar
COPY data/apache.access.log /app/apache.access.log
RUN chmod -R 777 /app

ENTRYPOINT ["app/server_start.sh"]