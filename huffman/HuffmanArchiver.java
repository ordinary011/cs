package com.shpp.p2p.cs.ldebryniuk.assignment15;

import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This class comprises all the preliminary logic before compression or decompression
 */
public class HuffmanArchiver {

    private final String DEFAULT_INPUT_FILENAME = "test.txt";
    private final String DEFAULT_OUTPUT_FILENAME = "test.txt.par";

    private final String COMPRESSION = "compression";
    private final String DECOMPRESSION = "decompression";

    private final String PAR_ENDING = ".par";
    private final String UAR_ENDING = ".uar";

    private final String ARCHIVE_FLAG = "-a";
    private final String UNARCHIVE_FLAG = "-u";

    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_BLUE = "\u001B[34m";

    /**
     * Determines operation. Either compression or decompression based on args
     * @param args array of arguments from a user
     */
    public void determineOperation(String[] args) {
        int paramCount = args.length;

        switch (paramCount) {
            case 0:
                compressOrDecompress(DEFAULT_INPUT_FILENAME, DEFAULT_OUTPUT_FILENAME, COMPRESSION);
                break;
            case 1:
                logicFor1Param(args[0], args[0]);
                break;
            case 2:
                logicFor2Params(args[0], args[1]);
                break;
            case 3:
                logicForFlag(args[0], args[1], args[2]);
                break;
            default:
                System.err.println("Sorry too many parameters. Please check maybe there are some redundant spaces");
                break;
        }
    }

    /**
     * Determines operation based on input file extension
     */
    private void logicFor1Param(String inputFileName, String outputFileName) {
        int indOfLastDot = inputFileName.lastIndexOf(".");

        if (indOfLastDot != -1 || inputFileName.endsWith(PAR_ENDING)) { // if inputFile has any extension or ends ".par"
            int indexOfFirstDot = inputFileName.indexOf(".");

            // remove extension for output file e.g. "poem.txt.par" -> "poem.txt" || "test.par" -> "test"
            outputFileName = inputFileName.substring(0, indOfLastDot);

            if (indexOfFirstDot != indOfLastDot) { // true if inputFileName has two extensions e.g. "poem.txt.par"
                compressOrDecompress(inputFileName, outputFileName, DECOMPRESSION);
            } else { // file ends only with ".par" e.g. "test.par" -> extension of output file can not be determined
                compressOrDecompress(inputFileName, outputFileName + UAR_ENDING, DECOMPRESSION);
            }

        } else { // inputFile doesn't have an extension e.g. "test"
            compressOrDecompress(inputFileName, outputFileName + PAR_ENDING, COMPRESSION);
        }
    }

    /**
     * Determines operation based on input and output file extensions
     */
    private void logicFor2Params(String inputFileName, String outputFileName) {
        if (inputFileName.endsWith(PAR_ENDING)) {
            if (outputFileName.indexOf('.') != -1) { // true if outputFileName has an extension
                compressOrDecompress(inputFileName, outputFileName, DECOMPRESSION);
            } else { // outputFileName doesn't have an extension
                compressOrDecompress(inputFileName, outputFileName + UAR_ENDING, DECOMPRESSION);
            }
        } else { // inputFile doesn't end with ".par". Hence we compress
            if (outputFileName.endsWith(PAR_ENDING)) {
                compressOrDecompress(inputFileName, outputFileName, COMPRESSION);
            } else {
                compressOrDecompress(inputFileName, outputFileName + PAR_ENDING, COMPRESSION);
            }
        }
    }

    /**
     * Determines operation based on the specified flag
     *
     * @param flag can be either "-a" or "-u"
     */
    private void logicForFlag(String flag, String inputFile, String outputFile) {
        if (flag.equals(ARCHIVE_FLAG)) {
            compressOrDecompress(inputFile, outputFile, COMPRESSION);
        } else if (flag.equals(UNARCHIVE_FLAG)) {
            compressOrDecompress(inputFile, outputFile, DECOMPRESSION);
        } else {
            System.err.println("Sorry could not recognize the following flag: " + flag);
        }
    }

    /**
     * Compresses or Decompresses the file
     *
     * @param operation can be either "compression" or "decompression" string
     */
    private void compressOrDecompress(String inputFile, String outputFile, String operation) {
        try (FileChannel inputFChan = (FileChannel) Files.newByteChannel(Paths.get(inputFile));
             FileChannel outputFChan = (FileChannel) Files.newByteChannel(Paths.get(outputFile),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            long startTime = System.currentTimeMillis();
            long inputFileSize = inputFChan.size();

            if (inputFileSize != 0) { // inputFileSize == 0 only when file is empty
                if (operation.equals(COMPRESSION)) {
                    new HuffCompressor().compressFile(inputFChan, outputFChan, inputFileSize);
                } else if (operation.equals(DECOMPRESSION)) {
                    new HuffDecompressor().decompressFile(inputFChan, outputFChan, inputFileSize);
                }
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            long outFSize = outputFChan.size();
            long efficiency = (operation.equals(COMPRESSION)) ? inputFileSize - outFSize : outFSize - inputFileSize;

            System.out.print(ANSI_GREEN);
            System.out.printf("Efficiency of %s: %d (bytes)\n", operation, efficiency);
            System.out.printf("Time of %s: %d (milliseconds)\n", operation, duration);
            System.out.print(ANSI_BLUE);
            System.out.printf("Input file size: %d (bytes)\n", inputFileSize);
            System.out.printf("Output file size: %d (bytes)\n", outFSize);
        } catch (Exception e) {
            System.err.println("Some mistake occurred. See the reason down below");
            e.printStackTrace();
        }
    }
}
