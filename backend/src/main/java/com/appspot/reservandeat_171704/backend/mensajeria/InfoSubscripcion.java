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

import com.appspot.reservandeat_171704.backend.entidades.Subscripcion;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.reservandeat_171704.datastore.Datastore.agregaEntidad;
import static net.reservandeat_171704.datastore.Datastore.busca;
import static net.reservandeat_171704.datastore.Datastore.getKey;

/** @author Gilberto Pacheco Gallegos */
public class InfoSubscripcion extends HttpServlet {
  @Override public String getServletInfo() {
    return "Mantiene los dispositivos web para notificaciones.";
  }
  @Override protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    final Subscripcion s =
        JSON.std.beanFrom(Subscripcion.class, request.getInputStream());
    if (!busca(Subscripcion.class, getKey(s)).isPresent()) {
      agregaEntidad(s);
      log("Subscripcion registrada: " + s.getKey());
    }
  }
}
