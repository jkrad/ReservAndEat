package com.appspot.reservandeat_171704.entidades;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Aviso {

    @NotNull
    private String key;
    @NotNull
    @Size(min = 1, max = 255)
    private String titulo;
    @NotNull
    @Size(min = 1, max = 255)
    private String texto;
    @NotNull
    private long modificacion;
    private boolean eliminado;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getModificacion() {
        return modificacion;
    }

    public void setModificacion(long modificacion) {
        this.modificacion = modificacion;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}
