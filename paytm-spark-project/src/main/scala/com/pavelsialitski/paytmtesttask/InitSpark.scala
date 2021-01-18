package com.pavelsialitski.paytmtesttask


import org.apache.spark.sql.SparkSession

trait InitSpark {

  protected def getSpark(appName: String): SparkSession = SparkSession.builder()
    .appName(appName)
    .config("spark.sql.hive.convertMetastoreParquet","false")
    .config("spark.sql.sources.bucketing.enabled", "true")
    .config("spark.sql.crossJoin.enabled", "true")
    .config("spark.sql.parquet.writeLegacyFormat", "true")
    .enableHiveSupport()
    .getOrCreate()

}
