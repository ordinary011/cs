package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Archiver {

    /**
     * Determines operation. Either compression or decompression
     */
    public void determineOperation(String[] args) {
        if (args.length == 0) {
            compressOrDecompress("test.txt", "test.txt.par", "compression");
        } else if (args.length == 1) {
            determineOpBasedOnExtension(args[0], args[0]);
        } else if (args.length == 2) {
            determineOpBasedOnExtension2(args[0], args[1]);
        } else if (args.length == 3) {
            String flag = args[0];
            if (flag.equals("-a")) {
                compressOrDecompress(args[1], args[2], "compression");
            } else if (flag.equals("-u")) {
                compressOrDecompress(args[1], args[2], "decompression");
            }
        } else {
            System.err.println("Sorry too many parameters. Please check maybe there are some redundant spaces");
        }
    }

    /**
     * Determines operation based on input file extension
     */
    private void determineOpBasedOnExtension(String inputFileName, String outputFileName) {
        int indOfLastDot = inputFileName.lastIndexOf(".");

        if (indOfLastDot != -1 || inputFileName.endsWith(".par")) { //if inputFile has any extension or ends with ".par"
            int indexOfFirstDot = inputFileName.indexOf(".");

            // remove extension for output file e.g. "poem.txt.par" -> "poem.txt" || "test.par" -> "test"
            outputFileName = inputFileName.substring(0, indOfLastDot); // todo

            if (indexOfFirstDot != indOfLastDot) { // true if inputFileName e.g. "poem.txt.par"
                compressOrDecompress(inputFileName, outputFileName, "decompression");
            } else { // file ends only with ".par" e.g. "test.par" -> extension of output file can not be determined
                compressOrDecompress(inputFileName, outputFileName + "uar", "decompression");
            }

        } else { // inputFile doesn't have an extension e.g. "test"
            compressOrDecompress(inputFileName, outputFileName + ".par", "compression");
        }
    }

    /**
     * Determines operation based on input file extension
     */
    private void determineOpBasedOnExtension2(String inputFileName, String outputFileName) {
        int indOfLastDot = inputFileName.lastIndexOf(".");

        if (indOfLastDot != -1 || inputFileName.endsWith(".par")) { //if inputFile has any extension or ends with ".par"
            int indexOfFirstDot = inputFileName.indexOf(".");

            // remove extension for output file e.g. "poem.txt.par" -> "poem.txt" || "test.par" -> "test"
            outputFileName = inputFileName.substring(0, indOfLastDot); // todo

            if (indexOfFirstDot != indOfLastDot) { // true if inputFileName e.g. "poem.txt.par"
                compressOrDecompress(inputFileName, outputFileName, "decompression");
            } else { // file ends only with ".par" e.g. "test.par" -> extension of output file can not be determined
                compressOrDecompress(inputFileName, outputFileName + "uar", "decompression");
            }

        } else { // inputFile doesn't have an extension e.g. "test"
            compressOrDecompress(inputFileName, outputFileName + ".par", "compression");
        }
    }

    /**
     * Compresses Or Decompresses the file
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

            if (operation.equals("compression")) {
                new Compressor().compressFile(inputFChan, outputFChan, inputFileSize);
            } else if (operation.equals("decompression")) {
                new Decompressor().decompressFile(inputFChan, outputFChan);
            }

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            long outFSize = outputFChan.size();
            long efficiency = (operation.equals("compression")) ? inputFileSize - outFSize : outFSize - inputFileSize;

            System.out.printf("efficiency of %s: %d (bytes)\n", operation, efficiency);
            System.out.printf("time of compression: %d (milliseconds)\n", duration);
            System.out.printf("input file size: %d (bytes)\n", inputFileSize);
            System.out.printf("output file size: %d (bytes)\n", outFSize);
        } catch (Exception e) {
            System.err.println("jo bro mistake down here");
            e.printStackTrace();
        }
    }
}
