package com.pavelsialitski.paytmtesttask


case class ApplicationConfig(applicationName: String
                  ,sparkMaster: String = sys.env.getOrElse("ENV_CONFIG_SPARK_MASTER", "local[*]")
                  ,header: Boolean = false
                  ,inputFilePath: String = "inputFile.csv"
                  ,cc: Int = 123)

