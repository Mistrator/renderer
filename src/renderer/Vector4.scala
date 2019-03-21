package renderer

import scala.math.sqrt

class Vector4(val v : Array[Double]) {
  
  def size: Int = 4
  
  def x = v(0)
  def y = v(1)
  def z = v(2)
  def w = v(3)
  
  def apply(i: Int) = v(i)
  
  override def equals(that: Any) : Boolean = {
    that match {
      case that: Vector4 => {
        if (!that.isInstanceOf[Vector4]) {
          return false
        }
        
        for (i <- 0 until size) {
          if (!Helpers.doubleEqual(v(i), that(i))) {
            return false
          }
        }
        
        return true
      }
      case _ => return false
    }
  }
  
  def +(that: Vector4) = Vector4(this.x+that.x, this.y+that.y, this.z+that.z, this.w+that.w)
  
  def -(that: Vector4) = Vector4(this.x-that.x, this.y-that.y, this.z-that.z, this.w-that.w)
  
  def *(that: Double) = Vector4(this.x*that, this.y*that, this.z*that, this.w*that)
  
  def /(that: Double) = Vector4(this.x/that, this.y/that, this.z/that, this.w/that)
  
  // dot product
  def *(that: Vector4) : Double = this.x*that.x + this.y*that.y + this.z*that.z + this.w*that.w
  
  def length : Double = sqrt(x*x + y*y + z*z + w*w)
  
  // normalize to length 1
  def normalize() = {
    val len = length
    Vector4(x/len, y/len, z/len, w/len)
  }
  
  def homogenize() = Vector4(x/w, y/w, z/w, 1)
  
  override def toString = "(" + x + ", " + y + ", " + z + ", " + w + ")"
}


object Vector4 {
  
  def apply(values: Array[Double]) = {
    values.length match {
      case 3 => new Vector4(values ++ Array(1.0))
      case 4 => new Vector4(values)
      case _ => throw new IllegalArgumentException("You must supply a 3- or 4-element array")
    }
  }
  
  def apply(x: Double, y: Double, z: Double, w: Double) = new Vector4(Array(x, y, z, w))
  
  def apply(x: Double, y: Double, z: Double) = new Vector4(Array(x, y, z, 1.0))
  
  def apply() = new Vector4(Array(0.0, 0.0, 0.0, 1.0))
}
