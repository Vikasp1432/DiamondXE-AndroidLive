package com.diamondxe.Utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.Okio;
import okio.Source;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamRequestBody extends RequestBody {
    private final MediaType mediaType;
    private final InputStream inputStream;

    public InputStreamRequestBody(MediaType mediaType, InputStream inputStream) {
        this.mediaType = mediaType;
        this.inputStream = inputStream;
    }

    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public void writeTo(okio.BufferedSink sink) throws IOException {
        Source source = Okio.source(inputStream);
        sink.writeAll(source);
    }
}

