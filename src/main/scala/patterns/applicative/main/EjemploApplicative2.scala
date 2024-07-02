package eoi.de.examples
package patrones.applicative

import cats.implicits._

object FormValidations {
  // Definimos un Type Alias, usaremos FormData en lugar de Map[String, String]
  private type FormData = Map[String, String]

  private val NAME = "name"
  private val EMAIL = "email"

  /** Obtiene el valor de un campo del formulario, verificando que no esté vacío
    * @param name
    *   Nombre del campo
    * @param form
    *   Formulario
    * @return
    */
  // (name: String) es un parámetro de entrada de la función getNonBlank
  // (form: FormData) es un parámetro de entrada de la función getNonBlank
  // (name: String)(form: FormData) es una función que recibe dos parámetros de entrada, el primero es un String y el segundo es un Map[String, String]
  // Y por qué no: (name: String, form: FormData)
  // Porque se está usando un estilo de programación funcional, en el que se pueden componer funciones de manera más sencilla y clara.
  private def getNonBlank(
      name: String,
  )(form: FormData): Either[List[String], String] = form.get(name)
    .toRight(List(s"$name field not specified")).flatMap(value =>
      Right(value).ensure(List(s"$name cannot be blank"))(_.nonEmpty),
    )

  /** Obtiene el valor de un campo del formulario
    * @param name
    *   Nombre del campo
    * @param form
    *   Formulario
    * @return
    */
  private def getValue(
      name: String,
  )(form: FormData): Either[List[String], String] = form.get(name)
    .toRight(List(s"$name field not specified"))

  /** Verifica que un campo no esté vacío
    * @param name
    *   Nombre del campo
    * @param value
    *   Valor del campo
    * @return
    */
  private def nonBlank(
      name: String,
  )(value: String): Either[List[String], String] = Right(value)
    .ensure(List(s"$name cannot be blank"))(_.nonEmpty)

  /** Verifica que un email contenga @
    * @param email
    *   Email
    * @return
    */
  private def emailCheck(email: String): Either[List[String], String] =
    Right(email).ensure(List("Email must contain @"))(_.contains("@"))

  /** Valida un formulario
    * @param formData
    *   Formulario
    * @return
    */
  // parMapN es una función de Cats que permite aplicar funciones a varios argumentos en paralelo.
  // En este caso, se aplican las funciones getNonBlank(NAME) y getNonBlank(EMAIL) a formData en paralelo.
  // partMapN es un atajo para aplicar mapN y luego flatMap. Es un método de Applicative, que es un tipo de efecto secundario que admite operaciones de mapeo y combinación.
  // Si ambas funciones tienen éxito, se devuelve un Right con el formulario.
  // Si alguna de las funciones falla, se devuelve un Left con la lista de errores.

  def validateForm(formData: FormData): Either[List[String], FormData] = (
    getNonBlank(NAME)(formData),
    getNonBlank(EMAIL)(formData).flatMap(emailCheck),
  ).parMapN((_, _) => formData)
}

object EjemploApplicative2 extends App {

  val formData = Map("name" -> "", "email" -> "invalid")

  val result = FormValidations.validateForm(formData)
  println(result)
  // Devuelve: Left(List("Name cannot be blank", "Email must contain @"))
}
