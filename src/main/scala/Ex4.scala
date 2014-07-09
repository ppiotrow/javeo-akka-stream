//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a
import akka.actor.{ActorSystem}
import akka.stream.scaladsl.Flow
import akka.stream.{MaterializerSettings, FlowMaterializer}

import scala.io.{Source}

//sbt -mem 128
object Ex4 extends App {

  implicit val sys = ActorSystem("javeo")
  val mat = FlowMaterializer(MaterializerSettings())
  val source= Source.fromFile("/home/przemko/log.txt", "utf-8")
  Flow(source.getLines())
    .map(Bank.fromString)
    .filter(_.amount<100)
    .take(1000)
    .foreach(println)
    .consume(mat)
//    .onComplete(mat)(_=>sys.shutdown())
}