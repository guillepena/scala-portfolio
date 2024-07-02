package eoi.de.examples
package spark.sql.dataframes

import org.scalatest.funsuite.AnyFunSuiteLike

class DataGen02Test extends AnyFunSuiteLike {

}


import org.scalatest.funsuite.AnyFunSuite

class CSVTests extends AnyFunSuite {

  // Instantiate the object you are testing
  def convertToProperCSV(lineaCsv: String): String = {
    val cols = lineaCsv.split(",")
    // Todas las columnas llevan comillas
    cols.map { field =>
      "\"" + field + "\""
    }.mkString(",")
  }

  test("convertToProperCSV should add quotes around each field of a CSV line") {
    val input = "1,John,Doe,USA"
    val expectedOutput = """"1","John","Doe","USA""""
    assert(convertToProperCSV(input) == expectedOutput)
  }

  test("convertToProperCSV should handle empty strings correctly") {
    val input = ""
    val expectedOutput = "\"\""
    assert(convertToProperCSV(input) == expectedOutput)
  }

  test("convertToProperCSV should handle input with only commas correctly") {
    val input = ",,,"
    val expectedOutput = "\"\",\"\",\"\",\"\""
    assert(convertToProperCSV(input) == expectedOutput)
  }
}