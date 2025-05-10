// #Sireum #Logika
import org.sireum._

//distSq <= radius*radius + 1 & distSq >= radius*radius - 1

val cx: Z = randomInt()
assume(cx < 5 & cx > -5)

val cy: Z = randomInt()
assume(cy < 5 & cy > -5)

val radius: Z = randomInt()
assume(radius > 1 & radius < 10)

def drawPixel(a: Z, b: Z, x: Z, y: Z): Z = {
  Contract(
    Requires(
      (a == cx + x & b == cy + y) |
      (a == cx - x & b == cy + y) |
      (a == cx + x & b == cy - y) |
      (a == cx - x & b == cy - y) |
      (a == cx + y & b == cy + x) |
      (a == cx - y & b == cy + x) |
      (a == cx + y & b == cy - x) |
      (a == cx - y & b == cy - x)),
    Ensures( // ensure points a,b has a radius close to the ideal x^2+y^2
      Res == x*x + y*y ))
  val dx: Z = a - cx
  val dy: Z = b - cy

  // distance squared from pixel (a, b) to circle center (cx, cy)
  return dx * dx + dy * dy
}

