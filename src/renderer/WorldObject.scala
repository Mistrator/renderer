package renderer

import scala.math.sin
import scala.math.cos

class WorldObject(val model: Model, var worldMatrix: Matrix4, var collisionBox: Option[CollisionBox]) {
  
  /**
   * Check whether a point is inside the collision box.
   * Returns false if there is no collision box.
   */
  def isInCollisionBox(point: Vector4) : Boolean = {
    collisionBox match {
      case Some(b) => {
        // the collision box doesn't rotate with the object, it only translates
        val center = worldMatrix * Vector4.zero
        val bottomLeft = center + b.bottomLeftCorner
        val topRight = center + b.topRightCorner
        
        return bottomLeft.x <= point.x && point.x <= topRight.x && 
        bottomLeft.y <= point.y && point.y <= topRight.y &&
        bottomLeft.z <= point.z && point.z <= topRight.z
      }
      case None => return false
    }
  }
}

object WorldObject {
  
  /**
   * Build a matrix that maps the model from model space
   * to desired position and orientation in world space.
   * Rotation is given in radians in Euler angles (x, y, z).
   * The coordinate system is left-handed, so positive rotation is counter-clockwise when looking towards
   * the positive direction of a coordinate axis.
   */
  def buildWorldMatrix(position: Vector4, eulerRotation: Vector4) : Matrix4 = {
    val matr = Helpers.buildTransRotMatrix(position, eulerRotation)
    matr._1 * matr._2
  }
  
  def buildWorldMatrix(position: Vector4) : Matrix4 = buildWorldMatrix(position, Vector4(0, 0, 0))
}
