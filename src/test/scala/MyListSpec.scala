import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen.{const, frequency}
import org.scalacheck.Prop.{classify, forAll}
import org.scalacheck.{Arbitrary, Gen, Prop}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}

class MyListSpec extends PropSpec with GeneratorDrivenPropertyChecks with Matchers {

  def genEmpty = const(MyNil)

  def genMyCons: Gen[MyList[Int]] =
    for {
      head <- arbitrary[Int]
      tail <- frequency((10, genMyCons), (1, genEmpty))
    } yield MyCons(head, tail)

  implicit def arbMyList =
    Arbitrary(Gen.frequency((1, genEmpty), (10, genMyCons)))

  property("Reversing twice is identity") {
    forAll { xs: MyList[Int] =>
      println(xs)
      xs.reverse.reverse == xs
    }
  }

}
