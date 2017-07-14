package com.appspot.reservandeat_171704.backend;

import com.appspot.reservandeat_171704.backend.entidades.Rol;

import net.reservandeat_171704.si.Renglon;

import java8.util.function.Function;

import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static net.reservandeat_171704.datastore.Datastore.getKey;

/**
 * Created by shadow on 26/06/2017.
 */
public class FuncionRolRenglon implements Function<Rol, Renglon> {

    @Override
    public Renglon apply(Rol modelo) {
        return new Renglon(keyToString(getKey(modelo)), modelo.getDescripcion());
    }
}
