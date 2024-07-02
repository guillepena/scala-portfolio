package eoi.de.examples
package patrones.functors

object Ejemplo1App extends App {

  import cats.Functor
  import cats.instances.list._

  private val lista = List(1, 2, 3, 4)

  // Mappeo de la lista con una función que multiplica por 2 cada elemento de la lista
  val dobleLista = Functor[List].map(lista)(_ * 2)

  println(dobleLista) // Salida: List(2, 4, 6, 8)

  // Es equivalente al siguiente bloque de código utilizando el mapa standar de Scala:
  private val lista2 = List(1, 2, 3, 4)
  val dobleLista2 = lista2.map(_ * 2)
}
