package ppiotrow

//taken from http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a
import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.stream.{FlowMaterializer, MaterializerSettings}
import ppiotrow.Bank.Transfer

import scala.io.Source

//Reactive streams FTW
//Uses all cores in about 100%
//No out of memory error
//Statically Typed
//Hidden complexity - if you only remove Flow() and consume you get the same code as in Ex2
//Back pressure
//Possibility to use akka-remote

object Ex4 extends App {

  implicit val sys = ActorSystem("javeo")
  val mat = FlowMaterializer(MaterializerSettings())
  val source = Source.fromFile(Bank.fileLocation, "utf-8")
  Flow(source.getLines())
    .map(Transfer.fromString)
    .filter(_.amount<100) //~0.001 of all transactions
    .take(1000000)
    .foreach(println)
    .consume(mat)
//    .onComplete(mat)(_=>sys.shutdown()) use instead of consume to stop program
}