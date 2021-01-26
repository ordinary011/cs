package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.nio.ByteBuffer;

/**
 * The following class contains class fields and methods
 * that are used both in compression and decompression
 */
abstract class CommonUtils {

    private final int UNIQUE_BYTES_MAXCOUNT = 256; // bytes

    final int MAGABYTE = 1024 * 1024; // bytes

    /**
     * contains compressed data string
     */
    final StringBuilder compressedDataStr = new StringBuilder();

    /**
     * contains chunk of data that is read from the read channel
     */
    final ByteBuffer readBuff = ByteBuffer.allocate(MAGABYTE);

    /**
     * contains "byte : encodingForTheByte" pair
     */
    byte[] table;

    /**
     * Determines encoding length for a byte
     *
     * @param uniqueBytesCount amount of unique bytes in the file
     * @return amount of bits that are needed for encoding one byte
     */
    int findEncodingLen(int uniqueBytesCount) {
        return uniqueBytesCount == UNIQUE_BYTES_MAXCOUNT ? Byte.SIZE : Integer.toBinaryString(uniqueBytesCount).length();
    }

}
