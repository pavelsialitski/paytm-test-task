package com.pavelsialitski.paytmtesttask

import org.apache.log4j.Logger
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DoubleType



class Worker (config: ApplicationConfig) extends InitSpark {

  val logger: Logger = Logger.getLogger(this.getClass.getName)

  private val applicationName = config.applicationName




  //get spark
  private val spark = getSpark(applicationName)

  def run (): Unit ={


    println("csdPricingAppName = "+applicationName)


    // Testing spark availability below
    val version = spark.version
    println(s"SPARK VERSION = $version")

    val appName = spark.sparkContext.appName
    println(s"SPARK appName $appName")

    val master = spark.sparkContext.master
    println(s"SPARK master $master")

    //pull in config
    val inputFilePath: String = config.inputFilePath
    val header: Boolean = config.header
    val countryListFilePath: String = config.countryListFile
    val stationListFilePath: String = config.stationListFile
    val dataFolder: String = config.dataFolder




    // End of load test file

    val countryDF = spark.read.option("header",header).csv(countryListFilePath)
    //countryDF.show()

    val stationDF = spark.read.option("header",header).csv(stationListFilePath)
    //stationDF.show


    val stationsWithCuntryNames = countryDF
      .join(stationDF,Seq("COUNTRY_ABBR"),"inner")

    //stationsWithCuntryNames.show()

    val weatherDataDF = spark.read.option("header",header).csv(dataFolder)
      .withColumnRenamed("STN---","STN_NO")

    //weatherDataDF.show()
    //weatherDataDF.count()

    val dataWithCountryNameDF = stationsWithCuntryNames
      .join(weatherDataDF, Seq("STN_NO"), "inner")
      .withColumn("YEAR",substring(col("YEARMODA"),0,4))

    //dataWithCountryNameDF.describe()



    // Which country had the hottest average mean temperature over the year?
    // column TEMP


    val hottestAverageMeanTemp = dataWithCountryNameDF
      .filter(col("TEMP").notEqual("9999.9"))
      .withColumn("TEMP_DOUBLE", col("TEMP").cast(DoubleType))
      .groupBy("COUNTRY_FULL","YEAR")
      .agg(avg("TEMP_DOUBLE").as("AVG_MEAN_YEARLY"))
      .orderBy(desc("AVG_MEAN_YEARLY"))


    hottestAverageMeanTemp.show()










    logger.info("Worker finished, closing spark")


    // after job is done - close spark
    spark.close()


  }

}
