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

    opt[String]("inputFilePath").action( (x, c) =>
      c.copy( inputFilePath = x) ).text("path to input file")

    opt[Unit]("headerIncluded").action( (_, c) =>
      c.copy( header = true) ).text("mention this flag if the csv file includes the header")

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

