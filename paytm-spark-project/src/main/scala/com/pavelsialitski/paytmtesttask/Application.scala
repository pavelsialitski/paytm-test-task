package com.pavelsialitski.paytmtesttask




import org.apache.log4j.Logger

object Application extends App {

  val logger: Logger = Logger.getLogger(this.getClass.getName)

  logger.info("Starting application: " + this.getClass.getPackage)
  logger.info("Command line args: " + args.toList.toString)

  val applicationName = "paytmtesttask"
        //scala.sys.env.getOrElse("ENVVARIABLE", "Pricing spark template (default)")



  val parser = new scopt.OptionParser[ApplicationConfig](applicationName) {
    head(applicationName, "1.0")

    opt[Int]('C', "cc").action( (x, c) =>
      c.copy(cc = x) ).text("foo is an integer property")

    opt[Boolean]('B', "myBoolParam").action( (x, c) =>
      c.copy( myBoolParam = x) ).text("myBoolParam is a bool param")

    opt[String]("csdPricingAppName").action( (x, c) =>
      c.copy( applicationName = x) ).text("application name is loaded from env config but can be overwritten from command line")

    opt[String]("sparkMaster").action( (x, c) =>
      c.copy( sparkMaster = x) ).text("sparkMaster is loaded from env config SPARK_MASTER but can be overwritten from command line")

  }


  private val config = parser.parse(args, ApplicationConfig(applicationName = applicationName)) match {
    case Some(c) => c

    case None =>
     sys.exit(0)
  }

  logger.info("Loaded parameters: " + config.toString)
  logger.info("Creating worker object")

  val worker:Worker = new Worker(config)

  worker.run()

  logger.info("Exiting the application: " + this.getClass.getPackage)



}

