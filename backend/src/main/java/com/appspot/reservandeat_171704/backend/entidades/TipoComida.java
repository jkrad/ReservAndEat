package com.appspot.reservandeat_171704.backend.entidades;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class TipoComida {

    private Long key;
    @NotNull
    @Size(min = 1, max = 255)
    private String nombre;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
