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

import net.ramptors.base.Property;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java8.util.Optional;
import java8.util.function.Consumer;
import java8.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Collections.disjoint;
import static net.ramptors.base.UtilBase.getMensaje;
import static net.ramptors.base.UtilBase.isNullOrEmpty;
import static net.ramptors.base.UtilBase.isPresent;
import static net.ramptors.base.UtilObjetos.getParametroGenerico;

/**
 * @param <MF> Tipo del ViewModel
 * @author Gilberto Pacheco Gallegos
 */
public abstract class CtrlBase<MF extends ModeloForm> {

    private final String[] rolesPermitidos;
    private final Class<MF> tipoModeloForm = getParametroGenerico(this, 0);
    private Optional<Sesion> sesion = Optional.empty();
    private Optional<MF> modeloForm = Optional.empty();

    protected abstract void procesa();

    protected CtrlBase(String... rolesPermitidos) {
        this.rolesPermitidos = rolesPermitidos;
    }

    public Class<MF> getTipoModeloForm() {
        return tipoModeloForm;
    }

    public Optional<MF> getModeloForm() {
        return modeloForm;
    }

    public Optional<Sesion> getSesion() {
        return sesion;
    }

    public void procesa(final Optional<Sesion> sesion, MF modeloForm) {
        try {
            this.modeloForm = Optional.of(modeloForm);
            autorizaAcceso(sesion);
            this.sesion = sesion;
            procesa();
        } catch (Exception e) {
            procesaError("Error procesando forma.", e);
        }
    }

    /**
     * Analiza una excepción para poder mostrar el error de error que lleva.
     * Normalmente cuendo un EJB detecta un error, lanza otras excepciones y
     * guarda la causa original. Esta última es necesaria para mostrar el error
     * que manda el manejador de base de datos.
     *
     * @param mensaje un error relacionado con el fallo.
     * @param e excepción que describe un error.
     */
    public void procesaError(String mensaje, Throwable e) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, mensaje, e);
        procesaError(e);
    }

    private void autorizaAcceso(final Optional<Sesion> sesion) {
        if (rolesPermitidos.length > 0) {
            if (!sesion.isPresent()
                    || disjoint(asList(rolesPermitidos), sesion.get().getRoles())) {
                throw new RuntimeException("Acceso no autorizado.");
            }
        }
    }

    private void procesaError(Throwable e) {
        while (isPresent(e.getCause())) {
            e = e.getCause();
        }
        if (e instanceof ConstraintViolationException) {
            final ConstraintViolationException ex = (ConstraintViolationException) e;
            muestraViolaciones(ex.getConstraintViolations());
        } else {
            final Throwable ex = e;
            getModeloForm().ifPresent(new Consumer<MF>() {
                @Override
                public void accept(MF mf) {
                    mf.setError(getMensaje(ex));
                }
            });
        }
    }

    /**
     * Toma las violaciones y las muestraViolaciones.
     *
     * @param violaciones conjunto de violaciones a restricciones de Bean
     * Validation.
     */
    private void muestraViolaciones(
            final Set<ConstraintViolation<?>> violaciones) {
        getModeloForm().ifPresent(new Consumer<MF>() {
            @Override
            public void accept(final MF mf) {
                mf.setError("Hay errores en los datos capturados.");
                StreamSupport.stream(violaciones).forEach(
                        new Consumer<ConstraintViolation<?>>() {
                    @Override
                    public void accept(ConstraintViolation<?> violacion) {
                        final String propiedad = violacion.getPropertyPath().toString();
                        final String mensaje = violacion.getMessage();
                        if (isNullOrEmpty(propiedad)) {
                            mf.setError(mensaje);
                        } else {
                            muestraError(propiedad, mensaje);
                        }
                    }
                });
            }
        });
    }

    /**
     * Muestra un mensaje de error de una violacion.
     *
     * @param propiedad propiedad de entity al que se asocia el mensaje.
     * @param mensaje versión corta del mensaje.
     */
    private void muestraError(String propiedad, String mensaje) {
        Property.set(getModeloForm().get(), propiedad + "Error", mensaje);
    }
}
