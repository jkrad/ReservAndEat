package com.appspot.reservandeat_171704;

import net.reservandeat_171704.si.ModeloFormAbc;

/**
 * Created by shadow on 26/06/2017.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ModeloFormRol extends ModeloFormAbc {
    private String rol;
    private String rolError;
    private String descripcion;
    private String descripcionError;
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String nombre) {
        this.descripcion = nombre;
    }
    public String getDescripcionErrorError() {
        return descripcionError;
    }
    public void setDescripcionErrorError(String nombreError) {
        this.descripcionError = nombreError;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getRolError() {
        return rolError;
    }

    public void setRolError(String rolError) {
        this.rolError = rolError;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }
}
