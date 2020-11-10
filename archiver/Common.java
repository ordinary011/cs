package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.nio.ByteBuffer;

public class Common {

    protected final int byteSize = 8;; // bytes
    protected final int megaByte = 1024 * 1024; // bytes
    protected final int usedBytesForSavingTableSize = 4;
    protected final int usedBytesForSavingUncompressedData = 8;

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
