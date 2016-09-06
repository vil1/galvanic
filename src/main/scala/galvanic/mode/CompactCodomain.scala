package galvanic.mode

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag

@implicitNotFound(msg = "There is no available compact representation of codomains of type ${C}")
trait CompactCodomain[C] extends galvanic.CodomainRepresentation[C]{

}


object compact {

  implicit object BooleanCompactCodomain extends CompactCodomain[Boolean] {
    override type Repr = Array[Long]

    override def store(value: Boolean, index: Int, repr: Array[Long]): Unit = {
      val bucket = index / 64
      val position = index % 64
      val word = repr(bucket)
      if(value) {
        repr.update(bucket, word | (1l << position))
      } else {
        repr.update(bucket, word  & (-1L ^ (1l << position)))
      }
    }

    override def get(index: Int, repr: Array[Long]): Boolean = {
      val bucket = index / 64
      val position = index % 64
      (repr(bucket) & (1L << position)) != 0
    }

    override def init(size: Int)(implicit ct: ClassTag[Boolean]): Array[Long] = new Array[Long]((size / 64) + (if(size % 64 > 0) 1 else 0))
  }

}



