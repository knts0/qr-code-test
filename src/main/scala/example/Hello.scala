package example

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.{MatrixToImageConfig, MatrixToImageWriter}
import com.google.zxing.qrcode.QRCodeWriter

import javax.imageio.ImageIO
import java.nio.file.Files
import java.nio.file.Paths


object Hello extends Greeting with App {

  val matrix = new QRCodeWriter().encode("https://www.google.com/", BarcodeFormat.QR_CODE, 320, 320)

  val config = new MatrixToImageConfig(0xff0000ff, 0xffffffff);
  val qrCodeImage = MatrixToImageWriter.toBufferedImage(matrix, config)
  ImageIO.write(qrCodeImage, "png", Files.newOutputStream(Paths.get("/Users/kana/Downloads/qrcode.png")))
}

trait Greeting {
  lazy val greeting: String = "hello"
}
