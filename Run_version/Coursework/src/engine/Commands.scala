package engine

sealed trait Command

case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Line(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Circle(x: Int, y: Int, r: Int) extends Command
case class TextAt(x: Int, y: Int, text: String) extends Command

// For commands that refer to "draw" or "fill"
case class Draw(color: String, commands: List[Command]) extends Command
case class Fill(color: String, command: Command) extends Command

