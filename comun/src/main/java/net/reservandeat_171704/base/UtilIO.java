package net.reservandeat_171704.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java8.util.Optional;
import java8.util.function.Function;

import static net.reservandeat_171704.base.UtilBase.isPresent;

public class UtilIO {

    public static final String UTF_8 = "UTF-8";

    public static void leeOutputStream(InputStream is, OutputStream os)
            throws IOException {
        if (isPresent(is) && isPresent(os)) {
            final OutputStream o = os;
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                o.write(buffer, 0, bytesRead);
            }
        }
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public static Optional<byte[]> leeBytes(InputStream is) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            leeOutputStream(is, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                baos.close();
            } catch (IOException ignored) {
            }
            if (isPresent(is)) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
        final byte[] bytes = baos.toByteArray();
        return bytes.length == 0 ? Optional.<byte[]>empty() : Optional.of(bytes);
    }

    public static Optional<String> leeString(InputStream is) {
        return leeBytes(is).map(new Function<byte[], String>() {
            @Override
            public String apply(byte[] bytes) {
                try {
                    return new String(bytes, UTF_8);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private UtilIO() {
    }
}
