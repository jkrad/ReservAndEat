package com.appspot.reservandeat_171704.backend;

import com.appspot.reservandeat_171704.ModeloFormColonia;

import com.google.appengine.api.datastore.Query;

import net.ramptors.base.UtilBase;
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
import com.appspot.reservandeat_171704.backend.entidades.Colonia;

/**
 * Created by shadow on 25/06/2017.
 */

public class CtrlColonia extends CtrlAbcHttp<ModeloFormColonia, Colonia> {


    public CtrlColonia() {
        super("Colonia Nueva", new Datastore<Colonia>() {
        });
    }

    @Override
    protected String getTitulo() {
        return getModelo().get().getNombre();
    }

    @Override
    protected Stream<Renglon> getRenglones(Stream<Colonia> entidades) {
        return entidades.map(new FuncionColoniaRenglon());
    }



    @Override
    protected void llenaModelo() {
        final ModeloFormColonia modeloForm = getModeloForm().get();
        getModelo().ifPresent(new Consumer<Colonia>() {
            @Override
            public void accept(Colonia modelo) {
                validaModeloForm(modeloForm, modelo);
                modelo.setNombre(modeloForm.getNombre());
                modelo.setUpperNombre(UtilBase.toUpperCase(modelo.getNombre()));
            }
        });
    }

    @Override
    protected void muestraModelo() {
        getModelo().ifPresent(new Consumer<Colonia>() {
            @Override
            public void accept(Colonia modelo) {
                getModeloForm().get().setNombre(texto(modelo.getNombre()));
            }
        });
    }

    private void validaModeloForm(ModeloFormColonia modeloForm,
                                  Colonia modelo) {
        if (isTrue(modeloForm.getNuevo())) {
            if (new Consulta<Colonia>() {
            }.setFilter(
                    new Query.FilterPredicate("upperNombre", EQUAL,
                            toUpperCase(texto(modeloForm.getNombre()))))
                    .lista().count() > 0) {
                throw new RuntimeException(
                        "El nombre de la colonia ya está asignado.");
            }
        } else {
            if (new Consulta<Colonia>() {
            }.setFilter(
                    and(new Query.FilterPredicate("upperNombre", EQUAL,
                                    toUpperCase(modeloForm.getNombre())),
                            new Query.FilterPredicate(KEY_RESERVED_PROPERTY, NOT_EQUAL,
                                    getKey(modelo))))
                    .addSort(KEY_RESERVED_PROPERTY).lista().count() > 0) {
                throw new RuntimeException(
                        "El nombre de la colonia ya está asignado.");
            }
        }
    }

}