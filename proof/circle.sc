// #Sireum #Logika
import org.sireum._
import org.sireum.justification._

def drawPixel(a: Z, b: Z): Unit = {
  // add to state
  // pass
}


def drawCircle(): Unit = {
  var x: Z = 0
  Deduce(|- (x == 0))

  var y: Z = randomInt() //At(y, 0)
  assume(y > 0)

  var d: Z = 1 - y


  val centerX: Z = randomInt()
  val centerY: Z = randomInt()

  // plot eight points at once
  def plotEightPoints(x1: Z, y1: Z): Unit = {
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

  // iterate over one octant and reflect poZs to complete the circle
  while (x < y) {
    x = x + 1
    if (d < 0) {
      d = d + 2 * x + 1
    } else {
      y = y - 1
      d = d + 2 * (x - y) + 1
    }
    plotEightPoints(x, y)
  }
}