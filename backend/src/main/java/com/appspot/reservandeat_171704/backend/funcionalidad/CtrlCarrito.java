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
package com.appspot.reservandeat_171704.backend.funcionalidad;

import com.appspot.reservandeat_171704.backend.entidades.Archivo;
import com.appspot.reservandeat_171704.backend.entidades.Restaurante;
import com.appspot.reservandeat_171704.backend.entidades.Cliente;
import com.appspot.reservandeat_171704.backend.entidades.DetalleDeVenta;
import com.appspot.reservandeat_171704.backend.entidades.Producto;
import com.appspot.reservandeat_171704.backend.entidades.Usuario;
import com.appspot.reservandeat_171704.backend.entidades.Venta;
import com.appspot.reservandeat_171704.backend.mensajeria.Mensajeria;
import com.appspot.reservandeat_171704.funcionalidad.ModeloFormCarrito;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.TransactionOptions;

import net.reservandeat_171704.datastore.Datastore;
import net.reservandeat_171704.datastore.Transaccion;
import net.reservandeat_171704.si.Renglon;
import net.reservandeat_171704.si.Sesion;
import net.reservandeat_171704.si.backend.CtrlAbcHttp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import java8.util.function.Consumer;
import java8.util.function.Function;
import java8.util.stream.Collectors;
import java8.util.stream.Stream;

import static com.appspot.reservandeat_171704.backend.multimedia.UtilMultimedia
    .getArchivoUrl;
import static java8.util.stream.StreamSupport.stream;
import static net.reservandeat_171704.base.UtilBase.isNullOrEmpty;
import static net.reservandeat_171704.base.UtilBase.isPresent;
import static net.reservandeat_171704.base.UtilBase.newBigDecimal;
import static net.reservandeat_171704.base.UtilFormatos.format;
import static net.reservandeat_171704.base.UtilFormatos.getFormatoEntero;
import static net.reservandeat_171704.base.UtilFormatos.getFormatoFechaHora;
import static net.reservandeat_171704.base.UtilFormatos.getFormatoPrecio;
import static net.reservandeat_171704.base.UtilFormatos.parseEntero;
import static net.reservandeat_171704.base.UtilFormatos.parseFechaHoraWeb;
import static net.reservandeat_171704.datastore.Datastore.agregaEntidad;
import static net.reservandeat_171704.datastore.Datastore.busca;
import static net.reservandeat_171704.datastore.Datastore.getKey;
import static net.reservandeat_171704.datastore.Datastore.modificaEntidad;

