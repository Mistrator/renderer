package renderer

import scala.io.Source
import java.io.FileNotFoundException
import scala.collection.mutable.Buffer

/*
	Level file format:
	
	<camera X> <camera Y> <camera Z>
	<n: number of objects>
	(n times)
		<X> <Y> <Z> // world space coordinates
		<material (SOLID/WIREFRAME)> <color R> <color G> <color B> <color A>
		<m: number of triangles in current object>
		(m times)
		<t1x> <t1y> <t1z> <t2x> <t2y> <t2z> <t3x> <t3y> <t3z> // object space
*/
class WorldLoader {
  
  // a way to make sure that a resource is closed after use
  private def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
    resource.close()
  }
    
  private def parseWorld(data: Array[String]) : Option[World] = {
    try {
      val camPos = data(0).split(' ').map(_.toDouble)
      val camera = new Camera(Vector4(camPos(0), camPos(1), camPos(2)), Vector4(0, 0, 0))
      
      val objects = Buffer[WorldObject]()
      
      val n = data(1).toInt
      var cline = 1
      
      for (i <- 0 until n) {
        cline += 1
        val worldPos = data(cline).split(' ').map(_.toDouble)
        val wx = worldPos(0)
        val wy = worldPos(1)
        val wz = worldPos(2)
        
        cline += 1
        val appearance = data(cline).split(' ')
        val material = appearance(0) match {
          case "SOLID" => SOLID
          case "WIREFRAME" => WIREFRAME
          case _ => return None // invalid material type
        }
        val r = appearance(1).toInt
        val g = appearance(2).toInt
        val b = appearance(3).toInt
        val alpha = appearance(4).toInt
        
        val color = Vertex.packRGBA(r, g, b, alpha)
        
        cline += 1
        val m = data(cline).toInt
        
        val triangles = Buffer[Triangle]()
        
        for (j <- 0 until m) {
          cline += 1
          val coords = data(cline).split(' ').map(_.toDouble)
          val v0 = new Vertex(Vector4(coords.slice(0, 3)), color)
          val v1 = new Vertex(Vector4(coords.slice(3, 6)), color)
          val v2 = new Vertex(Vector4(coords.slice(6, 9)), color)
          
          triangles += new Triangle(Array(v0, v1, v2), material)
        }
        
        val model = new Model(triangles.toArray)
        
        val xCoords = triangles.flatMap(t => t.vertices.map(v => v.position.x))
        val yCoords = triangles.flatMap(t => t.vertices.map(v => v.position.y))
        val zCoords = triangles.flatMap(t => t.vertices.map(v => v.position.z))
        
        // build a collision box around the outermost vertices
        val modelWidth = xCoords.max - xCoords.min
        val modelHeight = yCoords.max - yCoords.min
        val modelDepth = zCoords.max - zCoords.min
        
        val worldObj = new WorldObject(model, 
            WorldObject.buildWorldMatrix(Vector4(Array(wx, wy, wz))), 
            Some(new CollisionBox(modelWidth, modelHeight, modelDepth)))
        
        objects += worldObj
      }
      
      val world = new World(objects, camera)
      return Some(world)
      
    } catch {
      case nf: NumberFormatException => return None
      case eof: ArrayIndexOutOfBoundsException => return None
    }
  }
  
  def loadWorld(filePath: String) : Option[World] = {
    try {
      using(Source.fromFile(filePath)) { src => {
        val lines = Buffer[String]()
        for (line <- src.getLines) {
          lines += line
        }
        return parseWorld(lines.toArray)
      }}
    } catch {
      case e: FileNotFoundException => return None
    }
  }
}