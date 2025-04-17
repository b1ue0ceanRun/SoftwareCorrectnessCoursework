// src/main/scala/engine/ScalaBridge.scala
package engine

import java.util
import scala.jdk.CollectionConverters._

object ScalaBridge {
  private val engine = new DrawingEngine

  def interpret(script: String): util.List[Drawable] = {
    val cmds = DSLParser.parseCommands(script)
    engine.interpretCommands(cmds)
    engine.getDrawables.asJava
  }
}
