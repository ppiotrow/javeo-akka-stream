package ppiotrow

import scala.io.Source
import Bank.Transfer

//Problem
//Load all to memory and process
object Ex1 extends App {
  val source = Source.fromFile(Bank.fileLocation, "utf-8")
  println("Loaded")

  val lines = source.getLines().toList
  //here it should explode
  println("Text loaded")

  val transfers = lines.map(Transfer.fromString)
  println("Transactions loaded")

  transfers.filter(_.amount<100).foreach(println)
}