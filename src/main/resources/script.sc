import ppiotrow.Bank

"sample data generator"
object TestDataGenerator {
  def generate(gb: Long): Unit = {
    import java.io.{FileOutputStream, PrintWriter}
    val output = new PrintWriter(new FileOutputStream("/home/przemko/log.txt"), false)
    val lineLength = Bank.transfer.toString.size
    Stream.range(1, gb*1024l*1024l*1024l/lineLength, 1).foreach(_=> output.println(Bank.transfer))
    output.close()
  }
 }

Bank.transfer
//TestDataGenerator.generate(8l)
Bank.fromString("JVOT WUYA 92185 HUF")