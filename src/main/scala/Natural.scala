object Naturals {

  sealed trait Natural
  case class Succ(of: Natural) extends Natural {
    //Fun fact: toString runs out of stack if we don't help it
    override def toString = s"Natural(${toInt(this)})"
  }
  case object Zero extends Natural

  private def toInt(n: Natural, acc: Int = 0): Int =
    n match {
      case Succ(n) => toInt(n, acc + 1)
      case Zero    => acc
    }

  private def fromInt(i: Int, acc: Natural): Natural = i match {
    case 0 => acc
    case i => fromInt(i - 1, Succ(acc))
  }

  def fromInt(i: Int): Natural = fromInt(i, Zero)

  def isEven(natural: Natural): Boolean = natural match {
    case Zero     => true
    case Succ(of) => !isEven(of)
  }

}
