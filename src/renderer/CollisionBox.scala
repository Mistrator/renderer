package renderer

class CollisionBox(val width: Double, val height: Double, val depth: Double)  {
  
  def bottomLeftCorner = Vector4(-width/2, -height/2, -depth/2)
  
  def topRightCorner = Vector4(width/2, height/2, depth/2)
}