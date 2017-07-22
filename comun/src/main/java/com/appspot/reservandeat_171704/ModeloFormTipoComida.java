
package com.appspot.reservandeat_171704;

import net.ramptors.si.ModeloFormAbc;


@SuppressWarnings({"WeakerAccess", "unused"})
public class ModeloFormTipoComida extends ModeloFormAbc {
  private String nombre;
  private String nombreError;
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
}

