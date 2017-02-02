import Naturals.{Natural, Succ, Zero}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen.{const, frequency}
import org.scalacheck.Prop.{classify, forAll}
import org.scalacheck.{Arbitrary, Gen, Prop}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import Gen.choose
import Gen.oneOf

class NaturalsSpec extends PropSpec with GeneratorDrivenPropertyChecks with Matchers {

  val min = 1
  val max = 50000

  def zeroGen = const(Zero)

  def nonZeroGen: Gen[Natural] = choose(min, max) map Naturals.fromInt

  implicit def arbNatural: Arbitrary[Natural] = Arbitrary(
    Gen.oneOf(zeroGen, nonZeroGen) // ScalaTest has a different `oneOf`!
  )

  /**
    * Fails because Naturals.isEven is not stack-safe.
    */
  property("Even plus 1 is odd, odd plus 1 is even") {
    forAll { (n: Natural) =>
      Naturals.isEven(n) == !Naturals.isEven(Succ(n))
    }
  }

}
