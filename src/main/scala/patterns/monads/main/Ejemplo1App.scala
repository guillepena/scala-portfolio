package eoi.de.examples
package patrones.monads

object Ejemplo1App extends App {

  import cats.Monad
  import cats.instances.list._

  val lista = List(1, 2, 3, 4)

  // Encadenamiento de funciones en una lista, duplicando cada elemento
  val listaDuplicada = Monad[List].flatMap(lista)(x => List(x, x))

  println(listaDuplicada) // Salida: List(1, 1, 2, 2, 3, 3, 4, 4)

}
