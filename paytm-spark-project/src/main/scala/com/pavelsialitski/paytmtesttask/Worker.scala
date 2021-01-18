package com.pavelsialitski.paytmtesttask

import org.apache.log4j.Logger
import org.apache.spark.sql.functions.sum

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




    // test run

    val sumHundred = spark.range(1, 101).toDF("Number").agg(sum("Number").as("Sum 1 to 100") )

    sumHundred.show()

    // End of Testing

    logger.info("Worker finished, closing spark")


    // after job is done - close spark
    spark.close()


  }

}
