
package com.appspot.reservandeat_171704.seguridad;

import net.reservandeat_171704.si.ModeloForm;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ModeloFormIniciarSesion extends ModeloForm {

    private String id;
    private String contrasena;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
