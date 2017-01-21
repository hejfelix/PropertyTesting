
<!-- $size: 16:9 -->
<!-- $theme: default -->

```scala
case class MyCons[T](head: T, tail: MyList[T]) extends MyList[T] {

  override def foldLeft[U](zero: U)(f: (U, T) => U): U =
    f(tail.foldLeft(zero)(f), head)

  override def map[U](f: (T) => U): MyList[U] =
    MyCons(f(head), tail.map(f))

  override def isEmpty = false
  
...

}
```

---

```
case object MyNil extends MyList[Nothing] {

  override def foldLeft[U](zero: U)(f: (U, Nothing) => U): U = zero
  
  override def map[U](f: (Nothing) => U): MyList[U] = MyNil

  override def reverse: MyList[Nothing] = MyNil

  override def isEmpty = true

  override def length = 0

  override def toString = ""

  override def concat[U >: Nothing](xs: MyList[U]) = xs
}
```