package ppiotrow

import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.stream.{MaterializerSettings, FlowMaterializer}
import scala.concurrent.duration._
import scala.io.Source

//No explicit dependency between 1-second-tics and 5-second-ticks
//but the streams are back pressured by transfers producer
object Ex9 extends App{
  implicit val sys = ActorSystem("javeo")
  val mat = FlowMaterializer(MaterializerSettings(1,1,1,1)) //no buffers to better present
                                                    // dependency between producer and two streams
  val source = Source.fromFile(Bank.fileLocation, "utf-8")

  val input = Flow(source.getLines()).take(10).toProducer(mat)
  val tickPerSecond = Flow(1.second, () => Tick)
  val tickPer5Seconds = Flow(5.seconds, () => Tick)

  val consumedPerSecond = tickPerSecond.zip(input).foreach(t => println("1Second:"+t))
  val consumedPer5Seconds = tickPer5Seconds.zip(input).foreach(t => println("5Seconds:"+t))

 //If you just invoke consume and complete in bellow lines then some transfers may skip 5-seconds stream
  consumedPerSecond.consume(mat)
  consumedPer5Seconds.onComplete(mat)(_=>sys.shutdown())
}

