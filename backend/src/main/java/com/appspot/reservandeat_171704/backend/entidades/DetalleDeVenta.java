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

import net.reservandeat_171704.datastore.Datastore;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static net.reservandeat_171704.base.UtilBase.isPresent;
import static net.reservandeat_171704.base.UtilBase.newBigDecimal;

/**
 * Su parent es de tipo Venta
 *
 * @author Gilberto Pacheco Gallegos
 */
public class DetalleDeVenta {

    @NotNull
    private Key parent;
    private Long key;
    @NotNull
    @Min(1)
    private Long cantidad;
    @NotNull
    @Digits(integer = 6, fraction = 2)
    private String precio;
    @NotNull
    private Key producto;

    @SuppressWarnings("unused")
    public DetalleDeVenta() {
    }

    public DetalleDeVenta(Venta parent, Producto producto) {
        this.parent = Datastore.getKey(parent);
        this.producto = Datastore.getKey(producto);
        cantidad = 1L;
        precio = producto.getPrecio();
    }

    public Key getParent() {
        return parent;
    }

    public void setParent(Key parent) {
        this.parent = parent;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Key getProducto() {
        return producto;
    }

    public void setProducto(Key producto) {
        this.producto = producto;
    }

    public BigDecimal subtotal() {
        final BigDecimal pr = newBigDecimal(getPrecio());
        return isPresent(pr) ? pr.multiply(new BigDecimal(getCantidad()))
                : BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
    }
}
