package com.appspot.reservandeat_171704.backend.entidades;

/**
 * Created by shadow on 26/06/2017.
 */
@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class Servicio {
    private Long key;

    private String nombre;

    private String upperNombre;

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

    public String getUpperNombre() {
        return upperNombre;
    }

    public void setUpperNombre(String upperNombre) {
        this.upperNombre = upperNombre;
    }
}