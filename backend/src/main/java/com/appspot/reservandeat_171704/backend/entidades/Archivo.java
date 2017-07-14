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
package com.appspot.reservandeat_171704.backend.entidades;

import com.appspot.reservandeat_171704.backend.multimedia.UtilMultimedia;
import com.google.appengine.tools.cloudstorage.GcsFilename;

import java.io.IOException;

/** @author Gilberto Pacheco Gallegos */
public class Archivo {
  private Long key;
  private String nombre;
  private String bucket;
  private String servingUrl;
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
  public String getBucket() {
    return bucket;
  }
  public void setBucket(String bucket) {
    this.bucket = bucket;
  }
  public String getServingUrl() {
    return servingUrl;
  }
  public void setServingUrl(String servingUrl) {
    this.servingUrl = servingUrl;
  }
  public void antesDeEliminar() {
    try {
      UtilMultimedia.gcsService
          .delete(new GcsFilename(getBucket(), getNombre()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}