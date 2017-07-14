package com.appspot.reservandeat_171704.backend;

import com.appspot.reservandeat_171704.backend.entidades.TipoComida;

import net.reservandeat_171704.si.Renglon;

import java8.util.function.Function;

import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static net.reservandeat_171704.datastore.Datastore.getKey;

public class FuncionTipoComidaRenglon
        implements Function<TipoComida, Renglon> {

    @Override
    public Renglon apply(TipoComida modelo) {
        return new Renglon(keyToString(getKey(modelo)), modelo.getNombre());
    }
}
