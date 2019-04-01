package renderer

import scalafx.application.JFXApp
import scalafx.scene.Scene

import java.util.Timer
import java.util.TimerTask
import scalafx.animation.AnimationTimer
import javafx.scene.input.KeyCode

import scala.collection.mutable.Map
import scala.math.Pi

object Engine extends JFXApp {
  val width = 640
  val height = 480
  
  stage = new JFXApp.PrimaryStage
  stage.title.value = "Renderer"
  stage.width = width
  stage.height = height
  
  val scene = new Scene
  stage.scene = scene
  
  val screen = new Screen(width, height, scene)
  
  val loader = new WorldLoader
  val world = loader.loadWorld("/home/miska/Opiskelu/CS-C2120_Ohjelmointistudio_2/renderer/testworld")
  
  // is a certain key currently down
  val inputs = Map[KeyCode, Boolean]().withDefaultValue(false)
  
  world match {
    case Some(x) => {
      val renderer = new Renderer
      var currentFrame = 0
      
      scene.setOnKeyPressed(k => inputs(k.getCode) = true)
      scene.setOnKeyReleased(k => inputs(k.getCode) = false)
      
      val mainLoopTimer = AnimationTimer {t =>
        update(x, currentFrame)
        draw(x, renderer, screen)
        currentFrame += 1
      }
      
      mainLoopTimer.start()
    }
    case None => println("Failed to load world")
  }
  
  def update(world: World, currentFrame: Int) = {
    // temp test
    world.objects(0).worldMatrix = WorldObject.buildWorldMatrix(Vector4(0, 0, 3), Vector4(0.0 + currentFrame / 400.0, 0.0 + currentFrame / 800.0, 0.0))
    
    val MovementSpeed = 0.02
    val RotationSpeed = 0.01
    
    val rotationMat = Helpers.buildTransRotMatrix(Vector4(), world.camera.orientation)._2 
    val forward = rotationMat * Vector4(0.0, 0.0, 1.0)
    val right = rotationMat * Vector4(1.0, 0.0, 0.0)
    
    if (inputs(KeyCode.W)) {
      world.camera.position += forward * MovementSpeed
    }
    if (inputs(KeyCode.S)) {
      world.camera.position += forward * (-MovementSpeed)
    }
    if (inputs(KeyCode.D)) {
      world.camera.position += right * MovementSpeed
    }
    if (inputs(KeyCode.A)) {
      world.camera.position += right * (-MovementSpeed)
    }
    if (inputs(KeyCode.LEFT)) {
      world.camera.orientation += Vector4(0.0, -RotationSpeed, 0.0)
    }
    if (inputs(KeyCode.RIGHT)) {
      world.camera.orientation += Vector4(0.0, RotationSpeed, 0.0)
    }
    if (inputs(KeyCode.UP)) {
      val newOri = world.camera.orientation + Vector4(-RotationSpeed, 0.0, 0.0)
      if (newOri.x > -Pi/2.0) {
        world.camera.orientation = newOri
      }
    }
    if (inputs(KeyCode.DOWN)) {
      val newOri = world.camera.orientation + Vector4(RotationSpeed, 0.0, 0.0)
      if (newOri.x < Pi/2.0) {
        world.camera.orientation = newOri
      }
    }
  }
  
  def draw(world: World, renderer: Renderer, screen: Screen) = {
    val rendVert = renderer.render(world)
    screen.drawImage(rendVert)
  }
}
