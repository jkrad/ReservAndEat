
package com.appspot.reservandeat_171704.backend.entidades;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class Rol {

    @NotNull
    @Size(min = 1, max = 255)
    private String key;
    @NotNull
    @Size(min = 1, max = 255)
    private String descripcion;

    public Rol() {
    }

    public Rol(String key, String descripcion) {
        this.key = key;
        this.descripcion = descripcion;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
