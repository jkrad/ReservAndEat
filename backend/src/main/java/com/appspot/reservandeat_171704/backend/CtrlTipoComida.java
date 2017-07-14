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
package com.appspot.reservandeat_171704.backend;

import com.appspot.reservandeat_171704.ModeloFormTipoComida;
import com.appspot.reservandeat_171704.backend.entidades.TipoComida;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import net.reservandeat_171704.base.UtilBase;
import net.reservandeat_171704.datastore.Consulta;
import net.reservandeat_171704.datastore.Datastore;
import net.reservandeat_171704.si.Renglon;
import net.reservandeat_171704.si.backend.CtrlAbcHttp;

import java8.util.function.Consumer;
import java8.util.stream.Stream;

import static com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY;
import static com.google.appengine.api.datastore.Query.CompositeFilterOperator.and;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static com.google.appengine.api.datastore.Query.FilterOperator.NOT_EQUAL;
import static net.reservandeat_171704.base.UtilBase.isTrue;
import static net.reservandeat_171704.base.UtilBase.texto;
import static net.reservandeat_171704.datastore.Datastore.getKey;
import static org.bouncycastle.util.Strings.toUpperCase;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class CtrlTipoComida
        extends CtrlAbcHttp<ModeloFormTipoComida, TipoComida> {

    public CtrlTipoComida() {
        super("TipoComida Nueva", new Datastore<TipoComida>() {
        });
    }

    @Override
    protected String getTitulo() {
        return getModelo().get().getNombre();
    }

    @Override
    protected Stream<Renglon> getRenglones(Stream<TipoComida> entidades) {
        return entidades.map(new FuncionTipoComidaRenglon());
    }

    @Override
    protected void llenaModelo() {
        final ModeloFormTipoComida modeloForm = getModeloForm().get();
        getModelo().ifPresent(new Consumer<TipoComida>() {
            @Override
            public void accept(TipoComida modelo) {
                validaModeloForm(modeloForm, modelo);
                modelo.setNombre(modeloForm.getNombre());
                modelo.setUpperNombre(UtilBase.toUpperCase(modelo.getNombre()));
            }
        });
    }

    @Override
    protected void muestraModelo() {
        getModelo().ifPresent(new Consumer<TipoComida>() {
            @Override
            public void accept(TipoComida modelo) {
                getModeloForm().get().setNombre(texto(modelo.getNombre()));
            }
        });
    }

    private void validaModeloForm(ModeloFormTipoComida modeloForm,
            TipoComida modelo) {
        if (isTrue(modeloForm.getNuevo())) {
            if (new Consulta<TipoComida>() {
            }.setFilter(
                    new FilterPredicate("upperNombre", EQUAL,
                            toUpperCase(texto(modeloForm.getNombre()))))
                    .lista().count() > 0) {
                throw new RuntimeException(
                        "El nombre de la comida ya está asignado.");
            }
        } else {
            if (new Consulta<TipoComida>() {
            }.setFilter(
                    and(new FilterPredicate("upperNombre", EQUAL,
                            toUpperCase(modeloForm.getNombre())),
                            new FilterPredicate(KEY_RESERVED_PROPERTY, NOT_EQUAL,
                                    getKey(modelo))))
                    .addSort(KEY_RESERVED_PROPERTY).lista().count() > 0) {
                throw new RuntimeException(
                        "El nombre de la comida ya está asignado.");
            }
        }
    }
}
