package eoi.de.examples
package patrones.applicative

import cats.implicits._

object Ejemplo3App extends App {

  val a: Option[Int] = 3.some
  val b: Option[Int] = 2.some

  val c: Option[Int] = (a, b).mapN(_ + _)
  // c es Some(5)
  println(c)
}
