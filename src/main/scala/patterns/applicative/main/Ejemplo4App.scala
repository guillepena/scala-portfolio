package eoi.de.examples
package patrones.applicative

object Ejemplo4App extends App {

  /** Clase que envuelve un valor
    * @param value
    *   Valor
    * @tparam T
    *   Tipo del valor
    */
  final case class WrappedValue[T](value: T)

  /** Clase que envuelve un valor y permite aplicar funciones
    * @param wrappedValue
    *   Valor
    * @tparam T
    *   Tipo del valor
    */
  implicit class ApplicativeWrapper[T](wrappedValue: WrappedValue[T]) {
    // Permite aplicar una función al valor envuelto
    def map[B](f: T => B): WrappedValue[B] = WrappedValue(f(wrappedValue.value))
    // Permite aplicar una función envuelta al valor envuelto
    def <*>[B](that: WrappedValue[T => B]): WrappedValue[B] =
      WrappedValue(that.value(wrappedValue.value))
  }

  // Función que suma 1 a un entero
  val addOne: Int => Int = _ + 1
  // Valor envuelto
  val x = WrappedValue(5)
  // Función envuelta
  val addOneWrapped = WrappedValue(addOne)

  // Aplicamos la función envuelta al valor envuelto
  val result = x <*> addOneWrapped

  println(result.value) // Output: 6
}
