package com.appspot.reservandeat_171704.muchos_a_muchos;

import net.reservandeat_171704.si.ModeloFormAbc;
import net.reservandeat_171704.si.Renglon;

import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ModeloFormUsuarios extends ModeloFormAbc {

    private String key;
    private String campoKeyMuestra;
    private String keyError;
    private String contrasena;
    private String contrasenaObligatoriaMuestra;
    private String contrasenaError;
    private String confirma;
    private String confirmaObligatoriaMuestra;
    private String confirmaError;
    private String debeConfirmar;
    private String debeConfirmarError;
    private String telefono;
    private String telefonoError;
    private String nombre;
    private String nombreError;
    private List<String> roles;
    private String rolesError;
    private List<Renglon> rolesOpciones;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCampoKeyMuestra() {
        return campoKeyMuestra;
    }

    public void setCampoKeyMuestra(String campoKeyMuestra) {
        this.campoKeyMuestra = campoKeyMuestra;
    }

    public String getKeyError() {
        return keyError;
    }

    public void setKeyError(String keyError) {
        this.keyError = keyError;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getContrasenaObligatoriaMuestra() {
        return contrasenaObligatoriaMuestra;
    }

    public void setContrasenaObligatoriaMuestra(
            String contrasenaObligatoriaMuestra) {
        this.contrasenaObligatoriaMuestra = contrasenaObligatoriaMuestra;
    }

    public String getContrasenaError() {
        return contrasenaError;
    }

    public void setContrasenaError(String contrasenaError) {
        this.contrasenaError = contrasenaError;
    }

    public String getConfirma() {
        return confirma;
    }

    public void setConfirma(String confirma) {
        this.confirma = confirma;
    }

    public String getConfirmaObligatoriaMuestra() {
        return confirmaObligatoriaMuestra;
    }

    public void setConfirmaObligatoriaMuestra(String confirmaObligatoriaMuestra) {
        this.confirmaObligatoriaMuestra = confirmaObligatoriaMuestra;
    }

    public String getConfirmaError() {
        return confirmaError;
    }

    public void setConfirmaError(String confirmaError) {
        this.confirmaError = confirmaError;
    }

    public String getDebeConfirmar() {
        return debeConfirmar;
    }

    public void setDebeConfirmar(String debeConfirmar) {
        this.debeConfirmar = debeConfirmar;
    }

    public String getDebeConfirmarError() {
        return debeConfirmarError;
    }

    public void setDebeConfirmarError(String debeConfirmarError) {
        this.debeConfirmarError = debeConfirmarError;
    }

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getRolesError() {
        return rolesError;
    }

    public void setRolesError(String rolesError) {
        this.rolesError = rolesError;
    }

    public List<Renglon> getRolesOpciones() {
        return rolesOpciones;
    }

    public void setRolesOpciones(List<Renglon> rolesOpciones) {
        this.rolesOpciones = rolesOpciones;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonoError() {
        return telefonoError;
    }

    public void setTelefonoError(String telefonoError) {
        this.telefonoError = telefonoError;
    }
}
