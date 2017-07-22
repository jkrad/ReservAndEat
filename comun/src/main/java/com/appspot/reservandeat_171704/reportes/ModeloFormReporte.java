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
package com.appspot.reservandeat_171704.reportes;


import net.ramptors.si.ModeloForm;

import java.util.List;

/** @author Gilberto Pacheco Gallegos */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ModeloFormReporte extends ModeloForm {
  private List<RegistroVenta> lista;
  private String total;
  public List<RegistroVenta> getLista() {
    return lista;
  }
  public void setLista(List<RegistroVenta> lista) {
    this.lista = lista;
  }
  public String getTotal() {
    return total;
  }
  public void setTotal(String total) {
    this.total = total;
  }
}
