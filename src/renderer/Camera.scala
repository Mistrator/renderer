package renderer

import scala.math.sin
import scala.math.cos

class Camera(var position: Vector4, var orientation: Vector4) {
  
  var viewMatrix: Matrix4 = buildViewMatrix(Vector4(0, 0, 0), Vector4(0, 0, 0))
  
  def buildViewMatrix(position: Vector4, orientation: Vector4) = Helpers.buildTransRotMatrix(position * (-1), orientation * (-1))
  
  /*
  def buildViewMatrix(position: Vector4, orientation: Vector4) = {
    val cosPitch = cos(orientation.y)
    val sinPitch = sin(orientation.y)
    val cosYaw = cos(orientation.x)
    val sinYaw = sin(orientation.x)
    
    val xAxis = Vector4(cosYaw, 0, -sinYaw)
    val yAxis = Vector4(sinYaw*sinPitch, cosPitch, cosYaw*sinPitch)
    val zAxis = Vector4(sinYaw*cosPitch, -sinPitch, cosPitch*cosYaw) * (-1)
    
    new Matrix4(Array(Array(xAxis.x, xAxis.y, xAxis.z, xAxis*position*(-1)),
        Array(yAxis.x, yAxis.y, yAxis.z, yAxis*position*(-1)),
        Array(zAxis.x, zAxis.y, zAxis.z, zAxis*position*(-1)),
        Array(0, 0, 0, 1)))
  }*/
  
  def buildViewMatrix() : Matrix4 = buildViewMatrix(this.position, this.orientation)
}
