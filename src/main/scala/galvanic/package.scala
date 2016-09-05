import scala.reflect.ClassTag

/**
  * @author Valentin Kasas
  */
package object galvanic {


  /**
    * Typeclass witnessing the fact that the domain of type N is small enough
    * to be stored as an array.
    *
    * @tparam N the underlying type of this SmallDomain
    */
  trait SmallDomain[N] {
    /**
      * Translates a value of the underlying type N to an suitable index in an
      * array (i.e. a value in `0 until size`).
      *
      * This MUST be the inverse of `fromIndex`
      *
      * @param n a value of type N
      * @return an index in `0 until size`
      */
    def asIndex(n: N): Int

    /**
      * Translates an index in `0 until size` to a value of type N.
      * This method is only used during the construction of a
      * GalvanizedFunction[N,?] instance.
      *
      * This MUST be the inverse of `asIndex`
      *
      * @param i an index in `0 until size`
      * @return the corresponding value of type N
      */
    def fromIndex(i: Int): N

    /**
      * @return the number of possible values of type N
      */
    def size: Int
  }

  implicit object byteSmallDomain extends SmallDomain[Byte] {
    override def asIndex(n: Byte): Int = n + 128
    override def fromIndex(n: Int): Byte = (n - 128).toByte
    override val size = 256
  }

  implicit object shortSmallDomain extends SmallDomain[Short] {
    override def asIndex(n: Short): Int = n + 32768
    override def fromIndex(n: Int): Short = (n - 32768).toShort
    override val size = 65536
  }

  final class GalvanizedFunction[Domain : SmallDomain, Codomain](codomain: Array[Codomain])
    extends (Domain => Codomain) {

    val domain = implicitly[SmallDomain[Domain]]

    @inline override def apply(argument: Domain) = codomain(domain.asIndex(argument))

  }

  /**
    * "Galvanize" a function by pre-computing its whole codomain and storing it as an array.
    * Since `function` is called with every possible `D`, it MUST be a total function, and
    * should not throw an exception for any value of type `D`.
    *
    * @param function the function to "galvanize", must be total
    * @tparam D the domain of the function, must be small i.e. there must be an instance of
    *           `SmallDomain[D]` in the implicit scope
    * @tparam C the codomain (result type) of the function
    * @return the galvanized function
    */
  def galvanize[D: SmallDomain, C: ClassTag](function: D => C): GalvanizedFunction[D, C] = {
    val domain = implicitly[SmallDomain[D]]
    val codomain = new Array[C](domain.size)
    (0 until domain.size) foreach (i => codomain.update(i ,function(domain.fromIndex(i))))
    new GalvanizedFunction[D, C](codomain)
  }
}
