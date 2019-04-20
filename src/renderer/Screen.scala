package renderer

import scalafx.scene.Group
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import scalafx.scene.paint.CycleMethod
import javafx.scene.effect.BlendMode

import scala.math.max
import scala.math.round

class Screen(width: Int, height: Int, scene: Scene) {
  
  val canvas = new Canvas(width, height)
  val gc = canvas.getGraphicsContext2D
  scene.content = canvas
  
  // map coordinate from -1.0..1.0 to 0..(pixelDim-1)
  def toPixelCoordinate(c: Double, pixelDim: Int) : Double = {
    var nc = (c+1.0)/2.0 // map to 0..1
    return (nc*pixelDim)
  }
  
  // clamp number to an interval
  def clamp(x: Double, low: Double, high: Double) : Double = {
    if (x < low) return low
    if (x > high) return high
    return x
  }
  
  // map a number from [origLow, origHigh] to [newLow, newHigh]
  def mapToRange(x: Double, origLow: Double, origHigh: Double, newLow: Double, newHigh: Double) : Double = {
    val slope = (newHigh-newLow) / (origHigh-origLow)
    return newLow + slope*(x-origLow)
  }
  
  // map z coordinate from clip space back to screen space
  def zInverse(z: Double, near: Double, far: Double) : Double = {
    val pos = Vector4(0, 0, z)
    val invProjMat = Matrix4(Array(Array(1, 0, 0, 0), Array(0, 1, 0, 0),
        Array(0, 0, 0, 1), Array(0, 0, (near-far)/(2*far*near), (far+near)/(2*far*near))))
    val invPos = invProjMat * pos
    return invPos.homogenize().z
  }
  
  def drawImage(trig: Array[Triangle]) = {
    // reset the canvas
    gc.clearRect(0, 0, width, height)
    gc.setFill(Color.Black)
    gc.fillRect(0, 0, width, height)
    
    for (ct <- trig) {
      val xPoints = ct.vertices.map(v => toPixelCoordinate(v.position.x, width))
      val yPoints = ct.vertices.map(v => toPixelCoordinate(-v.position.y, height))
      val zPoints = ct.vertices.map(v => mapToRange(zInverse(v.position.z, Constants.NearPlane, Constants.FarPlane), Constants.NearPlane, Constants.FarPlane, 0.0, 1.0))
      
      ct.material match {
        case SOLID => {
          val cZDist = zPoints.sum / 3.0
          val cr = clamp(ct.vertices.foldLeft(0.0)((r, v) => r + v.r) / (3*255.0) - cZDist, 0.0, 1.0)
          val cg = clamp(ct.vertices.foldLeft(0.0)((g, v) => g + v.g) / (3*255.0) - cZDist, 0.0, 1.0)
          val cb = clamp(ct.vertices.foldLeft(0.0)((b, v) => b + v.b) / (3*255.0) - cZDist, 0.0, 1.0)
          val ca = clamp(ct.vertices.foldLeft(0.0)((a, v) => a + v.a) / (3*255.0), 0.0, 1.0)
          
          val drawColor = Color.color(cr, cg, cb, ca)
          gc.setFill(drawColor)
          gc.fillPolygon(xPoints, yPoints, xPoints.length)
        }
        case WIREFRAME => {
          val lineColor = Color.color(ct.vertices(0).r/255.0, ct.vertices(0).g/255.0, ct.vertices(0).b/255.0, ct.vertices(0).a/255.0)
          gc.setStroke(lineColor)
          gc.strokePolygon(xPoints, yPoints, xPoints.length)
        }
      }
    }
  }
}