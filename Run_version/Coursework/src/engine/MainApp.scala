package engine

object MainApp {
  def main(args: Array[String]): Unit = {
    // 1. Create some example commands
    val commands: List[Command] = List(
      BoundingBox(0, 0, 10, 10),         // sets bounding box
      Line(0, 0, 10, 10),               // diagonal line
      Rectangle(2, 2, 5, 5),            // small rectangle
      Circle(8, 8, 5),                  // circle partially clipped
      TextAt(3, 3, "Hello!"),
      Draw("red", List(
        Circle(5, 5, 3),
        Line(0, 10, 10, 0)
      )),
      Fill("blue", Rectangle(0, 0, 2, 2)), // fill portion of the bounding box
      Fill("green", Circle(8, 8, 2))      // fill smaller circle near (8,8)
    )

    // 2. Create the drawing engine
    val engine = new DrawingEngine

    // 3. Interpret the commands
    engine.interpretCommands(commands)

    // The console output will show each “drawing” or “filling” action,
    // with bounding box checks. This is how you can verify correctness.
  }
}
