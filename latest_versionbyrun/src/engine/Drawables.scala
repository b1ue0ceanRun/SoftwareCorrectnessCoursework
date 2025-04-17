// engine/Drawable.scala
package engine

import java.awt.Color

sealed trait Drawable

case class DPixel(x: Int, y: Int, color: Color) extends Drawable
case class DText(x: Int, y: Int, text: String, color: Color) extends Drawable
