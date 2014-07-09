//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a

import Bank.{Currency}
import akka.actor.{ActorSystem}
import akka.stream.scaladsl.Flow
import akka.stream.{MaterializerSettings, FlowMaterializer}
import scala.io.{Source}

//dlaczego mapFuture? Backpreasure
object Ex6 extends App {

  implicit val sys = ActorSystem("javeo.eu")
  implicit val ec = sys.dispatcher
  val mat = FlowMaterializer(MaterializerSettings())
  val source= Source.fromFile("/home/przemko/log.txt", "utf-8")

  val input = Flow(source.getLines()).map(Bank.fromString).take(30)

  input.mapFuture { t =>
      WebService.convertToEUR(t.amount, t.currency)
      .map(newAmount => t.copy(amount = newAmount, currency = Currency("EUR") ))  }
    .foreach(println)
    .onComplete(mat)(_=>sys.shutdown())
}