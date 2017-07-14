package com.appspot.reservandeat_171704.backend.entidades;

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class Restaurante {

    private Long key;
    @NotNull
    @Size(min = 1, max = 255)
    private String nombre;
    @NotNull
    private String telefono;
    @NotNull
    private String puestoEncargado;
    @NotNull
    private String correo;
    @NotNull
    private String contrasena;

    private List<Key> roles = new ArrayList<>();
    private String upperKey;

    private Key comida;

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPuestoEncargado() {
        return puestoEncargado;
    }

    public void setPuestoEncargado(String puestoEncargado) {
        this.puestoEncargado = puestoEncargado;
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

    public List<Key> getRoles() {
        return roles;
    }

    public void setRoles(List<Key> roles) {
        this.roles = roles;
    }

    public String getUpperKey() {
        return upperKey;
    }

    public void setUpperKey(String upperKey) {
        this.upperKey = upperKey;
    }

    public Key getComida() {
        return comida;
    }

    public void setComida(Key comida) {
        this.comida = comida;
    }
}
