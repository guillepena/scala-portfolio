package eoi.de.examples
package patrones.monoids

object Ejemplo1App extends App {
  import cats.instances.string._
  import cats.syntax.semigroup._

  val a = "Hola"
  val b = " mundo"
  val c = a |+| b // Concatenación de cadenas utilizando el operador |+|
  println(c) // Salida: Hola mundo

}
