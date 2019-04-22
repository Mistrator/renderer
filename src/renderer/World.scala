package renderer

import scala.collection.mutable.Buffer

class World(val objects: Buffer[WorldObject], val camera: Camera) {
  
  /**
   * Is a point inside the collision box of some object in the world
   */
  def collides(point: Vector4) = objects.exists(o => o.isInCollisionBox(point))
}
