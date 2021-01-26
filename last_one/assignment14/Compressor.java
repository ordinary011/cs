package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The following class comprises all the logic for file compression
 * The entry method is compressFile(). It reads the whole file by chunks two times.
 * In the first iteration of reading it will search for unique bytes in the file.
 * In the second iteration of reading it reads chunk of data, creates compressed data representation for that chunk
 * and writes it to the output (compressed) file
 */
class Compressor extends CommonUtils {

    /**
     * byte and a sequence of bits (as string) that are used for encoding the byte
     */
    private final HashMap<Byte, String> byteToEncodedByte = new HashMap<>();

    private final int USED_BYTES_FOR_SAVING_UNCOMPRESSED_DATA = 8;
    private final int USED_BYTES_FOR_SAVING_TABLE_SIZE = 4;
    private int bufferSequentialNum = 1; // first buffer
    private int totalBufferCount;

    /**
     * Entry method for compressing the data. It contains all the steps of the compression
     *
     * @param inputFChan    channel between current process and the file that needs to compressed
     * @param outputFChan   channel between current process and the file that will contain compressed data
     * @param inputFileSize size of the file that is going to be compressed (in bytes)
     * @throws IOException when reading/writing to/from the file some mistakes may occur
     */
    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        totalBufferCount = (int) Math.ceil(inputFileSize / (double) MAGABYTE);

        HashSet<Byte> uniqueBytes = findUniqueBytes(inputFChan);

        createRelationTable(uniqueBytes);

        compressAndWrite(inputFChan, outputFChan, inputFileSize);
    }

    /**
     * Searches for the unique bytes in the file that needs to be compressed
     *
     * @param inputFChan channel between current process and the file that needs to compressed
     * @return uniqueBytes HashSet of unique bytes within the original file
     * @throws IOException when reading from a file some mistakes may occur
     */
    private HashSet<Byte> findUniqueBytes(FileChannel inputFChan) throws IOException {
        HashSet<Byte> uniqueBytes = new HashSet<>();

        // read the whole input file and find unique bytes
        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                uniqueBytes.add(readBuff.get());
            }
            readBuff.rewind(); // set the position inside the buff to the beginning
        }

        return uniqueBytes;
    }

    /**
     * Creates relation table of byte to its encoding
     *
     * @param uniqueBytes HashSet of unique bytes within the original file
     */
    private void createRelationTable(HashSet<Byte> uniqueBytes) {
        int usedBitsForEncoding = findEncodingLen(uniqueBytes.size());
        table = new byte[uniqueBytes.size() * 2];
        byte encodingForTheByte = 0;

        int tableIndex = 0;
        int indexForEncodedStr = Byte.SIZE - usedBitsForEncoding;
        for (Byte uniqueByte : uniqueBytes) {
            table[tableIndex++] = uniqueByte;
            table[tableIndex++] = encodingForTheByte;

            String encodedByteString = String.format("%8s", Integer.toBinaryString(encodingForTheByte))
                    .replace(' ', '0');
            encodedByteString = encodedByteString.substring(indexForEncodedStr); // "00000011" -> "11"
            byteToEncodedByte.put(uniqueByte, encodedByteString);

            encodingForTheByte++;
        }
    }

    /**
     * Compresses data by chunks and writes it directly to the compreseed file
     *
     * @param readFChan  channel between current process and the file that needs to compressed
     * @param writeFChan channel between current process and the file that will contain compressed data
     * @param inputFSize size of the file that is going to be compressed (in bytes)
     * @throws IOException when reading/writing to/from a file some mistakes may occur
     */
    private void compressAndWrite(FileChannel readFChan, FileChannel writeFChan, long inputFSize) throws IOException {
        readFChan.position(0); // we will read file from the beginning again
        int offsetBeforeCompressedData = USED_BYTES_FOR_SAVING_TABLE_SIZE +
                USED_BYTES_FOR_SAVING_UNCOMPRESSED_DATA + table.length; // in bytes

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

        // if file is more than a readBuffer capacity read and write by pieces the rest of the file
        while ((bytesInsideReadBuffer = readFChan.read(readBuff)) != -1) { // read file; -1 means end of file
            sizeOfCompressedDataChunk = createCompressedDataString(bytesInsideReadBuffer);

            if (sizeOfCompressedDataChunk != writeBuff.capacity()) {
                writeBuff = ByteBuffer.allocate(sizeOfCompressedDataChunk);
            }

            writeCompressedDataToAFile(writeFChan, writeBuff);
        }
    }

    /**
     * creates a string that represents a compressed data chunk
     *
     * @param bytesInsideReadBuffer amount of bytes inside read buffer
     * @return size Of Compressed Data Chunk (in bytes)
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
                (int) Math.ceil(compressedDataStr.length() / (double) Byte.SIZE) :
                compressedDataStr.length() / Byte.SIZE;
    }

    /**
     * Adds compressed data to the write buff by slicing compressedDataString into bytes
     *
     * @param writeFChan channel between current process and the file that will contain compressed data
     * @param writeBuff  buffer that is used for writing the data to the ouput (compressed) file
     * @throws IOException when writing to the file some mistakes may occur
     */
    private void writeCompressedDataToAFile(FileChannel writeFChan, ByteBuffer writeBuff) throws IOException {
        String remainderForTheNextBuffer = "";
        String oneByte;
        for (int i = 0; i < compressedDataStr.length(); i += Byte.SIZE) {
            if ((i + Byte.SIZE) <= compressedDataStr.length()) { // if string has 8 chars (bits) inside
                oneByte = compressedDataStr.substring(i, i + Byte.SIZE);
                writeBuff.put((byte) Integer.parseInt(oneByte, 2));
            } else { // if string is less than a byte
                String lessThanByte = compressedDataStr.substring(i);
                if (bufferSequentialNum == totalBufferCount) { // if this is the last write buffer
                    int zeroBitsTillEnd = Byte.SIZE - lessThanByte.length();
                    oneByte = lessThanByte + createZeroBitsStr(zeroBitsTillEnd);
                    writeBuff.put((byte) Integer.parseInt(oneByte, 2));
                } else { // leave remainder for the next buffer
                    remainderForTheNextBuffer = lessThanByte;
                }
            }
        }
        writeBuff.rewind();
        compressedDataStr.setLength(0);
        if (remainderForTheNextBuffer.length() > 0) compressedDataStr.append(remainderForTheNextBuffer);

        // write to the file
        writeFChan.write(writeBuff);
        writeBuff.rewind();

        bufferSequentialNum++;
    }

    /**
     * creates a string of zeroes that later are used to fill out the space in the last compressed byte
     *
     * @param zeroBitsTillEnd number of zero bits that are to be creates (as a string)
     * @return string of zeroes
     */
    private String createZeroBitsStr(int zeroBitsTillEnd) {
        StringBuilder zeroBitsStr = new StringBuilder();
        for (int i = 0; i < zeroBitsTillEnd; i++) {
            zeroBitsStr.append("0");
        }

        return zeroBitsStr.toString();
    }

}
