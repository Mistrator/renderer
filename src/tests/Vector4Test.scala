package tests

import org.junit.Test
import org.junit.Assert._

import renderer.Vector4

class Vector4Test {
  
  @Test def vectorEquality() = {
    val vec1 = Vector4(1, 2, 3, 4)
    
    assertEquals("vector must equal itself", vec1, vec1)
    
    val vec2 = Vector4(2, 2, 3, 4)
    
    assertNotEquals("different vectors must not be equal", vec1, vec2)
    
    val vec1SmallError = Vector4(1, 2 + 4e-10, 3, 4)
    
    assertEquals("small floating point imprecisions must be accepted", vec1, vec1SmallError)
    
    val vec1LargeError = Vector4(1, 2, 3 + 1e-8, 4)
    
    assertNotEquals("larger floating point imprecisions must not be accepted", vec1, vec1LargeError)
  }
  
  @Test def dotProduct() = {
    val eps = 1e-9
    
    val vec1 = Vector4(5, 3, 2, 1)
    
    assertEquals(39, vec1 * vec1, eps)
    
    val vec2 = Vector4(2, 1, 4, 7)
    
    assertEquals(28, vec1 * vec2, eps)
    assertEquals(28, vec2 * vec1, eps)
    
    val vec3 = Vector4(1, 1, 1, 0)
    val vec4 = Vector4(0, 0, 0, 1)
    
    assertEquals("the dot product of two orthogonal vectors is 0", 0, vec3 * vec4, eps)
  }
}