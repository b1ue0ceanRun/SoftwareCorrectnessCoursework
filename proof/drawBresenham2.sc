// #Sireum #Logika

import org.sireum._
@pure def abs(x: Z): Z = {
  Contract(
    Ensures(Res == (if (x >= 0) x else -x))
  )
  if (x >= 0) {
    return x
  } else {
    return -x
  }
}

@pure def drawBresenham(x1: Z, y1: Z, x2: Z, y2: Z): Unit = {
  var dx: Z = abs(x2 - x1)
  var dy: Z = -abs(y2 - y1)
  val sx: Z = if (x1 < x2) 1 else -1
  val sy: Z = if (y1 < y2) 1 else -1
  var err: Z = dx + dy
  var cx: Z = x1
  var cy: Z = y1
  assume(sx == 1 || sx == -1)
  assume(sy == 1 || sy == -1)

  while (true) {
    Invariant(
      Modifies(cx, cy, err),
      // Ensure we are moving in the correct x direction from the start point.
      sx * (cx - x1) >= 0,

      // Ensure we are not overshooting the x end point.
      sx * (x2 - cx) >= 0,

      // Ensure we are moving in the correct y direction from the start point.
      sy * (cy - y1) >= 0,

      // Ensure we are not overshooting the y end point.
      sy * (y2 - cy) >= 0


    )

    if (cx == x2 && cy == y2) {
      return
    }

    val e2: Z = 2 * err


    if (e2 >= dy && cx != x2) {
      err = err + dy
      cx = cx + sx
    }

    if (e2 <= dx && cy != y2) {
      err = err + dx
      cy = cy + sy
    }
  }

  assert(cx == x2 && cy == y2)
}
