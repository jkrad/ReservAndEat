package com.appspot.reservandeat_171704.backend.entidades;

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class Restaurante {

    @NotNull
    private String key; //Este se va a manejar como el correo
    @NotNull
    @Size(min = 1, max = 255)
    private String nombreRestaurante;//Nombre del restaurante
    @NotNull
    private String telefono;
    @NotNull
    private String puestoEncargado;//Puesto del gerente?
    @NotNull
    private String contrasena;

    private List<Key> roles = new ArrayList<>();
    private String upperKey;
    private Key comida;
    @NotNull
    private String diaInicio;
    @NotNull
    private String diaFin;
    @NotNull
    private String horaInicio;
    @NotNull
    private String horaFin;

    private Key colonia;
    @NotNull
    private String numExterior;
    @NotNull
    private String numInterior;

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
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

    public String getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(String diaInicio) {
        this.diaInicio = diaInicio;
    }

    public String getDiaFin() {
        return diaFin;
    }

    public void setDiaFin(String diaFin) {
        this.diaFin = diaFin;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Key getColonia() {
        return colonia;
    }

    public void setColonia(Key colonia) {
        this.colonia = colonia;
    }

    public String getNumExterior() {
        return numExterior;
    }

    public void setNumExterior(String numExterior) {
        this.numExterior = numExterior;
    }

    public String getNumInterior() {
        return numInterior;
    }

    public void setNumInterior(String numInterior) {
        this.numInterior = numInterior;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
}
