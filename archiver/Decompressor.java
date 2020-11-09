package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class Decompressor extends Archiver {

    private final HashMap<String, Byte> relTable = new HashMap<>();
    private ByteBuffer readBuff = ByteBuffer.allocate(megaByte);
    StringBuilder compressedDataStr = new StringBuilder();

    public void decompress(String compressedFile, String decompressedFile) {
        try (FileChannel readFChan = (FileChannel) Files.newByteChannel(Paths.get(compressedFile));
             FileChannel writeFChan = (FileChannel) Files.newByteChannel(Paths.get(decompressedFile),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            int bytesInsideReadBuff = readFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            int tableSizeInBytes = readBuff.getInt();
            long bytesLeftToDecompress = readBuff.getLong();
            int usedBitesForEncoding = findEncodingLen(tableSizeInBytes / 2);

            byte[] table = new byte[tableSizeInBytes];
            readBuff.get(table, 0, tableSizeInBytes);

            // create relation table
            for (int i = 0; i < table.length; i += 2) {
                byte encodedByte = table[i + 1];
                String encodedByteStr =
                        String.format("%8s", Integer.toBinaryString(encodedByte & 0xFF)).replace(' ', '0');

                int diff = encodedByteStr.length() - usedBitesForEncoding;
                encodedByteStr = (diff == 0) ?
                        encodedByteStr : encodedByteStr.substring(diff); // needs explanations
                relTable.put(encodedByteStr, table[i]);
            }

            // recreate string of compressedData from compressed data
            int offsetBeforeCompressedData = table.length + bytesForSavingTableSize + bytesForSavingUncompressedData;
            for (int i = offsetBeforeCompressedData; i < bytesInsideReadBuff; i++) {
                String encodedByte =
                        String.format("%8s", Integer.toBinaryString(readBuff.get() & 0xFF)).replace(' ', '0');
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
            writeFChan.write(writeBuff);
            writeBuff.rewind();

            // continue reading till end of the compressed file
            int bytesInsideRBuffer;
            while ((bytesInsideRBuffer = readFChan.read(readBuff)) != -1) { // read from a file to a buffer; -1 means end of file
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
                writeFChan.write(writeBuff);
                writeBuff.rewind();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytesArr(byte[] fileBytes, int start, int end) {
        byte[] res = new byte[end - start];

        int j = 0;
        for (int i = start; i < end; i++, j++) {
            res[j] = fileBytes[i];
        }

        return res;
    }
}
