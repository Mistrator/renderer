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
  val width = Constants.WindowWidth
  val height = Constants.WindowHeight
  
  stage = new JFXApp.PrimaryStage
  stage.title.value = "Renderer"
  stage.width = width
  stage.height = height
  
  val scene = new Scene
  stage.scene = scene
  
  val screen = new Screen(width, height, scene)
  
  val loader = new WorldLoader
  val world = loader.loadWorld("./world")
  
  // is a certain key currently down
  val inputs = Map[KeyCode, Boolean]().withDefaultValue(false)
  
  world._1 match {
    case Some(x) => {
      val renderer = new Renderer
      var currentFrame = 0
      
      // listen to keyboard inputs
      scene.setOnKeyPressed(k => inputs(k.getCode) = true)
      scene.setOnKeyReleased(k => inputs(k.getCode) = false)
      
      val mainLoopTimer = AnimationTimer {t =>
        update(x, currentFrame)
        draw(x, renderer, screen)
        currentFrame += 1
      }
      
      mainLoopTimer.start()
    }
    case None => {
      println("Failed to load world, reason: " + world._2)
    }
  }
  
  /**
   * Handle user input, alter object positions etc.
   */
  def update(world: World, currentFrame: Int) = {
    
    // rotate the first object in the world for demo purposes
    // comment out to stop the rotation
    world.objects(0).worldMatrix = WorldObject.buildWorldMatrix(Vector4(0, 4.87, 0), Vector4(0.0 + currentFrame / 200.0, 0.0 + currentFrame / 400.0, 0.0))
    
    val rotationMat = Helpers.buildTransRotMatrix(Vector4(), world.camera.orientation)._2 
    val forward = rotationMat * Vector4(0.0, 0.0, 1.0)
    val right = rotationMat * Vector4(1.0, 0.0, 0.0)
    val up = Vector4(0.0, 1.0, 0.0)
    
    var newCamPos = world.camera.position
    
    // camera movement
    if (inputs(KeyCode.W)) {
      newCamPos += forward * Constants.MovementSpeed
    }
    if (inputs(KeyCode.S)) {
      newCamPos += forward * (-Constants.MovementSpeed)
    }
    if (inputs(KeyCode.D)) {
      newCamPos += right * Constants.MovementSpeed
    }
    if (inputs(KeyCode.A)) {
      newCamPos += right * (-Constants.MovementSpeed)
    }
    if (inputs(KeyCode.Q)) {
      newCamPos += up * Constants.MovementSpeed
    }
    if (inputs(KeyCode.Z)) {
      newCamPos += up * (-Constants.MovementSpeed)
    }
    
    // collision detection
    if (!world.collides(newCamPos)) {
      world.camera.position = newCamPos
    }
    
    // camera orientation
    if (inputs(KeyCode.LEFT)) {
      world.camera.orientation += Vector4(0.0, -Constants.RotationSpeed, 0.0)
    }
    if (inputs(KeyCode.RIGHT)) {
      world.camera.orientation += Vector4(0.0, Constants.RotationSpeed, 0.0)
    }
    if (inputs(KeyCode.UP)) {
      val newOri = world.camera.orientation + Vector4(-Constants.RotationSpeed, 0.0, 0.0)
      if (newOri.x > -Pi/2.0) {
        world.camera.orientation = newOri
      }
    }
    if (inputs(KeyCode.DOWN)) {
      val newOri = world.camera.orientation + Vector4(Constants.RotationSpeed, 0.0, 0.0)
      if (newOri.x < Pi/2.0) {
        world.camera.orientation = newOri
      }
    }
  }
  
  /**
   * Draw the world to screen
   */
  def draw(world: World, renderer: Renderer, screen: Screen) = {
    val rendVert = renderer.render(world)
    screen.drawImage(rendVert)
  }
}

