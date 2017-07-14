package net.reservandeat_171704.base;

import java.math.BigDecimal;
import java.util.Collection;

import java8.util.Optional;

public class UtilBase {

    public static boolean isPresent(Object obj) {
        return obj != null;
    }

    public static String texto(String s) {
        return s == null ? "" : s;
    }

    public static String toUpperCase(String s) {
        return s == null ? null : s.toUpperCase();
    }

    public static boolean isNullOrEmpty(Optional<String> s) {
        return !s.isPresent() || s.get().length() == 0;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNullOrEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static <T> boolean isNullOrEmpty(T[] c) {
        return c == null || c.length == 0;
    }

    public static BigDecimal newBigDecimal(String valor) {
        return valor == null ? null : new BigDecimal(valor);
    }

    public static String toPlainString(BigDecimal b) {
        return b == null ? null : b.toPlainString();
    }

    public static String eliminaSufijo(String texto, String sufijo) {
        return texto == null ? "" : texto.substring(0, texto.lastIndexOf(sufijo));
    }

    public static boolean isTrue(String campo) {
        return "true".equals(campo);
    }

    public static String getMensaje(Throwable e) {
        final String localizedMessage = e.getLocalizedMessage();
        return isNullOrEmpty(localizedMessage) ? e.toString() : localizedMessage;
    }

    private UtilBase() {
    }
}
