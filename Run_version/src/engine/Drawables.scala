// src/main/scala/engine/Drawable.scala
package engine

import java.awt.Color

sealed trait Drawable

case class DLine(x1: Int, y1: Int, x2: Int, y2: Int, color: Color) extends Drawable
case class DRect(x1: Int, y1: Int, x2: Int, y2: Int, color: Color) extends Drawable
case class DCircle(cx: Int, cy: Int, r: Int, color: Color) extends Drawable
case class DText(x: Int, y: Int, text: String, color: Color) extends Drawable
