package renderer

import scalafx.scene.Group
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import scalafx.scene.paint.CycleMethod
import javafx.scene.effect.BlendMode

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
    // gc.setGlobalBlendMode(BlendMode.SRC_OVER)
    gc.setFill(Color.Black)
    gc.fillRect(0, 0, width, height)
    
    for (ct <- trig) {
      val xPoints = ct.vertices.map(v => toPixelCoordinate(v.position.x, width))
      val yPoints = ct.vertices.map(v => toPixelCoordinate(-v.position.y, height))
      
      // for now color the entire triangle with the color of the first vertex
      val drawColor = Color.color(ct.vertices(0).r/255.0, ct.vertices(0).g/255.0, ct.vertices(0).b/255.0, ct.vertices(0).a/255.0) 
      
      ct.material match {
        case SOLID => {
          for (i <- 0 until 3) {
            val a = i
            val b = (i+1)%3
            val c = (i+2)%3
            
            val abx = (xPoints(a)+xPoints(b)) / 2.0
            val aby = (yPoints(a)+yPoints(b)) / 2.0
            val abColor = Color.color((ct.vertices(a).r+ct.vertices(b).r)/(2*255.0), (ct.vertices(a).g+ct.vertices(b).g)/(2*255.0),
                (ct.vertices(a).b+ct.vertices(b).b)/(2*255.0), (ct.vertices(a).a+ct.vertices(b).a)/(2*255.0))
            val cColor = Color.color(ct.vertices(c).r/255.0, ct.vertices(c).g/255.0, ct.vertices(c).b/255.0, ct.vertices(c).a/255.0)
            
            val stops = Seq(new Stop(0, cColor), new Stop(1, abColor))
            val linearGradient = new LinearGradient(xPoints(c), yPoints(c), abx, aby, false, CycleMethod.NoCycle, stops)
            
            gc.setFill(linearGradient)
            gc.fillPolygon(xPoints, yPoints, xPoints.length)
          }
          // gc.setFill(drawColor)
          // gc.fillPolygon(xPoints, yPoints, xPoints.length)
        }
        case WIREFRAME => {
          gc.setStroke(drawColor)
          gc.strokePolygon(xPoints, yPoints, xPoints.length)
        }
      }
    }
  }
}