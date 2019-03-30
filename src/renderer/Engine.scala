package renderer

import scalafx.application.JFXApp
import scalafx.scene.Scene

import java.util.Timer
import java.util.TimerTask
import scalafx.animation.AnimationTimer

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
      var currentFrame = 0
      
      val mainLoopTimer = AnimationTimer {t =>
        // test object
        x.objects(0).worldMatrix = WorldObject.buildWorldMatrix(Vector4(0, 0, 3), Vector4(0.0 + currentFrame / 400.0, 0.0 + currentFrame / 800.0, 0.0))
        
        val rendVert = renderer.render(x)
        screen.drawImage(rendVert)
        currentFrame += 1
      }
      
      mainLoopTimer.start()
    }
    case None => println("Failed to load world")
  }
}
