package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTreeNode;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.InternalTreeNode;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class HuffDecompressor {

    int MEGABYTE = 1024 * 1024;
//    int MEGABYTE = 200;
    private final ByteBuffer readBuff = ByteBuffer.allocate(MEGABYTE);
    int totallyDecodedBytes = 0;

    /**
     * Starting method for decompressing the data
     *
     * @throws IOException
     */
    void decompressFile(FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        int totalNumberOfRBuffers = (int) Math.ceil(inputFChan.size() / (double) MEGABYTE);

        // read first chunk of data to the buff
        int bytesInsideReadBuffer = inputFChan.read(readBuff);
        readBuff.rewind(); // set the position inside the buff to the beginning
        int RBufferSequentialNum = 1; // first read buffer

        int redundantBitsInLastCompressedByte = readBuff.get();
        int tableInfoSize = readBuff.get();

        ArrayList<Integer> usedBitsForEncodingTheByte = new ArrayList<>();
        for (int i = 0; i < (tableInfoSize / 2); i++) {
            int bytesCountWithTheSameEncodingLength = readBuff.get() & 0xff; // byte to unsigned byte in the integer
            int encodingLenOfBytesCount = readBuff.get(); // byte to unsigned byte in the integer

//            System.out.println();
            for (int j = 0; j < bytesCountWithTheSameEncodingLength; j++) {
                usedBitsForEncodingTheByte.add(encodingLenOfBytesCount);
            }
        }
        int longestEncodingLength = usedBitsForEncodingTheByte.get(0);

        // recreate tree
        InternalTreeNode rootNode = new InternalTreeNode();
        for (int i = 0; i < usedBitsForEncodingTheByte.size(); i++) {
            int tableByte = readBuff.get();

            int encodingForTheByte;
            if (longestEncodingLength > 8) {
                encodingForTheByte = readBuff.getShort();
            } else {
                encodingForTheByte = readBuff.get();
            }

            int usedBitsForEncoding = usedBitsForEncodingTheByte.get(i);
            int firstBitInEncoding = encodingForTheByte >>> (usedBitsForEncoding - 1); //00001010 >>> 3 becomes 00000001
            firstBitInEncoding &= 1; // 00000001 & 11100000 becomes 00000000 11100000

            rootNode.recreateTreeLeaf(tableByte, encodingForTheByte, usedBitsForEncoding, firstBitInEncoding, rootNode);
        }

        // read the rest of the first buffer
        int remainedBytesInFirstRBuff = bytesInsideReadBuffer - readBuff.position();
        BTreeNode currentNode = rootNode;
        ArrayList<Byte> decompressedBytes = new ArrayList<>();
        int lastBitPosition = 0;
        int lastByteInCurrentRBuff = remainedBytesInFirstRBuff - 1;
        for (int i = 0; i < remainedBytesInFirstRBuff; i++) {

            int compressedByte = readBuff.get();

            // if this is the last byte in the last read buffer
            if (totalNumberOfRBuffers == RBufferSequentialNum && i == lastByteInCurrentRBuff) {
                lastBitPosition = redundantBitsInLastCompressedByte;
            }

            for (int j = 8 - 1; j >= lastBitPosition; j--) {
                currentNode = ((InternalTreeNode) currentNode).findEncodedByte(compressedByte, j);
                if (currentNode.isTreeLeaf()) {
                    decompressedBytes.add(
                            ((TreeLeaf) currentNode).getByteValue()
                    );
                    currentNode = rootNode;
                }
            }

        }
        readBuff.rewind(); // set the position inside the buff to the beginning
        RBufferSequentialNum++;

        ByteBuffer writeBuff = ByteBuffer.allocate(decompressedBytes.size());
        for (Byte decompressedByte : decompressedBytes) {
            writeBuff.put(decompressedByte);
        }
        writeBuff.rewind();

        // write to the file
        outputFChan.write(writeBuff);
        writeBuff.rewind();
        totallyDecodedBytes += decompressedBytes.size();

        // if compressed file is larger than 1 megabyte continue reading till end of it
        for (; RBufferSequentialNum <= totalNumberOfRBuffers; RBufferSequentialNum++) {
            bytesInsideReadBuffer = inputFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            decompressedBytes = new ArrayList<>();
            lastBitPosition = 0;
            lastByteInCurrentRBuff = bytesInsideReadBuffer - 1;
            for (int i = 0; i < bytesInsideReadBuffer; i++) {

                int compressedByte = readBuff.get();

                // if this is the last byte to decompress
                if (totalNumberOfRBuffers == RBufferSequentialNum && i == lastByteInCurrentRBuff) {
                    lastBitPosition = redundantBitsInLastCompressedByte;
                }

                for (int j = 8 - 1; j >= lastBitPosition; j--) {
                    currentNode = ((InternalTreeNode) currentNode).findEncodedByte(compressedByte, j);
                    if (currentNode.isTreeLeaf()) {
                        decompressedBytes.add(
                                ((TreeLeaf) currentNode).getByteValue()
                        );
                        currentNode = rootNode;
                    }
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning

            if (writeBuff.capacity() != decompressedBytes.size()) {
                writeBuff = ByteBuffer.allocate(decompressedBytes.size());
            }
            for (Byte decompressedByte : decompressedBytes) {
                writeBuff.put(decompressedByte);
            }
            writeBuff.rewind();

            // write to a file
            outputFChan.write(writeBuff);
            writeBuff.rewind();
            totallyDecodedBytes += decompressedBytes.size();
        }
        System.out.println(inputFChan.size() - inputFChan.position());
    }

}
