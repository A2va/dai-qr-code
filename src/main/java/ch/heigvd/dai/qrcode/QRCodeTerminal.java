// MIT License
//
// Copyright (c) 2022 Auties00
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

// Original repo: https://github.com/Auties00/QrToTerminal
// The author of the repo did not specify a licence, but if you look for the package
// com.github.auties00:qr-terminal on mvnrepository.com (https://mvnrepository.com/artifact/com.github.auties00/qr-terminal),
// you can see that it is distributed under the MIT licence.
// We modified the code slightly to suit our needs.

package ch.heigvd.dai.qrcode;

import com.google.zxing.common.BitMatrix;
import java.util.Objects;

/** Utility class for displaying QR codes in the terminal. */
public class QRCodeTerminal {

  // Terminal symbols used for representing QR code blocks.
  private static final char FULL_BLOCK = '█';
  private static final char EMPTY_BLOCK = ' ';
  private static final char UPPER_HALF_BLOCK = '▀';
  private static final char LOWER_HALF_BLOCK = '▄';

  // Terminal ANSI color codes.
  public static final String BLACK_BG = "\033[40m  \033[0m";
  public static final String WHITE_BG = "\033[37m  \033[0m";

  /**
   * Prints a QR code in the terminal.
   *
   * @param matrix The BitMatrix containing the QR code data.
   * @param compact Whether to print in a compact form or full size.
   */
  public static void display(BitMatrix matrix, boolean compact) {
    System.out.println(generateQrString(matrix, compact));
  }

  /**
   * Generates a string representation of the QR code.
   *
   * @param matrix The BitMatrix object containing the QR code.
   * @param compact Whether to print in compact form or full size.
   * @return The string representation of the QR code.
   */
  public static String generateQrString(BitMatrix matrix, boolean compact) {
    Objects.requireNonNull(matrix, "QR data matrix cannot be null");
    return compact ? generateCompactQr(matrix) : generateFullQr(matrix);
  }

  /**
   * Generates a compact QR code representation using half-blocks.
   *
   * @param matrix The BitMatrix containing the QR code data.
   * @return A string that represents the QR code in compact form.
   */
  private static String generateCompactQr(BitMatrix matrix) {
    StringBuilder qrCode = new StringBuilder();

    // Iterate over QR code matrix and generate rows with upper and lower half blocks
    for (int row = 0; row < matrix.getHeight(); row += 2) {
      qrCode.append("\n");
      for (int col = 0; col < matrix.getWidth(); col++) {
        boolean topPixel = matrix.get(col, row);
        boolean bottomPixel = row + 1 < matrix.getHeight() && matrix.get(col, row + 1);

        if (topPixel && bottomPixel) {
          qrCode.append(EMPTY_BLOCK);
        } else if (topPixel) {
          qrCode.append(LOWER_HALF_BLOCK);
        } else if (bottomPixel) {
          qrCode.append(UPPER_HALF_BLOCK);
        } else {
          qrCode.append(FULL_BLOCK);
        }
      }
    }

    qrCode.append("\n");

    return qrCode.toString();
  }

  /**
   * Generates a full QR code representation using ANSI background colors.
   *
   * @param matrix The BitMatrix containing the QR code data.
   * @return A string that represents the QR code in full size.
   */
  private static String generateFullQr(BitMatrix matrix) {
    return matrix.toString(BLACK_BG, WHITE_BG);
  }
}
