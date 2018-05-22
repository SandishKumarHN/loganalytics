package com.geminidata.logparser

case class Log(ip: String, clientId: String,
               userId: String, dateTime: String, method: String,
               endpoint: String, protocol: String,
               responseCode: Int, contentSize: Long)

object Log {

  // regex pattern to parse the apache access log
  val pattern = """^(\S+) (\S+) (\S+) \[([\w:/]+\s[+\-]\d{4})\] "(\S+) (\S+) (\S+)" (\d{3}) (\d+)""".r
  """^(\S+ )?(\S+) (\S+) (\S+) \[([\w:\/]+\s[+\-]\d{4})\] "(.+?)(?=" )" (\d{3}|-) (Cache:\S+ )?(\d+|-)\s?"?([^"]*)"?\s?"?([^"]*)?"?(.*)""".r

  def parseRow(row: String): Log =  {
    val parsedLog = pattern.findFirstMatchIn(row)
    if (parsedLog.isEmpty) {
      throw new RuntimeException("Cannot parse log line: " + row)
    }

    val finalLog = parsedLog.get
    Log(finalLog.group(1), finalLog.group(2), finalLog.group(3),finalLog.group(4), finalLog.group(5), finalLog.group(6),
      finalLog.group(7), finalLog.group(8).toInt, finalLog.group(9).toLong)
  }

}
