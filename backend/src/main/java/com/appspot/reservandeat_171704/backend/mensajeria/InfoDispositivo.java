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
package com.appspot.reservandeat_171704.backend.mensajeria;

import com.appspot.reservandeat_171704.backend.entidades.Dispositivo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java8.util.function.Consumer;

import static net.reservandeat_171704.base.UtilIO.leeString;
import static net.reservandeat_171704.datastore.Datastore.agregaEntidad;
import static net.reservandeat_171704.datastore.Datastore.busca;

/** @author Gilberto Pacheco Gallegos */
public class InfoDispositivo extends HttpServlet {
  @Override public String getServletInfo() {
    return "Mantiene los dispositivos para notificaciones.";
  }
  @Override protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    leeString(request.getInputStream()).ifPresent(new Consumer<String>() {
      @Override public void accept(String key) {
        if (!busca(Dispositivo.class, key).isPresent()) {
          agregaEntidad(new Dispositivo(key));
        }
      }
    });
  }
}