package renderer

import scalafx.scene.Group
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas

class Screen(width: Int, height: Int, scene: Scene) {
  
  val canvas = new Canvas(width, height)
  val gc = canvas.getGraphicsContext2D
  scene.content = canvas
  
  // map coordinate from -1.0..1.0 to 0..(pixelDim-1)
  def toPixelCoordinate(c: Double, pixelDim: Int) : Double = {
    var nc = (c+1.0)/2.0 // map to 0..1
    return (nc*pixelDim)
  }
  
  def drawImage(trig: Array[Triangle]) = {
    // reset the canvas
    gc.clearRect(0, 0, width, height)
    gc.setFill(Color.CornflowerBlue)
    gc.fillRect(0, 0, width, height)
    
    for (ct <- trig) {
      val xPoints = ct.vertices.map(v => toPixelCoordinate(v.position.x, width))
      val yPoints = ct.vertices.map(v => toPixelCoordinate(-v.position.y, height))
      
      // for now color the entire triangle with the color of the first vertex
      gc.setFill(Color.color(ct.vertices(0).r/255.0, ct.vertices(0).g/255.0, ct.vertices(0).b/255.0, ct.vertices(0).a/255.0))
      
      ct.material match {
        case SOLID => {
          gc.fillPolygon(xPoints, yPoints, xPoints.length)
        }
        case WIREFRAME => {
          gc.strokePolygon(xPoints, yPoints, xPoints.length)
        }
      }
    }
  }
}