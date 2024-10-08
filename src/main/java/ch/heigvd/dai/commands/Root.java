package ch.heigvd.dai.commands;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        // Check if the output file exists and if the user wants to overwrite it
        // https://github.com/remkop/picocli/issues/1275
        if (groupOutput.getOutputFilePath() != null && groupOutput.getOutputFilePath().exists() && !groupOutput.isForce()) {
            String response = null;
            while (!"Y".equalsIgnoreCase(response)) {
                System.out.println("Do you want ot overwrite " + groupOutput.getOutputFilePath().getName() + " ? Y/N");

                try {
                    // I use a BufferedReader to read the user's response because System.console() returns null in IntelliJ IDEA
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    response = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if ("N".equalsIgnoreCase(response)) { return 0; }
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


        // TODO: Call the QRCodeGenerator class with the provided parameters

        System.out.println("Params : " +
                "text = " + text + ", " +
                "outputFilePath = " + groupOutput.getOutputFilePath() + ", " +
                "outputFormat = " + groupOutput.getOutputFormat() + ", " +
                "force = " + groupOutput.isForce() + ", " +
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