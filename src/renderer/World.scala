package renderer

import scala.collection.mutable.Buffer

class World {
  
  val objects = Buffer[WorldObject]()
  val camera = new Camera(Vector4(0, 0, 0), Vector4(0, 0, 1))
}