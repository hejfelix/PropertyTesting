import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

import scala.util.parsing.combinator.RegexParsers
import org.scalacheck.Gen

class EmailParserSpec extends FlatSpec with PropertyChecks with Matchers with RegexParsers {

  val nonEmptyAlphaStr = for {
    initial <- Gen.alphaChar
    tail    <- Gen.alphaStr
  } yield initial + tail

  val domainGenerator = for {
    domainStart <- nonEmptyAlphaStr
    domainEnd   <- nonEmptyAlphaStr
  } yield s"$domainStart.$domainEnd"

  val emailGenerator: Gen[String] = for {
    name   <- nonEmptyAlphaStr
    domain <- domainGenerator
  } yield s"$name@$domain"

  val parser = EmailParser()

  "EmailParser" should "parse simple user parts" in {
    forAll(nonEmptyAlphaStr) { user =>
      parser.parseString(parser.user, user) should matchPattern {
        case parser.Success(`user`, _) =>
      }
    }
  }

  "EmailParser" should "parse simple domains" in {
    forAll(domainGenerator) { domain =>
      parser.parseString(parser.domain, domain) should matchPattern {
        case parser.Success(`domain`, _) =>
      }
    }
  }

  "EmailParser" should "parse simple e-mails" in {
    forAll(emailGenerator) { email =>
      parser.parseString(parser.email, email) should matchPattern {
        case parser.Success(Email(_, _), _) =>
      }
    }

  }

}
