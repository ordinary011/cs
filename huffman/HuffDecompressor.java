package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.InternalTreeNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class HuffDecompressor {

    private final ByteBuffer readBuff = ByteBuffer.allocate(1024 * 1024);

    /**
     * Starting method for decompressing the data
     *
     * @throws IOException
     */
    void decompressFile(FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        // read first chunk of data to the buff
        int bytesInsideReadBuffer = inputFChan.read(readBuff);
        readBuff.rewind(); // set the position inside the buff to the beginning

        int redundantBitsInLastCompressedByte = readBuff.get();
        int secondByte = readBuff.get();
        int tableInfoSize = secondByte >>> 4;
        int theLongestEncodingLength = secondByte & 0x0F;

        bytesInsideReadBuffer -= 2;

        ArrayList<Integer> usedBitsForEncodingTheByte = new ArrayList<>();
        for (int i = 0; i < tableInfoSize; i++) {
            int bytesCountWithTheSameEncodingLength = readBuff.get();
            bytesInsideReadBuffer--;

            for (int j = 0; j < bytesCountWithTheSameEncodingLength; j++) {
                usedBitsForEncodingTheByte.add(theLongestEncodingLength);
            }

            theLongestEncodingLength--;
        }

        InternalTreeNode rootNode = new InternalTreeNode();

        for (int i = 0; i < usedBitsForEncodingTheByte.size(); i++) {
            int tableByte = readBuff.get();
            int encodingForTheByte = readBuff.get();
            int usedBitsForEncoding = usedBitsForEncodingTheByte.get(i);
            int firstBitInEncoding = encodingForTheByte >>> (usedBitsForEncoding - 1); // 00001010 >>> 00000001

            rootNode.recreateTreeLeaf(tableByte, encodingForTheByte, usedBitsForEncoding, firstBitInEncoding, rootNode);
        }

        System.out.println(readBuff.get());
    }

}
