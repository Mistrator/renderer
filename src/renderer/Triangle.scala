package renderer

class Triangle(val vertices: Array[Vertex], val material: MaterialType) {
  
  def *(that: Matrix4) : Triangle = {
    val newVert = Array.ofDim[Vertex](vertices.length)
    for (i <- 0 until vertices.length) {
      newVert(i) = vertices(i) * that
    }
    return new Triangle(newVert, material)
  }
}
