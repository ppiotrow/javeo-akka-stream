import scala.io.Source

//Problem
//Iteracyjne przechodzenie po elementach.
//Niskie zużycie pamieci ale i rdzeni.
object Ex2 extends App {
  val source = Source.fromFile("/home/przemko/log.txt", "utf-8")
  println("Wczytano")

  val lines = source.getLines()
  println("Pobrano tekst")

  val transfers: Iterator[Bank.Transfer] = lines.map(Bank.fromString)
  println("Zaladowano transakcje")

  transfers.filter(_.amount<100).foreach(println)
}