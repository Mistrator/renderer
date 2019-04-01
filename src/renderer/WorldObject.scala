package renderer

import scala.math.sin
import scala.math.cos

class WorldObject(val model: Model, var worldMatrix: Matrix4)

object WorldObject {
  
  // build a matrix that maps the model from model space
  // to desired position and orientation in world space
  // rotation is given in radians in Euler angles (x, y, z)
  // the coordinate system is left-handed, so positive rotation is counter-clockwise when looking towards
  // the positive direction of a coordinate axis
  def buildWorldMatrix(position: Vector4, eulerRotation: Vector4) : Matrix4 = {
    val matr = Helpers.buildTransRotMatrix(position, eulerRotation)
    matr._1 * matr._2
  }
  
  def buildWorldMatrix(position: Vector4) : Matrix4 = buildWorldMatrix(position, Vector4(0, 0, 0))
}