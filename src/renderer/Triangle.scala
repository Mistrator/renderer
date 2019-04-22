package renderer

class Triangle(val vertices: Array[Vertex], val material: MaterialType) {
  
  /**
   * Multiply all vertices by a matrix.
   */
  def *(that: Matrix4) : Triangle = {
    val newVert = Array.ofDim[Vertex](vertices.length)
    for (i <- 0 until vertices.length) {
      newVert(i) = vertices(i) * that
    }
    return new Triangle(newVert, material)
  }
  
  /**
   * Multiply all vertices by a scalar.
   */
  def *(that: Double) : Triangle = {
    val newVert = Array.ofDim[Vertex](vertices.length)
    for (i <- 0 until vertices.length) {
      newVert(i) = vertices(i) * that
    }
    return new Triangle(newVert, material)
  }
}
