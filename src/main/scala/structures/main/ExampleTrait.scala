package eoi.de.examples
package structures.main

object ExampleTrait extends App {

  trait Moving {
    def move(x: Int, y: Int): Unit
  }
  class Point(var x: Int, var y: Int) extends Moving {
    def move(newX: Int, newY: Int): Unit = {
      this.x = newX
      this.y = newY
    }
    override def toString: String = s"($x, $y)"
  }
  val point = new Point(0, 0)
  println(point.toString)
  point.move(1, 1)
  println(point.toString)
}
