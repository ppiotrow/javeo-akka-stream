package ppiotrow

import scala.io.Source
import Bank.Transfer

//Problem
//Loads all transactions to memory and process.
object Ex1 extends App {
  val source = Source.fromFile(Bank.fileLocation, "utf-8")
  println("Loaded")

  val lines = source.getLines().toList
  //here it should explode

  val transfers = lines.map(Transfer.fromString)

  transfers.filter(_.amount<100).foreach(println)
}