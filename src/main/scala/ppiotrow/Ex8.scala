package ppiotrow

//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a
import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.stream.{FlowMaterializer, MaterializerSettings}
import ppiotrow.Bank.{Transfer, Currency}
import scala.concurrent.duration._
import scala.io.Source

//expand if you have slow producer and fast consumer
//ask web service once per n seconds and caches result
//unfortunately expand needs that strange generics
//every call to webservice prints "PING"
//all cores used - show htop
//you can uncomment [1] to show that transfers stream doesn't slow down to ticks
object Ex8 extends App {

  implicit val sys = ActorSystem("javeo")
  implicit val ec = sys.dispatcher
  implicit val sh = sys.scheduler
  val mat = FlowMaterializer(MaterializerSettings())
  val source = Source.fromFile(Bank.fileLocation, "utf-8")

  val input = Flow(source.getLines()).map(Transfer.fromString).take(1000000)

  val rates = Flow(2.seconds, () => Tick)
    .mapFuture(_ => WebService.euroRates)
    .expand[Map[Bank.Currency, Double],Map[Bank.Currency, Double]](identity, x=>(x,x)).toProducer(mat) //explain extrapolate

  val inputInEUR = input.zip(rates).map{ case (transfer, rates) =>
    transfer.copy(currency = Currency("EUR"), amount = (transfer.amount * rates(transfer.currency)).toLong)//lost precision
  }
  //[1]//.foreach(println)
  .onComplete(mat)( _ => sys.shutdown())
}