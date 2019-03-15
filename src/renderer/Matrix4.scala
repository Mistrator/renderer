package renderer

class Matrix4(val m: Array[Array[Double]]) {
  
  def size = 4

  def apply(row: Int, col: Int) = m(row)(col)
  
  def apply(row: Int) = m(row)
  
  def update(row: Int, col: Int, value: Double) = m(row)(col) = value
  
  // check pairwise equality for elements taking floating-point imprecisions into account
  override def equals(that: Any) : Boolean = {
    that match {
      case that: Matrix4 => {
        if (!that.isInstanceOf[Matrix4]) {
          return false
        }
        
        for (i <- 0 until size) {
          for (j <- 0 until size) {
            if (!Helpers.doubleEqual(m(i)(j), that(i)(j))) {
              return false
            }
          }
        }
        
        return true
      }
      case _ => return false
    }
  }
  
  def +(that: Double) = Matrix4(Array.tabulate(size, size)((i, j) => m(i)(j) + that))
  
  def -(that: Double) = Matrix4(Array.tabulate(size, size)((i, j) => m(i)(j) - that))
  
  def *(that: Double) = Matrix4(Array.tabulate(size, size)((i, j) => m(i)(j) * that))
  
  def /(that: Double) = Matrix4(Array.tabulate(size, size)((i, j) => m(i)(j) / that))
  
  def +(that: Matrix4) = Matrix4(Array.tabulate(size, size)((i, j) => m(i)(j) + that(i)(j)))
  
  def -(that: Matrix4) = Matrix4(Array.tabulate(size, size)((i, j) => m(i)(j) - that(i)(j)))
  
  def *(that: Matrix4) = Matrix4(Array.tabulate(size, size)((i, j) => {
     var sum = 0.0     
     for (k <- 0 until size) {
       sum += (m(i)(k) * that(k)(j))  
     }
     sum
  }))
  
  def *(that: Vector4) : Vector4 = Vector4(Array.tabulate(size)(i => {
    var sum = 0.0
    for (j <- 0 until size) {
      sum += m(i)(j) * that(j)
    }
    sum
  }))
  
  override def toString = {
    var str = ""
    for (row <- m) {
      str += row.mkString("(", ", ", ")\n")
    }
    str
  }
}


object Matrix4 {
  
  def apply(values: Array[Array[Double]]) = new Matrix4(values)
  
  def apply() = new Matrix4(Array.ofDim[Double](4, 4))
  
  val identity = new Matrix4(Array(Array(1, 0, 0, 0), Array(0, 1, 0, 0), Array(0, 0, 1, 0), Array(0, 0, 0, 1)))
  
}
