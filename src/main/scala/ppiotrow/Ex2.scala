package ppiotrow

import scala.io.Source
import Bank.Transfer

//Problem
//Iteration prevent us from loading all transfers to memory
//Low memory usage but also only one core used. //tip: show htop
object Ex2 extends App {
  val source = Source.fromFile(Bank.fileLocation, "utf-8")
  val lines = source.getLines()
  val transfers: Iterator[Bank.Transfer] = lines.map(Transfer.fromString)

  transfers.filter(_.amount<100).foreach(println)
}