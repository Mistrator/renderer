package renderer

class Camera {
  
  var viewMatrix: Matrix4 = buildViewMatrix(Vector4(0, 0, 0), Vector4(0, 0, 1))
  
  def buildViewMatrix(position: Vector4, orientation: Vector4) = {
    ???
  }
}
