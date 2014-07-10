package ppiotrow

//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a

import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.stream.{FlowMaterializer, MaterializerSettings}
import ppiotrow.Bank.{Transfer, Currency}

import scala.concurrent.duration._
import scala.io.Source

//conflate if you have fast producer and slow consumer
//program counts number of transfers between tics
object Ex7 extends App {

  implicit val sys = ActorSystem("javeo")
  implicit val ec = sys.dispatcher
  implicit val sh = sys.scheduler
  val mat = FlowMaterializer(MaterializerSettings())
  val source = Source.fromFile(Bank.fileLocation, "utf-8")

  val input = Flow(source.getLines()).map(Transfer.fromString)
  val ticks = Flow(1.second, () => Tick)

  val summarized = input
    .conflate[Int](_ => 0, (sum, _) => sum + 1)
    .toProducer(mat)

  ticks.zip(summarized).foreach{
    case (_, summary) => println(summary)
  }.onComplete(mat)( _ => sys.shutdown())
}