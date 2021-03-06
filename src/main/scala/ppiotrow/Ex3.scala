package ppiotrow

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import ppiotrow.Bank.Transfer
import scala.io.Source
import Bank.Transfer

//Linked actors to process concurrently transactions using push pattern
//Akka gives us scaling up for free
//Problem:
//Mailboxes growing
//OutOfMemoryError
//No static typing
object Ex3 extends App {

  val sys = ActorSystem("javeo")
  val lines: Iterator[String] = Source.fromFile(Bank.fileLocation, "utf-8").getLines()
  lazy val map = sys.actorOf(Props(new MapActor(filter)))
  lazy val filter = sys.actorOf(Props(new FilterActor(1000, writer)))
  //  lazy val hardOp = sys.actorOf(Props(new HardOperationActor(writer))) change also filter actor's next to hardOp
  lazy val writer = sys.actorOf(Props[WriterActor])

  lines.foreach(map ! _)
}

class MapActor(next: ActorRef) extends Actor {
  def receive = {
    case s: String => next ! transform(s)
  }

  def transform(s: String) = Transfer.fromString(s)
}

class FilterActor(threshold: Long, next: ActorRef) extends Actor {
  def receive = {
    case t@Transfer(_, _, amount, _) if amount < threshold => next ! t
  }
}

class WriterActor extends Actor {
  def receive = {
    case t => println(t)
  }
}

class HardOperationActor(next: ActorRef) extends Actor {
  def receive = {
    case a: Any =>
      Thread.sleep(1000)
      next ! a
  }
}
