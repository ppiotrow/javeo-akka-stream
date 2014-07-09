//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a

import Bank.{Currency}
import akka.actor.{ActorSystem}
import akka.stream.scaladsl.Flow
import akka.stream.{MaterializerSettings, FlowMaterializer}
import scala.io.{Source}
import scala.concurrent.duration._

//expand
//ask web serice once per n seconds
object Ex8 extends App {

  implicit val sys = ActorSystem("javeo")
  implicit val ec = sys.dispatcher
  implicit val sh = sys.scheduler

  val mat = FlowMaterializer(MaterializerSettings())
  val source= Source.fromFile("/home/przemko/log.txt", "utf-8")

  val input = Flow(source.getLines()).map(Bank.fromString).take(1000000)

  val rates = Flow(1.second, () => Tick)
    .mapFuture(_ => WebService.euroRates)
    .expand[Map[Bank.Currency, Double],Map[Bank.Currency, Double]](identity, x=>(x,x)).toProducer(mat)

  val inputInEUR = input.zip(rates).map{ case (transfer, rates) =>
    transfer.copy(currency = Currency("EUR"), amount = (transfer.amount * rates(transfer.currency)).toLong)//lost precision
  }
  .onComplete(mat)( _ => sys.shutdown())
}