package eoi.de.examples
package patrones.monoids

import cats.Monoid
import cats.implicits.catsSyntaxSemigroup

object Ejemplo2App extends App {

  private val conjunto1 = Set(1, 2, 3, 4)
  private val conjunto2 = Set(3, 4, 5, 6)

  implicit val setSymmetricDifferenceMonoid: Monoid[Set[Int]] =
    new Monoid[Set[Int]] {
      def empty = Set.empty[Int]
      def combine(x: Set[Int], y: Set[Int]): Set[Int] = x.diff(y)
        .union(y.diff(x))
    }

  // Para usarlo en otro ejemplo tendremos que importarlo:
  // import setSymmetricDifferenceMonoid._

  // Diferencia simétrica de ambos conjuntos utilizando el operador |+|
  private val resultado = conjunto1 |+| conjunto2

  println(resultado)
  // Salida: Set(1, 2, 5, 6)

}
