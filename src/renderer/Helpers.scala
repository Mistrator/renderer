package renderer

import scala.math.abs
import scala.math.sin
import scala.math.cos

object Helpers {
  
  val DoubleEps = 1e-9
  
  def doubleEqual(first: Double, second: Double) = abs(first - second) < DoubleEps
  
  // build a matrix that maps a vertex
  // to desired position and orientation
  // rotation is given in radians in Euler angles (x, y, z)
  // the coordinate system is left-handed, so positive rotation is counter-clockwise when looking towards
  // the positive direction of a coordinate axis
  // construction using: http://www.opengl-tutorial.org/assets/faq_quaternions/index.html
  def buildTransRotMatrix(position: Vector4, eulerRotation: Vector4) : (Matrix4, Matrix4) = {
    val a = cos(eulerRotation.x)
    val b = sin(eulerRotation.x)
    val c = cos(eulerRotation.y)
    val d = sin(eulerRotation.y)
    val e = cos(eulerRotation.z)
    val f = sin(eulerRotation.z)
    
    val ad = a*d
    val bd = b*d
    
    val rotation = Matrix4(Array(Array(c*e, -c*f, d, 0.0), Array(bd*e + a*f, -bd*f + a*e, -b*c, 0.0), 
        Array(-ad*e + b*f, ad*f + b*e, a*c, 0.0), Array(0.0, 0.0, 0.0, 1.0)))
        
    val translation = Matrix4(Array(Array(1.0, 0.0, 0.0, position.x), Array(0.0, 1.0, 0.0, position.y),
        Array(0.0, 0.0, 1.0, position.z), Array(0.0, 0.0, 0.0, 1.0)))
    
    return (translation, rotation)
  }
}