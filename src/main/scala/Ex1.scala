import scala.io.Source

//Problem
//Wczytanie do pamięci a następnie przetworzenie
object Ex1 extends App {
  val source = Source.fromFile("/home/przemko/log.txt", "utf-8")
  println("Wczytano")

  val lines = source.getLines().toList
  //W tym momencie po około 3 minutach się zawiesza
  println("Pobrano tekst")

  val transfers = lines.map(Bank.fromString)
  println("Zaladowano transakcje")

  transfers.filter(_.amount<1000).foreach(println)
}