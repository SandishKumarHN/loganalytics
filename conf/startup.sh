#!/usr/bin/env bash
#docker build -t job_server_docker .
#docker run --hostname=localhost --privileged=true -t -i -p 8090:8090 -p 9999:9999 job_server_docker

cd /app

curl --data-binary @target/geminidata_2.11-0.1.jar 127.0.0.1:8090/jars/geminidata
curl -X POST 'localhost:8090/contexts/logs-context'
curl 'localhost:8090/contexts'

#http://172.21.0.2:34757
#curl -d "" "127.0.0.1:8090/jobs?appName=geminidata&classPath=com.geminidata.logparser.GetorCreateLogs&context=logs-context"
#curl -d "" "127.0.0.1:8090/jobs?appName=geminidata&classPath=com.geminidata.logparser.TopIpAddress&context=logs-context"
#curl -d "" "127.0.0.1:8090/jobs?appName=geminidata&classPath=com.geminidata.logparser.HitsPerUrl&context=logs-context"
#docker cp job-server-tests/target/scala-2.11/job-server-tests_2.11-0.8.1-SNAPSHOT.jar c8d21f8d2f17:/app/
