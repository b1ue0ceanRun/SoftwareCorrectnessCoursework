import java.awt.{Color, Graphics2D}
import javax.swing.SwingUtilities
import scala.util.control.Breaks.break

object Drawer {
  // panel ref
  private var graphicPanel: GraphicPanel = null
  private var boundingBoxCoordinates: List[Int] = List.empty

  
  // set panel ref
  def setGraphicPanel(panel: GraphicPanel): Unit = {
    graphicPanel = panel
  }
  
  def drawPixel(x: Int, y: Int): Unit = {
    // add to state
    State.addPixel(x, y)
  }
  
  // use Bresenham's algorithm to draw a line from (x0, y0) to (x1, y1)
  def drawLine(x0: Int, y0: Int, x1: Int, y1: Int): Unit = {
    var x = x0
    var y = y0
    val dx = Math.abs(x1 - x0)
    val dy = Math.abs(y1 - y0)
    val sx = if (x0 < x1) 1 else -1
    val sy = if (y0 < y1) 1 else -1
    var err = dx - dy
    while (true) {
      drawPixel(x, y) // draw current pixel
      if (x == x1 && y == y1) return // stop when end point is reached
      val e2 = 2 * err
      if (e2 > -dy) {
        err -= dy
        x += sx
      }
      if (e2 < dx) {
        err += dx
        y += sy
      }
    }
  }

  def drawRectangle(x0: Int, y0: Int, x1: Int, y1: Int): Unit = {
    drawLine(x0, y0, x1, y0)
    drawLine(x0, y0, x0, y1)
    drawLine(x0, y1, x1, y1)
    drawLine(x1, y1, x1, y0)
  }

  def drawText(x: Int, y: Int, t: String): Unit = {
    /*
    (BOUNDINGBOX (10 10) (400 400)):
    (TEXTAT (100 100) Hello):
     */
    //State.addText(t, x, y)
    graphicPanel.writeText(t, x, y)

    println(s"Should show text $t on coordinates $x and $y")
  }

  def drawBoundingBox(x0: Int, y0: Int, x1: Int, y1: Int): Unit = {
    if (x0 >= 0 & y0 >= 0 & x1 <= 700 & y1 <= 635) { // TODO: add error handling - case when we re-draw bounding box + show error in the error window
      drawRectangle(x0, y0, x1, y1)
      boundingBoxCoordinates = List(x0, y0, x1, y1)
      println(boundingBoxCoordinates)
    }
    else {println("Error - your Bounding box is outside of the Drawing box.")} // TODO: show in errorWindow
  }

  def drawFill(c: String, g: Command): Unit = {
      println(c)
      println(g.toString)
      // TODO: continue here

  }

  // compute points along a circle using the midpoint algorithm
  def drawCircle(centerX: Int, centerY: Int, radius: Int): Unit = {
    var x = 0
    var y = radius
    var d = 1 - radius
  
    // plot eight points at once
    def plotEightPoints(x: Int, y: Int): Unit = {
      val cx = centerX
      val cy = centerY

      // draw pixels at the 8 symmetric points of a circle
      drawPixel(cx + x, cy + y)
      drawPixel(cx - x, cy + y)
      drawPixel(cx + x, cy - y)
      drawPixel(cx - x, cy - y)
      drawPixel(cx + y, cy + x)
      drawPixel(cx - y, cy + x)
      drawPixel(cx + y, cy - x)
      drawPixel(cx - y, cy - x)
    }

    plotEightPoints(x, y)

    // iterate over one octant and reflect points to complete the circle
    while (x < y) {
      x += 1
      if (d < 0) {
        d += 2 * x + 1
      } else {
        y -= 1
        d += 2 * (x - y) + 1
      }
      plotEightPoints(x, y)
    }
}
  
  def drawSequence(): Unit = {
    new Thread(() => {
      SwingUtilities.invokeLater(() => graphicPanel.clearPixels())
      State.clearPixels()

      for (instruction <- State.getState) {
        renderCommand(instruction)

        SwingUtilities.invokeLater(() => updateGraphics())
        Thread.sleep(400)
      }
    }).start()
  }

  // sync panel with state
    private def isInsideBox(Point: (Int, Int), x0:Int, y0:Int, x1:Int, y1:Int): Boolean = {
      val x = Point._1
      val y = Point._2

      if(x >= x0 && x <= x1 && y >= y0 && y <= y1) {true}
      else {false}
    }

    def changeColor(c: String): Color = {
      c match {
        case "blue" => Color.BLUE
        case "red" => Color.RED
        case "green" => Color.GREEN
        case "cyan" => Color.CYAN
        case _ => Color.BLACK
      }
    }

private def updateGraphics(): Unit = {
    if (graphicPanel != null) {
      graphicPanel.clearPixels()

      val pixels = State.getPixels

      if (boundingBoxCoordinates.length >= 4) {
        val x0 = boundingBoxCoordinates(0)
        val y0 = boundingBoxCoordinates(1)
        val x1 = boundingBoxCoordinates(2)
        val y1 = boundingBoxCoordinates(3)

        pixels.foreach {
          case (x, y) =>
            if (isInsideBox((x, y), x0, y0, x1, y1))
              graphicPanel.addPixel(x, y)
        }
      } else {
        // No bounding box, draw all pixels
        pixels.foreach {
          case (x, y) =>
            graphicPanel.addPixel(x, y)
        }
      }

      graphicPanel.update()
    }
  }

  
def renderCommand(cmd: Command): Unit = {
  cmd match {
    case Circle(x, y, r) =>
      drawCircle(x, y, r)
    case Line(x0, y0, x1, y1) =>
      drawLine(x0, y0, x1, y1)
    case Rectangle(x0, y0, x1, y1) =>
      drawRectangle(x0, y0, x1, y1)
    case TextAt(x, y, t) =>
      drawText(x, y, t)
    case BoundingBox(x0, y0, x1, y1) =>
      drawBoundingBox(x0, y0, x1, y1)
    case Fill(c, g) =>
      drawFill(c, g)
    case Draw(color, cmds) =>
      cmds.foreach(renderCommand)
  }
}

}


// input examples for testing:

/*
(BOUNDINGBOX (10 10) (400 400)):
(RECTANGLE (100 100) (450 450)):
(CIRCLE (300 300) 300):

(BOUNDINGBOX (10 10) (400 400)):
(FILL pink (CIRCLE (300 300) 300):): ERROR: "'):' expected but end of source found"
 */

/*
(BOUNDINGBOX (10 10) (400 400)):
(TEXTAT (100 100) Hello):
*/

/*
(BOUNDINGBOX (10 10) (400 400)):
(DRAW cyan (CIRCLE (300 300) 300):, (RECTANGLE (100 100) (450 450)):)
 */