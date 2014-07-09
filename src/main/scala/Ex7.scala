//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a

import Bank.{Currency}
import akka.actor.{ActorSystem}
import akka.stream.scaladsl.Flow
import akka.stream.{MaterializerSettings, FlowMaterializer}
import scala.io.{Source}
import scala.concurrent.duration._

//conflate
object Ex7 extends App {

  implicit val sys = ActorSystem("javeo")
  implicit val ec = sys.dispatcher
  implicit val sh = sys.scheduler

  val mat = FlowMaterializer(MaterializerSettings())
  val source = Source.fromFile("/home/przemko/log.txt", "utf-8")

  val input = Flow(source.getLines()).map(Bank.fromString)

  val ticks = Flow(1.second, () => Tick)

  val summarized = input.mapFuture { t =>
    WebService.convertToEUR(t.amount, t.currency)
      .map(newAmount => t.copy(amount = newAmount, currency = Currency("EUR") ))  }
    .conflate[Int](_ => 0, (sum, _) => sum + 1)
    .toProducer(mat)

  ticks.zip(summarized).foreach{
    case (_, summary) => println(summary)
  }.onComplete(mat)( _ => sys.shutdown())
}