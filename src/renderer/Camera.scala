package renderer

class Camera(var position: Vector4, var orientation: Vector4) {
  
  var viewMatrix: Matrix4 = buildViewMatrix(Vector4(0, 0, 0), Vector4(0, 0, 1))
  
  def buildViewMatrix(position: Vector4, orientation: Vector4) = {
    Matrix4.identity // placeholder for testing
  }
  
  def buildViewMatrix() : Matrix4 = buildViewMatrix(this.position, this.orientation)
}
