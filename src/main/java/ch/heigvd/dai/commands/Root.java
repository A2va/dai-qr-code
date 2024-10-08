package ch.heigvd.dai.commands;

import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
        description = "A command line interface (CLI) tool that allows users to generate QR codes by providing text or URLs as input.",
        version = "1.0.0",
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)

public class Root implements Callable<Integer>{

    public enum AvailableOutputFormat {
        TEXT, JPEG;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public enum AvailableInputFormat {
        TEXT, FILE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @CommandLine.Parameters(
            index = "0",
            description = "The text or file to be encoded as a QR code.")
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

        public File getOutputFilePath() {
            return outputFilePath;
        }

        public AvailableOutputFormat getOutputFormat() {
            return outputFormat;
        }
    }

    @CommandLine.Option(
            names = {"-s", "--show"},
            description = "Force the display of the QR code in the terminal if the output is a file, by default the QR code is displayed if no output is provided.",
            required = false)
    protected boolean show = false;

    @CommandLine.Option(
            names = {"-if", "--input-format"},
            description = "Input format, default text (possible format: ${COMPLETION-CANDIDATES})",
            required = false)
    protected AvailableInputFormat inputFormat = AvailableInputFormat.TEXT;

    @Override
    public Integer call() {

        // TODO: Call the QRCodeGenerator class with the provided parameters

        System.out.println("Params : " +
                "text = " + text + ", " +
                "outputFilePath = " + groupOutput.getOutputFilePath() + ", " +
                "outputFormat = " + groupOutput.getOutputFormat() + ", " +
                "show = " + show + ", " +
                "inputFormat = " + inputFormat);

        return 0;
    }

    public String getText() {
        return text;
    }

    public boolean isShow() {
        return show;
    }
}