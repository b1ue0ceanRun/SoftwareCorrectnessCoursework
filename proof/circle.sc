// #Sireum #Logika
import org.sireum._

val cx: Z = randomInt()
assume(cx == 5)
val cy: Z = randomInt()
assume(cy == 5)
val radius: Z = randomInt()
assume(radius == 10)
Deduce(|- (radius >= 0))

val delta: Z = 10


/*
Contract(
  Requires(
    // Known from the loop invariants
    x >= 0,
    y >= 0,
    y <= radius,
    d == (x + 1)*(x + 1) + y*y - radius*radius - y
  ),
  Ensures(
    (a - cx)*(a - cx) + (b - cy)*(b - cy) >= radius*radius - delta &
      (a - cx)*(a - cx) + (b - cy)*(b - cy) <= radius*radius + delta
  ))
 */

def _drawPixel(a: Z, b: Z, x: Z, y: Z): Z = {
  val dx: Z = a - cx
  val dy: Z = b - cy
  return dx * dx + dy * dy
}

def drawPixel(a: Z, b: Z, x: Z, y: Z): Z = {

  val dx = a - cx
  val dy = b - cy
  return dx*dx + dy*dy
}

def drawCircle(): Unit = {
  assume(radius > 0)
  var x: Z = 0
  Deduce(|- (x == 0))

  var y: Z = radius

  var d: Z = 1 - radius
  
  val measure_entry: Z = y - x
  Deduce(|- (measure_entry > 0))

  // octant pixels
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
      y >= 0,
      y <= radius,
      d == (x + 1)*(x + 1) + y*y - radius*radius - y, //midpoint decision
    )
    val measure_before: Z = y - x
    x = x + 1

    if (d < 0) {
      d = d + 2 * x + 1
    } else {
      y = y - 1
      d = d + 2 * (x - y) + 1
    }

    val measure_after: Z = y - x
    Deduce(|- (measure_after < measure_before))

    drawPixel(cx + x, cy + y, x, y)
    drawPixel(cx - x, cy + y, x, y)
    drawPixel(cx + x, cy - y, x, y)
    drawPixel(cx - x, cy - y, x, y)
    drawPixel(cx + y, cy + x, x, y)
    drawPixel(cx - y, cy + x, x, y)
    drawPixel(cx + y, cy - x, x, y)
    drawPixel(cx - y, cy - x, x, y)
  }
    // termination condition
    Deduce(|- (!(x < y)))

  }
  