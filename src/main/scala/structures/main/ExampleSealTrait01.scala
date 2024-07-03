package eoi.de.examples
package structures.main

object ExampleSealTrait01 extends App {

  sealed trait Animal {
    def sound(): String
  }

  trait Runner {
    def run(): String = "¡Estoy corriendo!"
  }

  trait Flying {
    def fly(): String = "¡Estoy volando!"
  }

  case class Dog(name: String) extends Animal with Runner {
    def sound(): String = "Guau"
    override def run(): String =
      s"¡Soy $name y estoy corriendo como un perro!"
  }

  case class Cat(name: String) extends Animal with Runner {
    def sound(): String = "Miau"
  }

  case class Bird(name: String) extends Animal with Flying {
    def sound(): String = "Pio"
  }

  val dog = Dog("Toby")
  println(dog.run())

  val cat = Cat("Garfield")
  println(cat.run())

  val bird = Bird("Piolín")
  println(bird.fly())

}
