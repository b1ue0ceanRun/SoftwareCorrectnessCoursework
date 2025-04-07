package engine

object DSLParser {
  // Entry point: parse the entire DSL script into a List of Command
  def parseCommands(script: String): List[Command] = {
    // A simple approach: split by lines or parentheses, filter empties, parse each
    // For real usage, you might want a more robust parser, but this gives an idea.

    // This is a naive line-based approach (one command per line).
    // If your commands are multiline, you’ll need a more sophisticated method.
    val lines = script
      .split("\n")
      .map(_.trim)
      .filterNot(_.isEmpty)
      .toList

    lines.map(parseLine)
  }

  private def parseLine(line: String): Command = {
    // A naive approach that tries to match known patterns via regex.
    // Example: (LINE (x1 y1) (x2 y2)) => we can do a pattern-match.
    // For robust code, you’d want a full tokenizer plus a small parser combinator.
    // We’ll do a few quick pattern matches:

    val boundingBoxRegex = """^\(BOUNDING-BOX\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)\)$""".r
    val lineRegex         = """^\(LINE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)\)$""".r
    val rectRegex         = """^\(RECTANGLE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)\)$""".r
    val circleRegex       = """^\(CIRCLE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*(\d+)\)$""".r
    val textRegex         = """^\(TEXT-AT\s*\(\s*(\d+)\s+(\d+)\s*\)\s*(.*?)\)$""".r
    val drawRegex         = """^\(DRAW\s+(\S+)\s+(.*)\)$""".r
    val fillRegex         = """^\(FILL\s+(\S+)\s+(.*)\)$""".r

    line match {
      // BOUNDING-BOX
      case boundingBoxRegex(x1, y1, x2, y2) =>
        BoundingBox(x1.toInt, y1.toInt, x2.toInt, y2.toInt)

      // LINE
      case lineRegex(x1, y1, x2, y2) =>
        Line(x1.toInt, y1.toInt, x2.toInt, y2.toInt)

      // RECTANGLE
      case rectRegex(x1, y1, x2, y2) =>
        Rectangle(x1.toInt, y1.toInt, x2.toInt, y2.toInt)

      // CIRCLE
      case circleRegex(x, y, r) =>
        Circle(x.toInt, y.toInt, r.toInt)

      // TEXT-AT
      // The text match (.*?) captures everything after the coordinates.
      // If the text might contain parentheses, you need a more advanced parser.
      case textRegex(x, y, textString) =>
        TextAt(x.toInt, y.toInt, textString.trim)

      // DRAW
      // The commands portion is everything after the color. We can parse *that* recursively:
      case drawRegex(color, rest) =>
        // Suppose user typed: (DRAW red (LINE (0 0) (1 1)) (CIRCLE (5 5) 3))
        // We might parse out all subcommands. This is trickier if they are on one line.
        // As an example, let's do something simplistic:
        val subCmds = parseSubCommands(rest.trim)
        Draw(color, subCmds)

      // FILL
      case fillRegex(color, rest) =>
        // We expect just one subcommand, e.g. (FILL green (CIRCLE (5 5) 3))
        val subCmds = parseSubCommands(rest.trim)
        if (subCmds.nonEmpty) Fill(color, subCmds.head)
        else throw new IllegalArgumentException("FILL must contain exactly one subcommand")

      // Otherwise we can’t parse it
      case _ =>
        throw new IllegalArgumentException(s"Cannot parse line: $line")
    }
  }

  // Very naive subparser for commands inside (DRAW color g1 g2 ...)
  // Realistically, you'd want a more robust approach or a real parser library.
  private def parseSubCommands(sub: String): List[Command] = {
    // We can cheat by splitting on top-level parentheses. This is non-trivial to do correctly
    // if parentheses are nested in text. So we assume the user’s DSL is well-formed.
    // For demonstration, we do a naive approach:
    //   e.g.  (LINE (0 0) (1 1)) (CIRCLE (5 5) 3)
    //   =>   List("(LINE (0 0) (1 1))", "(CIRCLE (5 5) 3)")
    //
    // Then parseLine(...) on each piece.
    val splitted = splitTopLevelCommands(sub)
    splitted.map(parseLine)
  }

  // Dummy function that tries to find parentheses at top level
  private def splitTopLevelCommands(sub: String): List[String] = {
    val result  = scala.collection.mutable.ListBuffer.empty[String]
    var balance = 0
    val sb      = new StringBuilder

    for (c <- sub) {
      if (c == '(') {
        // If we see a '(', we start a new command if balance was 0 and sb is non-empty
        if (balance == 0 && sb.nonEmpty) {
          // flush
          result += sb.toString.trim
          sb.clear()
        }
        balance += 1
      }
      sb.append(c)
      if (c == ')') {
        balance -= 1
        // If we come back to zero, we close that subcommand
        if (balance == 0) {
          result += sb.toString.trim
          sb.clear()
        }
      }
    }
    // Possibly flush if anything is left
    if (sb.nonEmpty) {
      result += sb.toString.trim
    }
    result.toList.filter(_.nonEmpty)
  }
}

