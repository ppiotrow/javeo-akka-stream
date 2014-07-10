package ppiotrow
//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a
import scala.concurrent.forkjoin.ThreadLocalRandom.{current => rnd}

object Bank {

  case class Account(name: String) extends AnyVal {
    override def toString = s"$name"
  }
  object Account {
    def random = Account((1 to 4).map(_ => ('A' + rnd.nextInt(26)).toChar).mkString)
  }

  case class Currency(name: String) extends AnyVal {
    override def toString = s"$name"
  }

  object Currency {
    private val currencies = Seq("PLN", "USD", "EUR", "HUF", "LEI")

    def random = Currency(currencies(rnd.nextInt(currencies.length)))
  }

  case class Transfer(from: Account, to: Account, amount: Long, currency: Currency) {
    override def toString = s"$from $to $amount $currency"
  }

  object Transfer {
    def fromString(s: String): Transfer = {
      val tokens = s.split(" ").toList
      Transfer(Account(tokens(0)), Account(tokens(1)), tokens(2).toInt, Currency(tokens(3)))
    }

    def random = Transfer(Account.random, Account.random, rnd.nextLong(100000), Currency.random)
  }

  val fileLocation = "target/bigdata.txt"
}

case object Tick

object WebService {

  import ppiotrow.Bank.Currency
  import scala.concurrent._
  import scala.concurrent.ExecutionContext.Implicits.global

  private val euroRate = Map("PLN" -> 0.24, "USD" -> 0.73, "HUF" -> 0.003, "EUR" -> 1.00, "LEI" -> 0.22)

  def convertToEUR(amount: Long, c: Currency): Future[Long] = {
    Future {
      val currRate = euroRate.getOrElse(c.name, throw new RuntimeException)
      //Thread.sleep(10)
      (amount * currRate).toLong //lost precision;)
    }
  }

  def euroRates: Future[Map[Currency, Double]] = Future {
    println("PING")
    Thread.sleep(100)
    euroRate.map { case (k, v) => (Currency(k), v)}
  }

}