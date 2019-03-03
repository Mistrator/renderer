package renderer

import scala.collection.mutable.Buffer

class World {
  
  val objects = Buffer[WorldObject]()
  val camera = new Camera()
}