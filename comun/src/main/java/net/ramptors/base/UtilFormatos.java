/* Copyright 2017 Gilberto Pacheco Gallegos Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. */
package net.ramptors.base;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static net.ramptors.base.UtilBase.isNullOrEmpty;
import static net.ramptors.base.UtilBase.isPresent;

/**
 * Funciones de apoyo al procesamiento de datos.
 */
@SuppressWarnings("WeakerAccess")
public class UtilFormatos {

    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final String FORMATO_ENTERO = "#,##0";
    private static final String FORMATO_PRECIO = "#,##0.00";
    private static final String FORMATO_FECHA = "yyyy-MM-dd";
    private static final String FORMATO_HORA = "HH:mm";
    private static final String FORMATO_FECHA_HORA = FORMATO_FECHA + "'T'"
            + FORMATO_HORA;
    private static final String FORMATO_FECHA_HORA_TZ = FORMATO_FECHA_HORA + " z";

    private UtilFormatos() {
    }

    public static DecimalFormat getFormatoEntero() {
        return new DecimalFormat(FORMATO_ENTERO);
    }

    public static DecimalFormat getFormatoPrecio() {
        final DecimalFormat formato = new DecimalFormat(FORMATO_PRECIO);
        formato.setParseBigDecimal(true);
        return formato;
    }

    public static SimpleDateFormat getFormatoFecha() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA,
                Locale.US);
        dateFormat.setTimeZone(UTC);
        return dateFormat;
    }

    public static SimpleDateFormat getFormatoHora() {
        final SimpleDateFormat dateFormat
                = new SimpleDateFormat(FORMATO_HORA, Locale.US);
        dateFormat.setTimeZone(UTC);
        return dateFormat;
    }

    public static SimpleDateFormat getFormatoFechaHora(TimeZone timeZone) {
        final SimpleDateFormat dateFormat
                = new SimpleDateFormat(FORMATO_FECHA_HORA, Locale.US);
        dateFormat.setTimeZone(timeZone);
        return dateFormat;
    }

    public static SimpleDateFormat getFormatoWebFechaHoraTimeZone() {
        return new SimpleDateFormat(FORMATO_FECHA_HORA_TZ, Locale.US);
    }

    public static String format(Format formato, Object obj) {
        if (isPresent(obj)) {
            return formato.format(obj);
        } else {
            return "";
        }
    }

    public static <T> T parse(Class<T> tipo, Format formato, String txt,
            String mensajeDeError) {
        try {
            if (isNullOrEmpty(txt)) {
                return null;
            } else {
                return tipo.cast(formato.parseObject(txt));
            }
        } catch (ParseException e) {
            throw new RuntimeException(mensajeDeError);
        }
    }

    public static Long parseEntero(String texto, String mensajeDeError) {
        return parse(Long.class, getFormatoEntero(), texto, mensajeDeError);
    }

    public static BigDecimal parsePrecio(String texto, String mensajeDeError) {
        return parse(BigDecimal.class, getFormatoPrecio(), texto, mensajeDeError);
    }

    public static Date parseFechaWeb(String texto, String mensajeDeError) {
        return parse(Date.class, getFormatoWebFechaHoraTimeZone(),
                isNullOrEmpty(texto) ? null : texto + "T00:00 UTC", mensajeDeError);
    }

    public static Date parseHoraWeb(String texto, String mensajeDeError) {
        return parse(Date.class, getFormatoWebFechaHoraTimeZone(),
                isNullOrEmpty(texto) ? null : "1970-01-01T" + texto + " UTC",
                mensajeDeError);
    }

    public static Date parseFechaHoraWeb(String texto, TimeZone timeZone,
            String mensajeDeError) {
        return parse(Date.class, getFormatoFechaHora(timeZone), texto,
                mensajeDeError);
    }

    public static Calendar creaCalendarEnCeros(TimeZone timeZone) {
        final Calendar c = Calendar.getInstance(timeZone);
        c.clear();
        return c;
    }
}
