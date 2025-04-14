
object Drawer {
  // panel ref
  private var graphicPanel: GraphicPanel = null
  
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
    // Just clear the display before drawing
    //if (graphicPanel != null) {
      //  graphicPanel.clearPixels();
   // }
    
    // Clear pixel state (but keep instructions)
    //State.clearPixels();
    
    State.getState.foreach {
        case Circle(x, y, r) => 
            Drawer.drawCircle(x, y, r)
            // update after each shape
            updateGraphics()
            Thread.sleep(500) // pause for effect
        case Line(x0, y0, x1, y1) =>
          Drawer.drawLine(x0, y0, x1, y1)
            // update after each shape
            updateGraphics()
            Thread.sleep(500) // pause for effect
        case Rectangle(x0, y0, x1, y1) =>
          Drawer.drawRectangle(x0, y0, x1, y1)
          // update after each shape
          updateGraphics()
          Thread.sleep(500) // pause for effect
        }
    }
  
  // sync panel with state
  private def updateGraphics(): Unit = {
  if (graphicPanel != null) {
    //graphicPanel.clearPixels() // clear existing pixels
    
    // add all current pixels from state
    State.getPixels.foreach { case (x, y) => 
      graphicPanel.addPixel(x, y)
    }
    graphicPanel.update() // repaint
  }
}
}


// (CIRCLE (400 400) 30):