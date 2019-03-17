package renderer

import scalafx.scene.Group
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas

class Screen(width: Int, height: Int, scene: Scene) {
  
  val canvas = new Canvas(width, height)
  val gc = canvas.getGraphicsContext2D
  scene.content = canvas
  
  def drawImage(/*trig: Array[Triangle]*/ x: Int) = {
    // reset the canvas
    gc.clearRect(0, 0, width, height)
    gc.setFill(Color.CornflowerBlue)
    gc.fillRect(0, 0, width, height)
    
    gc.setFill(Color.Red)
    gc.fillRect(x, 100, 100, 100)
  }
}