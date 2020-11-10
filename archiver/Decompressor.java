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

            decompress(bytesInsideReadBuff, inputFChan, outputFChan);

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

    private void decompress(int bytesInsideReadBuff, FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        // recreate compressedDataChunk string from bytes of compressed data
        int offsetBeforeCompressedData = table.length +
                usedBytesForSavingTableSize + usedBytesForSavingUncompressedData;
        for (int i = offsetBeforeCompressedData; i < bytesInsideReadBuff; i++) {
            String encodedByte =
                    String.format("%8s", Integer.toBinaryString(readBuff.get() & 0xFF))
                            .replace(' ', '0');
            compressedDataStr.append(encodedByte);
        }
        readBuff.rewind();

        int neededWBuffCapacity = compressedDataStr.length() / usedBitesForEncoding;
        neededWBuffCapacity =
                (neededWBuffCapacity > bytesLeftToDecompress) ? // true if there are redundant zeroes at the end
                        (int) bytesLeftToDecompress : neededWBuffCapacity;
        ByteBuffer writeBuff = ByteBuffer.allocate(neededWBuffCapacity);

        // fill the write buffer
        int j = 0;
        for (int i = 0; i < neededWBuffCapacity; i++) {
            String encodedByte = compressedDataStr.substring(j, j + usedBitesForEncoding);
            j += usedBitesForEncoding;

            byte decompressedByte = relTable.get(encodedByte);
            writeBuff.put(decompressedByte);
        }
        writeBuff.rewind();
        String remainder = compressedDataStr.substring(j);
        compressedDataStr.setLength(0);
        if (remainder.length() > 0) compressedDataStr.append(remainder);
        bytesLeftToDecompress -= neededWBuffCapacity;

        // write to a file
        outputFChan.write(writeBuff);
        writeBuff.rewind();

        // continue reading till end of the compressed file
        int bytesInsideRBuffer;
        while ((bytesInsideRBuffer = inputFChan.read(readBuff)) != -1) { // read from a file to a buffer; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning
            // recreate string of compressedData from compressed data
            for (int i = 0; i < bytesInsideRBuffer; i++) {
                String encodedByte =
                        String.format("%8s", Integer.toBinaryString(readBuff.get() & 0xFF)).replace(' ', '0');
                compressedDataStr.append(encodedByte);
            }
            readBuff.rewind(); // set the position inside the buff to the beginning

            neededWBuffCapacity = compressedDataStr.length() / usedBitesForEncoding;
            neededWBuffCapacity = (neededWBuffCapacity > bytesLeftToDecompress) ?
                    (int) bytesLeftToDecompress : neededWBuffCapacity; // if there are zeroes at the end
            if (writeBuff.capacity() != neededWBuffCapacity) {
                writeBuff = ByteBuffer.allocate(neededWBuffCapacity);
            }

            int j1 = 0;
            for (int i = 0; i < neededWBuffCapacity; i++) {
                String encodedByte = compressedDataStr.substring(j1, j1 + usedBitesForEncoding);
                j1 += usedBitesForEncoding;

                byte decompressedByte = relTable.get(encodedByte);
                writeBuff.put(decompressedByte);
            }
            writeBuff.rewind();
            remainder = compressedDataStr.substring(j1);
            compressedDataStr.setLength(0);
            if (remainder.length() > 0) compressedDataStr.append(remainder);
            bytesLeftToDecompress -= neededWBuffCapacity;

            // write to a file
            outputFChan.write(writeBuff);
            writeBuff.rewind();
        }
    }

    private void createRelationTable(int tableSizeInBytes) {
        table = new byte[tableSizeInBytes];
        readBuff.get(table, 0, tableSizeInBytes); // get table from a read buff

        // create relation table todo efficiency
        for (int i = 0; i < table.length; i += 2) {
            byte encodingForTheByte = table[i + 1];
            String encodedByteStr = String.format("%8s", Integer.toBinaryString(encodingForTheByte & 0xFF))
                    .replace(' ', '0');

            int offsetBeforeEncoding = byteSize - usedBitesForEncoding;
            encodedByteStr = encodedByteStr.substring(offsetBeforeEncoding);
            relTable.put(encodedByteStr, table[i]);
        }
    }
}
