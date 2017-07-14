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

import com.google.appengine.api.datastore.Key;

import net.reservandeat_171704.base.BigDecimalAdd;
import net.reservandeat_171704.datastore.Datastore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import java8.util.Optional;
import java8.util.function.Function;
import java8.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static java8.util.stream.StreamSupport.stream;

/** @author Gilberto Pacheco Gallegos */
@SuppressWarnings("unused") public class Venta {
  private Key parent; // Es el cliente
  private Long key;
  private Long registro;
  @Future
  private Date fechaHoraDeEntrega;
  @Size(min = 1, max = 255)
  private String direccionDeEntrega;
  private List<Key> detalles = new ArrayList<>();
  public Venta() {}
  public Venta(Key parent) {
    this.parent = parent;
  }
  public Long getKey() {
    return key;
  }
  public void setKey(Long key) {
    this.key = key;
  }
  public Long getRegistro() {
    return registro;
  }
  public void setRegistro(Long registro) {
    this.registro = registro;
  }
  public Date getFechaHoraDeEntrega() {
    return fechaHoraDeEntrega;
  }
  public void setFechaHoraDeEntrega(Date fechaHoraDeEntrega) {
    this.fechaHoraDeEntrega = fechaHoraDeEntrega;
  }
  public String getDireccionDeEntrega() {
    return direccionDeEntrega;
  }
  public void setDireccionDeEntrega(String direccionDeEntrega) {
    this.direccionDeEntrega = direccionDeEntrega;
  }
  public List<Key> getDetalles() {
    return detalles;
  }
  public void setDetalles(List<Key> detalles) {
    this.detalles = detalles;
  }
  public Key getParent() {
    return parent;
  }
  public void setParent(Key parent) {
    this.parent = parent;
  }
  public Optional<DetalleDeVenta> busca(final Producto p) {
    final Key productoKey = Datastore.getKey(p);
    return stream(getDetalles())
        .map(new Function<Key, DetalleDeVenta>() {
          @Override public DetalleDeVenta apply(Key k) {
            return Datastore.busca(DetalleDeVenta.class, k).get();
          }
        }).filter(new Predicate<DetalleDeVenta>() {
          @Override public boolean test(DetalleDeVenta d) {
            return Objects.equals(productoKey, d.getProducto());
          }
        }).findFirst();
  }
  public boolean elimina(Key productoKey) {
    for (final ListIterator<Key> it = getDetalles().listIterator();
        it.hasNext(); ) {
      final Key detalleKey = it.next();
      final Optional<DetalleDeVenta> d =
          Datastore.busca(DetalleDeVenta.class, detalleKey);
      if (d.isPresent() && Objects.equals(productoKey, d.get().getProducto())) {
        it.remove();
        return true;
      }
    }
    return false;
  }
  public BigDecimal total() {
    return stream(getDetalles())
        .map(new Function<Key, Optional<DetalleDeVenta>>() {
          @Override public Optional<DetalleDeVenta> apply(Key k) {
            return Datastore.busca(DetalleDeVenta.class, k);
          }
        }).filter(new Predicate<Optional<DetalleDeVenta>>() {
          @Override public boolean test(Optional<DetalleDeVenta> opt) {
            return opt.isPresent();
          }
        }).map(new Function<Optional<DetalleDeVenta>, DetalleDeVenta>() {
          @Override public DetalleDeVenta apply(Optional<DetalleDeVenta> opt) {
            return opt.get();
          }
        })
        .map(new Function<DetalleDeVenta, BigDecimal>() {
          @Override public BigDecimal apply(DetalleDeVenta d) {
            requireNonNull(d, "DetalleDeVenta no encontrado.");
            return d.subtotal();
          }
        }).reduce(BigDecimal.ZERO.setScale(2, RoundingMode.UP),
            new BigDecimalAdd());
  }
}
