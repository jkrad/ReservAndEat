package com.appspot.reservandeat_171704.backend.entidades;

import javax.validation.constraints.NotNull;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings("unused")
public class Subscripcion {

    @NotNull
    private String key;
    @NotNull
    private String llave;
    @NotNull
    private String auth;

    public Subscripcion() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
