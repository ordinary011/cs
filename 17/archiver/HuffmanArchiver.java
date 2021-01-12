package com.shpp.p2p.cs.ldebryniuk.assignment17.archiver;

import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
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

    private final String PAR_ENDING = ".par"; // standart ending of the name for archived file
    private final String UAR_ENDING = ".uar"; // unarchived (used when output file name can not be determined)

    private final String ARCHIVE_FLAG = "-a";
    private final String UNARCHIVE_FLAG = "-u";

    private final String ANSI_BLUE = "\u001B[34m";
    private final String ANSI_GREEN = "\u001B[32m";

    /**
     * Determines operation. Either compression or decompression based on args
     *
     * @param args array of arguments from a user
     */
    public void determineOperation(String[] args) {
        int paramCount = args.length;

        switch (paramCount) {
            case 0:
                compressOrDecompress(DEFAULT_INPUT_FILENAME, DEFAULT_OUTPUT_FILENAME, COMPRESSION);
                break;
            case 1:
                logicFor1Param(args[0]);
                break;
            case 2:
                logicFor2Params(args[0], args[1]);
                break;
            case 3:
                logicForFlags(args[0], args[1], args[2]);
                break;
            default:
                System.err.println("Sorry too many parameters. Please check maybe there are some redundant spaces");
                break;
        }
    }

    /**
     * Determines operation based on input file extension
     *
     * @param inputFileName relative path to the input file
     */
    private void logicFor1Param(String inputFileName) {
        if (inputFileName.endsWith(PAR_ENDING)) {
            int indexOfFirstDot = inputFileName.indexOf(".");
            int indOfLastDot = inputFileName.lastIndexOf(".");

            // remove ".par" ending for output file e.g. "poem.txt.par" -> "poem.txt" || "test.par" -> "test"
            String outputFileName = inputFileName.substring(0, indOfLastDot);

            if (indexOfFirstDot != indOfLastDot) { // true if inputFileName has two extensions e.g. "poem.txt.par"
                compressOrDecompress(inputFileName, outputFileName, DECOMPRESSION);
            } else { // file ends only with ".par" e.g. "test.par" -> extension of output file can not be determined
                compressOrDecompress(inputFileName, outputFileName + UAR_ENDING, DECOMPRESSION);
            }

        } else { // inputFile doesn't end with ".par" or doesn't have an extension e.g. "test"
            compressOrDecompress(inputFileName, inputFileName + PAR_ENDING, COMPRESSION);
        }
    }

    /**
     * Determines operation based on input and output file extensions
     *
     * @param inputFileName  relative path to the input file
     * @param outputFileName relative path to the output file
     */
    private void logicFor2Params(String inputFileName, String outputFileName) {
        if (inputFileName.endsWith(PAR_ENDING)) { // PAR_ENDING identify that we must decompress
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
     * * Determines operation based on the specified flag
     *
     * @param flag       can be either "-a" or "-u"
     * @param inputFile  relative path to the input file
     * @param outputFile relative path to the output file
     */
    private void logicForFlags(String flag, String inputFile, String outputFile) {
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
     * @param inputFile  relative path to the input file
     * @param outputFile relative path to the output file
     * @param operation  can be either "compression" or "decompression" string
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
            long runTimeDuration = endTime - startTime;
            long outFSize = outputFChan.size();
            long efficiency = (operation.equals(COMPRESSION)) ? inputFileSize - outFSize : outFSize - inputFileSize;

            logResults(operation, efficiency, runTimeDuration, inputFileSize, outFSize);
        } catch (NoSuchFileException e) {
            System.err.println("Could not find file with the following name: " + inputFile);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Logs the result of compression or decompression
     *
     * @param operation       can be either "compression" or "decompression" string
     * @param efficiency      efficiency of compression or decompression (difference between input and output files)
     * @param runTimeDuration duration of the program runtime
     * @param inputFSize      size of input file
     * @param outFSize        size of output file
     */
    private void logResults(String operation, long efficiency, long runTimeDuration, long inputFSize, long outFSize) {
        System.out.print(ANSI_GREEN);
        System.out.printf("Efficiency of %s: %d (bytes)\n", operation, efficiency);
        System.out.printf("Time of %s: %d (milliseconds)\n", operation, runTimeDuration);
        System.out.print(ANSI_BLUE);
        System.out.printf("Input file size: %d (bytes)\n", inputFSize);
        System.out.printf("Output file size: %d (bytes)\n", outFSize);
    }
}
