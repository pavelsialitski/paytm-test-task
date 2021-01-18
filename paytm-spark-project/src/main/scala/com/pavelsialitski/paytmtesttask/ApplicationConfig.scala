package com.pavelsialitski.paytmtesttask


case class ApplicationConfig(applicationName: String
                  ,sparkMaster: String = sys.env.getOrElse("ENV_CONFIG_SPARK_MASTER", "local[*]")
                  ,myBoolParam: Boolean = false
                  ,bb: String = "some string"
                  ,aa: String = "adfg"
                  ,cc: Int = 123)

