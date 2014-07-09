package ppiotrow

//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a
import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.stream.{FlowMaterializer, MaterializerSettings}
import scala.concurrent.duration._
import scala.io.Source

object Ex5 extends App {

  implicit val sys = ActorSystem("javeo.eu")
  val mat = FlowMaterializer(MaterializerSettings())
  val source = Source.fromFile("/home/przemko/log.txt", "utf-8")

  val input = Flow(source.getLines()).take(10).toProducer(mat)
  val ticks = Flow(1.second, () => Tick)

  ticks.zip(input).foreach(println).onComplete(mat)(_=>sys.shutdown())
}