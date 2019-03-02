package renderer

sealed trait MaterialType
  
case object SOLID extends MaterialType
case object WIREFRAME extends MaterialType
