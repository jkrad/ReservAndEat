package com.appspot.reservandeat_171704.backend.muchos_a_uno;

import com.appspot.reservandeat_171704.backend.entidades.Restaurante;
import net.ramptors.si.Renglon;
import javax.servlet.http.HttpServletRequest;
import java8.util.function.Function;

import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static net.ramptors.datastore.Datastore.getKey;

public class FuncionRestauranteRenglon implements Function<Restaurante, Renglon> {

    private final HttpServletRequest req;

    public FuncionRestauranteRenglon(HttpServletRequest req) {
        this.req = req;
    }

    @Override
    public Renglon apply(Restaurante modelo) {
        return new Renglon(keyToString(getKey(modelo)), modelo.getNombreRestaurante());
    }
}
