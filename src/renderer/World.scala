package renderer

import scala.collection.mutable.Buffer

class World(val objects: Buffer[WorldObject], val camera: Camera) {
  
  /**
   * Is the point within some object's collision box.
   */
  def collides(point: Vector4) = objects.exists(o => o.isInCollisionBox(point))
}
