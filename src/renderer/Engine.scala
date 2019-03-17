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
  
  var pos = 0
  val t = new Timer
  val jeqq = new TimerTask {
    def run() = {
      pos += 1
      pos %= width
      screen.drawImage(pos)
    }
  }
  t.schedule(jeqq, 17, 17)

  //screen.drawImage(Array[Triangle]())
}
