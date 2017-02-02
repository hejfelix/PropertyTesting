% Property Based Testing
% Felix Palludan Hargreaves

## Disclaimer

* I'm no expert
* I don't always use property based testing
    - even though I want to

## Ideal World

- Well defined programming language running on imaginary hardware (e.g.: pseudo code)
- Prove subprograms
- Compose proofs to prove larger software projects

---

## Real World

$a + c \geq a$

$\forall a \in \mathbb{Z}, c \in \mathbb{N}$

is **not true** due to overflow

---

## Real World part 2

$\neg (a < b) \Leftrightarrow a \geq b$

$\forall a, b \in \mathbb{Q}$

Also **not true** due to `NaN`

---

## Naturals.scala

```scala
sealed trait Natural
case class  Succ(of: Natural) extends Natural
case object Zero              extends Natural
```

```scala
def isEven(natural: Natural): Boolean = natural match {
  case Zero     => true
  case Succ(of) => !isEven(of)
}
```


# Can we prove code?

## Proof

> Prove that `isEven(n)` is true
>*iff* **n** has an even number of $Succ()$ levels

----

**Base case:**

`isEven(Zero) == true`

follows by definition.

\

`isEven(Succ(Zero)) == !true == false`

Holds with single match-case application


---

**Inductive case:** Given some natural number, **n**, show that it works for `Succ(n)`.

* If **n** is even
    - `isEven(Succ(n)) == !isEven(n) == false`{.scala}
* If **n** is odd
    - `isEven(Succ(n)) == !isEven(n) == true`{.scala}


## Does that proof hold?

## Given

The Scala language specification: **Yes**

## Given

Language spec + JVM spec + knowledge of Scala compiler implementation: **No**

```scala
scala> import Naturals._
import Naturals._

scala> isEven(fromInt(9))
res6: Boolean = false

scala> isEven(fromInt(42))
res7: Boolean = true

scala> isEven(fromInt(90000))
java.lang.StackOverflowError
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  at Naturals$.isEven(Natural.scala:14)
  ...
```

## Now what?

Proofs don't scale to reality. We **have to test**!
