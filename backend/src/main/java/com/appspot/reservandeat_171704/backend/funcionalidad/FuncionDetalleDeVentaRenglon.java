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
import com.appspot.reservandeat_171704.backend.entidades.DetalleDeVenta;
import com.appspot.reservandeat_171704.backend.entidades.Producto;

import net.reservandeat_171704.si.Renglon;

import java.math.BigDecimal;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import java8.util.function.Function;

import static com.appspot.reservandeat_171704.backend.multimedia.UtilMultimedia
    .getMiniaturaUrl;
import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static net.reservandeat_171704.datastore.Datastore.busca;
import static net.reservandeat_171704.datastore.Datastore.getKey;

/** @author Gilberto Pacheco Gallegos */
class FuncionDetalleDeVentaRenglon
    implements Function<DetalleDeVenta, Renglon> {
  private final HttpServletRequest req;
  FuncionDetalleDeVentaRenglon(HttpServletRequest req) {
    this.req = req;
  }
  @Override @SuppressWarnings("MalformedFormatString")
  public Renglon apply(DetalleDeVenta modelo) {
    final Producto producto = busca(Producto.class, modelo.getProducto()).get();
    return new Renglon(keyToString(getKey(modelo)),
        getMiniaturaUrl(req, busca(Archivo.class, producto.getImagen()).get()),
        producto.getNombre(), MessageFormat.format(
        "{0,number,#,##0.00} a ${1,number,#,##0.00} = ${2,number,#,##0.00}",
        modelo.getCantidad(), new BigDecimal(modelo.getPrecio()),
        modelo.subtotal()));
  }
}
