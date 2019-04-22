package renderer

class Vertex(val position: Vector4, val r: Int, val g: Int, val b: Int, val a: Int) {
  
  def *(that: Matrix4) : Vertex = new Vertex(that * position, r, g, b, a)
  def *(that: Double) : Vertex = new Vertex(position * that, r, g, b, a)
}
