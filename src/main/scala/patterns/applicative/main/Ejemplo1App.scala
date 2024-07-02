package eoi.de.examples
package patrones.applicative

object Ejemplo1App extends App {

  import cats.Applicative
  import cats.instances.list._

  val lista1: List[Int] = List(1, 2, 3)
  val lista2: List[Int] = List(10, 20, 30)

  // Applicative es un tipo de dato que representa un contexto que admite operaciones de mapeo y combinación.
  // Está dentro de la librería Cats.
  // Applicative[List].product combina dos listas en una lista de tuplas.
  // Applicative[List].product(lista1, lista2) es equivalente a lista1.flatMap(a => lista2.map(b => (a, b)))
  val combined: List[(Int, Int)] = Applicative[List].product(lista1, lista2)

  // Salida: List((1,10), (1,20), (1,30), (2,10), (2,20), (2,30), (3,10), (3,20), (3,30))
  println(combined)

}

object ConFlatMapApp extends App {
  val lista1: List[Int] = List(1, 2, 3)
  val lista2: List[Int] = List(10, 20, 30)
  val listaCombinada: List[(Int, Int)] = lista1
    .flatMap(a => lista2.map(b => (a, b)))

  println(listaCombinada) // Output: List((1,10), (1,20), (1,30), (2,10), (2,20), (2,30), (3,10), (3,20), (3,30))
}
