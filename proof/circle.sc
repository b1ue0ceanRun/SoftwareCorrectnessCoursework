// #Sireum #Logika
import org.sireum._
import org.sireum.justification._

//distSq <= radius*radius + 1 & distSq >= radius*radius - 1

val cx: Z = randomInt()
assume(cx < 20 & cx > -20)

val cy: Z = randomInt()
assume(cy < 20 & cy > -20)

val radius: Z = randomInt()
assume(radius > 0 & radius < 20)


def drawPixel(a: Z, b: Z, x: Z, y: Z): Unit = {
  Contract(
    Requires(
      (a == cx + x || a == cx - x || a == cx + y || a == cx - y) &&
      (b == cy + y || b == cy - y || b == cy + x || b == cy - x),
      x >= 0,
      y >= 0,
      y <= radius
    )
  )
  val dx: Z = a - cx
  val dy: Z = b - cy

  val distSq: Z = dx * dx + dy * dy

}


def drawCircle(): Unit = {
  var x: Z = 0
  var y: Z = radius
  var d: Z = 1 - radius

  drawPixel(cx + x, cy + y, x, y)
  drawPixel(cx - x, cy + y, x, y)
  drawPixel(cx + x, cy - y, x, y)
  drawPixel(cx - x, cy - y, x, y)
  drawPixel(cx + y, cy + x, x, y)
  drawPixel(cx - y, cy + x, x, y)
  drawPixel(cx + y, cy - x, x, y)
  drawPixel(cx - y, cy - x, x, y)

  while (x < y) {
    Invariant(
      Modifies(x, y, d),
      x >= 0,
      y <= radius
    )

    x = x + 1

    if (d < 0) {
      d = d + 2 * x + 1
    } else {
      y = y - 1
      d = d + 2 * (x - y) + 1
    }

    drawPixel(cx + x, cy + y, x, y)
    drawPixel(cx - x, cy + y, x, y)
    drawPixel(cx + x, cy - y, x, y)
    drawPixel(cx - x, cy - y, x, y)
    drawPixel(cx + y, cy + x, x, y)
    drawPixel(cx - y, cy + x, x, y)
    drawPixel(cx + y, cy - x, x, y)
    drawPixel(cx - y, cy - x, x, y)
  }
}
