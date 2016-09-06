package galvanic.mode

import galvanic.CodomainRepresentation

import scala.reflect.ClassTag

/**
  * @author Valentin Kasas
  */
object default {

  implicit def defaultCodomainRepresentation[C]: galvanic.CodReprAux[C, Array[C]] = new CodomainRepresentation[C] {
    override type Repr = Array[C]

    override def store(value: C, index: Int, repr: Array[C]): Unit = repr.update(index, value)

    @inline override def get(index: Int, repr: Array[C]): C = repr.apply(index)

    override def init(size: Int)(implicit ct: ClassTag[C]): Array[C] = new Array[C](size)
  }

}
