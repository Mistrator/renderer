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
  val world = loader.loadWorld("/home/miska/Opiskelu/CS-C2120_Ohjelmointistudio_2/renderer/testworld")
  
  world match {
    case Some(x) => {
      val renderer = new Renderer
      val rendVert = renderer.render(x)
      screen.drawImage(rendVert)
    }
    case None => println("Failed to load world")
  }
}
