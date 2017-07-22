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
package net.ramptors.si;

import java.util.List;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings("unused")
public class ModeloFormAbc extends ModeloForm {

    private List<Renglon> lista;
    private String detalleTitulo;
    private String nuevo;
    private String eliminarMuestra;

    public List<Renglon> getLista() {
        return lista;
    }

    public void setLista(List<Renglon> lista) {
        this.lista = lista;
    }

    public String getDetalleTitulo() {
        return detalleTitulo;
    }

    public void setDetalleTitulo(String detalleTitulo) {
        this.detalleTitulo = detalleTitulo;
    }

    public String getNuevo() {
        return nuevo;
    }

    public void setNuevo(String nuevo) {
        this.nuevo = nuevo;
    }

    public String getEliminarMuestra() {
        return eliminarMuestra;
    }

    public void setEliminarMuestra(String eliminarMuestra) {
        this.eliminarMuestra = eliminarMuestra;
    }
}
