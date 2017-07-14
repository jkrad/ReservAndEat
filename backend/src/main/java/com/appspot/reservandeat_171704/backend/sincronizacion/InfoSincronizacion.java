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
package com.appspot.reservandeat_171704.backend.sincronizacion;

import com.appspot.reservandeat_171704.backend.mensajeria.Mensajeria;
import com.appspot.reservandeat_171704.entidades.Aviso;
import com.fasterxml.jackson.jr.ob.JSON;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import net.reservandeat_171704.datastore.Consulta;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java8.util.Optional;
import java8.util.stream.Collectors;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static net.reservandeat_171704.datastore.Datastore.agregaEntidad;
import static net.reservandeat_171704.datastore.Datastore.busca;
import static net.reservandeat_171704.datastore.Datastore.modificaEntidad;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class InfoSincronizacion extends HttpServlet {

    @Override
    public String getServletInfo() {
        return "Realiza la sincronización";
    }

    @Override
    protected void doGet(HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        JSON.std.write(new Consulta<Aviso>() {
        }
                .setFilter(new FilterPredicate("eliminado", EQUAL, false))
                .lista().collect(Collectors.<Aviso>toList()),
                resp.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        int modificados = 0;
        final List<Aviso> lista
                = JSON.std.listOfFrom(Aviso.class, request.getInputStream());
        for (final Aviso modeloCliente : lista) {
            final Optional<Aviso> modeloServidor
                    = busca(Aviso.class, modeloCliente.getKey());
            if (modeloServidor.isPresent()) {
                final Aviso avisoServidor = modeloServidor.get();
                if (modeloCliente.isEliminado() && !avisoServidor.isEliminado()) {
                    /* CONFLICTO. El registro está en el servidor, pero ha sido
           * eliminado en el cliente. Lo eliminamos, porque optamos por no
           * revivir lo que se ha borrado. */
                    avisoServidor.setEliminado(true);
                    modificaEntidad(avisoServidor);
                    modificados++;
                } else if (!avisoServidor.isEliminado()) {
                    /* CONFLICTO. El registro está tanto en el servidor como en el
           * cliente. Los datos pueden ser diferentes. PREVALECE LA FECHA MÁS
            * GRANDE. Cuando gana el servidor no se hace nada. */
                    final long modificaciónCliente = modeloCliente.getModificacion();
                    final long modificaciónServidor
                            = avisoServidor.getModificacion();
                    if (modificaciónCliente > modificaciónServidor) {
                        // La versión del móvil es más nueva y prevalece.
                        modificaEntidad(modeloCliente);
                        modificados++;
                    }
                }
            } else {
                /* CONFLICTO. El objeto no ha estado en el servidor. AGREGARLO. Si ya
         * está eliminado, simplemente no se agrega. */
                if (!modeloCliente.isEliminado()) {
                    agregaEntidad(modeloCliente);
                    modificados++;
                }
            }
        }
        final Map<String, String> data = new HashMap<>();
        data.put("sincronizacion", modificados == 0 ? "terminada" : "procesando");
        Mensajeria.notifica(data);
    }
}
