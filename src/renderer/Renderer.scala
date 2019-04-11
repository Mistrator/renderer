package renderer

import scala.collection.mutable.Buffer
import scala.math.min
import scala.math.abs

class Renderer {
  
  def buildProjectionMatrix(near: Double, far: Double) = {
      Matrix4(Array(Array(1, 0, 0, 0), Array(0, 1, 0, 0), 
        Array(0, 0, (far+near)/(far-near), -(2*far*near)/(far-near)), Array(0, 0, 1, 0)))
  }
  
  /*
   * Return the parts of the triangle that are inside the view volume
   */
  def clipTriangle(trg: Triangle) : Array[Triangle] = {
    Array(trg)
  }
  
  def render(world: World) : Array[Triangle] = {
    val clipSpaceTrg = toClipSpace(world)
    
    // clip triangles to view volume
    val clippedTrg = clipSpaceTrg.foldLeft(Array[Triangle]())((a, b) => a ++ clipTriangle(b))
    
    return clippedTrg.map(t => new Triangle(t.vertices.map(v => new Vertex(v.position.homogenize(), v.color)), t.material)).filter(x => isVisible(x))
  }
  
  /*
   * Compare triangles by their distance, one with first non-overlapping vertex that has larger z comes first
   */
  private def trgCompare(a: Triangle, b: Triangle) : Boolean = {
    val zDistA = a.vertices.map(v => v.position.z).sortWith(_ > _)
    val zDistB = b.vertices.map(v => v.position.z).sortWith(_ > _)
    
    for (i <- 0 until a.vertices.length) {
      val az = zDistA(i)
      val bz = zDistB(i)
      
      // check that z coordinates don't overlap
      if (abs(az-bz) > 1e-3) {
        return az > bz  
      }
    }
    
    // the triangles overlap completely, so we can just say that the second one comes first
    return false
  }
  
  private def toClipSpace(world: World) : Array[Triangle] = {
    val projMatrix = buildProjectionMatrix(Constants.NearPlane, Constants.FarPlane)
    val viewMatrix = world.camera.buildViewMatrix()
    
    val projected = Buffer[Triangle]()
    
    for (obj <- world.objects) {
      val worldMatrix = obj.worldMatrix
      for (trg <- obj.model.mesh) {
        projected += trg * worldMatrix * viewMatrix * projMatrix
      }
    }
    
    // sort triangles by their closest vertex to camera such that furthest triangles come first  
    return projected.sortWith(trgCompare).toArray
  }
  
  private def isVisible(trg: Triangle) : Boolean = {
    trg.vertices.exists(v => isInClipVolume(v))
  }
  
  private def isInClipVolume(v: Vertex) : Boolean = {
    v.position.z < 1 && v.position.x >= -1 && v.position.x <= 1 && v.position.y >= -1 && v.position.y <= 1
  }
}