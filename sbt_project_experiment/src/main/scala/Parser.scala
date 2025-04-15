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

// scala parsing resource
object Parser extends RegexParsers {
    
  // define number as possibly negative, multiple digits and a decimal part and map to int
  def number: Parser[Int] = """-?\d+""".r ^^ (_.toInt)

  // map two numbers, seperated by whitespace, enclosed in parenthesis to a tuple
  def point: Parser[(Int, Int)] =
    "(" ~> number ~ number <~ ")" ^^ {
      case x ~ y => (x, y)
    }

  def word: Parser[String] = """[a-zA-Z_]+""".r ^^ {_.toString} // empty spaces missing

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

  def textAt: Parser[TextAt] =
    "(" ~> "TEXTAT" ~ point ~ word <~ "):" ^^ {
      case _  ~ (x, y) ~ t => TextAt(x, y, t)
    }

  def boundingBox: Parser[BoundingBox] =
    "(" ~> "BOUNDINGBOX" ~ point ~ point <~ "):" ^^ {
      case _ ~ (x1, y1) ~ (x2, y2) => BoundingBox(x1, y1, x2, y2)
    }

  // general command parser
  def command: Parser[Command] = circle | line | rectangle | textAt | boundingBox


def receiveCode(code: String): Either[String, List[Command]] = {
  // split by colon and filter out empty entries, add : back, since parser expects it
  val commands = code.split(":").map(_.trim).filter(_.nonEmpty).map(_ + ":")

  val results = commands.map(parseSingleCommand)

  if (results.exists(_.isLeft)) {
    // display first error
    val firstError = results.collectFirst { case Left(msg) => msg }
    Left(firstError.getOrElse("Unknown parsing error"))
  } else {
    val parsedCommands = results.collect { case Right(cmd) => cmd }.toList
    parsedCommands.foreach(State.addInstruction)
    Right(parsedCommands)
  }
}

// helper
def parseSingleCommand(code: String): Either[String, Command] = parseAll(command, code.trim) match {
  case Success(result, _) => Right(result)
  case NoSuccess(msg, _) => Left(msg)
}
/*
     Example:
    (CIRCLE (0 0) 70):
    (RECTANGLE (10 10) (400 400)):
    (LINE (10 10) (400 400)):

  // Example: (CIRCLE (120 120) 30):
  // (RECTANGLE (10 10) (400 400)):
  // (LINE (10 10) (400 400)):
  // (TEXTAT (100 100) Hello):
  (BOUNDINGBOX (10 10) (400 400)):
     */
}
