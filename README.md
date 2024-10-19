# Pratical work 1 DAI - QR Code Generator

This project has been made in the context of [DAI](https://github.com/heig-vd-dai-course/) courses at HEIG-VD

## Authors

```
      █████████████████████████████████        █████████████████████████████████
      █████████████████████████████████        █████████████████████████████████
      ████ ▄▄▄▄▄ █▀▄█▀ ▄█ ▀█ ▄▄▄▄▄ ████        ████ ▄▄▄▄▄ █▀▄█▀  ▄ ▄█ ▄▄▄▄▄ ████
      ████ █   █ █▄   ▄ ▀ ▀█ █   █ ████        ████ █   █ █▄   ▄▀ ▀▀█ █   █ ████
      ████ █▄▄▄█ █ ▀█▀██▄ ▀█ █▄▄▄█ ████        ████ █▄▄▄█ █ ▀█▀█▄▀███ █▄▄▄█ ████
      ████▄▄▄▄▄▄▄█ ▀▄█ █ █▄█▄▄▄▄▄▄▄████        ████▄▄▄▄▄▄▄█ ▀▄█ █▄▀▄█▄▄▄▄▄▄▄████
      ████▄ █▄██▄▄▀█ ▀█▀▀  ▀▄▄▄█▄ ▀████        ████▄▄█ ██▄▄██ ▀█▄█▀▀▀▄▄▄█▄ ▀████
      ████ █▀▄  ▄▄ ▄▄██▀ █▀  █ ▄█▄ ████        ████▀▄▀ ▀█▄  ▄▄██▄█ ▀  █ ▄█▄ ████
      ████▄▄▄█▀ ▄▀▄█▀▀ █▄ ▀█▀ ▄█ ▀▀████        ████▄ ▄▄▀▀▄▀ █▀▀ █▄▀▀█▀ ▄█ ▀▀████
      ████▄▀▄▀▀█▄▀▀▀▄█ ▀██▄█ ▄█▀█▄█████        ████▄▀  ▀▀▄ ▀  █ ▀█▀▀█ ▄█▀█▄█████
      ████▄▄▄▄██▄▄▀▄▄▄█▄ █ ▄▄▄ ▄ ▀ ████        ████▄▄█▄██▄▄ █ ▄█▄ ▄ ▄▄▄ ▄ ▀ ████
      ████ ▄▄▄▄▄ █▄ ▀▀█ █  █▄█ ▄▄▀▀████        ████ ▄▄▄▄▄ █▄▄ ▀█ █  █▄█ ▄▄▀▀████
      ████ █   █ █▀▀█  ▀▄█ ▄▄   ▄▀█████        ████ █   █ █▀ ▀  ▀▄█ ▄▄   ▄▀█████
      ████ █▄▄▄█ █▀▄▀   ▄  ▀▄ ██ █ ████        ████ █▄▄▄█ █▀█    ▄  ▀▄ ██ █ ████
      ████▄▄▄▄▄▄▄█▄▄▄▄██▄▄██▄█▄███▄████        ████▄▄▄▄▄▄▄█▄▄█▄██▄▄██▄█▄███▄████
      █████████████████████████████████        █████████████████████████████████
      █████████████████████████████████        █████████████████████████████████
           Antoine Leresche (A2va)                Robin Forestier (Forestierr)  
```

## Description

A command-line interface (CLI) tool that allows users to generate QR codes by providing text or URLs as input.

### Features:

- [X] Create QR codes from text / url / file / ...
- [X] Display the QR in the terminal (ascii art)
- [X] Save in a file (ascii art)
- [X] Save as image
- [ ] Change size / shape / encoding

## Installation

Start by cloning the repository:

```bash
git clone https://github.com/A2va/dai-qr-code.git
```

Then, you can compile the project into a JAR located into the target folder:
```bash
./mvnw package
```

## Usage

When running qrcode with the `help` option, you can have a full description on all other options:
```bash
a2va@linux:~$ java -jar target/qrcode-1.0-SNAPSHOT.jar --help
Usage: qrcode-1.0-SNAPSHOT.jar [-hsV] [-if=<inputFormat>] [-o=<outputFilePath>
                               [-of=<outputFormat>] [-f]] <text>
A command line interface (CLI) tool that allows users to generate QR codes by
providing text or URLs as input.
      <text>      The text or file to be encoded as a QR code.
  -f, --force     Don't prompt to overwrite if file exists.
  -h, --help      Show this help message and exit.
      -if, --input-format=<inputFormat>
                  Input format, default text (possible format: text, file)
  -o, --output=<outputFilePath>
                  The path to the output file.
      -of, --output-format=<outputFormat>
                  The output format, (possible format: text, jpeg)
  -s, --show      Force the display of the QR code in the terminal if the
                    output is a file, by default the QR code is displayed if no
                    output is provided.
  -V, --version   Print version information and exit.
```

Here is the most basic usage of the program (`java -jar` has been removed for better readability)
```bash
a2va@linux:~$ qrcode "https://example.com/generate-qr-from-url"
```

By default, the QR code is displayed in the terminal below your command.
You can also output the code to a text file or a binary image file (like jpeg), this can be done by:
```bash
a2va@linux:~$ qrcode --output-format=JPEG -o "output.jpg" "https://example.com/generate-qr-from-url"
```

Finally, the text to be encoded in the QR code can be taken from an existing file, in this mode it also reads any text line by line, you can do it with:
```bash
a2va@linux:~$ qrcode --input-format=file -i input.txt --output-format=JPEG -o "output.jpg"
```
The above command creates as many images as there are lines in the input file. 
The file will be named `output.jpg, output1.jpg, output2.jpg` and so on.
