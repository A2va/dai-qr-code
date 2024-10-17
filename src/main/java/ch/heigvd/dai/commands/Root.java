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
package ch.heigvd.dai.commands;

import ch.heigvd.dai.qrcode.QRCodeGenerator;
import java.io.*;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
    description =
        "A command line interface (CLI) tool that allows users to generate QR codes by providing text or URLs as input.",
    version = "1.0.0",
    scope = CommandLine.ScopeType.INHERIT,
    mixinStandardHelpOptions = true)
public class Root implements Callable<Integer> {

  public enum AvailableOutputFormat {
    TEXT,
    JPEG;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public enum AvailableInputFormat {
    TEXT,
    FILE;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  @CommandLine.Parameters(index = "0", description = "The text or file to be encoded as a QR code.")
  protected String text;

  /*
   * Creation of a dependent group to group the output file path and output format options.
   * If an output format is provided, the output file path is required.
   * But if the output file path is provided, the output format is optional.
   * And lastly, if no output file path is provided, the output format is also useless.
   */
  @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
  OutputDependent groupOutput = new OutputDependent();

  static class OutputDependent {
    @CommandLine.Option(
        names = {"-o", "--output"},
        description = "The path to the output file.",
        required = true)
    protected File outputFilePath;

    @CommandLine.Option(
        names = {"-of", "--output-format"},
        description = "The output format, (possible format: ${COMPLETION-CANDIDATES}) ",
        required = false)
    protected AvailableOutputFormat outputFormat;

    @CommandLine.Option(
        names = {"-f", "--force"},
        description = "Don't prompt to overwrite if file exists.",
        required = false)
    protected boolean force = false;

    public File getOutputFilePath() {
      return outputFilePath;
    }

    public AvailableOutputFormat getOutputFormat() {
      return outputFormat;
    }

    public boolean isForce() {
      return force;
    }
  }

  @CommandLine.Option(
      names = {"-s", "--show"},
      description =
          "Force the display of the QR code in the terminal if the output is a file, by default the QR code is displayed if no output is provided.",
      required = false)
  protected boolean show = false;

  @CommandLine.Option(
      names = {"-if", "--input-format"},
      description = "Input format, default text (possible format: ${COMPLETION-CANDIDATES})",
      required = false)
  protected AvailableInputFormat inputFormat = AvailableInputFormat.TEXT;

  @Override
  public Integer call() {

    // Check if the output file exists and if the user wants to overwrite it
    // https://github.com/remkop/picocli/issues/1275
    if (groupOutput.getOutputFilePath() != null
        && groupOutput.getOutputFilePath().exists()
        && !groupOutput.isForce()) {
      String response = null;
      while (!"Y".equalsIgnoreCase(response)) {
        System.out.println(
            "Do you want ot overwrite " + groupOutput.getOutputFilePath().getName() + " ? Y/N");

        try {
          // I use a BufferedReader to read the user's response because System.console() returns
          // null in IntelliJ IDEA
          BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
          response = reader.readLine();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        if ("N".equalsIgnoreCase(response)) {
          return 0;
        }
      }
    }

    // If the input format is a file, verify that the file exists
    if (inputFormat == AvailableInputFormat.FILE) {
      File file = new File(text);
      if (!file.exists()) {
        System.err.println("The file " + text + " does not exist.");
        System.err.println("Please provide a valid file path.");
        return 1;
      }
    }

    if (inputFormat == AvailableInputFormat.TEXT) {
      try {
        QRCodeGenerator qrCodeGenerator = new QRCodeGenerator(getText());

        if (groupOutput.getOutputFormat() == null || isShow()) {
          qrCodeGenerator.show();
        }

        saveOutput(qrCodeGenerator, groupOutput.getOutputFilePath());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (inputFormat == AvailableInputFormat.FILE) {
      try (BufferedReader reader = new BufferedReader(new FileReader(text))) {
        String line;
        int numfile = 1;
        File filename = groupOutput.getOutputFilePath();
        while ((line = reader.readLine()) != null) {
          QRCodeGenerator qrCodeGenerator = new QRCodeGenerator(line);

          if (groupOutput.getOutputFormat() == null || isShow()) {
            qrCodeGenerator.show();
          }

          saveOutput(qrCodeGenerator, filename);

          // Add a number to the filename ex : file.jpg, file1.jpg, file2.jpg, ...
          String[] split = groupOutput.getOutputFilePath().getName().split("\\.");
          String newFilename = split[0] + numfile + "." + split[1];
          filename = new File(filename.getParent(), newFilename);
          numfile++;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return 0;
  }

  public String getText() {
    return text;
  }

  public boolean isShow() {
    return show;
  }

  private void saveOutput(QRCodeGenerator qrCodeGenerator, File outputFile) {
    ByteArrayOutputStream outputStream =
        switch (groupOutput.getOutputFormat()) {
          case JPEG -> {
            try {
              yield qrCodeGenerator.generateImage(groupOutput.getOutputFormat().toString());
            } catch (IOException e) {
              e.printStackTrace();
              yield null;
            }
          }
          case TEXT -> {
            try {
              yield qrCodeGenerator.generateText();
            } catch (Exception e) {
              e.printStackTrace();
              yield null;
            }
          }
          case null -> {
            yield null;
          }
        };

    if (outputStream != null && outputFile != null) {
      try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile.getAbsolutePath())) {
        outputStream.writeTo(fileOutputStream);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
