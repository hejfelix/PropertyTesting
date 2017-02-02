import scala.util.parsing.combinator.RegexParsers

case class Email(user: String, domain: String)

case class EmailParser() extends RegexParsers {

  val letter: Parser[String]      = """[a-zA-Z]""".r
  val digit: Parser[String]       = """[0-9]""".r
  val specialChar: Parser[String] = """[!#$%&'*+-/=?^_`{|}~]""".r
  val dot: Parser[String]         = """[.]""".r
  val at: Parser[String]          = """@""".r
  val hyphen: Parser[String]      = """-""".r

  val quote: Parser[String]      = """"""".r
  val quotedPart: Parser[String] = quote <~ (emailCharacter | dot).* ~> quote

  val emailCharacter: Parser[String] = letter | digit | specialChar

  val firstOrLastCharacter: Parser[String] =
    (dot ~> emailCharacter) | emailCharacter

  val middleCharacter: Parser[String] = firstOrLastCharacter | dot

  val user: Parser[String] = firstOrLastCharacter ~ middleCharacter.* ~ firstOrLastCharacter.? ^^ {
    case first ~ middles ~ last => s"$first${middles.mkString}${last.mkString}"
  }

  val domainChars: Parser[String] = (letter | digit | hyphen)
  val domainLabel: Parser[String] = ((letter.+ ~ domainChars.*) | (domainChars.* ~ letter.+)) ^^ {
    case xs ~ ys => s"${xs.mkString}${ys.mkString}"
  }

  val domain:Parser[String] = domainLabel ~ (dot ~> domainLabel).* ^^ {
    case label ~ (remaining@(_ :: _)) => s"""$label.${remaining.mkString(".")}"""
    case label ~ (Nil) => s"""$label"""
  }

  val email: Parser[Email] = (user <~ at) ~ domain ^^ {
    case user ~ domain => Email(user, domain)
  }

  def parseString[T](p:Parser[T],string:String) = this.parseAll(p,string.toCharArray)

}
