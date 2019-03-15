package renderer

import scala.io.Source
import java.io.FileNotFoundException
import scala.collection.mutable.Buffer

class WorldLoader {
  
  // a way to make sure that a resource is closed after use
  private def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
    resource.close()
  }
    
  private def parseWorld(data: Array[String]) : Option[World] = ???
  
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