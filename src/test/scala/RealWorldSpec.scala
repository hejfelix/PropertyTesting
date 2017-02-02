import org.scalatest.{FlatSpec, Matchers, PropSpec}
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import Gen.const
import Gen.frequency

class RealWorldSpec extends PropSpec with GeneratorDrivenPropertyChecks with Matchers {

  /*
      Generators
   */
  val naturals             = arbitrary[Int].filter(_ >= 0)
  val doubles: Gen[Double] = frequency((1, const(Double.NaN)), (10, arbitrary[Double]))

  /*
      Properties
   */
  property("Adding naturals should increase result") {
    forAll(naturals, naturals) { (a, b) =>
      a + b should be >= a
    }
  }

  property("Comparing doubles should be consistent") {
    forAll(doubles, doubles) { (a: Double, b: Double) =>
      whenever(!(a > b)) {
        b should be >= a
      }
    }
  }

}
