package com.appspot.reservandeat_171704.seguridad;

import net.ramptors.si.ModeloForm;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ModeloFormIniciarSesion extends ModeloForm {

    private String correo;
    private String contrasena;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
