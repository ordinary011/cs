package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

public class Decompressor extends CommonUtils {

    private final HashMap<String, Byte> relTable = new HashMap<>();
    private ByteBuffer writeBuff;
    private long bytesLeftToDecompress;
    private int usedBitesForEncoding;

    public void decompressFile(FileChannel inputFChan, FileChannel outputFChan) throws IOException {
            // read first chunk of data to the buff
            inputFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            // get table size, uncompressed data size, usedBitesForEncoding, and create relation table hashMap
            int tableSizeInBytes = readBuff.getInt();
            bytesLeftToDecompress = readBuff.getLong(); // size of uncompressed data
            usedBitesForEncoding = findEncodingLen(tableSizeInBytes / 2);
            createRelationTable(tableSizeInBytes);

            decompress(readBuff.remaining(), inputFChan, outputFChan);
    }

    private void createRelationTable(int tableSizeInBytes) {
        table = new byte[tableSizeInBytes];
        readBuff.get(table, 0, tableSizeInBytes); // get table from a read buff

        // create relation table todo later A
        int offsetBeforeEncodedByte = byteSize - usedBitesForEncoding;
        for (int i = 0; i < table.length; i += 2) {
            byte encodingForTheByte = table[i + 1];
            String encodedByteStr = String.format("%8s", Integer.toBinaryString(encodingForTheByte & 0xFF))
                    .replace(' ', '0');

            encodedByteStr = encodedByteStr.substring(offsetBeforeEncodedByte);
            relTable.put(encodedByteStr, table[i]);
        }
    }

    private void decompress(int bytesInsideRBuff, FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        int neededWBuffCapacity = recreateCompressedDataChunkStr(bytesInsideRBuff);

        writeBuff = ByteBuffer.allocate(neededWBuffCapacity);

        fillWBuffAndWrite(outputFChan, neededWBuffCapacity);

        // continue reading till end of the compressed file
        int bytesInsideRBuffer;
        while ((bytesInsideRBuffer = inputFChan.read(readBuff)) != -1) { // read file; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning

            neededWBuffCapacity = recreateCompressedDataChunkStr(bytesInsideRBuffer);

            if (writeBuff.capacity() != neededWBuffCapacity) {
                writeBuff = ByteBuffer.allocate(neededWBuffCapacity);
            }

            fillWBuffAndWrite(outputFChan, neededWBuffCapacity);
        }
    }

    /**
     * Recreates compressedDataChunkString from bytes of compressed data
     */
    private int recreateCompressedDataChunkStr(int bytesInsideReadBuff) {
        // append compressed bytes as string to compressedDataStr
        for (int i = 0; i < bytesInsideReadBuff; i++) {
            String encodedByte = String.format("%8s", Integer.toBinaryString(readBuff.get() & 0xFF))
                    .replace(' ', '0');
            compressedDataStr.append(encodedByte);
        }
        readBuff.rewind(); // set the position inside the buff to the beginning

        int WBuffCapacity = compressedDataStr.length() / usedBitesForEncoding;
        if (WBuffCapacity > bytesLeftToDecompress) { // if there are redundant zeroes at the end of compressedDataStr
            WBuffCapacity = (int) bytesLeftToDecompress;
        }

        return WBuffCapacity;
    }

    /**
     * fill write buffer and write to a file
     */
    private void fillWBuffAndWrite(FileChannel outputFChan, int WBuffCapacity) throws IOException {
        int j = 0;
        for (int i = 0; i < WBuffCapacity; i++) {
            String encodedByte = compressedDataStr.substring(j, j + usedBitesForEncoding);
            j += usedBitesForEncoding;

            byte decompressedByte = relTable.get(encodedByte);
            writeBuff.put(decompressedByte);
        }
        writeBuff.rewind();

        String remainderForNextBuffer = compressedDataStr.substring(j);
        compressedDataStr.setLength(0);
        if (remainderForNextBuffer.length() > 0) compressedDataStr.append(remainderForNextBuffer);
        bytesLeftToDecompress -= WBuffCapacity;

        // write to a file
        outputFChan.write(writeBuff);
        writeBuff.rewind();
    }
}
