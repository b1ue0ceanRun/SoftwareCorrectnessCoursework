import scala.util.parsing.combinator._

sealed trait Command
case class Circle(x: Int, y: Int, r: Int) extends Command
case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Line(x1: Int, y1: Int, x2: Int, y2:Int) extends Command
case class TextAt(x: Int, y: Int, t: String) extends Command
case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Fill(c: String, g: Command) extends Command
case class Draw(c: String, commands: List[Command]) extends Command

object Parser extends RegexParsers {

  def number: Parser[Int] = """-?\d+""".r ^^ (_.toInt)

  def point: Parser[(Int, Int)] =
    "(" ~> number ~ number <~ ")" ^^ {
      case x ~ y => (x, y)
    }

  def word: Parser[String] =
    """[a-zA-Z_][a-zA-Z0-9_%|\s]*""".r ^^ (_.trim)

  def bool: Parser[Boolean] = """true|false""".r ^^ (_.toBoolean)

  def circle: Parser[Circle] =
    "(" ~> "CIRCLE" ~ point ~ number <~ "):" ^^ {
      case _ ~ (x, y) ~ r => Circle(x, y, r)
    }

  def rectangle: Parser[Rectangle] =
    "(" ~> "RECTANGLE" ~ point ~ point <~ "):" ^^ {
      case _ ~ (x1, y1) ~ (x2, y2) => Rectangle(x1, y1, x2, y2)
    }

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

  def fill: Parser[Fill] =
    "(" ~> "FILL" ~ word ~ command <~ "):" ^^ {
      case _ ~ c ~ g => Fill(c, g)
    }

  def draw: Parser[Draw] =
    "(" ~> "DRAW" ~ word ~ repsep(command, opt(whiteSpace)) <~ "):" ^^ {
      case _ ~ c ~ cmds => Draw(c, cmds)
    }

  def command: Parser[Command] =
    circle | rectangle | line | textAt | boundingBox | fill | draw

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
