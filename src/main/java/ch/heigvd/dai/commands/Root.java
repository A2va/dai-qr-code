package ch.heigvd.dai.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
        description = "A command-line interface (CLI) tool that allows users to generate QR codes by providing text or URLs as input.",
        version = "1.0.0",
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)

public class Root implements Callable<Integer>{

    public enum AvailableOutputFormat {
        TEXT,
        JPEG
    }

    public enum AvailableInputFormat {
        TEXT,
        FILE
    }

    @CommandLine.Parameters(
            index = "0",
            description = "The text or URL or file to encode as a QR code.")
    protected String text;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "The path to the output file.",
            required = false)
    protected String outputFilePath;

    @CommandLine.Option(
            names = {"-of", "--output-format"},
            description = "The output format, (possible format: ${COMPLETION-CANDIDATES}) ",
            required = false)
    protected AvailableOutputFormat outputFormat = AvailableOutputFormat.TEXT;

    @CommandLine.Option(
            names = {"-s", "--show"},
            description = "Force showing the QR code in the terminal if the output is a file, by default show is enabled when no output was provided.",
            required = false)
    protected boolean show = false;

    @CommandLine.Option(
            names = {"-if", "--input-format"},
            description = "Either text or file, if set to file you must provide a valid path as a positional parameter, (possible format: ${COMPLETION-CANDIDATES})",
            required = false)
    protected AvailableInputFormat inputFormat = AvailableInputFormat.TEXT;

    @Override
    public Integer call() {

        // TODO: Call the QRCodeGenerator class with the provided parameters

        System.out.println("Params : " +
                "text = " + text + ", " +
                "outputFilePath = " + outputFilePath + ", " +
                "outputFormat = " + outputFormat + ", " +
                "show = " + show + ", " +
                "inputFormat = " + inputFormat);

        return 0;
    }

    public String getText() {
        return text;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public AvailableOutputFormat getOutputFormat() {
        return outputFormat;
    }

    public boolean isShow() {
        return show;
    }
}