import scala.{Either, Right}
import scala.util._
import scala.util.Success
import scala.util.parsing.combinator._
sealed trait Command
case class Circle(x: Double, y: Double, r: Double) extends Command

// scala parsing resource
class Parser(state: State) extends RegexParsers {
    
  // define number as possibly negative, multiple digits and a decimal part and map to double
  def number: Parser[Double] = """-?\d+(\.\d+)?""".r ^^ (_.toDouble)

  // map two numbers, seperated by whitespace, enclosed in parenthesis to a tuple
  def point: Parser[(Double, Double)] =
  "(" ~> number ~ number <~ ")" ^^ 
  {
    case x ~ y => (x, y)
  }

  // map (CIRCLE (x y) r) to Circle(x, y, r)
  def circle: Parser[Circle] =
  "(" ~> "CIRCLE" ~ point ~ number <~ "):" ^^ 
  {
    case _ ~ (x, y) ~ r => Circle(x, y, r)
  }
  
  def command: Parser[Command] = circle

  def receiveCode(code: String): Either[String, Command] = {
    parseAll(command, code) match { // add drawing instruction upon succesful parsing
      case Success(result, _) =>
        state.addInstruction(result match {
          case Circle(x, y, r) => List("CIRCLE", x, y, r)
        }) 
        Right(result)

      case NoSuccess(msg, _) => Left(msg)
    }
  }

  // (CIRCLE (12 12) 3):


}


