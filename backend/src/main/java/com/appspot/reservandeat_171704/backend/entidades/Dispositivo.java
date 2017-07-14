package com.appspot.reservandeat_171704.backend.entidades;

import javax.validation.constraints.NotNull;

public class Dispositivo {

    @NotNull
    private String key;

    @SuppressWarnings("unused")
    public Dispositivo() {
    }

    public Dispositivo(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
