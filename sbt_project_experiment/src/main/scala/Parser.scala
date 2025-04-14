import scala.{Either, Right}
import scala.util._
import scala.util.Success
import scala.util.parsing.combinator._

sealed trait Command
case class Circle(x: Int, y: Int, r: Int) extends Command

// scala parsing resource
class Parser(state: State) extends RegexParsers {
    
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
  
  // general command parser
  def command: Parser[Command] = circle

  // take raw string code input and try to parse it into a command
  def receiveCode(code: String): Either[String, Command] = parseAll(command, code) match { 
    // add drawing instruction upon successful parsing
    case Success(result, _) =>
      state.addInstruction(result)    // store the Command itself
      Right(result)

    case NoSuccess(msg, _) =>
      Left(msg)                       // return error
  }

  // Example: (CIRCLE (12 12) 3):
}
