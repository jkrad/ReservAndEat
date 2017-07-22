package com.appspot.reservandeat_171704.backend.muchos_a_muchos;

import com.appspot.reservandeat_171704.backend.entidades.Rol;

import net.ramptors.si.Renglon;

import java8.util.function.Function;

import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static net.ramptors.datastore.Datastore.getKey;

/**
 * @author Gilberto Pacheco Gallegos
 */
class FuncionRolRenglon implements Function<Rol, Renglon> {

    @Override
    public Renglon apply(Rol modelo) {
        return new Renglon(keyToString(getKey(modelo)), modelo.getKey(),
                modelo.getDescripcion());
    }
}
