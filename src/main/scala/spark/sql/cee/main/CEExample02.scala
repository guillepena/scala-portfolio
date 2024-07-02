package eoi.de.examples
package spark.sql
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.Literal
import org.apache.spark.sql.catalyst.expressions.UnaryExpression
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType

object ExampleFunctions {
  val toLower: UnaryExpression with CodegenFallback = new UnaryExpression
    with CodegenFallback {
    override def child: Expression = Literal("HELLO WORLD")
    override protected def nullSafeEval(input: Any): Any =
      input.toString.toLowerCase

    override def canEqual(that: Any): Boolean = true

    override protected def withNewChildInternal(
        newChild: Expression,
    ): Expression = newChild

    override def dataType: DataType = StringType
    // Provide required implementations for Product trait
    override def productArity: Int = 0
    override def productElement(n: Int): Any =
      throw new IndexOutOfBoundsException

  }

}

object CEExample02 extends App {

  println(ExampleFunctions.toLower.eval()) // prints "hello world"

}
