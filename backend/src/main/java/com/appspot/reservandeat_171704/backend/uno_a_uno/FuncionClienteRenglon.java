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
package com.appspot.reservandeat_171704.backend.uno_a_uno;

import com.appspot.reservandeat_171704.backend.entidades.Cliente;
import com.appspot.reservandeat_171704.backend.entidades.Usuario;

import net.reservandeat_171704.si.Renglon;

import java8.util.function.Function;

import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static net.reservandeat_171704.datastore.Datastore.busca;
import static net.reservandeat_171704.datastore.Datastore.getKey;

/** @author Gilberto Pacheco Gallegos */
class FuncionClienteRenglon implements Function<Cliente, Renglon> {
  @Override public Renglon apply(Cliente modelo) {
    final Usuario usuario = busca(Usuario.class, modelo.getParent()).get();
    return new Renglon(keyToString(getKey(modelo)), modelo.getKey(),
        usuario.getNombre());
  }
}