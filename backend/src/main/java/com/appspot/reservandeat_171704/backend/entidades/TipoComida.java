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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class TipoComida {

    private Long key;
    @NotNull
    @Size(min = 1, max = 255)
    private String nombre;
    private String upperNombre;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUpperNombre() {
        return upperNombre;
    }

    public void setUpperNombre(String upperNombre) {
        this.upperNombre = upperNombre;
    }
}
