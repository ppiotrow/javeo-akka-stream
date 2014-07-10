package ppiotrow

import ppiotrow.Bank.Transfer
import ppiotrow.TestDataGenerator.GB
import java.io.{File, FileOutputStream, PrintWriter}

object Generate3GBfile extends App {
  TestDataGenerator.generate(GB(3))
}

object TestDataGenerator {

  case class GB(n: Long) {
    require(n < 10) //safety
    def toBytes: Long = n * 1024 * 1024 * 1024
  }

  def generate(fileSize: GB): Unit = {
    val dest = new File(Bank.fileLocation)
    dest.createNewFile()
    val output = new PrintWriter(new FileOutputStream(dest), false)
    val lineLength = Transfer.random.toString.size + 1
    Stream.range(1, fileSize.toBytes / lineLength, 1).foreach(_ => output.println(Transfer.random))
    output.close()
  }
}