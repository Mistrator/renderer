package renderer

class Vertex(val position: Vector4, val color: Int) {
  // color is represented as 8-bit RGBA values packed in 32 bits
  // such that bits 0..7 = R, 8..15 = G, 16..23 = B, 24..31 = A
  
  def r = color & ((1 << 8) - 1)
  def g = (color >> 8) & ((1<<8) - 1)
  def b = (color >> 16) & ((1<<8) - 1)
  def a = (color >> 24) & ((1<<8) - 1)
  
  def *(that: Matrix4) : Vertex = new Vertex(that * position, color)
}

object Vertex {
    def packRGBA(red: Int, green: Int, blue: Int, alpha: Int) : Int = {
    if (red < 0 || red > 255 || green < 0 || green > 255 ||
        blue < 0 || blue > 255 || alpha < 0 || alpha > 255) {
      throw new IllegalArgumentException("Color parameter out of range")
    }
    
    var packed = red
    packed = packed | (green << 8)
    packed = packed | (blue << 16)
    packed = packed | (alpha << 24)
    
    return packed
  }
}
