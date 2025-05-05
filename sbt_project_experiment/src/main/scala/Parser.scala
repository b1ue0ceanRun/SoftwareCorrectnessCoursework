import scala.{Either, Right}
import scala.util._
import scala.util.Success
import scala.util.parsing.combinator._

sealed trait Command
case class Circle(x: Int, y: Int, r: Int) extends Command
case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Line(x1: Int, y1: Int, x2: Int, y2:Int) extends Command
case class TextAt(x: Int, y: Int, t: String) extends Command
case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Fill(c: String, g: Command) extends Command
case class Draw(c: String, commands: List[Command]) extends Command

// scala parsing resource
object Parser extends RegexParsers { //TODO: error about handling whitespace ???
    
  // define number as possibly negative, multiple digits and a decimal part and map to int
  def number: Parser[Int] = """-?\d+""".r ^^ (_.toInt)

  // map two numbers, seperated by whitespace, enclosed in parenthesis to a tuple
  def point: Parser[(Int, Int)] =
    "(" ~> number ~ number <~ ")" ^^ {
      case x ~ y => (x, y)
    }

  def word: Parser[String] = """[a-zA-Z_][a-zA-Z0-9_%_|\s][0-9_a-zA-Z_%|\s]*""".r ^^ {_.toString} // TODO: if number is first element of the word - now, it causes error
  def bool: Parser[Boolean] = """true|false""".r ^^ (_.toBoolean)


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

  def textAt: Parser[TextAt] = // TODO: fix
    "(" ~> "TEXTAT" ~ point ~ word <~ "):" ^^ {
      case _  ~ (x, y) ~ t => TextAt(x, y, t)
    }

  // map (BOUNDINGBOX (x1 y1) (x2 y2)) to BOUNDINGBOX(x1, y1, x2, y2)
  def boundingBox: Parser[BoundingBox] =
    "(" ~> "BOUNDINGBOX" ~ point ~ point <~ "):" ^^ {
      case _ ~ (x1, y1) ~ (x2, y2) => BoundingBox(x1, y1, x2, y2)
    }
  


  // general command parser
  def command: Parser[Command] = circle | rectangle | line | textAt | boundingBox | fill | draw

  def fill: Parser[Fill] =
    "(" ~> "FILL" ~ word ~ command <~ "):" ^^ {
      case _ ~ c ~ g => Fill(c, g)
    }

    // (FILL BLUE (CIRCLE (200 200) 70):):

  def draw: Parser[Draw] =
  "(" ~> "DRAW" ~ word ~ rep(command) <~ "):" ^^ {
    case _ ~ c ~ cmds => Draw(c, cmds)
  }
  // (DRAW RED (CIRCLE (100 100) 30): (LINE (10 10) (200 200)): ):


def receiveCode(code: String): Either[String, List[Command]] = {
  parseAll(rep(command), code.trim) match {
    case NoSuccess(msg, _) =>
      Left(s"Parsing error: $msg")
    case Success(result, _) =>
      result.foreach(State.addInstruction) // optional side effect
      Right(result)
  }
}

}
