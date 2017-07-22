package com.appspot.reservandeat_171704.backend;

import com.appspot.reservandeat_171704.backend.entidades.Colonia;


import net.ramptors.si.Renglon;

import java8.util.function.Function;

import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static net.ramptors.datastore.Datastore.getKey;

/**
 * Created by shadow on 25/06/2017.
 */

public class FuncionColoniaRenglon implements Function<Colonia, Renglon> {
    @Override public Renglon apply(Colonia modelo) {
        return new Renglon(keyToString(getKey(modelo)), modelo.getNombre());
    }
}