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
"use strict";
(function () {
    const url = "infoSincronizacion";
    window.addEventListener("sincronizacionTerminada", () => {
        window.fetch(url, {method: "GET", credentials: "include"}).
                then(respuesta => {
                    if (respuesta.ok) {
                        return respuesta.json();
                    } else {
                        throw new Error(respuesta.statusText);
                    }
                })
                .then(lista => window.infoAvisoListo
                            .then(info => info.reemplaza(lista || []))
                            .then(() => window.dispatchEvent(new Event("sincronizado")))
                )
                .catch(window.muestraExcepcion);
    });
    window.addEventListener("sincronizar", () => {
        window.infoAvisoListo
                .then(info => info.consultaTodos())
                .then(lista => window.fetch(url, {method: "POST",
                        credentials: "include", body: JSON.stringify(lista)}))
                .then(respuesta => {
                    if (!respuesta.ok) {
                        throw new Error(respuesta.statusText);
                    }
                })
                .catch(window.muestraExcepcion);
    });
})();