package renderer

import scala.collection.mutable.Buffer
import scala.math.min
import scala.math.abs
import java.lang.IllegalArgumentException

class Renderer {
  
  private def buildProjectionMatrix(near: Double, far: Double) = {
      Matrix4(Array(Array(1, 0, 0, 0), Array(0, 1, 0, 0), 
        Array(0, 0, (far+near)/(far-near), -(2*far*near)/(far-near)), Array(0, 0, 1, 0)))
  }
  
  /**
   * Find the intersection point of a line and a plane.
   * The function assumes that such point exists and is unique.
   */
  private def planeLineIntersection(linePos: Vector4, lineDir: Vector4, planePos: Vector4, planeNormal: Vector4) : Vector4 = {
    val w = linePos - planePos
    val t = -(planeNormal*w) / (planeNormal * lineDir)
    val isectPoint = linePos + lineDir * t
    return isectPoint
  }
  
  /**
   * Clip a triangle with a plane and return the clipped parts that are in the visible side.
   * The plane normal points towards the visible side.
   */
  private def clipTriangleWith(trg: Triangle, planePos: Vector4, planeNormal: Vector4) : Array[Triangle] = {
    val visible = Buffer[Vertex]()
    val invisible = Buffer[Vertex]()
    
    for (vert <- trg.vertices) {
      val v = planePos - vert.position
      if (planeNormal * v <= 0.0) {
        visible += vert
      } else {
        invisible += vert
      }
    }
    
    visible.length match {
      case 0 => return Array[Triangle]() // the triangle is completely invisible
      case 1 => {
        val isectPoint1 = planeLineIntersection(visible(0).position, invisible(0).position - visible(0).position, planePos, planeNormal)
        val isectPoint2 = planeLineIntersection(visible(0).position, invisible(1).position - visible(0).position, planePos, planeNormal)
        
        // we could interpolate the vertex colors but this'll do for now
        val clipped = new Triangle(Array(visible(0), new Vertex(isectPoint1, invisible(0).color), new Vertex(isectPoint2, invisible(1).color)), trg.material)
        return Array(clipped)
      }
      case 2 => {
        val isectPoint1 = planeLineIntersection(visible(0).position, invisible(0).position - visible(0).position, planePos, planeNormal)
        val isectPoint2 = planeLineIntersection(visible(1).position, invisible(0).position - visible(1).position, planePos, planeNormal)
        
        // the triangle is split to two visible parts
        // again, we could interpolate the colors
        val clipped1 = new Triangle(Array(visible(0), new Vertex(isectPoint1, invisible(0).color), new Vertex(isectPoint2, invisible(0).color)), trg.material)
        val clipped2 = new Triangle(Array(visible(1), visible(0), new Vertex(isectPoint2, invisible(0).color)), trg.material)
        return Array(clipped1, clipped2)
      }
      case 3 => return Array(trg) // the triangle is completely visible
    }
    throw new IllegalArgumentException("The triangle had more than 3 vertices")
  }
  
  /**
   * Return the parts of the triangle that are inside the view volume
   */
  private def clipTriangle(trg: Triangle) : Array[Triangle] = {
   var visible = Buffer[Triangle](trg)
   val planes = Array[(Vector4, Vector4)]( // (plane position, plane normal), the normal points inwards (to the visible side)
       (Vector4(-1, 0, 0), Vector4(1, 0, 0)), // left plane
       (Vector4(1, 0, 0), Vector4(-1, 0, 0)), // right plane
       (Vector4(0, -1, 0), Vector4(0, 1, 0)), // bottom plane
       (Vector4(0, 1, 0), Vector4(0, -1, 0)), // top plane
       (Vector4(0, 0, 0), Vector4(0, 0, 1)), // near plane
       (Vector4(0, 0, 1), Vector4(0, 0, -1)) // far plane
       )

   for (plane <- planes) {
     var newVisible = Buffer[Triangle]()
     for (t <- visible) {
       newVisible ++= clipTriangleWith(t, plane._1, plane._2)
     }
     visible = newVisible
   }
   return visible.toArray
  }
  
  /**
   * Project world's triangles to clip space and clip triangles to view volume.
   * Returns an array of visible triangles sorted in the order they should be drawn.
   */
  def render(world: World) : Array[Triangle] = {
    // map triangles to clip space
    val clipSpaceTrg = toClipSpace(world)
    
    // clip triangles to view volume
    val clippedTrg = clipSpaceTrg.foldLeft(Array[Triangle]())((a, b) => a ++ clipTriangle(b))
    
    return clippedTrg
  }
  
  /**
   * Compare triangles by their distance, one with first non-overlapping vertex that has larger z comes first
   */
  private def trgCompare(a: Triangle, b: Triangle) : Boolean = {
    val zDistA = a.vertices.map(v => v.position.z).sortWith(_ > _)
    val zDistB = b.vertices.map(v => v.position.z).sortWith(_ > _)
    
    for (i <- 0 until a.vertices.length) {
      val az = zDistA(i)
      val bz = zDistB(i)
      
      // check that z coordinates don't overlap
      if (abs(az-bz) > Constants.DoubleEps) {
        return az > bz  
      }
    }
    
    // the triangles overlap completely, so we can just say that the second one comes first
    return false
  }
  
  /**
   * Project world's triangles from view space to clip space.
   */
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
