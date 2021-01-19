package com.pavelsialitski.paytmtesttask


case class ApplicationConfig(applicationName: String
                             ,sparkMaster: String = sys.env.getOrElse("ENV_CONFIG_SPARK_MASTER", "local[*]")
                             ,header: Boolean = false
                             ,inputFilePath: String = "inputFile.csv"
                             ,countryListFile: String = "countrylist.csv"
                             ,stationListFile: String = "stationlist.csv"

                             ,dataFolder: String = "data/2019"

                             ,cc: Int = 123,
                            )

