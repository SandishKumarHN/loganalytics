# How to run the project #

###Step1: Package the project
```
sbt package

```
###Step2: Compose docker image 
```
docker-compose up --build
```
###Step3: Upload the jar into spark job server
```
curl --data-binary @target/scala-2.11/loganalytics_2.11-0.1.jar 127.0.0.1:8090/jars/geminidata
```
###Step4: Create spark user context to share context between job
``` 
curl -X POST 'localhost:8090/contexts/logs-context'
curl 'localhost:8090/contexts'
```
###Step5: Get or Create RDD. 
``` 
curl -d "" "127.0.0.1:8090/jobs?appName=geminidata&classPath=com.geminidata.logparser.GetorCreateLogs&context=logs-context"
```
###Step6: Run first job (TopIpAddress)  using logs RDD created from step5
``` 
curl -d "" "127.0.0.1:8090/jobs?appName=geminidata&classPath=com.geminidata.logparser.TopIpAddress&context=logs-context"
```
###Step6: Run second job (HitsPerUrl)  using logs RDD created from step5
``` 
curl -d "" "127.0.0.1:8090/jobs?appName=geminidata&classPath=com.geminidata.logparser.HitsPerUrl&context=logs-context"
```

###Final: Open browser at http://127.0.0.1:8090] and check result on each job.

