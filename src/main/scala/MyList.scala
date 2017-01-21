import scala.annotation.tailrec

sealed trait MyList[+T] {
  def foldLeft[U](zero: U)(f: (U, T) => U): U
  def map[U](f: T => U): MyList[U]
  def concat[U >: T](xs: MyList[U]): MyList[U]
  def reverse: MyList[T]
  def isEmpty: Boolean
  def length: Int
}

object MyList {
  def apply[T](xs: T*) = xs.foldRight(MyNil: MyList[T])((x, xs) => MyCons(x, xs))
}

case object MyNil extends MyList[Nothing] {

  override def foldLeft[U](zero: U)(f: (U, Nothing) => U): U = zero

  override def map[U](f: (Nothing) => U): MyList[U] = MyNil

  override def reverse: MyList[Nothing] = MyNil

  override def isEmpty = true

  override def length = 0

  override def toString = ""

  override def concat[U >: Nothing](xs: MyList[U]) = xs
}

case class MyCons[T](head: T, tail: MyList[T]) extends MyList[T] {

  override def foldLeft[U](zero: U)(f: (U, T) => U): U =
    f(tail.foldLeft(zero)(f), head)

  override def map[U](f: (T) => U): MyList[U] =
    MyCons(f(head), tail.map(f))

  override def isEmpty = false

  override def length = foldLeft(0)((x, _) => x + 1)

  @tailrec
  private def rev(acc: MyList[T] = MyNil, xs: MyList[T]): MyList[T] = xs match {
    case MyCons(h, t) => rev(MyCons(h, acc), t)
    case _            => acc
  }

  override def reverse: MyList[T] = rev(MyNil, MyCons(head, tail))

  override def concat[U >: T](xs: MyList[U]) = MyCons(head, tail.concat(xs))

  override def toString =
    foldLeft(List.empty[T])( (a,b) => b +: a).mkString("MyList(",",",")")
}
