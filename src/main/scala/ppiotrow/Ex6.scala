package ppiotrow

//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a
import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.stream.{FlowMaterializer, MaterializerSettings}
import ppiotrow.Bank.{Transfer, Currency}
import scala.io.Source

//why mapFuture? Backpreasure
object Ex6 extends App {

  implicit val sys = ActorSystem("javeo")
  implicit val ec = sys.dispatcher
  val mat = FlowMaterializer(MaterializerSettings())
  val source= Source.fromFile(Bank.fileLocation, "utf-8")

  val input = Flow(source.getLines()).map(Transfer.fromString).take(30)

  input.mapFuture { t =>
      WebService.convertToEUR(t.amount, t.currency)
      .map(newAmount => t.copy(amount = newAmount, currency = Currency("EUR") ))  }
    .foreach(println)
    .onComplete(mat)(_=>sys.shutdown())
}