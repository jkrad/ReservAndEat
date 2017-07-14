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
package com.appspot.reservandeat_171704.backend.reportes;

import com.appspot.reservandeat_171704.backend.entidades.DetalleDeVenta;
import com.appspot.reservandeat_171704.backend.entidades.Producto;
import com.appspot.reservandeat_171704.backend.entidades.Venta;
import com.appspot.reservandeat_171704.reportes.ModeloFormReporte;
import com.appspot.reservandeat_171704.reportes.RegistroVenta;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import net.reservandeat_171704.base.BigDecimalAdd;
import net.reservandeat_171704.datastore.Consulta;
import net.reservandeat_171704.si.CtrlBase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import java8.util.Optional;
import java8.util.function.BinaryOperator;
import java8.util.function.Consumer;
import java8.util.function.Function;
import java8.util.function.Predicate;
import java8.util.function.Supplier;
import java8.util.stream.Collectors;
import java8.util.stream.RefStreams;
import java8.util.stream.Stream;

import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.ADMINISTRADOR;
import static com.google.appengine.api.datastore.Query
    .CompositeFilterOperator.and;
import static com.google.appengine.api.datastore.Query.FilterOperator
    .GREATER_THAN_OR_EQUAL;
import static com.google.appengine.api.datastore.Query.FilterOperator
    .LESS_THAN_OR_EQUAL;
import static com.google.appengine.api.datastore.Query.FilterOperator.NOT_EQUAL;
import static java8.util.stream.StreamSupport.stream;
import static net.reservandeat_171704.base.UtilFormatos.format;
import static net.reservandeat_171704.base.UtilFormatos.getFormatoPrecio;
import static net.reservandeat_171704.datastore.Datastore.busca;

/** @author Gilberto Pacheco Gallegos */
public class CtrlReporte extends CtrlBase<ModeloFormReporte> {
  public CtrlReporte() {
    super(ADMINISTRADOR);
  }
  @Override
  protected void procesa() {
    final Date día = new Date();
    final Calendar c1 = creaCalendar();
    c1.setTime(día);
    c1.set(Calendar.HOUR_OF_DAY, 0);
    c1.set(Calendar.MINUTE, 0);
    c1.set(Calendar.SECOND, 0);
    c1.set(Calendar.MILLISECOND, 0);
    final Calendar c2 = creaCalendar();
    c2.setTime(día);
    c2.set(Calendar.HOUR_OF_DAY, 23);
    c2.set(Calendar.MINUTE, 59);
    c2.set(Calendar.SECOND, 59);
    c2.set(Calendar.MILLISECOND, 999);
    final Long fecha1 = c1.getTimeInMillis();
    final Long fecha2 = c2.getTimeInMillis();
    final DecimalFormat fmt = getFormatoPrecio();
    final LinkedHashMap<String, BigDecimal> map = new LinkedHashMap<>();
    new Consulta<Venta>() {}.setFilter(
        and(new FilterPredicate("registro", NOT_EQUAL, null),
            new FilterPredicate("registro", GREATER_THAN_OR_EQUAL, fecha1),
            new FilterPredicate("registro", LESS_THAN_OR_EQUAL, fecha2)))
        .addSort("registro")
        .lista()
        .map(new Function<Venta, Stream<Key>>() {
          @Override public Stream<Key> apply(Venta venta) {
            return stream(venta.getDetalles());
          }
        })
        .reduce(RefStreams.<Key>empty(),
            new BinaryOperator<Stream<Key>>() {
              @Override
              public Stream<Key> apply(Stream<Key> s1, Stream<Key> s2) {
                return RefStreams.concat(s1, s2);
              }
            })
        .map(new Function<Key, Optional<DetalleDeVenta>>() {
          @Override public Optional<DetalleDeVenta> apply(Key key) {
            return busca(DetalleDeVenta.class, key);
          }
        })
        .filter(new Predicate<Optional<DetalleDeVenta>>() {
          @Override
          public boolean test(Optional<DetalleDeVenta> opt) {
            return opt.isPresent();
          }
        })
        .map(new Function<Optional<DetalleDeVenta>, DetalleDeVenta>() {
          @Override
          public DetalleDeVenta apply(Optional<DetalleDeVenta> opt) {
            return opt.get();
          }
        })
        .forEach(new Consumer<DetalleDeVenta>() {
          @Override public void accept(final DetalleDeVenta d) {
            busca(Producto.class, d.getProducto()).ifPresent(
                new Consumer<Producto>() {
                  @Override public void accept(Producto p) {
                    final String nombre = p.getNombre();
                    BigDecimal total =
                        Optional.ofNullable(map.get(nombre)).orElseGet(
                            new Supplier<BigDecimal>() {
                              @Override public BigDecimal get() {
                                return BigDecimal.ZERO
                                    .setScale(2, RoundingMode.DOWN);
                              }
                            });
                    total = total.add(d.subtotal());
                    map.put(nombre, total);
                  }
                });
          }
        });
    final List<RegistroVenta> lista = stream(map.entrySet())
        .sorted(new Comparator<Map.Entry<String, BigDecimal>>() {
          @Override public int compare(Map.Entry<String, BigDecimal> e1,
              Map.Entry<String, BigDecimal> e2) {
            return e2.getValue().compareTo(e1.getValue());
          }
        })
        .map(new Function<Map.Entry<String, BigDecimal>, RegistroVenta>() {
          @Override
          public RegistroVenta apply(Map.Entry<String, BigDecimal> entry) {
            return new RegistroVenta(entry.getKey(),
                format(fmt, entry.getValue()));
          }
        })
        .collect(Collectors.<RegistroVenta>toList());
    final BigDecimal granTotal = stream(map.entrySet())
        .map(new Function<Map.Entry<String, BigDecimal>, BigDecimal>() {
          @Override
          public BigDecimal apply(Map.Entry<String, BigDecimal> entry) {
            return entry.getValue();
          }
        })
        .reduce(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN),
            new BigDecimalAdd());
    final ModeloFormReporte modeloForm = getModeloForm().get();
    modeloForm.setLista(lista);
    modeloForm.setTotal(format(fmt, granTotal));
  }
  private Calendar creaCalendar() {
    return Calendar.getInstance(getTimeZone(), getLocale());
  }
  private TimeZone getTimeZone() {
    return TimeZone.getTimeZone("America/Mexico_City");
  }
  private Locale getLocale() {
    return new Locale("es-MX");
  }
}
