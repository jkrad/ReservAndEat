package com.appspot.reservandeat_171704.backend.muchos_a_uno;

import com.appspot.reservandeat_171704.backend.FuncionTipoComidaRenglon;
import com.appspot.reservandeat_171704.backend.entidades.TipoComida;
import com.appspot.reservandeat_171704.backend.entidades.Restaurante;
import com.appspot.reservandeat_171704.backend.mensajeria.Mensajeria;
import com.appspot.reservandeat_171704.muchos_a_uno.ModeloFormRestaurantes;

import net.reservandeat_171704.datastore.Consulta;
import net.reservandeat_171704.datastore.Datastore;
import net.reservandeat_171704.si.Renglon;
import net.reservandeat_171704.si.backend.CtrlAbcHttp;

import java.util.HashMap;
import java.util.Map;

import java8.util.function.Consumer;
import java8.util.stream.Collectors;
import java8.util.stream.Stream;

import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.GERENTE;
import static net.reservandeat_171704.base.UtilBase.texto;

import static net.reservandeat_171704.datastore.Datastore.adaptaKey;
import static net.reservandeat_171704.datastore.Datastore.adaptaString;

public class CtrlRestaurantes
        extends CtrlAbcHttp<ModeloFormRestaurantes, Restaurante> {

    public CtrlRestaurantes() {
        super("Restaurante Nuevo", new Datastore<Restaurante>() {
        }, GERENTE);
    }

    @Override
    protected String getTitulo() {
        return getModelo().get().getNombre();
    }

    @Override
    protected Stream<Renglon> getRenglones(Stream<Restaurante> entidades) {
        return entidades.map(new FuncionRestauranteRenglon(getRequest().get()));
    }

    @Override
    protected void llenaModelo() {
        final ModeloFormRestaurantes modeloForm = getModeloForm().get();
        getModelo().ifPresent(new Consumer<Restaurante>() {
            @Override
            public void accept(Restaurante modelo) {
                try {
                    modelo.setTipoComida(adaptaString(modeloForm.getTipoComida()));
                    modelo.setNombre(modeloForm.getNombre());
                } finally {
                    // Esto es para que no descarte la imagen capturada cuando hay error.
                    //modeloForm.setImagen(null);
                }
            }
        });
    }

    @Override
    protected void muestraModelo() {
        final ModeloFormRestaurantes modeloForm = getModeloForm().get();
        getModelo().ifPresent(new Consumer<Restaurante>() {
            @Override
            public void accept(Restaurante modelo) {

                modeloForm.setNombre(texto(modelo.getNombre()));
                modeloForm.setTipoComida(adaptaKey(modelo.getTipoComida()));
            }
        });
    }

    @Override
    protected void muestraOpciones() {
        final ModeloFormRestaurantes modeloForm = getModeloForm().get();
        modeloForm.setTipoComidaOpciones(
                new Consulta<TipoComida>() {
                }.lista().map(new FuncionTipoComidaRenglon())
                        .collect(Collectors.<Renglon>toList()));
    }

    @Override
    protected void agregaModelo() {
        super.agregaModelo();
        final Map<String, String> data = new HashMap<>();
        data.put("noticia", "Hay nuevos restaurantes.");
        Mensajeria.notifica(data);

    }

}
