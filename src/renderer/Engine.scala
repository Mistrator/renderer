package renderer

import scalafx.application.JFXApp
import scalafx.scene.Scene

import java.util.Timer
import java.util.TimerTask

object Engine extends JFXApp {
  val width = 640
  val height = 480
  
  stage = new JFXApp.PrimaryStage
  stage.title.value = "Renderer"
  stage.width = width
  stage.height = height
  
  val scene = new Scene
  stage.scene = scene
  
  val screen = new Screen(640, 480, scene)
  
  val loader = new WorldLoader
  
  val vert = Array.ofDim[Vertex](3)
  vert(0) = new Vertex(Vector4(0.0, 0.0, 1.0), Vertex.packRGBA(255, 0, 0, 255))
  vert(1) = new Vertex(Vector4(-1.0, 1.0, 1.0), Vertex.packRGBA(255, 0, 0, 255))
  vert(2) = new Vertex(Vector4(0.7, 0.9, 1.0), Vertex.packRGBA(255, 0, 0, 255))
  
  var trig = new Triangle(vert, SOLID)
  
  screen.drawImage(Array(trig))
}
