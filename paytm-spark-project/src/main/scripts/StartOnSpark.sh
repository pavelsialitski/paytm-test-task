#!/usr/bin/env bash

#source $(dirname $0)/InitAppEnv.sh

log4j_file="-Dlog4j.configuration=file:classes/log4j.properties"

spark-submit \
--conf "spark.driver.extraJavaOptions=${log4j_file}" \
--conf "spark.executor.extraJavaOptions=${log4j_file}" \
paytm-test-task-1.0.jar



#spark-submit \
#--driver-java-options "-Dlog4j.configurationFile=file:${CSDPRICING_APP_HOME}/${CSDPRICING_LOG_CONFIG_FILE_NAME}" \
#--master ${SPARK_MASTER} \
#--queue ${YARN_QUEUE} \
#${CSDPRICING_APP_HOME}/${CSDPRICING_APP_JAR} \
#"$@"



