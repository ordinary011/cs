package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Decompressor {

    private final HashMap<String, Byte> tableRel = new HashMap<>();

    public void decompress(String compressedFile, String decompressedFile) {
        try {
            Path path = Paths.get(compressedFile);
            byte[] fileBytes = Files.readAllBytes(path);

            // get table size
            byte[] firstFourBytes = getBytesArr(fileBytes, 0, 4);
            int tableSize = ByteBuffer.wrap(fileBytes).getInt(); // in bytes

            int usedBitesForEncoding = (tableSize / 2) / 2; // todo mistake may be here

            byte[] compressedSize = getBytesArr(fileBytes, 4, 12);
            long compressedDataSize = ByteBuffer.wrap(compressedSize).getLong(); // in bits

            int tableOffset = 12; // there are 12 bytes of data before table
            int endOfTable = tableOffset + tableSize;
            for (int i = tableOffset; i < endOfTable; i+=2) {
                byte encodedByte = fileBytes[i + 1];
                String encodedByteStr =
                        String.format("%8s", Integer.toBinaryString(encodedByte & 0xFF)).replace(' ', '0');
                int diff = encodedByteStr.length() - usedBitesForEncoding;
                encodedByteStr = (diff == 0) ? encodedByteStr : encodedByteStr.substring(diff);
                tableRel.put(encodedByteStr, fileBytes[i]);
            }

            int bytesInDecompressedFile = (int) (compressedDataSize / 8);
            byte[] res = new byte[bytesInDecompressedFile];

            for (int i = endOfTable; i < fileBytes.length; i++) {

                res[i] = 33;
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
