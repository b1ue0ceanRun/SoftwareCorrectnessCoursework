// src/main/scala/engine/DrawingEngine.scala
package engine

import java.awt.Color
import scala.collection.mutable.ListBuffer

class DrawingEngine {

  private val drawables = ListBuffer.empty[Drawable]
  private var color: Color = Color.black
  private var bbox: Option[BoundingBox] = None
  private var currentIndex: Int = -1

  def interpretCommands(commands: List[Command]): Unit = {
    drawables.clear()
    commands.zipWithIndex.foreach { case (cmd, i) =>
      currentIndex = i
      interpret(cmd)
    }
    currentIndex = -1
  }

  def getDrawables: List[Drawable] = drawables.toList
  def getCurrentIndex: Int = currentIndex

  private def plot(x: Int, y: Int): Unit = {
    bbox match {
      case Some(BoundingBox(x1, y1, x2, y2)) =>
        if (x >= x1 && x <= x2 && y >= y1 && y <= y2)
          drawables += DPixel(x, y, color)
      case None =>
        drawables += DPixel(x, y, color)
    }
  }

  private def drawBresenham(x1: Int, y1: Int, x2: Int, y2: Int): Unit = {
    var dx = Math.abs(x2 - x1)
    var dy = -Math.abs(y2 - y1)
    val sx = if (x1 < x2) 1 else -1
    val sy = if (y1 < y2) 1 else -1
    var err = dx + dy
    var cx = x1
    var cy = y1

    while (true) {
      plot(cx, cy)
      if (cx == x2 && cy == y2) return
      val e2 = 2 * err
      if (e2 >= dy) { err += dy; cx += sx }
      if (e2 <= dx) { err += dx; cy += sy }
    }
  }

  private def drawMidpointCircle(cx: Int, cy: Int, r: Int): Unit = {
    var x = 0
    var y = r
    var d = 1 - r
    while (x <= y) {
      plot(cx + x, cy + y); plot(cx - x, cy + y)
      plot(cx + x, cy - y); plot(cx - x, cy - y)
      plot(cx + y, cy + x); plot(cx - y, cy + x)
      plot(cx + y, cy - x); plot(cx - y, cy - x)
      x += 1
      if (d < 0) {
        d += 2 * x + 1
      } else {
        y -= 1
        d += 2 * (x - y) + 1
      }
    }
  }

  private def fillCircle(cx: Int, cy: Int, r: Int): Unit = {
    for {
      x <- cx - r to cx + r
      y <- cy - r to cy + r
      if (x - cx) * (x - cx) + (y - cy) * (y - cy) <= r * r
    } plot(x, y)
  }

  private def drawRectangle(x1: Int, y1: Int, x2: Int, y2: Int): Unit = {
    drawBresenham(x1, y1, x1, y2)
    drawBresenham(x1, y2, x2, y2)
    drawBresenham(x2, y2, x2, y1)
    drawBresenham(x2, y1, x1, y1)
  }

  private def fillRectangle(x1: Int, y1: Int, x2: Int, y2: Int): Unit = {
    val minX = math.min(x1, x2)
    val maxX = math.max(x1, x2)
    val minY = math.min(y1, y2)
    val maxY = math.max(y1, y2)

    for {
      x <- minX to maxX
      y <- minY to maxY
    } plot(x, y)
  }

  private def interpret(cmd: Command): Unit = cmd match {

    case BoundingBox(x1, y1, x2, y2) =>
      bbox = Some(BoundingBox(x1, y1, x2, y2))

    case Line(x1, y1, x2, y2) =>
      drawBresenham(x1, y1, x2, y2)

    case Rectangle(x1, y1, x2, y2) =>
      drawRectangle(x1, y1, x2, y2)

    case Circle(cx, cy, r) =>
      drawMidpointCircle(cx, cy, r)

    case TextAt(x, y, text) =>
      drawables += DText(x, y, text, color)

    case Draw(c, subs) =>
      val oldColor = color
      color = toColor(c)
      subs.foreach(interpret)
      color = oldColor

    case Fill(c, shape) =>
      val oldColor = color
      color = toColor(c)
      shape match {
        case Rectangle(x1, y1, x2, y2) => fillRectangle(x1, y1, x2, y2)
        case Circle(cx, cy, r)        => fillCircle(cx, cy, r)
        case _ =>
          println(s"Warning: unsupported fill shape $shape")
          interpret(shape)
      }
      color = oldColor
  }

  private def toColor(name: String): Color = name.toLowerCase match {
    case "red"   => Color.red
    case "green" => Color.green
    case "blue"  => Color.blue
    case "black" => Color.black
    case "gray"  => Color.gray
    case "yellow" => Color.yellow
    case _       => Color.darkGray
  }
}
