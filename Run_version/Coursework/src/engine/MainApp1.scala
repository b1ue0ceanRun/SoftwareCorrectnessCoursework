package engine

object MainApp {
  def main(args: Array[String]): Unit = {
    val script =
      """(BOUNDING-BOX (0 0) (16 12))
        |(CIRCLE (12 12) 3)
        |(CIRCLE (7 8) 6)
        |(LINE (7 8) (12 12))
        |(DRAW red (LINE (0 0) (16 12)) (CIRCLE (8 8) 5))
        |(FILL blue (RECTANGLE (1 1) (4 4)))
        |(TEXT-AT (10 10) "Hello!")
        |""".stripMargin

    // Parse
    val commands = DSLParser.parseCommands(script)

    // Create engine
    val engine = new DrawingEngine

    // Execute the commands
    engine.interpretCommands(commands)
  }
}

