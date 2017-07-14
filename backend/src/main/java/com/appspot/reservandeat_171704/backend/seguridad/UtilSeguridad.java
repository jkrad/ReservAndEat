package com.appspot.reservandeat_171704.backend.seguridad;

import net.reservandeat_171704.si.Sesion;

import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java8.util.J8Arrays;
import java8.util.Optional;
import java8.util.function.Function;
import java8.util.function.Predicate;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static net.reservandeat_171704.base.UtilBase.isNullOrEmpty;
import static net.reservandeat_171704.base.UtilBase.isPresent;

public class UtilSeguridad {

    static final String NOMBRE_DE_COOKIE = "x";

    private UtilSeguridad() {
    }

    public static String encripta(final String texto) {
        try {
            if (isNullOrEmpty(texto)) {
                return null;
            } else {
                final MessageDigest md = MessageDigest.getInstance("SHA-256");
                final byte[] bytes = md.digest(texto.getBytes(UTF_8));
                return Base64.toBase64String(bytes);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static Optional<Cookie> getCookie(HttpServletRequest request,
            final String name) {
        return Optional.ofNullable(request.getCookies()).flatMap(
                new Function<Cookie[], Optional<Cookie>>() {
            @Override
            public Optional<Cookie> apply(Cookie[] cookies) {
                return J8Arrays.stream(cookies)
                        .filter(new Predicate<Cookie>() {
                            @Override
                            public boolean test(Cookie cookie) {
                                return cookie.getName().equals(name);
                            }
                        }).findFirst();
            }
        });
    }

    public static Optional<Sesion> recuperaSesion(HttpServletRequest request)
            throws IOException {
        final Optional<Cookie> cookie = getCookie(request, NOMBRE_DE_COOKIE);
        if (cookie.isPresent()) {
            final String value = cookie.get().getValue();
            if (isPresent(value)) {
                final byte[] bytes = Base64.decode(value);
                try (DataInputStream dis
                        = new DataInputStream(new ByteArrayInputStream(bytes))) {
                    dis.readDouble();
                    final String id = dis.readUTF();
                    dis.readDouble();
                    final String roles = dis.readUTF();
                    return Optional
                            .of(new Sesion(id, new HashSet<>(asList(roles.split(",")))));
                }
            }
        }
        return Optional.empty();
    }
}
