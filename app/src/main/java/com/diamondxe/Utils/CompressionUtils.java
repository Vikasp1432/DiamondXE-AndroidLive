package com.diamondxe.Utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class CompressionUtils {

    // Method to compress data using GZIP and then encode it to Base64
    public static String compressAndEncodeBase64(byte[] data) {
        try {
            // Create a GZIPOutputStream to compress the data
            ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.close();

            // Convert compressed bytes to Base64 encoded string
            byte[] compressedBytes = bos.toByteArray();
            bos.close();

            // Encode to Base64
            return Base64.encodeToString(compressedBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
