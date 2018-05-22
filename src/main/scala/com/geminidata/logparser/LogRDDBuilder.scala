package com.geminidata.logparser

import com.geminidata.logparser.ResponseCode
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

trait LogRDDBuilder {
  val inputLog = "/app/apache.access.log"

  def build(sc: SparkContext): RDD[(ResponseCode, Log)] = {
    sc.textFile(inputLog).
      map(Log.parseRow).
      collect {
        case log => log.responseCode -> log
      }.
      sortByKey(ascending = false)
  }
}
