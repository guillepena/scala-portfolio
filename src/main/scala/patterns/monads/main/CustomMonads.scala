package eoi.de.examples
package patrones.monads

// Monada Result para manejar errores en pipelines de datos
sealed trait Result[+A]

case class Success[A](value: A) extends Result[A]

case class Failure(message: String) extends Result[Nothing]

object Result {

  // The recover function gives you a way to handle failure and continue computation.
  // It’s similar to 'catch' in traditional try-catch exception handling.
  def recover[A](result: Result[A])(f: String => A): Result[A] = result match {
    case Success(value) => Success(value)
    case Failure(message) => Success(f(message))
    case _ => Failure("Error")
  }

  def pure[A](value: A): Result[A] = Success(value)

  // Transforma el objecto envuelto aplicando la función
  def flatMap[A, B](result: Result[A])(f: A => Result[B]): Result[B] =
    result match {
      case Success(value) => f(value)
      case Failure(message) => Failure(message)
      case _ => Failure("Error")
    }

  def map[A, B](result: Result[A])(f: A => B): Result[B] =
    flatMap(result)(a => pure(f(a)))

  def apply[A](a: => A): Result[A] =
    try Success(a)
    catch { case e: Exception => Failure(e.getMessage) }
}

// Monada Future para operaciones asíncronas en pipelines de datos
object AsyncResult {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  case class AsyncResult[A](value: Future[Result[A]]) extends Result[A]

  def apply[A](a: => Future[A]): AsyncResult[A] = AsyncResult(
    a.map(Success(_)).recover { case e: Exception => Failure(e.getMessage) },
  )

}

object CustomMonads extends App {

  // Ejemplo de uso:
  private val dataProcessing: Result[Int] = Result {
    // Simulación de una operación que puede fallar
    val rawData = "123"
    rawData.toInt // Esto puede lanzar una excepción si rawData no es un número válido
  }

  private val dataProcessing2: Result[Int] = Result {
    // Simulación de una operación que puede fallar
    val rawData = "UnoDosTres"
    rawData.toInt // Esto puede lanzar una excepción si rawData no es un número válido
  }

  val transformedData: Result[String] = Result
    .flatMap(dataProcessing) { number =>
      Result.pure(s"Processed data: ${number * 2}")
    }

  val transformedData2: Result[String] = Result
    .flatMap(dataProcessing2) { number =>
      Result.pure(s"Processed data: ${number * 2}")
    }

  println(transformedData)

  println(transformedData2)

}
