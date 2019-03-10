package tests

import org.junit.Test
import org.junit.Assert._

import renderer.Vector4
import renderer.WorldObject

import scala.math.Pi

class WorldObjectTest {
  
  @Test def worldMatrixTranslation() = {
    val v1 = Vector4(0, 0, 0)
    val v2 = Vector4(3, -1, 2)
    
    val worldMatrix = WorldObject.buildWorldMatrix(Vector4(1, 2, 3))
    
    val v1t = worldMatrix * v1
    val v2t = worldMatrix * v2
    
    assertEquals(Vector4(1, 2, 3), v1t)
    assertEquals(Vector4(4, 1, 5), v2t)
  }
  
  @Test def worldMatrixRotation() = {
    val v1 = Vector4(0, 0, 0)
    val v2 = Vector4(0, 0, 2)
    val v3 = Vector4(3, 0, 0)
    val v4 = Vector4(0, 1, 0)
    
    val worldMatrix = WorldObject.buildWorldMatrix(Vector4(0, 0, 0), Vector4(-Pi/2, 0, 0)) // 0, 1, 0
    
    assertEquals(Vector4(0, 0, 0), worldMatrix * v1)
    assertEquals(Vector4(0, 2, 0), worldMatrix * v2)
    assertEquals(Vector4(3, 0, 0), worldMatrix * v3)
    assertEquals(Vector4(0, 0, -1), worldMatrix * v4)
  }
  
  @Test def worldMatrixTranslationRotation() = {
    val v1 = Vector4(1, 2, 3)
    
    val worldMatrix = WorldObject.buildWorldMatrix(Vector4(4, 5, 6), Vector4(Pi/2, 0, 0)) // 0, -1, 0
    
    assertEquals(Vector4(5, 2, 8), worldMatrix * v1) // 5, 5, 6
  }
  
  @Test def worldMatrixRotation2() = {
    val v = Vector4(1, 2, 3)
    val mat = WorldObject.buildWorldMatrix(Vector4(0, 0, 0), Vector4(Pi/2, 0, 0))
    assertEquals(Vector4(1, -3, 2), mat * v)
  }
}