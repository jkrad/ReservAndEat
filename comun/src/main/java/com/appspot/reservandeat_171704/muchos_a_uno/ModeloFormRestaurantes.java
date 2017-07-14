
package com.appspot.reservandeat_171704.muchos_a_uno;

import net.reservandeat_171704.si.ModeloFormAbc;
import net.reservandeat_171704.si.Renglon;

import java.util.List;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ModeloFormRestaurantes extends ModeloFormAbc {

    private String nombre;
    private String nombreError;
    private String telefono;
    private String correo;
    private String contrasena;
    private String tipoComida;
    private String tipoComidaError;
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

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public String getTipoComidaError() {
        return tipoComidaError;
    }

    public void setTipoComidaError(String tipoComidaError) {
        this.tipoComidaError = tipoComidaError;
    }

    public List<Renglon> getTipoComidaOpciones() {
        return tipoComidaOpciones;
    }

    public void setTipoComidaOpciones(List<Renglon> tipoComidaOpciones) {
        this.tipoComidaOpciones = tipoComidaOpciones;
    }
    
}
