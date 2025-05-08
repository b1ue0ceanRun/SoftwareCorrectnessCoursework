// #Sireum #Logika
import org.sireum._
import org.sireum.justification._

// in loop: (x, y) satisfies: x2+y2≤r2+xx2+y2≤r2+x
  // d tracks the error to determine whether the y-coordinate should decrease.


val centerX: Z = randomInt()
val centerY: Z = randomInt()
val radius: Z = randomInt()
val rad_square: Z = radius*radius


def drawPixel(a: Z, b: Z): Unit = {
  val dx = a - centerX
  val dy = b - centerY
  val distSq = dx * dx + dy * dy
  _drawPixel(distSq)
}

def _drawPixel(distSq: Z): Unit = {
  Contract(
    Ensures(distSq >= rad_square - 1 & distSq <= rad_square + 1)
  )

}




def drawCircle(): Unit = {
  var x: Z = 0
  Deduce(|- (x == 0))

  var y: Z = radius
  assume(y > 0)

  var d: Z = 1 - y
  Deduce(|- (d < y))
  Deduce(|- (d <= 0))

  // assume within bounds

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