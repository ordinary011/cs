package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class Decompressor extends Common {

    private final HashMap<String, Byte> relTable = new HashMap<>();
    private ByteBuffer writeBuff;
    private long bytesLeftToDecompress;
    private int usedBitesForEncoding;

    public void decompressFile(String compressedFile, String decompressedFile) {
        try (FileChannel inputFChan = (FileChannel) Files.newByteChannel(Paths.get(compressedFile));
             FileChannel outputFChan = (FileChannel) Files.newByteChannel(Paths.get(decompressedFile),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            long startTime = System.currentTimeMillis();
            long compressedFileSize = inputFChan.size();

            // read first chunk of data
            int bytesInsideReadBuff = inputFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            // get table size, uncompressed data size, usedBitesForEncoding, and create relation table hashMap
            int tableSizeInBytes = readBuff.getInt();
            bytesLeftToDecompress = readBuff.getLong(); // size of uncompressed data
            usedBitesForEncoding = findEncodingLen(tableSizeInBytes / 2);
            createRelationTable(tableSizeInBytes);

            decompress(readBuff.remaining(), inputFChan, outputFChan);

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            long decompressedFSize = outputFChan.size();
            long efficiency = decompressedFSize - compressedFileSize;
            System.out.printf("efficiency of decompression: %d (bytes)\n", efficiency);
            System.out.printf("time of compression: %d (milliseconds)\n", duration);
            System.out.printf("compressed file size: %d (bytes)\n", compressedFileSize);
            System.out.printf("decompressed file size: %d (bytes)\n", decompressedFSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void decompress(int bytesInsideReadBuff, FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        int neededWBuffCapacity = recreateCompressedDataChunkStr(bytesInsideReadBuff);

        writeBuff = ByteBuffer.allocate(neededWBuffCapacity);

        fillWBuffAndWrite(outputFChan, neededWBuffCapacity);

        // continue reading till end of the compressed file
        int bytesInsideRBuffer;
        while ((bytesInsideRBuffer = inputFChan.read(readBuff)) != -1) { // read from a file to a buffer; -1 means end of file
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
