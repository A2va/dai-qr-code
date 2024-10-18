// MIT License
//
// Copyright (c) 2024 Robin Forestier (Forestierr)
//                    Antoine Leresche (A2va)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
package ch.heigvd.dai.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeGenerator {

  private BitMatrix matrix;

  public QRCodeGenerator(String text) throws WriterException {
    QRCodeWriter writer = new QRCodeWriter();
    this.matrix = writer.encode(text, BarcodeFormat.QR_CODE, 20, 20);
  }

  /** Display the current qrcode in the standard output. */
  public void show() {
    QRCodeTerminal.display(matrix, true);
  }

  /**
   * Generates a QR code and encode it as an image and return it in a ByteArray.
   *
   * @param format the image format to use (e.g. "PNG", "JPEG")
   * @throws IOException if there is an error writing the image to the file
   * @return the ByteArrayOutputStream containing the image data
   */
  public ByteArrayOutputStream generateImage(String format) throws IOException {
    ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(matrix, format, imgOutputStream);
    return imgOutputStream;
  }

  /**
   * Generates a QR code and encode it as an text and return it in a ByteArray.
   *
   * @throws IOException if there is an error writing to the file
   * @return the ByteArrayOutputStream containing the text data
   */
  public ByteArrayOutputStream generateText() throws IOException {
    String text = QRCodeTerminal.generateQrString(matrix, true);
    ByteArrayOutputStream textOutputStream = new ByteArrayOutputStream();
    textOutputStream.write(text.getBytes());
    return textOutputStream;
  }
}
