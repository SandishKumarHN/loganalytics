package com.geminidata.logparser

import org.apache.spark.SparkContext
import SparkContext._
import com.geminidata.logparser.{Log, LogRDDBuilder, ResponseCode}
import com.typesafe.config.Config
import org.apache.spark.rdd.RDD


trait LogSparkJob extends  spark.jobserver.SparkJob  with spark.jobserver.NamedRddSupport with LogRDDBuilder {
  val rddName = "logs"

  def validate(sc: SparkContext, config: Config): spark.jobserver.SparkJobValidation = spark.jobserver.SparkJobValid

}

/**
  * job to get or create logs rdd
  */
object GetorCreateLogs extends LogSparkJob {
  override def runJob(sc: SparkContext, jobConfig: Config)= {
    val logs: RDD[(ResponseCode, Log)] = namedRdds.getOrElseCreate(
      rddName,
      build(sc)
    )

    logs.take(5)
  }
}

/**
  * job to get top ip address using shared rdd
  */
object TopIpAddress extends LogSparkJob {
  override def runJob(sc: SparkContext, jobConfig: Config) = {
    val logs: Option[RDD[(ResponseCode, Log)]] = namedRdds.get(rddName)
    val finalResult = logs
      .map { rdd=>
        val newRDD = rdd.map(rdd=> rdd._2)
          .map((log=>(log.ip,1)))
          .reduceByKey(_ + _)
          .filter(_._2 > 10)
          .map(_._1)

        newRDD.take(100)
      }.getOrElse(throw new IllegalStateException(s"RDD [$rddName] does not exist"))

    (finalResult)
  }

}

/**
  * job to get top hits per url using shared rdd
  */
object HitsPerUrl extends LogSparkJob {
  override def runJob(sc: SparkContext, jobConfig: Config)= {
    val logs: Option[RDD[(ResponseCode, Log)]] = namedRdds.get(rddName)
    val finalResult = logs
      .map { rdd=>
        val newRDD = rdd.map(rdd=> rdd._2)
          .map(log => (log.ip, 1))
          .reduceByKey(_ + _)

        newRDD.take(100)
      }.getOrElse(throw new IllegalStateException(s"RDD [$rddName] does not exist!"))

    (finalResult)
  }
}
