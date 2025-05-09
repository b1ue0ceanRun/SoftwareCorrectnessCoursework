// #Sireum #Logika
import org.sireum._
import org.sireum.justification._

def drawPixel(a: Z, b: Z, cx: Z, cy: Z, rad_square: Z): Unit = {
  val dx: Z = a - cx
  val dy: Z = b - cy
  val distSq: Z = dx * dx + dy * dy

  _drawPixel(distSq, rad_square)
}

def _drawPixel(distSq: Z, rad_square: Z): Unit = {
  Contract(
    Ensures(distSq <= rad_square + 1 & distSq >= rad_square - 1)
  )
}

def _drawCircle(cx: Z, cy: Z, radius: Z): Unit = {
  Contract(
    Requires(radius > 0)
  )

  var x: Z = 0
  var y: Z = radius
  var d: Z = 1 - radius

  val rad_square: Z = radius * radius

  def plotEightPoints(x1: Z, y1: Z): Unit = {
    Contract(Requires(x1 >= 0, y1 >= 0))

    drawPixel(cx + x1, cy + y1, cx, cy, rad_square)
    drawPixel(cx - x1, cy + y1, cx, cy, rad_square)
    drawPixel(cx + x1, cy - y1, cx, cy, rad_square)
    drawPixel(cx - x1, cy - y1, cx, cy, rad_square)
    drawPixel(cx + y1, cy + x1, cx, cy, rad_square)
    drawPixel(cx - y1, cy + x1, cx, cy, rad_square)
    drawPixel(cx + y1, cy - x1, cx, cy, rad_square)
    drawPixel(cx - y1, cy - x1, cx, cy, rad_square)
  }

  plotEightPoints(x, y)

  while (x < y) {
    Invariant(
      x >= 0,
      y >= 0,
      x <= y,
      y <= radius
    )

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

def drawCircle(): Unit = {
  val centerX: Z = randomInt()
  val centerY: Z = randomInt()
  val radius: Z = randomInt()

  assume(radius > 0)

  _drawCircle(centerX, centerY, radius)
}
