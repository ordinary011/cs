package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTreeNode;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.InternalTreeNode;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * The following class contains all the logic that is needed for decompression
 */
class HuffDecompressor extends CommonUtils {

    private final byte[] decompressedDataChunk = new byte[MEGABYTE * 4];
    private int redundantBitsInLastCompressedByte;
    private int RBufferSequentialNum = 1; // first read buffer
    private BTreeNode currentNode = null;

    /**
     * Starting method for decompressing the data
     *
     * @param inputFChan    reference to the input channel between our process and OS
     * @param outputFChan   reference to the output channel between our process and OS
     * @param inputFileSize size of input file
     * @throws IOException can be caused by reading or writing the file
     */
    void decompressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        int totalNumberOfRBuffers = (int) Math.ceil(inputFileSize / (double) MEGABYTE);

        // read first chunk of data to the buff
        int bytesInsideReadBuffer = inputFChan.read(readBuff);
        readBuff.rewind(); // set the position inside the buff to the beginning

        redundantBitsInLastCompressedByte = readBuff.get();
        int tableInfoSize = readBuff.get();

        InternalTreeNode rootNode = recreateTree(tableInfoSize);
        currentNode = rootNode;

        // read the rest of the first buffer
        int remainedBytesInFirstRBuff = bytesInsideReadBuffer - readBuff.position();
        decompressDataChunk(outputFChan, remainedBytesInFirstRBuff, totalNumberOfRBuffers, rootNode);

        // if compressed file is larger than 1 megabyte continue reading (by chunks) till end of it
        while (RBufferSequentialNum <= totalNumberOfRBuffers) {
            bytesInsideReadBuffer = inputFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            decompressDataChunk(outputFChan, bytesInsideReadBuffer, totalNumberOfRBuffers, rootNode);
        }
    }

    /**
     * decompresses the data from the input file
     *
     * @param outputFChan           reference to the output channel between our process and OS
     * @param bytesInsideReadBuffer amount of bytes Inside Read Buffer
     * @param totalNumberOfRBuffers total amount of read buffers
     * @param rootNode              reference to the tree root
     * @throws IOException can be caused by reading or writing the file
     */
    private void decompressDataChunk(FileChannel outputFChan, int bytesInsideReadBuffer,
                                     int totalNumberOfRBuffers, BTreeNode rootNode) throws IOException {
        int decompressedDataChunkIndex = 0;

        int offsetFromRight = 0;
        int lastByteInCurrentRBuff = bytesInsideReadBuffer - 1;
        for (int i = 0; i < bytesInsideReadBuffer; i++) {

            int compressedByte = readBuff.get();

            // if this is the last byte in the last read buffer
            if (totalNumberOfRBuffers == RBufferSequentialNum && i == lastByteInCurrentRBuff) {
                offsetFromRight = redundantBitsInLastCompressedByte;
            }

            for (int shiftBitsToRight = (Byte.SIZE - 1); shiftBitsToRight >= offsetFromRight; shiftBitsToRight--) {
                currentNode = ((InternalTreeNode) currentNode).findEncodedByte(compressedByte, shiftBitsToRight);
                if (currentNode.isTreeLeaf()) {
                    decompressedDataChunk[decompressedDataChunkIndex++] = ((TreeLeaf) currentNode).getByteValue();
                    currentNode = rootNode;
                }
            }

        }
        readBuff.rewind(); // set the position inside the buff to the beginning
        RBufferSequentialNum++;

        writeDecompressedToTheFile(outputFChan, decompressedDataChunkIndex);
    }

    /**
     * writes data to the output file
     *
     * @param outputFChan              reference to the output channel between our process and OS
     * @param decompressedDataChunkInd paste index inside decompressed data chunk array
     * @throws IOException can be caused by reading or writing the file
     */
    private void writeDecompressedToTheFile(FileChannel outputFChan, int decompressedDataChunkInd) throws IOException {
        if (writeBuff.capacity() != decompressedDataChunkInd) {
            writeBuff = ByteBuffer.allocate(decompressedDataChunkInd);
        }
        for (int i = 0; i < decompressedDataChunkInd; i++) {
            writeBuff.put(decompressedDataChunk[i]);
        }
        writeBuff.rewind();  // set the position inside the buff to the beginning

        // write to the file
        outputFChan.write(writeBuff);
        writeBuff.rewind();  // set the position inside the buff to the beginning
    }

    /**
     * creates an array with encoding lengths for each unique byte
     *
     * @param tableInfoSize amount of bytes that are used as information about the table (see algorithmExplanation.txt)
     * @return array that contains bytesCountWithTheSameEncodingLength and encodingLengthOfBytesCountWithTheSameLength
     */
    private ArrayList<Integer> createArrOfEncodingLengths(int tableInfoSize) {
        ArrayList<Integer> lengthsOfEncodings = new ArrayList<>();

        for (int i = 0; i < (tableInfoSize / 2); i++) {
            int bytesCountWithTheSameEncodingLength = readBuff.get() & 0xff; // byte to unsigned byte in the integer
            int encodingLenOfBytesCount = readBuff.get();

            for (int j = 0; j < bytesCountWithTheSameEncodingLength; j++) {
                lengthsOfEncodings.add(encodingLenOfBytesCount);
            }
        }

        return lengthsOfEncodings;
    }

    /**
     * recreates tree based on the encodings of the bytes
     *
     * @param tableInfoSize amount of bytes that are used as information about the table (see algorithmExplanation.txt)
     * @return reference to the tree root
     */
    private InternalTreeNode recreateTree(int tableInfoSize) {
        ArrayList<Integer> lengthsOfEncodings = createArrOfEncodingLengths(tableInfoSize);
        InternalTreeNode rootNode = new InternalTreeNode();

        for (Integer usedBitsForEncodingTheByte : lengthsOfEncodings) {
            int tableByte = readBuff.get();
            int usedBitsForEncoding = usedBitsForEncodingTheByte;

            int encodingForTheByte;
            if (usedBitsForEncoding > Byte.SIZE) {
                encodingForTheByte = readBuff.getShort();
            } else {
                encodingForTheByte = readBuff.get();
            }

            int firstBitInEncoding = encodingForTheByte >>> (usedBitsForEncoding - 1); //00001010 >>> 3 becomes 00000001
            firstBitInEncoding &= 1; // encodingForTheByte is < 0 then after shift we need to remove bit to the left

            rootNode.recreateTreeLeaf(tableByte, encodingForTheByte, usedBitsForEncoding, firstBitInEncoding);
        }

        return rootNode;
    }

}
