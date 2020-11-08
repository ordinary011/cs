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
    private final int kiloByte = 1024; // bytes
    private ByteBuffer readBuff = ByteBuffer.allocate(kiloByte);

    public void decompress(String compressedFile, String decompressedFile) {
        try (FileChannel readFChan = (FileChannel) Files.newByteChannel(Paths.get(compressedFile));
             FileChannel writeFChan = (FileChannel) Files.newByteChannel(Paths.get(decompressedFile),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            int bytesInsideReadBuff = readFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            int tableSizeInBytes = readBuff.getInt();
            long uncompressedDataSizeInBytes = readBuff.getLong();
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
            StringBuilder compressedDataStr = new StringBuilder();

            for (int i = offsetBeforeCompressedData; i < bytesInsideReadBuff; i++) {
                String encodedByte =
                        String.format("%8s", Integer.toBinaryString(readBuff.get() & 0xFF)).replace(' ', '0');
                compressedDataStr.append(encodedByte);
            }

            int bytesToBeWritten = (uncompressedDataSizeInBytes > kiloByte) ?
                    (kiloByte - offsetBeforeCompressedData) : (int) uncompressedDataSizeInBytes;
            // todo if more than a kilobyte check
            ByteBuffer writeBuff = ByteBuffer.allocate(bytesToBeWritten);

            int j = 0;
            for (int i = 0; i < bytesToBeWritten; i++) {
                String encodedByte = compressedDataStr.substring(j, j + usedBitesForEncoding);
                j += usedBitesForEncoding;

                if (relTable.containsKey(encodedByte)) {
                    byte decompressedByte = relTable.get(encodedByte);
                    writeBuff.put(decompressedByte);
                }
            }

            // write to a file
            writeBuff.rewind();
            writeFChan.write(writeBuff);


            // continue reading till end of the file
            int bytesInsideRBuffer;
            while ((bytesInsideRBuffer = readFChan.read(readBuff)) != -1) { // read from a file to a buffer; -1 means end of file
                compressedDataStr.setLength(0);
                if (bytesInsideRBuffer != writeBuff.capacity()) {
                    writeBuff = ByteBuffer.allocate(bytesInsideRBuffer);
                }
                writeBuff.rewind();

                for (int i = 0; i < bytesInsideRBuffer; i++) {
                    String encodedByte =
                            String.format("%8s", Integer.toBinaryString(readBuff.get() & 0xFF)).replace(' ', '0');
                    compressedDataStr.append(encodedByte);
                }

                int j1 = 0;
                for (int i = 0; i < bytesToBeWritten; i++) {
                    String encodedByte = compressedDataStr.substring(j1, j1 + usedBitesForEncoding);
                    j1 += usedBitesForEncoding;

                    if (relTable.containsKey(encodedByte)) {
                        byte decompressedByte = relTable.get(encodedByte);
                        writeBuff.put(decompressedByte);
                    }
                }

                // write to a file
                writeBuff.rewind();
                writeFChan.write(writeBuff);
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
