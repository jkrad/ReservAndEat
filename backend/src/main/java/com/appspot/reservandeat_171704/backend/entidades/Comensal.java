package com.appspot.reservandeat_171704.backend.entidades;

import com.google.appengine.api.datastore.Key;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;


public class Comensal {
  @NotNull
  private Key parent;
  @NotNull
  private String key;
  @NotNull @Past
  private Date nacimiento;
  @NotNull
  private Date horaFavorita;
  private Key ventaActual;
  private String upperKey;
  public Key getParent() {
    return parent;
  }
  public void setParent(Key parent) {
    this.parent = parent;
  }
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
  public Date getNacimiento() {
    return nacimiento;
  }
  public void setNacimiento(Date nacimiento) {
    this.nacimiento = nacimiento;
  }
  public Date getHoraFavorita() {
    return horaFavorita;
  }
  public void setHoraFavorita(Date horaFavorita) {
    this.horaFavorita = horaFavorita;
  }
  public Key getVentaActual() {
    return ventaActual;
  }
  public void setVentaActual(Key ventaActual) {
    this.ventaActual = ventaActual;
  }
  public String getUpperKey() {
    return upperKey;
  }
  public void setUpperKey(String upperKey) {
    this.upperKey = upperKey;
  }
}
