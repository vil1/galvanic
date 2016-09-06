package galvanic
package benchmarks

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

import scala.util.Random

/**
  * @author Valentin Kasas
  */
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class CompactionBenchmark {

  import galvanic._

  def isPrime(in: Short): Boolean = {
    if(in == 2) true
    else if(in < 2 || in % 2 == 0) false
    else {
      val top = Math.sqrt(in) + 1
      var i = 2
      var done = false
      while(i < top && !done) {
        if(in % i == 0) {
          done = true
        }
        i += 2
      }
      !done
    }
  }

  val compactIsPrime = {
    import mode.compact._
    galvanize(isPrime)
  }

  val normalIsPrime = galvanize(isPrime)

  val allShorts = (Short.MinValue to Short.MaxValue).map(_.toShort)


  val rand = new Random(System.currentTimeMillis())
  val max = Short.MaxValue * 2
  def getValue = (rand.nextInt(max) - Short.MaxValue).toShort

  @Benchmark
  def withCompaction(): Unit = {
    compactIsPrime(getValue)
  }

  @Benchmark
  def withoutCompaction():Unit = {
    normalIsPrime(getValue)
  }

  @Benchmark
  def standardIsPrime(): Unit = {
    isPrime(getValue)
  }
}
