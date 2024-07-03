package eoi.de.examples
package structures.main

// AnimalConNombre(nombre: String)
// El sonido es abstracto ya que no se puede definir un sonido genérico para todos los animales
abstract class AnimalWithName(val name: String) {
  val sound: String

}

// correr()
trait Runner extends AnimalWithName {
  def correr(): String =
    s"Digo $sound porque soy son un ${this.getClass.getSimpleName.stripSuffix("$")} - ¡Soy $name y estoy corriendo!"
}

trait Flying extends AnimalWithName {
  def fly(): String =
    s"Digo $sound porque soy son un ${this.getClass.getSimpleName.stripSuffix("$")} - ¡Soy $name y estoy volando!"
}

case class Dog(override val name: String, override val sound: String = "Guau")
  extends AnimalWithName(name) with Runner

case class Cat(override val name: String, override val sound: String = "Miau")
  extends AnimalWithName(name) with Runner

case class Bird(override val name: String, override val sound: String = "Pio")
  extends AnimalWithName(name) with Flying

class Lion(override val name: String, override val sound: String = "Rugido de león")
  extends AnimalWithName(name)
    with Runner

case object KingLion$ extends Lion("ReyLeon", "Rugido de rey león")

// Ejemplo de animal que corre y vuela

case class BirdRunner(override val name: String, override val sound: String = "Pio")
  extends AnimalWithName(name)
    with Runner
    with Flying

object ExampleSealTraitWithAbstract01 extends App {
  val dog = Dog("Toby")
  println(dog.correr())

  val cat = Cat("Garfield")
  println(cat.correr())

  val bird = Bird("Piolín")
  println(bird.fly())

  println(KingLion$.correr())

  val birdRunner = BirdRunner("Piolín")
  println(birdRunner.correr())
  println(birdRunner.fly())

}