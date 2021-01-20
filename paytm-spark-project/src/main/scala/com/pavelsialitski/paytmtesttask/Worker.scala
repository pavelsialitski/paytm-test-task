package com.pavelsialitski.paytmtesttask

import org.apache.log4j.Logger
import org.apache.spark.sql.expressions.{Window, WindowSpec}
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



    // 1. Which country had the hottest average mean temperature over the year?
    // column TEMP


    val hottestAverageMeanTemp = dataWithCountryNameDF
      .filter(col("TEMP").notEqual("9999.9"))
      .withColumn("TEMP_DOUBLE", col("TEMP").cast(DoubleType))
      .groupBy("COUNTRY_FULL","YEAR")
      .agg(avg("TEMP_DOUBLE").as("AVG_MEAN_YEARLY"))
      .orderBy(desc("YEAR"),

        desc("AVG_MEAN_YEARLY"))


    hottestAverageMeanTemp.show(1000)


    // 3. Which country had the second highest average mean wind speed over the year?

    val windowSpec: WindowSpec = Window
      .partitionBy("YEAR")
      .orderBy(desc("AVG_WDSP_MEAN_YEARLY"))

    // Could do this through group by and order - but Windows is amore universal way for his task (what if asked for 10th place)

    val highestAverageWindSpeed = dataWithCountryNameDF
      .filter(col("WDSP").notEqual("999.9"))
      .withColumn("WDSP_DOUBLE", col("WDSP").cast(DoubleType))
      .groupBy("COUNTRY_FULL","YEAR")
      .agg(avg("WDSP_DOUBLE").as("AVG_WDSP_MEAN_YEARLY"))
      .withColumn("WDSP_YEAR_RATING", row_number.over(windowSpec))
      .filter("WDSP_YEAR_RATING < 5")


    highestAverageWindSpeed.show()


    //2. Which country had the most consecutive days of tornadoes/funnel cloud
    //formations?

    val windowSpecByDate: WindowSpec = Window
      .partitionBy("YEAR", "COUNTRY_FULL")
      .orderBy(asc("DATE"))

    val consecutiveTornadoDays = dataWithCountryNameDF
      .withColumn("TORNADO",substring(col("FRSHTT"),6,1) )
      .filter(col("TORNADO").equalTo("1"))
      .withColumn("DATE", to_date(col("YEARMODA"),"yyyyMMdd"))
      .withColumn("DATE_RANK", row_number.over(windowSpecByDate))
      .withColumn("RANGE_START", date_add(col("DATE"),-col("DATE_RANK")))




    consecutiveTornadoDays.show()









    logger.info("Worker finished, closing spark")


    // after job is done - close spark
    spark.close()


  }

}
