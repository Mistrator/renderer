package renderer

import scala.collection.mutable.Buffer
import scala.math.min
import scala.math.abs
import java.lang.IllegalArgumentException

class Renderer {
  
  def buildProjectionMatrix(near: Double, far: Double) = {
      Matrix4(Array(Array(1, 0, 0, 0), Array(0, 1, 0, 0), 
        Array(0, 0, (far+near)/(far-near), -(2*far*near)/(far-near)), Array(0, 0, 1, 0)))
  }
  
  
  private def clipTriangleWith(trg: Triangle, isInside: Vertex => Boolean) : Array[Triangle] = {
    val visibleCnt = trg.vertices.count(v => isInside(v))
    visibleCnt match {
      case 0 => return Array[Triangle]() // triangle is completely outside
      case 1 => return Array[Triangle]() // TODO
      case 2 => return Array[Triangle]() // TODO
      case 3 => return Array(trg) // triangle is completely visible
    }
    throw new IllegalArgumentException("A triangle had more than 3 vertices")
  }
  
  /*
   * Return the parts of the triangle that are inside the view volume
   */
  private def clipTriangle(trg: Triangle) : Array[Triangle] = {
   var visible = Buffer[Triangle](trg)
   val planes = Array[Vertex => Boolean](
       v => v.position.x >= -1.0, // left plane
       v => v.position.x <= 1.0, // right plane
       v => v.position.y >= -1.0, // bottom plane
       v => v.position.y <= 1.0, // top plane
       v => v.position.z >= 0.0, // near plane
       v => v.position.z <= 1.0) // far plane
       
   for (plane <- planes) {
     var newVisible = Buffer[Triangle]()
     for (t <- visible) {
       newVisible ++= clipTriangleWith(t, plane)
     }
     visible = newVisible
   }
   return visible.toArray
  }
  
  def render(world: World) : Array[Triangle] = {
    // map triangles to clip space
    val clipSpaceTrg = toClipSpace(world)
    
    // clip triangles to view volume
    val clippedTrg = clipSpaceTrg.foldLeft(Array[Triangle]())((a, b) => a ++ clipTriangle(b))
    
    return clippedTrg
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
        val projTrg = trg * worldMatrix * viewMatrix * projMatrix
        projected += new Triangle(projTrg.vertices.map(v => new Vertex(v.position.homogenize(), v.color)), projTrg.material)
      }
    }
    
    // sort triangles by their closest vertex to camera such that furthest triangles come first  
    return projected.sortWith(trgCompare).toArray
  }
}