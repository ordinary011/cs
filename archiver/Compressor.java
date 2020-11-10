package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;

public class Compressor extends Common {

    private final HashMap<Byte, String> relTable = new HashMap<>();
    private int bufferSequentialNum = 1; // first buffer
    private long uncompressedFileSize; // bytes
    private int bufferCount;

    public void compressFile(String originalFile, String compressedFile) {
        try (FileChannel inputFChan = (FileChannel) Files.newByteChannel(Paths.get(originalFile));
             FileChannel outputFChan = (FileChannel) Files.newByteChannel(Paths.get(compressedFile),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            long startTime = System.currentTimeMillis();
            uncompressedFileSize = inputFChan.size();

            bufferCount = (int) Math.ceil(uncompressedFileSize / (double) megaByte);
            HashSet<Byte> uniqueBytes = findUniqueBytes(inputFChan);

            createRelationTable(uniqueBytes);

            compressAndWrite(inputFChan, outputFChan);

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            long outFSize = outputFChan.size();
            long efficiency = uncompressedFileSize - outFSize;
            System.out.printf("efficiency of compression: %d (bytes)\n", efficiency);
            System.out.printf("time of compression: %d (milliseconds)\n", duration);
            System.out.printf("original file size: %d (bytes)\n", uncompressedFileSize);
            System.out.printf("compressed file size: %d (bytes)\n", outFSize);
        } catch (Exception e) {
            System.err.println("jo bro mistake down here");
            e.printStackTrace();
        }
    }

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

    private void createRelationTable(HashSet<Byte> uniqueBytes) {
        int usedBitsForEncoding = findEncodingLen(uniqueBytes.size());
        table = new byte[uniqueBytes.size() * 2];
        byte encodingForTheByte = 0;

        int tableIndex = 0;
        for (Byte uniqueByte : uniqueBytes) {
            table[tableIndex++] = uniqueByte;
            table[tableIndex++] = encodingForTheByte;

            String encodedByteStr = String.format("%8s", Integer.toBinaryString(encodingForTheByte & 0xFF))
                    .replace(' ', '0');
            encodedByteStr = encodedByteStr.substring(encodedByteStr.length() - usedBitsForEncoding);//"00000011"->"11"
            relTable.put(uniqueByte, encodedByteStr);

            encodingForTheByte++;
        }
    }

    private void compressAndWrite(FileChannel readFChan, FileChannel writeFChan) throws IOException {
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
        writeBuff.putInt(relTable.size() * 2); // whole table size in bytes
        writeBuff.putLong(uncompressedFileSize); // uncompressed data size in bytes
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

    private int createCompressedDataString(int bytesInsideReadBuffer) {
        readBuff.rewind(); // set the position inside the buff to the beginning
        for (int i = 0; i < bytesInsideReadBuffer; i++) {
            byte byteInsideBuff = readBuff.get();
            compressedDataStr.append(relTable.get(byteInsideBuff));
        }
        readBuff.rewind(); // set the position inside the buff to the beginning after last read

        // if this is not the last buffer, there will be remainder bits for the next buffer. So we don't Math.ceil
        return (bufferCount == bufferSequentialNum) ? // true if buff is the last
                        (int) Math.ceil(compressedDataStr.length() / byteSize) : compressedDataStr.length() / byteSize;
    }

    /**
     * Add compressed data to the write buff by slicing compressedDataStr into bytes
     */
    private void writeCompressedDataToAFile(FileChannel writeFChan, ByteBuffer writeBuff) throws IOException {
        String remainderForTheNextBuffer = "";
        String oneByte;
        for (int i = 0; i < compressedDataStr.length(); i += byteSize) {
            if ((i + byteSize) <= compressedDataStr.length()) { // if string has 8 chars (bits) inside
                oneByte = compressedDataStr.substring(i, i + byteSize);
                writeBuff.put((byte) Integer.parseInt(oneByte, 2));
            } else { // if last encodedByteString is less than a byte
                String lessThanByte = compressedDataStr.substring(i);
                if (bufferSequentialNum == bufferCount) { // if this is the last write buffer
                    int zeroBitsToEnd = byteSize - lessThanByte.length();
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
