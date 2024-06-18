package com.brestats.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

/** 
 * This class is used to convert a list of pixels into a png file without swing classes
 */
public class PngCreator {
    public static byte[] createPng(int width, int height, int[] pixels) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeSignature(baos);
        writeIHDRChunk(baos, width, height);
        writeIDATChunk(baos, width, height, pixels);
        writeIENDChunk(baos);
        return baos.toByteArray();
    }

    private static void writeSignature(ByteArrayOutputStream baos) {
        byte[] pngSignature = new byte[] {
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
        };
        baos.write(pngSignature, 0, pngSignature.length);
    }

    private static void writeIHDRChunk(ByteArrayOutputStream baos, int width, int height) throws IOException {
        ByteArrayOutputStream chunkData = new ByteArrayOutputStream();
        writeInt(chunkData, width);
        writeInt(chunkData, height);
        chunkData.write(8);  // Bit depth
        chunkData.write(2);  // Color type (Truecolor)
        chunkData.write(0);  // Compression method
        chunkData.write(0);  // Filter method
        chunkData.write(0);  // Interlace method
        writeChunk(baos, "IHDR", chunkData.toByteArray());
    }

    private static void writeIDATChunk(ByteArrayOutputStream baos, int width, int height, int[] pixels) throws IOException {
        ByteArrayOutputStream rawData = new ByteArrayOutputStream();
        for (int y = 0; y < height; y++) {
            rawData.write(0);  // No filter
            for (int x = 0; x < width; x++) {
                int index = (y * width + x) * 3;
                rawData.write(pixels[index]);       // R
                rawData.write(pixels[index + 1]);   // G
                rawData.write(pixels[index + 2]);   // B
            }
        }
        byte[] compressedData = compress(rawData.toByteArray());
        writeChunk(baos, "IDAT", compressedData);
    }

    private static void writeIENDChunk(ByteArrayOutputStream baos) throws IOException {
        writeChunk(baos, "IEND", new byte[0]);
    }

    private static void writeChunk(ByteArrayOutputStream baos, String chunkType, byte[] data) throws IOException {
        writeInt(baos, data.length);
        baos.write(chunkType.getBytes("UTF-8"));
        baos.write(data);
        CRC32 crc = new CRC32();
        crc.update(chunkType.getBytes("UTF-8"));
        crc.update(data);
        writeInt(baos, (int) crc.getValue());
    }

    private static void writeInt(ByteArrayOutputStream baos, int value) {
        baos.write((value >> 24) & 0xFF);
        baos.write((value >> 16) & 0xFF);
        baos.write((value >> 8) & 0xFF);
        baos.write(value & 0xFF);
    }

    private static byte[] compress(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            baos.write(buffer, 0, count);
        }
        return baos.toByteArray();
    }
}
