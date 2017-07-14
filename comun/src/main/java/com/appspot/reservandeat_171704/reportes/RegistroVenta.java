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

/** @author Gilberto Pacheco Gallegos */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RegistroVenta {
  private String prdNombre;
  private String total;
  public RegistroVenta() {
  }
  public RegistroVenta(String prdNombre, String total) {
    this.prdNombre = prdNombre;
    this.total = total;
  }
  public String getPrdNombre() {
    return prdNombre;
  }
  public void setPrdNombre(String prdNombre) {
    this.prdNombre = prdNombre;
  }
  public String getTotal() {
    return total;
  }
  public void setTotal(String total) {
    this.total = total;
  }
}
