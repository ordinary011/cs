package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Decompressor extends Archiver {

    private final HashMap<String, Byte> relTable = new HashMap<>();

    public void decompress(String compressedFile, String decompressedFile) {
        try {
            Path path = Paths.get(compressedFile);
            byte[] compressedFileBytes = Files.readAllBytes(path);

            // get table size
            byte[] firstFourBytes = getBytesArr(compressedFileBytes, 0, 4);
            int tableSizeInBytes = ByteBuffer.wrap(compressedFileBytes).getInt();

//            int usedBitesForEncoding = (tableSizeInBytes / 2) / 2; // todo mistake may be here
            int usedBitesForEncoding = findEncodingLen(tableSizeInBytes / 2);

            byte[] compressedSize = getBytesArr(compressedFileBytes, 4, 12);
            int uncompressedDataSizeInBytes = (int) ByteBuffer.wrap(compressedSize).getLong();

            // create relation table
            int tableOffset = 12; // there are 12 bytes of data before table ()
            int endOfTable = tableOffset + tableSizeInBytes;
            for (int i = tableOffset; i < endOfTable; i += 2) {
                byte encodedByte = compressedFileBytes[i + 1];
                String encodedByteStr =
                        String.format("%8s", Integer.toBinaryString(encodedByte & 0xFF)).replace(' ', '0');
                int diff = encodedByteStr.length() - usedBitesForEncoding;
                encodedByteStr = (diff == 0) ?
                        encodedByteStr : encodedByteStr.substring(diff); // needs explanations
                relTable.put(encodedByteStr, compressedFileBytes[i]);
            }

            // create compressed string
            StringBuilder compressed = new StringBuilder();
            for (int i = endOfTable; i < compressedFileBytes.length; i++) {
                String encodedByte =
                        String.format("%8s", Integer.toBinaryString(compressedFileBytes[i] & 0xFF)).replace(' ', '0');
                compressed.append(encodedByte);
            }


            int j = 0;
            byte[] res = new byte[uncompressedDataSizeInBytes]; // todo can't create arr for more than 2,3 gigabytes
            for (int i = 0; i < uncompressedDataSizeInBytes; i+=usedBitesForEncoding) {
                String encoded = compressed.substring(i, i + usedBitesForEncoding);
                if (relTable.containsKey(encoded)) {
                    byte decompressedByte = relTable.get(encoded);
                    res[j] = decompressedByte;
                    j++;
                }
            }

            path = Paths.get(decompressedFile);
            Files.write(path, res);
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
