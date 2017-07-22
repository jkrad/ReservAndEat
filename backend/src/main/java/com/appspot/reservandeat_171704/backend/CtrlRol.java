package com.appspot.reservandeat_171704.backend;


import com.appspot.reservandeat_171704.ModeloFormRol;

import com.appspot.reservandeat_171704.backend.entidades.Rol;
import com.google.appengine.api.datastore.Query;


import net.ramptors.datastore.Consulta;
import net.ramptors.datastore.Datastore;
import net.ramptors.si.Renglon;
import net.ramptors.si.backend.CtrlAbcHttp;

import java8.util.function.Consumer;
import java8.util.stream.Stream;

import static com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY;
import static com.google.appengine.api.datastore.Query.CompositeFilterOperator.and;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static com.google.appengine.api.datastore.Query.FilterOperator.NOT_EQUAL;
import static net.ramptors.base.UtilBase.isTrue;
import static net.ramptors.base.UtilBase.texto;
import static net.ramptors.datastore.Datastore.getKey;
import static org.bouncycastle.util.Strings.toUpperCase;

/**
 * Created by shadow on 26/06/2017.
 */

public class CtrlRol extends CtrlAbcHttp<ModeloFormRol, Rol> {


    public CtrlRol() {
        super("Nuevo rol", new Datastore<Rol>() {
        });
    }

    @Override
    protected String getTitulo() {
        return getModelo().get().getDescripcion();
    }

    @Override
    protected Stream<Renglon> getRenglones(Stream<Rol> entidades) {
        return entidades.map(new FuncionRolRenglon());
    }



    @Override
    protected void llenaModelo() {
        final ModeloFormRol modeloForm = getModeloForm().get();
        getModelo().ifPresent(new Consumer<Rol>() {
            @Override
            public void accept(Rol modelo) {
                validaModeloForm(modeloForm, modelo);
                modelo.setKey(modeloForm.getRol());
                modelo.setDescripcion(modeloForm.getDescripcion());

            }
        });
    }

    @Override
    protected void muestraModelo() {
        getModelo().ifPresent(new Consumer<Rol>() {
            @Override
            public void accept(Rol modelo) {
                getModeloForm().get().setRol(texto(modelo.getKey()));
                getModeloForm().get().setDescripcion(texto(modelo.getDescripcion()));
            }
        });
    }

    private void validaModeloForm(ModeloFormRol modeloForm,
                                  Rol modelo) {
        if (isTrue(modeloForm.getNuevo())) {
            if (new Consulta<Rol>() {
            }.setFilter(
                    new Query.FilterPredicate("upperNombre", EQUAL,
                            toUpperCase(texto(modeloForm.getDescripcion()))))
                    .lista().count() > 0) {
                throw new RuntimeException(
                        "El rol ya está asignado.");
            }
        } else {
            if (new Consulta<Rol>() {
            }.setFilter(
                    and(new Query.FilterPredicate("upperNombre", EQUAL,
                                    toUpperCase(modeloForm.getDescripcion())),
                            new Query.FilterPredicate(KEY_RESERVED_PROPERTY, NOT_EQUAL,
                                    getKey(modelo))))
                    .addSort(KEY_RESERVED_PROPERTY).lista().count() > 0) {
                throw new RuntimeException(
                        "El rol ya está asignado.");
            }
        }
    }

}