/** @author Gilberto Pacheco Gallegos */
public class CtrlCarrito
    extends CtrlAbcHttp<ModeloFormCarrito, DetalleDeVenta> {
  public CtrlCarrito() {
    super("Selecciona un Producto", new Datastore<DetalleDeVenta>() {});
  }
  @Override protected void procesa() {
    final String accion = getModeloForm().get().getAccion();
    if (isPresent(accion)) {
      switch (accion) {
      case "guardarVenta":
        guardaVenta();
        break;
      case "pagar":
        registra();
      default:
        super.procesa();
      }
    }
  }
  @Override protected void eliminaModelo() {
    getSesion().ifPresent(new Consumer<Sesion>() {
      @Override public void accept(Sesion sesion) {
        final Key usuarioKey = getKey(Usuario.class, sesion.getId());
        try (Transaccion ignored = new Transaccion(
            TransactionOptions.Builder.withXG(true))) {
          final Cliente cliente =
              busca(usuarioKey, Cliente.class, sesion.getId()).get();
          final Venta venta =
              busca(Venta.class, cliente.getVentaActual()).get();
          getModelo().ifPresent(new Consumer<DetalleDeVenta>() {
            @Override public void accept(DetalleDeVenta dv) {
              if (venta.elimina(dv.getProducto())) {
                getInfo().elimina(dv);
                modificaEntidad(venta);
              }
            }
          });
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        inicia();
      }
    });
  }
  private void guardaVenta() {
    getSesion().ifPresent(new Consumer<Sesion>() {
      @Override public void accept(Sesion sesion) {
        final Key usuarioKey = getKey(Usuario.class, sesion.getId());
        final Cliente cliente =
            busca(usuarioKey, Cliente.class, sesion.getId()).get();
        final Venta venta =
            busca(Venta.class, cliente.getVentaActual()).get();
        final ModeloFormCarrito modeloForm = getModeloForm().get();
        venta.setDireccionDeEntrega(modeloForm.getDireccionDeEntrega());
        venta.setFechaHoraDeEntrega(parseFechaHoraWeb(
            modeloForm.getFechaHoraDeEntrega(), getTimeZone(),
            "Valor incorrecto para Fecha y Hora de Entrega."));
        modificaEntidad(venta);
        modeloForm.setSiguienteForm("compro-inicio");
      }
    });
  }
  private void registra() {
    final ModeloFormCarrito modeloForm = getModeloForm().get();
    if (isNullOrEmpty(modeloForm.getFechaHoraDeEntrega())) {
      throw new RuntimeException("Falta la fecha de entrega.");
    } else if (isNullOrEmpty(modeloForm.getDireccionDeEntrega())) {
      throw new RuntimeException("Falta la dirección de entrega.");
    }
    getSesion().ifPresent(new Consumer<Sesion>() {
      @Override public void accept(Sesion sesion) {
        final Key usuarioKey = getKey(Usuario.class, sesion.getId());
        try (Transaccion ignored = new Transaccion(
            TransactionOptions.Builder.withXG(true))) {
          final Cliente cliente =
              busca(usuarioKey, Cliente.class, sesion.getId()).get();
          Venta venta = busca(Venta.class, cliente.getVentaActual()).get();
          venta.setDireccionDeEntrega(modeloForm.getDireccionDeEntrega());
          venta.setFechaHoraDeEntrega(parseFechaHoraWeb(
              modeloForm.getFechaHoraDeEntrega(), getTimeZone(),
              "Valor incorrecto para Fecha y Hora de Entrega."));
          venta.setRegistro(new Date().getTime());
          modificaEntidad(venta);
          stream(venta.getDetalles())
              .map(new Function<Key, DetalleDeVenta>() {
                @Override public DetalleDeVenta apply(Key k) {
                  return busca(DetalleDeVenta.class, k).get();
                }
              }).forEach(new Consumer<DetalleDeVenta>() {
            @Override public void accept(DetalleDeVenta d) {
              final Producto p = busca(Producto.class, d.getProducto()).get();
              d.setPrecio(p.getPrecio());
              getInfo().modifica(d);
              p.decrementaExistencias(d.getCantidad());
              modificaEntidad(p);
            }
          });
          venta = new Venta();
          venta.setParent(getKey(cliente));
          agregaEntidad(venta);
          cliente.setVentaActual(getKey(venta));
          modificaEntidad(cliente);
          final Map<String, String> data = new HashMap<>();
          data.put("id", sesion.getId());
          data.put("texto", "Venta en proceso de entrega: " + venta.getKey());
          Mensajeria.notifica(data);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        modeloForm.setSiguienteForm("compro-inicio");
      }
    });
  }
  @Override protected String getTitulo() {
    final DetalleDeVenta modelo = getModelo().get();
    final Producto p = busca(Producto.class, modelo.getProducto()).get();
    return p.getNombre();
  }
  @Override
  protected Stream<Renglon> getRenglones(Stream<DetalleDeVenta> entidades) {
    return entidades.map(new FuncionDetalleDeVentaRenglon(getRequest().get()));
  }
  @Override
  protected List<Renglon> consulta() {
    final Sesion sesion = getSesion().get();
    final Key usuarioKey = getKey(Usuario.class, sesion.getId());
    final Cliente cliente =
        busca(usuarioKey, Cliente.class, sesion.getId()).get();
    Venta venta = busca(Venta.class, cliente.getVentaActual()).get();
    final ModeloFormCarrito modeloForm = getModeloForm().get();
    modeloForm
        .setFechaHoraDeEntrega(format(getFormatoFechaHora(getTimeZone()),
            venta.getFechaHoraDeEntrega()));
    modeloForm.setDireccionDeEntrega(venta.getDireccionDeEntrega());
    modeloForm.setTotal(format(getFormatoPrecio(), venta.total()));
    return stream(venta.getDetalles())
        .map(new Function<Key, DetalleDeVenta>() {
          @Override public DetalleDeVenta apply(Key k) {
            return busca(DetalleDeVenta.class, k).get();
          }
        }).map(new FuncionDetalleDeVentaRenglon(getRequest().get()))
        .collect(Collectors.<Renglon>toList());
  }
  @Override protected void llenaModelo() {
    final ModeloFormCarrito modeloForm = getModeloForm().get();
    getModelo().ifPresent(new Consumer<DetalleDeVenta>() {
      @Override public void accept(DetalleDeVenta modelo) {
        modelo.setCantidad(parseEntero(modeloForm.getCantidad(),
            "Formato incorrecto para cantidad."));
      }
    });
  }
  @Override protected void muestraModelo() {
    final ModeloFormCarrito modeloForm = getModeloForm().get();
    getModelo().ifPresent(new Consumer<DetalleDeVenta>() {
      @Override public void accept(DetalleDeVenta modelo) {
        final Producto p = busca(Producto.class, modelo.getProducto()).get();
        modeloForm.setImg(getArchivoUrl(getRequest().get(),
            busca(Archivo.class, p.getImagen()).get()));
        modeloForm.setNombre(p.getNombre());
        modeloForm
            .setCantidad(format(getFormatoEntero(), modelo.getCantidad()));
        modeloForm.setPrecio(
            format(getFormatoPrecio(), newBigDecimal(modelo.getPrecio())));
        final Restaurante categoria =
            busca(Restaurante.class, p.getCategoria()).get();
        modeloForm.setCategoria(categoria.getNombre());
      }
    });
  }
  private static TimeZone getTimeZone() {
    return TimeZone.getTimeZone("America/Mexico_City");
  }
}
