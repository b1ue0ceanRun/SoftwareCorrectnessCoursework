// #Sireum
// @Logika: --strict --timeout 2

import org.sireum._

// Define abs function with contract
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

// Define drawBresenham function
@pure def drawBresenham(x1: Z, y1: Z, x2: Z, y2: Z): Unit = {
  Contract(
    Requires(T), // Use T if no specific precondition
    Ensures(T)   // Use T if no specific postcondition
  )

  var dx: Z = abs(x2 - x1)
  var dy: Z = -abs(y2 - y1)
  val sx: Z = if (x1 < x2) 1 else -1
  val sy: Z = if (y1 < y2) 1 else -1
  var err: Z = dx + dy
  var cx: Z = x1
  var cy: Z = y1

  // Initial state assertions
  assert(dx >= 0)
  assert(dy <= 0)
  assert(err == dx + dy)
  assert(cx == x1)
  assert(cy == y1)

  // Verify that the current point is within acceptable deviation from the line
  assert(
    abs((cx - x1) * (y2 - y1) - (cy - y1) * (x2 - x1)) <= dx + abs(dy)
  )

  while (T) { // Note: Replace T with actual loop condition in real use
    Invariant(
      Modifies(cx, cy, err),
      abs((cx - x1) * (y2 - y1) - (cy - y1) * (x2 - x1)) <= dx + abs(dy)
    )

    assert(
      abs((cx - x1) * (y2 - y1) - (cy - y1) * (x2 - x1)) <= dx + abs(dy)
    )

    if (cx == x2 && cy == y2) {
      assert(cx == x2 && cy == y2)
      return
    }

    val e2: Z = err * 2
    assert(e2 == err * 2)

    if (e2 >= dy) {
      val oldErr: Z = err
      val oldCx: Z = cx

      err = err + dy
      cx = cx + sx

      assert(err == oldErr + dy)
      assert(cx == oldCx + sx)
    }

    if (e2 <= dx) {
      val oldErr: Z = err
      val oldCy: Z = cy

      err = err + dx
      cy = cy + sy

      assert(err == oldErr + dx)
      assert(cy == oldCy + sy)
    }

    // Removed Deduce here
  }
}
