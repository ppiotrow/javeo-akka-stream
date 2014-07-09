import Bank.Transfer
import akka.actor.{Props, ActorSystem, ActorRef, Actor}

import scala.io.Source

//Problem
//Mailboxes growing
//OutOfMemoryError
//no static typing
//sbt -mem 128
object Ex3 extends App {

  val sys = ActorSystem("javeo")
  val lines: Iterator[String] = Source.fromFile("/home/przemko/log.txt", "utf-8").getLines()
  println("Wczytano")

  lazy val map = sys.actorOf(Props(new MapActor(filter)))
  lazy val filter = sys.actorOf(Props(new FilterActor(100, writer)))
//  lazy val hardOp = sys.actorOf(Props(new HardOperationActor(writer)))
  lazy val writer = sys.actorOf(Props[WriterActor])
  //source.getLines().map(Bank.fromString).filter(_.amount<100).foreach(println)
  lines.foreach(map ! _)
}

class MapActor(next: ActorRef) extends Actor {
  def receive = {
    case s: String => next ! transform(s)
  }
  def transform(s: String) = Bank.fromString(s)
}

class FilterActor(threshold: Long, next: ActorRef) extends Actor {
  def receive = {
    case t @ Transfer(_,_,amount,_) => if (amount<100)  next ! t
  }
}

class WriterActor extends Actor {
  def receive = {
    case a: Any => println(a)
  }
}

class HardOperationActor(next: ActorRef) extends Actor {
  def receive = {
    case a: Any =>
      Thread.sleep(1000)
      next ! a
  }
}
