package renderer

import scala.math.abs

object Helpers {
  
  val DoubleEps = 1e-9
  
  def doubleEqual(first: Double, second: Double) = abs(first - second) < DoubleEps
}