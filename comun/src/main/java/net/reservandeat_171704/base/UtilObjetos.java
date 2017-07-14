package net.reservandeat_171704.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import java8.util.Optional;

import static net.reservandeat_171704.base.UtilBase.isPresent;

public class UtilObjetos {

    /**
     * Devuelve un parámetro genérico utilizado para construir un objeto.
     *
     * @param <T> Tipo esperado del parámetro.
     * @param objeto instancia cereada con parámetros genéricos.
     * @param indice índice del parámetro genérico.
     * @return una unstancia de la clase T, que es el parámetro genérico en el
     * índice indicado.
     */
    public static <T> Class<T> getParametroGenerico(Object objeto, int indice) {
        return getParametroGenerico(objeto.getClass(), indice);
    }

    /**
     * Devuelve un parámetro genérico de una clase.
     *
     * @param <T> Tipo esperado del parámetro.
     * @param tipo la clase que se analiza.
     * @param indice indice del parámetro genérico.
     * @return una unstancia de la clase T, que es el parámetro genérico en el
     * indice indicado.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getParametroGenerico(Class<?> tipo, int indice) {
        final Type[] parametrosGenericos = getParametrosGenericos(tipo);
        return (Class<T>) (parametrosGenericos[indice]);
    }

    /**
     * Identifica los tipos concretos que se usaron para crear un objeto cuya
     * clase usa parámetros genéricos.
     *
     * @param tipo la clase que se analiza.
     * @return un arreglo con los parámtros genéricos de la clase del objeto o
     * de su clase madre más inmediata que usa parámetros genéricos. Si no tiene
     * una clase madre con parámetros genéricos, se obtiene un arreglo vacío.
     */
    private static Type[] getParametrosGenericos(Class<?> tipo) {
        Type genericSuperclass = tipo.getGenericSuperclass();
        while (isPresent(genericSuperclass)
                && !(genericSuperclass instanceof ParameterizedType)) {
            genericSuperclass = ((Class<?>) genericSuperclass).getGenericSuperclass();
        }
        if (isPresent(genericSuperclass)) {
            final ParameterizedType pt = (ParameterizedType) genericSuperclass;
            return pt.getActualTypeArguments();
        } else {
            return new Type[0]; // Regresa un arreglo vacío.
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<List<T>> castList(final Class<T> tipoGenerico,
            Object obj) {
        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            if (list.isEmpty()) {
                return Optional.of((List<T>) list);
            } else {
                if (tipoGenerico.isInstance(list.get(0))) {
                    return Optional.of((List<T>) list);
                } else {
                    throw new ClassCastException("Tipo genérico incorrecto.");
                }
            }
        } else {
            return Optional.empty();
        }
    }

    private UtilObjetos() {
    }
}
