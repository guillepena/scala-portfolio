package eoi.de.examples
package spark.sql

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.functions.split
import org.apache.spark.sql.functions.udf

trait SparkSessionWrapper {
  implicit lazy val spark: SparkSession = {
    val conf = new org.apache.spark.SparkConf().setAppName("UDFExample01")
      .setMaster("local[*]")

    val sparkSession = SparkSession.builder().config(conf).getOrCreate()
    sparkSession.sparkContext.setLogLevel("ERROR")
    sparkSession
  }
}

object UDFExample01Functions extends SparkSessionWrapper {
  // Definir la UDF
  val wordCount: UserDefinedFunction = udf { (textContent: String) =>
    textContent.split(" ").groupBy(identity).map(e => (e._1, e._2.length))
  }

  def createDataFrame(data: Seq[(String, String)]): DataFrame = {
    import spark.implicits._
    data.toDF("id", "text")
  }

  def applyWordCount(df: DataFrame): DataFrame = {
    import spark.implicits._
    df.withColumn("wordCount", wordCount($"text"))
  }

  def explodeWordCount(df: DataFrame): DataFrame = {
    import spark.implicits._
    df.select($"id", explode($"wordCount"))
  }

}

object UDFExample01 extends App with SparkSessionWrapper {
  import UDFExample01Functions._
  // Crear el DataFrame
  val data = Seq(("1", "Hello world"), ("2", "I love Spark"))
  val df = createDataFrame(data)

  // Aplicar la UDF para contar las palabras
  val wordCountDF = applyWordCount(df)
  // Expander el mapa para tener una columna para cada palabra
  val explodedWCDF = explodeWordCount(wordCountDF)
  explodedWCDF.show()
}
