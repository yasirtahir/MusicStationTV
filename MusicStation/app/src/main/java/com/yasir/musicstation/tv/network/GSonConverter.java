package com.yasir.musicstation.tv.network;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;


public class GSonConverter implements Converter {
    private final Gson gson;
    private String encoding;
    /**
     * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    GSonConverter(Gson gson) {
        this(gson, "UTF-8");
    }
    /**
     * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use the specified encoding.
     */
    GSonConverter(Gson gson, String encoding) {
        this.gson = gson;
        this.encoding = encoding;
    }
    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        String charset = "UTF-8";
        if (body.mimeType() != null) {
            charset = retrofit.mime.MimeUtil.parseCharset(body.mimeType());
        }
        StringWriter isr = null;
        try {
            isr = new StringWriter();
            IOUtils.copy(body.in(), isr, charset);
            return gson.fromJson(isr.toString(), type);
        } catch (Throwable e) {
            throw new ConversionException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ignored) {
                    // No need to print this
                }
            }
        }
    }
    @Override
    public TypedOutput toBody(Object object) {
        try {
            return new JsonTypedOutput(gson.toJson(object).getBytes(encoding), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;
        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }
        @Override
        public String fileName() {
            return null;
        }
        @Override
        public String mimeType() {
            return mimeType;
        }
        @Override
        public long length() {
            return jsonBytes.length;
        }
        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }
}