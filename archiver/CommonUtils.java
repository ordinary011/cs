package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.nio.ByteBuffer;

/**
 * The following class contains class fields and methods
 * that are used both in compression and decompression
 */
abstract class CommonUtils {

    protected final int BYTE_SIZE = 8; // bytes
    protected final int MAGABYTE = 1024 * 1024; // bytes

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
     * @param uniqueBytesSize amount of unique bytes in the file
     * @return amount of bits that are needed for encoding one byte
     */
    protected int findEncodingLen(int uniqueBytesSize) {
        int exponent = 0;
        int byteSize = 8;
        for (int i = 0; i <= byteSize; i++) {
            if (uniqueBytesSize <= Math.pow(2, exponent)) {
                return exponent;
            }

            exponent++;
        }

        return -1;
    }

}
