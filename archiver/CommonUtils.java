package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.nio.ByteBuffer;

/**
 * The following class contains class fields and methods
 * that are used both in compression and decompression
 */
abstract class CommonUtils {

    private final int UNIQ_BYTES_MAXCOUNT = 256; // bytes

    protected final int MAGABYTE = 1024 * 1024; // bytes
    protected final int BYTE_SIZE = 8; // bytes

    /**
     * contains compressed data string
     */
    protected final StringBuilder compressedDataStr = new StringBuilder();

    /**
     * contains chunk of data that is read from the read channel
     */
    protected final ByteBuffer readBuff = ByteBuffer.allocate(MAGABYTE);

    /**
     *
     */
    protected byte[] table;

    /**
     * Determines encoding length for a byte
     *
     * @param uniqueBytesCount amount of unique bytes in the file
     * @return amount of bits that are needed for encoding one byte
     */
    protected int findEncodingLen(int uniqueBytesCount) {
        return uniqueBytesCount == UNIQ_BYTES_MAXCOUNT ? BYTE_SIZE : Integer.toBinaryString(uniqueBytesCount).length();
    }

}
