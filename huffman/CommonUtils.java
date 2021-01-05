package com.shpp.p2p.cs.ldebryniuk.assignment15;

import java.nio.ByteBuffer;

/**
 * The following class contains class fields
 * that are used both in compression and decompression
 */
abstract class CommonUtils {

    int MEGABYTE = 1024 * 1024; // bytes

    /**
     * contains chunk of data that is read from the read channel
     */
    ByteBuffer readBuff = ByteBuffer.allocate(MEGABYTE);

    /**
     * contains chunk of data that will be written to the output file
     */
    ByteBuffer writeBuff = ByteBuffer.allocate(1); // 1 byte is a default and later will be changed

}
