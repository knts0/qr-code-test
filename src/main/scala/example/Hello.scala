package example

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.{MatrixToImageConfig, MatrixToImageWriter}
import com.google.zxing.qrcode.QRCodeWriter
import org.apache.poi.ss.usermodel.{ClientAnchor, Workbook}
import org.apache.poi.util.{IOUtils, Units}
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, FileOutputStream, OutputStream}
import javax.imageio.ImageIO
import java.nio.file.Files
import java.nio.file.Paths


object Hello extends Greeting with App {

  val matrix = new QRCodeWriter().encode("https://www.google.com/", BarcodeFormat.QR_CODE, 320, 320)

  val config = new MatrixToImageConfig(0xff0000ff, 0xffffffff);
  val outputStream = new ByteArrayOutputStream()
  MatrixToImageWriter.writeToStream(matrix, "png", outputStream, config)
//  val qrCodeImage = MatrixToImageWriter.toBufferedImage(matrix, config)
//  ImageIO.write(qrCodeImage, "png", Files.newOutputStream(Paths.get("/Users/kana/Downloads/qrcode.png")))

  val book = new XSSFWorkbook()
  val sheet = book.createSheet()

  /** convert qr code to byte array */
  val bytes = outputStream.toByteArray
  outputStream.close()

  /** add image to workbook */
  val pictureIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG)

  /** get drawing instance */
  val patriarch = sheet.createDrawingPatriarch()

  /** get client anchor instance */
  val anchor = book.getCreationHelper().createClientAnchor()

  /** set position & size */
  anchor.setCol1(1)
  anchor.setRow1(1)
  anchor.setCol2(6)
  anchor.setRow2(6)

  /** set offset  */
//  anchor.setDx1(Units.EMU_PER_PIXEL * 10)
//  anchor.setDy1(Units.EMU_PER_PIXEL * 10)
//  anchor.setDx2(Units.EMU_PER_PIXEL * -10)
//  anchor.setDy2(Units.EMU_PER_PIXEL * -10)

  /** set anchor type of image */
  anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE)

  /** create picture */
  patriarch.createPicture(anchor, pictureIndex)

  /** create excel file */
  val excelOutputStream = new FileOutputStream("/Users/kana/Downloads/qr_code_test.xlsx")
  book.write(excelOutputStream)
  excelOutputStream.close()
}

trait Greeting {
  lazy val greeting: String = "hello"
}
