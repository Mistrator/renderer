package renderer

class WorldObject(val model: Model, var worldMatrix: Matrix4) {
  
}

object WorldObject {
  
  // build a matrix that maps the model from model space
  // to desired position and orientation in world space 
  def buildWorldMatrix(position: Vector4, orientation: Vector4) : Matrix4 = {
    ???
  }
  
  def buildWorldMatrix(position: Vector4) : Matrix4 = buildWorldMatrix(position, Vector4(0, 0, 1))
}