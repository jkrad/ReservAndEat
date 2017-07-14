package com.appspot.reservandeat_171704.backend.entidades;

import com.google.appengine.api.datastore.Key;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class Usuario {

    @NotNull
    private String key; //Este se va a manejar como el correo
    @NotNull
    @Size(min = 1, max = 100)
    private String contrasena; //Contraseña del usuario
    @NotNull
    @Size(min = 1, max = 255)
    private String nombre;//Nombre del usuario
    private String telefono;
    private List<Key> roles = new ArrayList<>();
    private String upperKey;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
