import scala.{Either, Right}
import scala.util._
import scala.util.Success
import scala.util.parsing.combinator._

sealed trait Command
case class Circle(x: Int, y: Int, r: Int) extends Command
case class Rectangle(x1: Int, y1: Int, x2: Int, y2:Int) extends Command
case class Line(x1: Int, y1: Int, x2: Int, y2:Int) extends Command

// scala parsing resource
object Parser extends RegexParsers {
    
  // define number as possibly negative, multiple digits and a decimal part and map to int
  def number: Parser[Int] = """-?\d+""".r ^^ (_.toInt)

  // map two numbers, seperated by whitespace, enclosed in parenthesis to a tuple
  def point: Parser[(Int, Int)] =
    "(" ~> number ~ number <~ ")" ^^ {
      case x ~ y => (x, y)
    }

  // map (CIRCLE (x y) r) to Circle(x, y, r)
  def circle: Parser[Circle] =
    "(" ~> "CIRCLE" ~ point ~ number <~ "):" ^^ {
      case _ ~ (x, y) ~ r => Circle(x, y, r)
    }

  // map (RECTANGLE (x1 y1) (x2 y2)) to Rectagle(x1, y1, x2, y2)
  def rectangle: Parser[Rectangle] =
    "(" ~> "RECTANGLE" ~ point ~ point <~ "):" ^^ {
      case _ ~ (x1, y1) ~ (x2, y2) => Rectangle(x1, y1, x2, y2)
    }

  // map (Line (x1 y1) (x2 y2)) to Line(x1, y1, x2, y2)
  def line: Parser[Line] =
    "(" ~> "LINE" ~ point ~ point <~ "):" ^^ {
      case _ ~ (x1, y1) ~ (x2, y2) => Line(x1, y1, x2, y2)
    }

  // general command parser
  def command: Parser[Command] = circle | line | rectangle


  // take raw string code input and try to parse it into a command
  def receiveCode(code: String): Either[String, Command] = parseAll(command, code) match { 
    // add drawing instruction upon successful parsing
    case Success(result, _) =>
      State.addInstruction(result)    // store the Command itself
      Right(result)

    case NoSuccess(msg, _) =>
      Left(msg)                       // return error
  }

  // Example: (CIRCLE (120 120) 30):
  // (RECTANGLE (10 10) (400 400)):
  // (LINE (10 10) (400 400)):
}
