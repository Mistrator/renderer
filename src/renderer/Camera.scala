package renderer

import scala.math.sin
import scala.math.cos

class Camera(var position: Vector4, var orientation: Vector4) {
  
  var viewMatrix: Matrix4 = buildViewMatrix(Vector4(0, 0, 0), Vector4(0, 0, 0))
  
  /**
   * Construct a view matrix representing the camera's position and orientation in the world space.
   * Rotation is given in radians in Euler angles (x, y, z) in a left-handed coordinate system,
   * so positive rotation is counter-clockwise when looking towards the positive direction
   * of a coordinate axis
   */
  def buildViewMatrix(position: Vector4, orientation: Vector4) = {
    val mat = Helpers.buildTransRotMatrix(position * (-1), orientation * (-1))
    mat._2 * mat._1 // first translate, then rotate
  }
  
  def buildViewMatrix() : Matrix4 = buildViewMatrix(this.position, this.orientation)
}
