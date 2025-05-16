import org.scalatest.funsuite.AnyFunSuite
import Parser._
import scala.util._

class ParserTest extends AnyFunSuite {

  test("Parse Circle") {
    val input = "(CIRCLE (10 20) 30):"
    val result = parseAll(circle, input)
    assert(result.successful)
    assert(result.get == Circle(10, 20, 30))
  }

  test("Parse Rectangle") {
    val input = "(RECTANGLE (0 0) (100 200)):"
    val result = parseAll(rectangle, input)
    assert(result.successful)
    assert(result.get == Rectangle(0, 0, 100, 200))
  }

  test("Parse Line") {
    val input = "(LINE (5 5) (50 50)):"
    val result = parseAll(line, input)
    assert(result.successful)
    assert(result.get == Line(5, 5, 50, 50))
  }

  test("Parse TextAt") {
    val input = "(TEXTAT (42 84) HelloWorld):"
    val result = parseAll(textAt, input)
    assert(result.successful)
    assert(result.get == TextAt(42, 84, "HelloWorld"))
  }

  test("Parse BoundingBox") {
    val input = "(BOUNDINGBOX (10 10) (20 20)):"
    val result = parseAll(boundingBox, input)
    assert(result.successful)
    assert(result.get == BoundingBox(10, 10, 20, 20))
  }

  test("Parse Fill") {
    val input = "(FILL BLUE (CIRCLE (200 200) 70):):"
    val result = parseAll(fill, input)
    assert(result.successful)
    assert(result.get == Fill("BLUE", Circle(200, 200, 70)))
  }

  test("Parse Draw") {
    val input =
      """(DRAW RED
        |  (CIRCLE (100 100) 30):
        |  (LINE (10 10) (200 200)):
        |):""".stripMargin

    val result = parseAll(draw, input)
    assert(result.successful, s"Parsing failed: ${result}")
    assert(result.get == Draw("RED", List(Circle(100, 100, 30), Line(10, 10, 200, 200))))
  }


  test("ReceiveCode returns Right with valid input") {
    val input = "(CIRCLE (1 2) 3):(LINE (4 5) (6 7)):"
    val result = receiveCode(input)
    assert(result.isRight)
    assert(result == Right(List(Circle(1, 2, 3), Line(4, 5, 6, 7))))
  }

  test("ReceiveCode returns Left with invalid input") {
    val input = "(INVALID (0 0)):"
    val result = receiveCode(input)
    assert(result.isLeft)
    assert(result.left.get.contains("Parsing error"))
  }
}
