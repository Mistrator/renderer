package renderer

class WorldObject(val model: Model, var worldMatrix: Matrix4) {
  
  // build a matrix that maps the model from model space
  // to desired position and orientation in world space 
  def buildWorldMatrix(position: Vector4, orientation: Vector4) = {
    ???
  }
}
