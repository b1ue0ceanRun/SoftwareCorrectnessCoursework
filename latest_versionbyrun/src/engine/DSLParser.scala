// src/main/scala/engine/DSLParser.scala
package engine

object DSLParser {

  def parseCommands(input: String): List[Command] = {
    val tokens = tokenize(input)
    val (commands, rest) = parseMany(tokens)
    if (rest.nonEmpty)
      throw new IllegalArgumentException("Unexpected tokens: " + rest.mkString(" "))
    commands
  }

  // === 1. Tokenizer ====================================================

  private def tokenize(input: String): List[String] = {
    val pattern = """\s*(?:(\()|(\))|(".*?")|([^\s()]+))""".r
    val tokens = pattern.findAllMatchIn(input).map { m =>
      if (m.group(1) != null) "("
      else if (m.group(2) != null) ")"
      else if (m.group(3) != null) m.group(3) // quoted string
      else m.group(4)
    }.toList
    tokens
  }

  // === 2. Parser =======================================================

  private def parseMany(tokens: List[String]): (List[Command], List[String]) = {
    var rest = tokens
    val commands = scala.collection.mutable.ListBuffer.empty[Command]
    while (rest.nonEmpty && rest.head == "(") {
      val (cmd, newRest) = parseOne(rest)
      commands += cmd
      rest = newRest
    }
    (commands.toList, rest)
  }

  private def parseOne(tokens: List[String]): (Command, List[String]) = tokens match {
    case "(" :: "BOUNDING-BOX" :: "(" :: x1 :: y1 :: ")" :: "(" :: x2 :: y2 :: ")" :: ")" :: rest =>
      (BoundingBox(x1.toInt, y1.toInt, x2.toInt, y2.toInt), rest)

    case "(" :: "LINE" :: "(" :: x1 :: y1 :: ")" :: "(" :: x2 :: y2 :: ")" :: ")" :: rest =>
      (Line(x1.toInt, y1.toInt, x2.toInt, y2.toInt), rest)

    case "(" :: "RECTANGLE" :: "(" :: x1 :: y1 :: ")" :: "(" :: x2 :: y2 :: ")" :: ")" :: rest =>
      (Rectangle(x1.toInt, y1.toInt, x2.toInt, y2.toInt), rest)

    case "(" :: "CIRCLE" :: "(" :: x :: y :: ")" :: r :: ")" :: rest =>
      (Circle(x.toInt, y.toInt, r.toInt), rest)

    case "(" :: "TEXT-AT" :: "(" :: x :: y :: ")" :: str :: ")" :: rest =>
      val cleanText = if (str.startsWith("\"")) str.drop(1).dropRight(1) else str
      (TextAt(x.toInt, y.toInt, cleanText), rest)

    case "(" :: "DRAW" :: color :: tail =>
      val (cmds, after) = parseMany(tail)
      after match {
        case ")" :: rest2 => (Draw(color, cmds), rest2)
        case _ => throw new IllegalArgumentException("Missing closing ')' in DRAW")
      }

    case "(" :: "FILL" :: color :: tail =>
      val (cmds, after) = parseMany(tail)
      if (cmds.size != 1) throw new IllegalArgumentException("FILL takes exactly one shape command")
      after match {
        case ")" :: rest2 => (Fill(color, cmds.head), rest2)
        case _ => throw new IllegalArgumentException("Missing closing ')' in FILL")
      }

    case _ =>
      throw new IllegalArgumentException("Cannot parse tokens: " + tokens.take(10).mkString(" "))
  }
}
