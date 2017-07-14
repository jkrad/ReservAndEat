/* Copyright 2017 Gilberto Pacheco Gallegos.
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
 * limitations under the License. */
"use strict";
class MedWeb {
    constructor(form, url) {
        Object.defineProperties(this, {
            form: {value: form},
            url: {value: url},
            paneles: {value: []},
            interruptores: {value: []},
            indicadores: {value: []},
            errores: {value: []},
            vistas: {value: {}}
        });
    }
    inicia() {
        this.form.addEventListener("accion",
                evt => this.ejecuta(evt.detail.accion, evt.detail.detalleId));
        this.procesaPaneles();
        this.ocultaIndicadores();
        this.ejecuta("inicia");
        this.ejecutaAlFrente();
        this.ejecutaInterruptores();
    }
    procesaPaneles() {
        this.procesaNodo(this.form);
        Array.prototype.forEach.call(this.form.children, child => {
            if (child.id) {
                this.paneles.push(child);
            }
            this.procesaHijo(child);
        });
    }
    procesaNodo(nodo) {
        if (nodo.tagName === "FORM") {
            nodo.addEventListener("submit", () => this.ejecuta("submit"));
        } else if (nodo.tagName === "LABEL") {
            if (nodo.querySelector(".valor")) {
                nodo.classList.add("focus");
            } else {
                nodo.addEventListener("focus", () => nodo.classList.add("focus"),
                        true);
                nodo.addEventListener("focusout",
                        () => nodo.classList.remove("focus"));
                this.configuraFlotante(nodo);
            }
            nodo.addEventListener("submit", () => this.ejecuta("submit"));
        } else if (nodo.tagName === "RMPT-INDICADOR") {
            this.indicadores.push(nodo);
        }
        if (nodo.id) {
            let id = nodo.id;
            this.agregaVista(id, nodo);
        }
        if (nodo.name) {
            let name = nodo.name;
            if (name === "alFrente") {
                Object.defineProperty(this, "alFrente", {value: nodo});
            }
            if (nodo.type === "button") {
                nodo.addEventListener("click", () => this.ejecuta(name));
            }
            this.agregaVista(name, nodo);
            if (name.endsWith("Error")) {
                this.errores.push(nodo);
            }
            if (nodo.name.endsWith("Muestra")) {
                this.interruptores.push(nodo);
            }
        }
    }
    procesaHijo(hijo) {
        this.procesaNodo(hijo);
        let children = hijo.children;
        if (children) {
            Array.prototype.forEach.call(children, child => {
                this.procesaHijo(child);
            });
        }
    }
    configuraFlotante(label) {
        let flotante = label.querySelector(".flotante");
        let texto = label.querySelector("input,textarea");
        if (flotante && texto) {
            let placeholder = flotante.textContent.trim();
            texto.addEventListener("change",
                    () => this.actualizaFlotante(texto, placeholder, flotante));
            texto.addEventListener("focus", () => {
                texto.placeholder = "";
                flotante.style.visibility = "visible";
            });
            texto.addEventListener("focusout",
                    () => this.actualizaFlotante(texto, placeholder, flotante));
            this.actualizaFlotante(texto, placeholder, flotante);
        }
    }
    actualizaFlotante(texto, placeholder, flotante) {
        texto.placeholder = placeholder;
        flotante.style.visibility = texto.value ? "visible" : "hidden";
    }
    agregaVista(id, nodo) {
        if (!this.vistas[id]) {
            this.vistas[id] = nodo;
        }
    }
    ejecutaAlFrente() {
        let alFrente = this.alFrente ? this.alFrente.value : null;
        if (alFrente) {
            this.paneles.forEach(p => setVisible(p, p.id === alFrente));
        }
    }
    ejecutaInterruptores() {
        this.interruptores.forEach(i => {
            let destinoId = eliminaSufijo(i.name, "Muestra");
            let visible = i.value === "true";
            let destino = this.vistas[destinoId];
            if (destino) {
                setVisible(destino, visible);
            }
        });
    }
    ejecuta(accion, detalleId) {
        this.limpiaErrores();
        this.vistas.accion.value = accion;
        if (detalleId) {
            this.vistas.detalleId.value = detalleId;
        }
        this.enviaAlControlador();
    }
    enviaAlControlador() {
        if (this.url) {
            this.muestraIndicadores();
            const formData = new FormData(this.form);
            this.llenaFormData(formData);
            window.fetch(this.url,
                    {method: "POST", credentials: "include", body: formData})
                    .then(respuesta => {
                        this.ocultaIndicadores();
                        if (respuesta.ok) {
                            return respuesta.json();
                        } else {
                            throw new Error(respuesta.statusText);
                        }
                    })
                    .then(modeloForm => this.procesaModeloForm(modeloForm))
                    .catch(e => {
                        this.ocultaIndicadores();
                        window.muestraExcepcion(e);
                    });
        }
    }
    ocultaIndicadores() {
        this.indicadores.forEach(i => setVisible(i, false));
    }
    muestraIndicadores() {
        this.indicadores.forEach(i => setVisible(i, true));
    }
    llenaFormData(formData) {}
    limpiaErrores() {
        this.errores.forEach(error => {
            error.value = "";
            error.dispatchEvent(new Event('change'));
        });
    }
    procesaModeloForm(modeloForm) {
        if (modeloForm.siguienteForm) {
            this.siguienteForm(modeloForm.siguienteForm);
        } else {
            this.muestraErrorModeloForm(modeloForm);
            this.muestraValores(modeloForm);
            this.ejecutaAlFrente();
            this.ejecutaInterruptores();
        }
    }
    siguienteForm(form) {
        location.hash = "#" + form;
        window.dispatchEvent(new Event("recarga"));
    }
    muestraErrorModeloForm(modeloForm) {
        let error = modeloForm.error;
        if (error) {
            muestraError(error);
        }
    }
    muestraValores(modeloForm) {
        for (let nombre in modeloForm) {
            switch (nombre) {
                case "siguienteForm":
                case "error":
                    continue;
                default :
                    if (nombre.endsWith("Opciones")) {
                        let destinoId = eliminaSufijo(nombre, "Opciones");
                        let destino = this.vistas[destinoId];
                        if (destino && "opciones" in destino) {
                            destino.opciones = modeloForm[nombre];
                        }
                    } else {
                        this.muestraValor(nombre, modeloForm[nombre]);
                    }
            }
        }
    }
    muestraValor(propiedad, valor) {
        let element = this.vistas[propiedad];
        if (element) {
            if ("value" in element) {
                if (element.tagName.toUpperCase() === "INPUT"
                        && element.type.toUpperCase() === "FILE") {
                    element.value = "";
                } else {
                    element.value = valor;
                }
            } else if ("src" in element) {
                element.src = valor;
            } else {
                element.textContent = valor;
            }
            element.dispatchEvent(new Event('change'));
        }
    }
}