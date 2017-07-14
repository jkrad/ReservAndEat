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

import java.util.List;

import java8.util.Optional;
import java8.util.function.Consumer;
import java8.util.function.Supplier;
import java8.util.stream.Collectors;
import java8.util.stream.Stream;

import static net.reservandeat_171704.base.UtilBase.isPresent;
import static net.reservandeat_171704.base.UtilBase.isTrue;
import static net.reservandeat_171704.base.UtilObjetos.getParametroGenerico;

/**
 * @author Gilberto Pacheco Gallegos
 */
public abstract class CtrlAbc<MF extends ModeloFormAbc, E>
        extends CtrlBase<MF> {

    private final String tituloDeNuevo;
    private final Info<E> info;
    private final Class<E> tipoEntidad = getParametroGenerico(this, 1);
    private Optional<E> modelo = Optional.empty();

    protected abstract String getTitulo();

    protected abstract Stream<Renglon> getRenglones(Stream<E> entidades);

    protected abstract void llenaModelo();

    protected abstract void muestraModelo();

    public CtrlAbc(String tituloDeNuevo, Info<E> info,
            String... rolesPermitidos) {
        super(rolesPermitidos);
        this.tituloDeNuevo = tituloDeNuevo;
        this.info = info;
    }

    public Info<E> getInfo() {
        return info;
    }

    protected void muestraOpciones() {
    }

    protected void autorizaBorrado() {
    }

    public Optional<E> getModelo() {
        return modelo;
    }

    protected void procesa() {
        getModeloForm().ifPresent(new Consumer<MF>() {
            @Override
            public void accept(MF mf) {
                final String accion = mf.getAccion();
                if (isPresent(accion)) {
                    switch (accion) {
                        case "inicia":
                        case "cerrar":
                            inicia();
                            break;
                        case "agregar":
                            agrega();
                            break;
                        case "detalle":
                            detalle();
                            break;
                        case "submit":
                            guarda();
                            break;
                        case "eliminar":
                            elimina();
                            break;
                    }
                }
            }
        });
    }

    protected void inicia() {
        getModeloForm().ifPresent(new Consumer<MF>() {
            @Override
            public void accept(MF mf) {
                mf.setAlFrente("maestro");
                mf.setLista(consulta());
            }
        });
    }

    private void agrega() {
        configuraDetalle("true");
        muestraOpciones();
        modelo = Optional.of(creaModelo());
        getModeloForm().get().setDetalleTitulo(tituloDeNuevo);
        muestraModelo();
    }

    protected void detalle() {
        configuraDetalle("false");
        muestraOpciones();
        modelo = Optional.of(get(getModeloForm().get()));
        setDetalleTitulo();
        muestraModelo();
    }

    private void guarda() {
        final MF mf = getModeloForm().get();
        if ("detalle".equals(mf.getAlFrente())) {
            muestraOpciones();
            if (isTrue(mf.getNuevo())) {
                modelo = Optional.of(creaModelo());
                setDetalleTitulo();
                llenaModelo();
                agregaModelo();
            } else {
                modelo = Optional.of(get(mf));
                setDetalleTitulo();
                llenaModelo();
                modificaModelo();
            }
        }
        inicia();
    }

    protected void elimina() {
        muestraOpciones();
        final String id = getModeloForm().get().getDetalleId();
        modelo = getInfo().busca(id);
        if (modelo.isPresent()) {
            setDetalleTitulo();
            autorizaBorrado();
            eliminaModelo();
        }
        inicia();
    }

    @SuppressWarnings("WeakerAccess")
    protected void modificaModelo() {
        getInfo().modifica(modelo.get());
    }

    protected void agregaModelo() {
        getInfo().agrega(modelo.get());
    }

    protected void eliminaModelo() {
        getInfo().elimina(modelo.get());
    }

    private void setDetalleTitulo() {
        final MF mf = getModeloForm().get();
        mf.setDetalleTitulo(
                isTrue(mf.getNuevo()) || !getModelo().isPresent() ? tituloDeNuevo
                : getTitulo());
    }

    protected List<Renglon> consulta() {
        return getRenglones(getInfo().consulta())
                .collect(Collectors.<Renglon>toList());
    }

    protected E get(MF modeloForm) {
        final String id = modeloForm.getDetalleId();
        return getInfo().<E>busca(id).orElseThrow(
                new Supplier<NullPointerException>() {
            @Override
            public NullPointerException get() {
                return new NullPointerException(
                        "No se encuentra el registro solicitado.");
            }
        });
    }

    private void configuraDetalle(String nuevo) {
        final MF mf = getModeloForm().get();
        mf.setNuevo(nuevo);
        mf.setEliminarMuestra(isTrue(nuevo) ? "false" : "true");
        mf.setAlFrente("detalle");
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private E creaModelo() {
        try {
            return tipoEntidad.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
