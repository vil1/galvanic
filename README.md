# Galvanic

**galvanize** /ˈɡalvənʌɪz/ *verb*

1.
  1. to subject to the action of an electric current especially for the purpose of stimulating physiologically <galvanize a muscle>
  2. to stimulate or excite as if by an electric shock <an issue that would galvanize public opinion>
2. to coat (iron or steel) with zinc; especially :  to immerse in molten zinc to produce a coating of zinc-iron alloy


Galvanic aims to make (part of) your code more performant (and in a sense closer to bare metal, hence the name) by simply wrapping it in the `zn` function.

The idea is dead simple : for any enumerable domain `D` and any codomain `C`, it is possible to represent any function `f: D => C` as an `Array[C]` whose length is the number of inhabitants of `D`, provided we're able to map any inhabitant of `D` to a index in such array. If `D` is "small" enough, it is acceptable to keep the array in memory, and then get values of `f(d)` in constant time !

Of course we still need to compute the value of `f` for each possible value in `D` so the performance gain really depends on how many time an application will call it during its lifetime.

## Examples


```scala
import galvanic._

val fasterFizzbuzz = zn { (in: Short) =>
  in match {
    case i if i % 3 == 0 && i % 5 == 0  => "fizzbuzz"
    case i if i % 3 == 0 => "fizz"
    case i if i % 5 == 0 => "buzz"
    case _ => in.toString
  }
}
```
## Modes

The way codomains are represented is made configurable through *modes* (à la [Rapture](http://github.com/propensive/rapture)). In the default mode, codomains of type `C` are simply represented as `Array[C]`.

There is a `compact` mode available that does its best to provide a more compact representation, one needs to import `galvanic.mode.compact._` in order to use it.

As of today, only `Boolean` can be represented in this mode (using bit-vectors represented as `Array[Long]`), support for compaction of numeric codomains is still WIP.


## Benchmark

Benchmarks are of sheer importance since this library is focused on performance. These are implemented as micro-benchmarks using JMH in the [benchmarks module](https://github.com/vil1/galvanic/tree/master/benchmarks)
Note that these benchmarks do not measure the time needed to *build* galvanized functions

```
[info] Benchmark                              Mode  Cnt     Score    Error  Units
[info] CompactionBenchmark.standardIsPrime    avgt   10    65,443 ±  1,162  ns/op
[info] CompactionBenchmark.withCompaction     avgt   10    13,957 ±  0,465  ns/op
[info] CompactionBenchmark.withoutCompaction  avgt   10    16,202 ±  2,361  ns/op
[info] GalvanicBenchmark.standardFizzbuzz     avgt   10  1258,615 ± 34,553  us/op
[info] GalvanicBenchmark.galvanizedFizzbuzz   avgt   10   259,120 ± 40,659  us/op
[info] GalvanicBenchmark.standardLog          avgt   10     9,400 ±  0,787  us/op
[info] GalvanicBenchmark.galvanizedLog        avgt   10     2,209 ±  0,590  us/op
```

 
 
