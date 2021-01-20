package com.pavelsialitski.paytmtesttask


// TODO Add to Application object so they are pulled from command line args

case class ApplicationConfig(applicationName: String
                             ,sparkMaster: String = sys.env.getOrElse("ENV_CONFIG_SPARK_MASTER", "local[*]")
                             ,header: Boolean = false
                             ,inputFilePath: String = "inputFile.csv"
                             ,countryListFile: String = "countrylist.csv"
                             ,stationListFile: String = "stationlist.csv"

                             ,dataFolder: String = "data/2019"

                             ,cc: Int = 123,
                            )

