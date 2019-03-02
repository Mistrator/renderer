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
  
  @Test def scalarAddition() = {
    val mat = Matrix4(Array(Array(5, 3, 1, 7), Array(2, 5, 4, 1), Array(8, 3, 1, 2), Array(6, 3, 3, 2)))    
    val add = Matrix4(Array(Array(7, 5, 3, 9), Array(4, 7, 6, 3), Array(10, 5, 3, 4), Array(8, 5, 5, 4)))

    assertEquals(add, mat + 2)
  }
  
  @Test def scalarSubtraction() = {
    val mat = Matrix4(Array(Array(5, 3, 1, 7), Array(2, 5, 4, 1), Array(8, 3, 1, 2), Array(6, 3, 3, 2)))
    val sub = Matrix4(Array(Array(2, 0, -2, 4), Array(-1, 2, 1, -2), Array(5, 0, -2, -1), Array(3, 0, 0, -1)))
    
    assertEquals(sub, mat - 3)
  }
  
  @Test def scalarMultiplication() = {
    val mat = Matrix4(Array(Array(5, 3, 1, 7), Array(2, 5, 4, 1), Array(8, 3, 1, 2), Array(6, 3, 3, 2)))
    val mul = Matrix4(Array(Array(10, 6, 2, 14), Array(4, 10, 8, 2), Array(16, 6, 2, 4), Array(12, 6, 6, 4)))
    
    assertEquals(mul, mat * 2)
  }
  
  @Test def scalarDivision() = {
    val mat = Matrix4(Array(Array(5, 3, 1, 7), Array(2, 5, 4, 1), Array(8, 3, 1, 2), Array(6, 3, 3, 2)))
    val div = Matrix4(Array(Array(5.0/2, 3.0/2, 1.0/2, 7.0/2), Array(2.0/2, 5.0/2, 4.0/2, 1.0/2),
        Array(8.0/2, 3.0/2, 1.0/2, 2.0/2), Array(6.0/2, 3.0/2, 3.0/2, 2.0/2)))
        
    assertEquals(div, mat / 2)
  }
}