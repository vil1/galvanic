package galvanic
package benchmarks

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._


/**
  * @author Valentin Kasas
  */
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class GalvanicBenchmark {

  import galvanic._

  val allBytesRange: Seq[Byte] = (Byte.MinValue to Byte.MaxValue).map(_.toByte)
  val allShortsRange: Seq[Short] = (Short.MinValue to Short.MaxValue).map(_.toShort)
  val logGalvanized = zn[Byte, Double](Math.log(_))
  val fizzbuzzGalvanized = zn(fizzbuzz)

  def fizzbuzz(in: Short): String = in match {
    case i if i % 3 == 0 && i % 5 == 0  => "fizzbuzz"
    case i if i % 3 == 0 => "fizz"
    case i if i % 5 == 0 => "buzz"
    case _ => in.toString
  }

  @Benchmark
  def standardLog(): Unit = {
    allBytesRange foreach (Math.log(_))
  }

  @Benchmark
  def galvanizedLog(): Unit = {
    allBytesRange foreach logGalvanized
  }

  @Benchmark
  def standardFizzbuzz(): Unit = {
    allShortsRange foreach fizzbuzz
  }

  @Benchmark
  def galvanizedFizzbuzz(): Unit = {
    allShortsRange foreach fizzbuzzGalvanized
  }

}
