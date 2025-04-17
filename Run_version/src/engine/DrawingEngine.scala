// src/main/scala/engine/DrawingEngine.scala
package engine

import java.awt.Color
import scala.collection.mutable.ListBuffer

class DrawingEngine {
  private val drawables = ListBuffer.empty[Drawable]
  private var color: Color = Color.black
  private var bbox: Option[BoundingBox] = None
  private var currentIndex: Int = -1
  def getCurrentIndex: Int = currentIndex


  def interpretCommands(commands: List[Command]): Unit = {
    drawables.clear()
    commands.zipWithIndex.foreach { case (cmd, i) =>
      currentIndex = i
      interpret(cmd)
    }
    currentIndex = -1

  }

  def getDrawables: List[Drawable] = drawables.toList

  private def interpret(cmd: Command): Unit = cmd match {
    case BoundingBox(x1, y1, x2, y2) =>
      bbox = Some(BoundingBox(x1, y1, x2, y2))

    case Line(x1, y1, x2, y2) =>
      drawables += DLine(x1, y1, x2, y2, color)

    case Rectangle(x1, y1, x2, y2) =>
      drawables ++= Seq(
        DLine(x1, y1, x1, y2, color),
        DLine(x1, y2, x2, y2, color),
        DLine(x2, y2, x2, y1, color),
        DLine(x2, y1, x1, y1, color)
      )

    case Circle(cx, cy, r) =>
      drawables += DCircle(cx, cy, r, color)

    case TextAt(x, y, text) =>
      drawables += DText(x, y, text, color)

    case Draw(c, subs) =>
      val prev = color
      color = toColor(c)
      subs.foreach(interpret)
      color = prev

    case Fill(c, shape) =>
      val oldColor = color
      color = toColor(c)
      shape match {
        case Rectangle(x1, y1, x2, y2) =>
          drawables += DRect(x1, y1, x2, y2, color)
        case Circle(cx, cy, r) =>
          drawables += DCircle(cx, cy, r, color)
        case _ =>
          println(s"Warning: FILL not supported for shape: $shape")
          interpret(shape) // fallback: draw the outline
      }
      color = oldColor

  }

  private def toColor(name: String): Color = name.toLowerCase match {
    case "red"   => Color.red
    case "green" => Color.green
    case "blue"  => Color.blue
    case "black" => Color.black
    case "gray"  => Color.gray
    case _       => Color.darkGray
  }
}
