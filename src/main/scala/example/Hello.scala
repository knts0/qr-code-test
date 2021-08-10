package example

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.{MatrixToImageConfig, MatrixToImageWriter}
import com.google.zxing.qrcode.QRCodeWriter
import org.apache.poi.ss.usermodel.{ClientAnchor, Workbook}
import org.apache.poi.xssf.usermodel.{XSSFClientAnchor, XSSFDrawing, XSSFWorkbook}

import java.io.{ByteArrayOutputStream, FileOutputStream}


object Hello extends Sample with App {

  val qrCodeByteArray = createQRCode()
  writeQRCodeToExcelWorkbook(qrCodeByteArray)

}

trait Sample {

  def createQRCode(): Array[Byte] = {
    /** set your content */
    val content = "https://www.google.com/"

    /** convert your content to QR code matrix */
    val matrix = new QRCodeWriter().encode(
      content,
      BarcodeFormat.QR_CODE,
      320,
      320
    )

    /** write qr code to output stream */
    val config = new MatrixToImageConfig(0xff0000ff, 0xffffffff);
    val outputStream = new ByteArrayOutputStream()
    MatrixToImageWriter.writeToStream(matrix, "png", outputStream, config)
    outputStream.close()

    /** save qr code as image file */
    // val qrCodeImage = MatrixToImageWriter.toBufferedImage(matrix, config)
    // val outputStream = Files.newOutputStream(Paths.get("/tmp/qrcode.png"))
    // ImageIO.write(qrCodeImage, "png", outputStream)
    // outputStream.close()

    /** convert qr code to byte array */
    val qrCodeByteArray = outputStream.toByteArray

    /** return */
    qrCodeByteArray
  }

  def writeQRCodeToExcelWorkbook(bytes: Array[Byte]): Unit = {

    val book = new XSSFWorkbook()
    val sheet = book.createSheet()

    /**
     * add image to workbook
     *
     * picture options:
     * Workbook.PICTURE_TYPE_PNG: png
     * Workbook.PICTURE_TYPE_JPEG: png
     * and more...
     */
    val pictureIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG)

    val anchor: XSSFClientAnchor = createAnchor(book)

    /** get drawing instance and create picture */
    val patriarch: XSSFDrawing = sheet.createDrawingPatriarch()
    patriarch.createPicture(anchor, pictureIndex)

    /** write excel file */
    val excelOutputStream = new FileOutputStream("/tmp/qr_code_test.xlsx")
    book.write(excelOutputStream)
    excelOutputStream.close()
  }

  private def createAnchor(book: XSSFWorkbook) = {
    /** get client anchor instance */
    val anchor: XSSFClientAnchor = book.getCreationHelper().createClientAnchor()

    /** set position & size */
    anchor.setCol1(1)
    anchor.setRow1(1)
    anchor.setCol2(6)
    anchor.setRow2(6)

    /** set offset */
    //  anchor.setDx1(Units.EMU_PER_PIXEL * 10)
    //  anchor.setDy1(Units.EMU_PER_PIXEL * 10)
    //  anchor.setDx2(Units.EMU_PER_PIXEL * -10)
    //  anchor.setDy2(Units.EMU_PER_PIXEL * -10)

    /** simpler code (set anchor offset & position) */
    //   val anchor: XSSFClientAnchor = patriarch.createAnchor(dx1, dy1, dx2, dy2, col1, row1, col2, row2)

    /**
     * set anchor type of image
     *
     * anchor type options
     * - MOVE_AND_RESIZE
     * - MOVE_DONT_RESIZE
     * - DONT_MOVE_DO_RESIZE
     * - DONT_MOVE_AND_RESIZE
     */
    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE)

    anchor
  }
}
