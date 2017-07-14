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
import com.appspot.reservandeat_171704.backend.entidades.Rol;
import com.appspot.reservandeat_171704.backend.entidades.Usuario;
import com.appspot.reservandeat_171704.backend.entidades.Venta;
import com.appspot.reservandeat_171704.backend.muchos_a_muchos.FuncionUsuarioRenglon;
import com.appspot.reservandeat_171704.uno_a_uno.ModeloFormClientes;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import net.reservandeat_171704.datastore.Consulta;
import net.reservandeat_171704.datastore.Datastore;
import net.reservandeat_171704.datastore.Transaccion;
import net.reservandeat_171704.si.Renglon;
import net.reservandeat_171704.si.backend.CtrlAbcHttp;

import java8.util.function.Consumer;
import java8.util.stream.Collectors;
import java8.util.stream.Stream;

import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.ADMINISTRADOR;
import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.GERENTE;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static java8.util.Objects.requireNonNull;
import static net.reservandeat_171704.base.UtilBase.isNullOrEmpty;
import static net.reservandeat_171704.base.UtilBase.isTrue;
import static net.reservandeat_171704.base.UtilBase.toUpperCase;
import static net.reservandeat_171704.base.UtilFormatos.format;
import static net.reservandeat_171704.base.UtilFormatos.getFormatoFecha;
import static net.reservandeat_171704.base.UtilFormatos.getFormatoHora;
import static net.reservandeat_171704.base.UtilFormatos.parseFechaWeb;
import static net.reservandeat_171704.base.UtilFormatos.parseHoraWeb;
import static net.reservandeat_171704.datastore.Datastore.adaptaKey;
import static net.reservandeat_171704.datastore.Datastore.adaptaString;
import static net.reservandeat_171704.datastore.Datastore.agregaEntidad;
import static net.reservandeat_171704.datastore.Datastore.getKey;

/** @author Gilberto Pacheco Gallegos */
public class CtrlClientes extends CtrlAbcHttp<ModeloFormClientes, Cliente> {
  public CtrlClientes() {
    super("Cliente Nuevo", new Datastore<Cliente>() {}, ADMINISTRADOR);
  }
  @Override protected String getTitulo() {
    return getModelo().get().getKey();
  }
  @Override
  protected Stream<Renglon> getRenglones(Stream<Cliente> entidades) {
    return entidades.map(new FuncionClienteRenglon());
  }
  @Override protected void llenaModelo() {
    final ModeloFormClientes modeloForm = getModeloForm().get();
    final String parentId = modeloForm.getParent();
    final Key usuario = adaptaString(parentId);
    final boolean nuevo = isTrue(modeloForm.getNuevo());
    validaModeloForm(parentId, usuario, nuevo);
    getModelo().ifPresent(new Consumer<Cliente>() {
      @Override public void accept(Cliente modelo) {
        if (nuevo) {
          //noinspection ConstantConditions
          requireNonNull(usuario);
          modelo.setKey(usuario.getName());
          modelo.setParent(usuario);
        }
        modelo.setNacimiento(parseFechaWeb(modeloForm.getNacimiento(),
            "Formato incorrecto para Nacimiento."));
        modelo.setHoraFavorita(parseHoraWeb(modeloForm.getHoraFavorita(),
            "Formato incorrecto para Hora Favorita."));
        modelo.setUpperKey(toUpperCase(modelo.getKey()));
      }
    });
  }
  @Override protected void agregaModelo() {
    getModelo().ifPresent(new Consumer<Cliente>() {
      @Override public void accept(Cliente modelo) {
        try (Transaccion ignored = new Transaccion()) {
          final Venta venta = new Venta(getKey(modelo));
          agregaEntidad(venta);
          modelo.setVentaActual(getKey(venta));
          getInfo().agrega(modelo);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
  @Override protected void muestraModelo() {
    getModelo().ifPresent(new Consumer<Cliente>() {
      @Override public void accept(Cliente modelo) {
        final ModeloFormClientes modeloForm = getModeloForm().get();
        modeloForm.setParent("");
        modeloForm.setCampoParentMuestra(modeloForm.getNuevo());
        modeloForm.setParent(adaptaKey(modelo.getParent()));
        modeloForm
            .setNacimiento(format(getFormatoFecha(), modelo.getNacimiento()));
        modeloForm.setHoraFavorita(
            format(getFormatoHora(), modelo.getHoraFavorita()));
      }
    });
  }
  @Override protected void muestraOpciones() {
    final ModeloFormClientes modeloForm = getModeloForm().get();
    modeloForm.setParentOpciones(new Consulta<Usuario>() {}.setFilter(
        new FilterPredicate("roles", EQUAL, getKey(Rol.class, GERENTE)))
        .lista().map(new FuncionUsuarioRenglon())
        .collect(Collectors.<Renglon>toList()));
  }
  @Override
  protected void elimina() {
    procesaError("Error de eliminación,",
        new RuntimeException("Eliminar no está permitido."));
  }
  private void validaModeloForm(String parentId, Key usuario, boolean nuevo) {
    if (nuevo && isNullOrEmpty(parentId)) {
      throw new RuntimeException("Falta seleccionar el identificador.");
    } else if (nuevo && new Consulta<Cliente>() {}.setFilter(
        new FilterPredicate("upperKey", EQUAL, toUpperCase(usuario.getName())))
        .lista().count() > 0) {
      throw new RuntimeException(
          "El identificador de usuario ya está seleccionado");
    }
  }
}
