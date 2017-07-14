/*
 * Copyright 2017 Gilberto Pacheco Gallegos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appspot.reservandeat_171704.uno_a_uno;

import net.reservandeat_171704.si.ModeloFormAbc;
import net.reservandeat_171704.si.Renglon;

import java.util.List;

/** @author Gilberto Pacheco Gallegos */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ModeloFormClientes extends ModeloFormAbc {
  private String campoParentMuestra;
  private String parent;
  private String parentError;
  private List<Renglon> parentOpciones;
  private String nacimiento;
  private String nacimientoError;
  private String horaFavorita;
  private String horaFavoritaError;
  public String getCampoParentMuestra() {
    return campoParentMuestra;
  }
  public void setCampoParentMuestra(String campoParentMuestra) {
    this.campoParentMuestra = campoParentMuestra;
  }
  public String getParent() {
    return parent;
  }
  public void setParent(String parent) {
    this.parent = parent;
  }
  public String getParentError() {
    return parentError;
  }
  public void setParentError(String parentError) {
    this.parentError = parentError;
  }
  public List<Renglon> getParentOpciones() {
    return parentOpciones;
  }
  public void setParentOpciones(List<Renglon> parentOpciones) {
    this.parentOpciones = parentOpciones;
  }
  public String getNacimiento() {
    return nacimiento;
  }
  public void setNacimiento(String nacimiento) {
    this.nacimiento = nacimiento;
  }
  public String getNacimientoError() {
    return nacimientoError;
  }
  public void setNacimientoError(String nacimientoError) {
    this.nacimientoError = nacimientoError;
  }
  public String getHoraFavorita() {
    return horaFavorita;
  }
  public void setHoraFavorita(String horaFavorita) {
    this.horaFavorita = horaFavorita;
  }
  public String getHoraFavoritaError() {
    return horaFavoritaError;
  }
  public void setHoraFavoritaError(String horaFavoritaError) {
    this.horaFavoritaError = horaFavoritaError;
  }
}
