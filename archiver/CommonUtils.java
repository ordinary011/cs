package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.nio.ByteBuffer;

/**
 * The following class contains class fields and methods
 * that are used both in compression and decompression
 */
public class CommonUtils {

    protected final int byteSize = 8;
    ; // bytes
    protected final int megaByte = 1024 * 1024; // bytes

    protected final StringBuilder compressedDataStr = new StringBuilder();
    protected final ByteBuffer readBuff = ByteBuffer.allocate(megaByte);
    protected byte[] table;

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
