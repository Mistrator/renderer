package renderer

class Model(val mesh: Array[Triangle]) {
  
  // set the color for all vertices of the mesh
  def setColor(red: Int, green: Int, blue: Int, alpha: Int) = {
    for (i <- 0 until mesh.length) {
      val newVertices = Array.ofDim[Vertex](mesh(i).vertices.length)
      for (j <- 0 until mesh(i).vertices.length) {
        newVertices(i) = new Vertex(mesh(i).vertices(j).position, Vertex.packRGBA(red, green, blue, alpha))
      }
      mesh(i) = new Triangle(newVertices, mesh(i).material)
    }
  }
  
  // set the material for all triangles of the mesh
  def setMaterial(material: MaterialType) = {
    for (i <- 0 until mesh.length) {
      mesh(i) = new Triangle(mesh(i).vertices, material)
    }
  }
}
