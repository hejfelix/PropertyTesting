import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

import scala.util.parsing.combinator.RegexParsers

class EmailParserSpec extends FlatSpec with PropertyChecks with Matchers with RegexParsers {

  "EmailParser" should "parse simple e-mails" in
  {

    val parser: EmailParser = new EmailParser()

    parser.parseString(parser.user, "hejfelix") should matchPattern
    {
      case parser.Success("hejfelix", _) =>
    }

    parser.parseString(parser.domain, "gmail.com") should matchPattern
    {
      case parser.Success("gmail.com", _) =>
    }

    parser.parseString(parser.domain, "com") should matchPattern
    {
      case parser.Success("com", _) =>
    }

    parser.parseString(parser.email, "hejfelix@gmail.com") should matchPattern
    {
      case parser.Success(Email("hejfelix", "gmail.com"), _) =>
    }

  }

}