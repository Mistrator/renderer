package tests

import org.junit.Test
import org.junit.Assert._

import renderer.Matrix4
import renderer.Vector4

class Matrix4Test {
  
  @Test def vectorMultiplication() = {
    val mat = Matrix4(Array(Array(5, 3, 2, 7), Array(2, 3, 9, 5), Array(1, 2, 9, 3), Array(5, 5, 3, 2)))
    val vec = Vector4(Array(2, 4, 3, 1))
    
    val expected = Vector4(Array(35, 48, 40, 41))
    
    assertEquals(expected, mat * vec)
  }
  
  @Test def matrixMultiplication() = {
    val left = Matrix4(Array(Array(2, 1, 3, 4), Array(3, 5, 7, 1), Array(6, 2, 4, 9), Array(5, 4, 1, 3)))
    val right = Matrix4(Array(Array(5, 5, 7, 9), Array(3, 2, 8, 1), Array(3, 6, 4, 4), Array(8, 9, 1, 2)))
    
    val expected = Matrix4(Array(Array(54, 66, 38, 39), Array(59, 76, 90, 62), Array(120, 139, 83, 90), Array(64, 66, 74, 59)))
    
    assertEquals(expected, left * right)
  }
}