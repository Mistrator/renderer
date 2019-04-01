package renderer

import scala.math.sin
import scala.math.cos

class Camera(var position: Vector4, var orientation: Vector4) {
  
  var viewMatrix: Matrix4 = buildViewMatrix(Vector4(0, 0, 0), Vector4(0, 0, 0))
  
  def buildViewMatrix(position: Vector4, orientation: Vector4) = {
    val mat = Helpers.buildTransRotMatrix(position * (-1), orientation * (-1))
    mat._2 * mat._1 // first translate, then rotate
  }
  
  def buildViewMatrix() : Matrix4 = buildViewMatrix(this.position, this.orientation)
}
