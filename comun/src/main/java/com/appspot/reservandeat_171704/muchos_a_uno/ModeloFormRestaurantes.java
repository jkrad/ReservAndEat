
package com.appspot.reservandeat_171704.muchos_a_uno;

import net.reservandeat_171704.si.ModeloFormAbc;
import net.reservandeat_171704.si.Renglon;

import java.util.List;


@SuppressWarnings({"unused", "WeakerAccess"})
public class ModeloFormRestaurantes extends ModeloFormAbc {

    private String nombre;
    private String nombreError;
    private String telefono;
    private String telefonoErro;
    private String correo;
    private String correoError;
    private String contrasena;
    private String comida;
    private String comidaError;
    private List<Renglon> tipoComidaOpciones;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreError() {
        return nombreError;
    }

    public void setNombreError(String nombreError) {
        this.nombreError = nombreError;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonoErro() {
        return telefonoErro;
    }

    public void setTelefonoErro(String telefonoErro) {
        this.telefonoErro = telefonoErro;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoError() {
        return correoError;
    }

    public void setCorreoError(String correoError) {
        this.correoError = correoError;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getComida() {
        return comida;
    }

    public void setComida(String comida) {
        this.comida = comida;
    }

    public String getComidaError() {
        return comidaError;
    }

    public void setComidaError(String comidaError) {
        this.comidaError = comidaError;
    }

    public List<Renglon> getTipoComidaOpciones() {
        return tipoComidaOpciones;
    }

    public void setTipoComidaOpciones(List<Renglon> tipoComidaOpciones) {
        this.tipoComidaOpciones = tipoComidaOpciones;
    }
}
