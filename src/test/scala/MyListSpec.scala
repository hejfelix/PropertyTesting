import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.PropertyChecks
import Gen._
import Arbitrary.arbitrary

import scala.util.parsing.combinator.RegexParsers

class MyListSpec extends FlatSpec with PropertyChecks with Matchers with RegexParsers {

  private def genEmpty = const(MyNil)

  private def genMyCons: Gen[MyList[Int]] =
    for {
      head <- arbitrary[Int]
      tail <- frequency((10, genMyCons), (1, genEmpty))
    } yield MyCons(head, tail)

  implicit def arbMyList = Arbitrary(genMyCons)

  "rev rev " should "be identity" in {
    forAll { list: MyList[Int] =>
      list.reverse.reverse shouldBe list
    }
  }

  "concat" should "have greater or equal length" in {
    forAll { (a: MyList[Int], b: MyList[Int]) =>
      a.concat(b).length should be >= a.length
    }
  }

}
