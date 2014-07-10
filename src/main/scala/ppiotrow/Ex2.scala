package ppiotrow

import scala.io.Source
import Bank.Transfer

//Problem
//Iteration.
//Low memory usage but also only one core used. //tip: show htop
object Ex2 extends App {
  val source = Source.fromFile(Bank.fileLocation, "utf-8")
  println("Loaded")

  val lines = source.getLines()
  println("Text loaded")

  val transfers: Iterator[Bank.Transfer] = lines.map(Transfer.fromString)
  println("Transactions loaded")

  transfers.filter(_.amount<100).foreach(println)
}