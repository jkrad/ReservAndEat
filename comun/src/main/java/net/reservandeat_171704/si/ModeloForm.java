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
package net.reservandeat_171704.si;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class ModeloForm {

    private String accion;
    private String detalleId;
    private String alFrente;
    private String siguienteForm;
    private String error;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(String detalleId) {
        this.detalleId = detalleId;
    }

    public String getAlFrente() {
        return alFrente;
    }

    public void setAlFrente(String alFrente) {
        this.alFrente = alFrente;
    }

    public String getSiguienteForm() {
        return siguienteForm;
    }

    public void setSiguienteForm(String siguienteForm) {
        this.siguienteForm = siguienteForm;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
