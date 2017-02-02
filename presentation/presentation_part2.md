# Frameworks

## Scala

* **Scalacheck** (<http://scalacheck.org/>)
* **Nyaya** (<https://github.com/japgolly/nyaya>)
* **Scalaprops** (<https://github.com/scalaprops/scalaprops>)

## JavaScript

* **JSVerify** (<https://github.com/jsverify/jsverify>)
* **TestCheck.js** (<https://github.com/leebyron/testcheck-js>)
* ...probably more

## Java / Groovy

* **junit-quickcheck** (<https://github.com/pholser/junit-quickcheck>)
* **Spock Genesis** (<https://github.com/Bijnagte/spock-genesis>)
* **QuickTheories** (<https://github.com/NCR-CoDE/QuickTheories>)

## List goes on

* Haskell: **QuickCheck** (<https://hackage.haskell.org/package/QuickCheck>)
* Elixir: **ExCheck** (<https://github.com/parroty/excheck>)
* F#: **FsCheck** (<https://github.com/fscheck/FsCheck>)

## Try out ScalaCheck

Navigate to root of repo and get a REPL:

Issue

```scala
sbt test:console
```

in root of this repo

# Generators

## Gen.choose

```scala
scala> import org.scalacheck.Gen
import org.scalacheck.Gen

scala> val range = Gen.choose(10,100)
range: org.scalacheck.Gen[Int] = org.scalacheck.Gen$$anon$3@72783b5e

scala> range.sample
res0: Option[Int] = Some(75)

scala> range.sample
res1: Option[Int] = Some(12)

scala> range.sample
res2: Option[Int] = Some(22)

scala> range.sample
res3: Option[Int] = Some(73)
```

## Gen.oneOf

```scala
scala> val vowel = Gen.oneOf('A', 'E', 'I', 'O', 'U', 'Y')
vowel: org.scalacheck.Gen[Char] = org.scalacheck.Gen$$anon$3@cc20d05

scala> vowel.sample
res0: Option[Char] = Some(Y)

scala> vowel.sample
res1: Option[Char] = Some(O)

scala> vowel.sample
res2: Option[Char] = Some(A)
```

## Gen.frequency
```scala
val randomLanguage =
  Gen
    .frequency(
      (10, "Scala"),
      (1, "Java"),
      (1, "Groovy"),
      (1, "JavaScript") )

val langs =
  List.fill(30) (randomLanguage.sample)
  .map(_.get)
  .mkString(", ")

println(langs)
//Scala, Scala, Scala, Scala, Scala, Scala, Scala, Java, Scala, Scala, Scala, Scala, Scala, Scala, Scala, Scala, Scala, Java, Scala, Scala, Scala, Scala, Groovy, Scala, Scala, Scala, JavaScript, Scala, Scala, Groovy
```


## Arbitrary generators

```scala
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

case class MyBool(b: Boolean)

implicit val arbMyBool: Arbitrary[MyBool] = Arbitrary {
   Gen.oneOf( MyBool(false), MyBool(true ) ) )
}
```

## Using arbitrary Generators

```scala
scala> Arbitrary.arbitrary[MyBool].sample
res4: Option[MyBool] = Some(MyBool(true))

scala> Arbitrary.arbitrary[MyBool].sample
res5: Option[MyBool] = Some(MyBool(false))
```

## Compose Generators
```scala
val min = 1
val max = 50000

def zeroGen = const(Zero)

def nonZeroGen: Gen[Natural] =
  choose(min, max).map(Naturals.fromInt)

implicit def arbNatural: Arbitrary[Natural] = Arbitrary(
  Gen.oneOf(zeroGen, nonZeroGen) // ScalaTest has a different `oneOf`!
)
```

## Monadic composition

```scala
val genLeaf = const(Leaf)

val genNode = for {
  v <- arbitrary[Int]
  left <- genTree
  right <- genTree
} yield Node(left, right, v)

def genTree: Gen[Tree] = oneOf(genLeaf, genNode)
```

## Properties

```scala
property("Even plus 1 is odd, odd plus 1 is even") {
  forAll { (n: Natural) =>
    Naturals.isEven(n) == !Naturals.isEven(Succ(n))
  }
}
```
