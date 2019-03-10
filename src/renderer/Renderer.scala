package renderer

import scala.collection.mutable.Buffer
import scala.math.min

class Renderer {
  
  def buildProjectionMatrix(near: Double, far: Double, fov: Double) = ???
  
  def render(world: World) : Array[Triangle] = {
    ???
  }
  
  private def closestVertexDist(trg: Triangle) : Double = {
    var minDist = Double.MaxValue
    for (vert <- trg.vertices) {
      minDist = min(minDist, vert.position.z)
    }
    minDist
  }
  
  private def toClipSpace(world: World) : Array[Triangle] = {
    val projMatrix = buildProjectionMatrix(0.1, 100.0, 90.0)
    val viewMatrix = world.camera.buildViewMatrix()
    
    val projected = Buffer[Triangle]()
    
    for (obj <- world.objects) {
      val worldMatrix = obj.worldMatrix
      for (trg <- obj.model.mesh) {
        projected += trg * worldMatrix * viewMatrix * projMatrix
      }
    }
    
    // sort triangles by their closest vertex to camera such that furthest triangles come first  
    projected.sortWith((a, b) => closestVertexDist(a) > closestVertexDist(b))
    
    return projected.toArray
  }
}