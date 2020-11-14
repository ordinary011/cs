package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The following class comprises all the logic for file compression
 */
class Compressor extends CommonUtils {

    /**
     * byte and a sequence of bits (as string) that are used for encoding the byte
     */
    private final HashMap<Byte, String> byteToEncodedByte = new HashMap<>();

    private final int usedBytesForSavingUncompressedData = 8;
    private final int usedBytesForSavingTableSize = 4;
    private int bufferSequentialNum = 1; // first buffer
    private int totalBufferCount;

    /**
     * Starting method for compressing the data
     *
     * @throws IOException
     */
    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        totalBufferCount = (int) Math.ceil(inputFileSize / (double) MAGABYTE);
        HashSet<Byte> uniqueBytes = findUniqueBytes(inputFChan);

        createRelationTable(uniqueBytes);

        compressAndWrite(inputFChan, outputFChan, inputFileSize);
    }

    /**
     * Searches for the unique bytes in the file
     *
     * @return uniqueBytes
     * @throws IOException
     */
    private HashSet<Byte> findUniqueBytes(FileChannel inputFChan) throws IOException {
        HashSet<Byte> uniqueBytes = new HashSet<>();

        // read the whole input file and find unique bytes
        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // read file; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                uniqueBytes.add(readBuff.get());
            }
            readBuff.rewind(); // set the position inside the buff to the beginning
        }

        return uniqueBytes;
    }

    /**
     * Creates relation table
     */
    private void createRelationTable(HashSet<Byte> uniqueBytes) {
        int usedBitsForEncoding = findEncodingLen(uniqueBytes.size());
        table = new byte[uniqueBytes.size() * 2];
        byte encodingForTheByte = 0;

        int tableIndex = 0;
        int indexForEncodedStr = BYTE_SIZE - usedBitsForEncoding;
        for (Byte uniqueByte : uniqueBytes) {
            table[tableIndex++] = uniqueByte;
            table[tableIndex++] = encodingForTheByte;

            String encodedByteStr = String.format("%8s", Integer.toBinaryString(encodingForTheByte & 0xFF))
                    .replace(' ', '0');
            encodedByteStr = encodedByteStr.substring(indexForEncodedStr); // "00000011" -> "11"
            byteToEncodedByte.put(uniqueByte, encodedByteStr);

            encodingForTheByte++;
        }
    }

    /**
     * compresses data and writes it directly to the file
     *
     * @throws IOException
     */
    private void compressAndWrite(FileChannel readFChan, FileChannel writeFChan, long inputFSize) throws IOException {
        readFChan.position(0); // we will read file from the beginning again
        int offsetBeforeCompressedData = usedBytesForSavingTableSize +
                usedBytesForSavingUncompressedData + table.length; // in bytes

        // read first data chunk and compress it
        int bytesInsideReadBuffer = readFChan.read(readBuff);
        int sizeOfCompressedDataChunk = createCompressedDataString(bytesInsideReadBuffer);

        // create first write buffer
        int writeBufferCapacity = offsetBeforeCompressedData + sizeOfCompressedDataChunk;
        ByteBuffer writeBuff = ByteBuffer.allocate(writeBufferCapacity);

        // fill the write buffer
        writeBuff.putInt(byteToEncodedByte.size() * 2); // whole table size in bytes
        writeBuff.putLong(inputFSize); // uncompressed data size in bytes
        writeBuff.put(table); // table of relations
        writeCompressedDataToAFile(writeFChan, writeBuff);

        // if file is more than a megabyte read and write by pieces the rest of the file
        while ((bytesInsideReadBuffer = readFChan.read(readBuff)) != -1) { // read file; -1 means end of file
            sizeOfCompressedDataChunk = createCompressedDataString(bytesInsideReadBuffer);

            if (sizeOfCompressedDataChunk != writeBuff.capacity()) {
                writeBuff = ByteBuffer.allocate(sizeOfCompressedDataChunk);
            }

            writeCompressedDataToAFile(writeFChan, writeBuff);
        }
    }

    /**
     * creates a string that represemts a compressed data chunk
     *
     * @return sizeOfCompressedDataChunk
     */
    private int createCompressedDataString(int bytesInsideReadBuffer) {
        readBuff.rewind(); // set the position inside the buff to the beginning
        for (int i = 0; i < bytesInsideReadBuffer; i++) {
            byte byteInsideBuff = readBuff.get();
            compressedDataStr.append(byteToEncodedByte.get(byteInsideBuff));
        }
        readBuff.rewind(); // set the position inside the buff to the beginning after last read

        // if this is not the last buffer, there might be remained bits for the next buffer. So we don't Math.ceil
        return (totalBufferCount == bufferSequentialNum) ? // true if buff is the last one
                (int) Math.ceil(compressedDataStr.length() / (double) BYTE_SIZE) :
                compressedDataStr.length() / BYTE_SIZE;
    }

    /**
     * Add compressed data to the write buff by slicing compressedDataStr into bytes
     */
    private void writeCompressedDataToAFile(FileChannel writeFChan, ByteBuffer writeBuff) throws IOException {
        String remainderForTheNextBuffer = "";
        String oneByte;
        for (int i = 0; i < compressedDataStr.length(); i += BYTE_SIZE) {
            if ((i + BYTE_SIZE) <= compressedDataStr.length()) { // if string has 8 chars (bits) inside
                oneByte = compressedDataStr.substring(i, i + BYTE_SIZE);
                writeBuff.put((byte) Integer.parseInt(oneByte, 2));
            } else { // if string is less than a byte
                String lessThanByte = compressedDataStr.substring(i);
                if (bufferSequentialNum == totalBufferCount) { // if this is the last write buffer
                    int zeroBitsToEnd = BYTE_SIZE - lessThanByte.length();
                    oneByte = lessThanByte + "0".repeat(zeroBitsToEnd);
                    writeBuff.put((byte) Integer.parseInt(oneByte, 2));
                } else { // leave remainder for the next buffer
                    remainderForTheNextBuffer = lessThanByte;
                }
            }
        }
        writeBuff.rewind();
        compressedDataStr.setLength(0);
        if (remainderForTheNextBuffer.length() > 0) compressedDataStr.append(remainderForTheNextBuffer);

        // write to a file
        writeFChan.write(writeBuff);
        writeBuff.rewind();

        bufferSequentialNum++;
    }

}
