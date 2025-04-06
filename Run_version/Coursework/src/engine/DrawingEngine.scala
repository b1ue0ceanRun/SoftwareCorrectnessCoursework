package engine

class DrawingEngine {
  // Holds the bounding box if set
  var boundingBox: Option[BoundingBox] = None

  // Current drawing color (resets after a DRAW command finishes)
  var currentColor: String = "black"

  /**
   * Interpret a list of Command objects in order.
   */
  def interpretCommands(commands: List[Command]): Unit = {
    commands.foreach(interpretCommand)
  }

  /**
   * Interpret a single command, dispatching to the appropriate draw/fill method.
   */
  private def interpretCommand(cmd: Command): Unit = {
    cmd match {
      // 1) BOUNDING-BOX
      case bb: BoundingBox =>
        boundingBox = Some(bb)
        println(s"Setting bounding box to [${bb.x1},${bb.y1}] - [${bb.x2},${bb.y2}]")

      // 2) LINE
      case Line(x1, y1, x2, y2) =>
        drawLine(x1, y1, x2, y2, currentColor)

      // 3) RECTANGLE
      case Rectangle(x1, y1, x2, y2) =>
        // You can draw 4 lines:
        drawLine(x1, y1, x1, y2, currentColor)
        drawLine(x1, y2, x2, y2, currentColor)
        drawLine(x2, y2, x2, y1, currentColor)
        drawLine(x2, y1, x1, y1, currentColor)

      // 4) CIRCLE
      case Circle(cx, cy, r) =>
        drawCircle(cx, cy, r, currentColor)

      // 5) TEXT-AT
      case TextAt(x, y, text) =>
        drawText(x, y, text, currentColor)

      // 6) DRAW (change color for subcommands)
      case Draw(color, subcommands) =>
        val oldColor = currentColor
        currentColor = color
        subcommands.foreach(interpretCommand)
        currentColor = oldColor

      // 7) FILL
      case Fill(color, shapeCommand) =>
        fillShape(shapeCommand, color)
    }
  }

  /**
   * Draws a line using Bresenham’s algorithm, printing each pixel that passes the bounding box check.
   */
  private def drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: String): Unit = {
    // Setup for Bresenham:
    var dx = math.abs(x2 - x1)
    val sx = if (x1 < x2) 1 else -1
    var dy = -math.abs(y2 - y1)
    val sy = if (y1 < y2) 1 else -1

    var err = dx + dy
    var cx = x1
    var cy = y1

    while (true) {
      // For each pixel on the line, check bounding box before "drawing"
      if (withinBoundingBox(cx, cy)) {
        println(s"Drawing pixel at ($cx, $cy) in color $color")
      }
      if (cx == x2 && cy == y2) return

      val e2 = 2 * err
      if (e2 >= dy) {
        err += dy
        cx += sx
      }
      if (e2 <= dx) {
        err += dx
        cy += sy
      }
    }
  }

  /**
   * Draws a circle using the Midpoint circle algorithm, printing each pixel that is inside bounding box.
   */
  private def drawCircle(cx: Int, cy: Int, r: Int, color: String): Unit = {
    var x = 0
    var y = r
    var d = 1 - r

    // Plot the circle’s 8 symmetric points
    def plotCirclePoints(cx: Int, cy: Int, x: Int, y: Int): Unit = {
      drawPixelIfInBounds(cx + x, cy + y, color)
      drawPixelIfInBounds(cx - x, cy + y, color)
      drawPixelIfInBounds(cx + x, cy - y, color)
      drawPixelIfInBounds(cx - x, cy - y, color)
      drawPixelIfInBounds(cx + y, cy + x, color)
      drawPixelIfInBounds(cx - y, cy + x, color)
      drawPixelIfInBounds(cx + y, cy - x, color)
      drawPixelIfInBounds(cx - y, cy - x, color)
    }

    while (x <= y) {
      plotCirclePoints(cx, cy, x, y)
      x += 1
      if (d < 0) {
        d += 2 * x + 1
      } else {
        y -= 1
        d += 2 * (x - y) + 1
      }
    }
  }

  /**
   * Places text at (x, y), for now just prints a message to console if within bounding box.
   */
  private def drawText(x: Int, y: Int, text: String, color: String): Unit = {
    if (withinBoundingBox(x, y)) {
      println(s"Placing text '$text' at ($x,$y) in color $color")
    } else {
      println(s"Skipping text '$text' at ($x,$y) because it’s outside bounding box.")
    }
  }

  /**
   * Fills the given shape with the specified color.
   * Demonstrates naive fill for rectangles, circles, lines, etc.
   */
  private def fillShape(cmd: Command, color: String): Unit = {
    cmd match {
      case Rectangle(x1, y1, x2, y2) =>
        // Fill every point within the rectangle
        val minX = math.min(x1, x2)
        val maxX = math.max(x1, x2)
        val minY = math.min(y1, y2)
        val maxY = math.max(y1, y2)

        for {
          px <- minX to maxX
          py <- minY to maxY
        } {
          if (withinBoundingBox(px, py)) {
            println(s"Filling pixel at ($px, $py) in color $color")
          }
        }

      case Circle(cx, cy, r) =>
        // Fill the area of the circle
        for {
          px <- (cx - r) to (cx + r)
          py <- (cy - r) to (cy + r)
        } {
          val dx = px - cx
          val dy = py - cy
          if ((dx * dx + dy * dy) <= (r * r) && withinBoundingBox(px, py)) {
            println(s"Filling pixel at ($px, $py) in color $color")
          }
        }

      case Line(x1, y1, x2, y2) =>
        // “Filling” a line: effectively same as drawing the line but in fill color
        val oldColor = currentColor
        currentColor = color
        drawLine(x1, y1, x2, y2, color)
        currentColor = oldColor

      case _ =>
        println(s"FILL is not implemented for command: $cmd")
    }
  }

  // --------------------------------------------------------------------------
  // Helper methods
  // --------------------------------------------------------------------------

  /**
   * Checks if (x, y) is within the bounding box (if any). If no bounding box,
   * everything is considered in-bounds.
   */
  private def withinBoundingBox(x: Int, y: Int): Boolean = {
    boundingBox match {
      case Some(BoundingBox(x1, y1, x2, y2)) =>
        x >= x1 && x <= x2 && y >= y1 && y <= y2
      case None => true
    }
  }

  /**
   * Draws the pixel at (x, y) only if in bounding box.
   */
  private def drawPixelIfInBounds(x: Int, y: Int, color: String): Unit = {
    if (withinBoundingBox(x, y)) {
      println(s"Drawing pixel at ($x, $y) in color $color")
    }
  }
}
