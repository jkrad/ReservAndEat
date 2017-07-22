package com.appspot.reservandeat_171704.backend.muchos_a_muchos;

import com.appspot.reservandeat_171704.backend.entidades.Usuario;

import net.ramptors.si.Renglon;

import java8.util.function.Function;

import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static net.ramptors.datastore.Datastore.getKey;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class FuncionUsuarioRenglon implements Function<Usuario, Renglon> {

    @Override
    public Renglon apply(Usuario modelo) {
        return new Renglon(keyToString(getKey(modelo)), modelo.getKey(),
                modelo.getNombre());
    }
}
