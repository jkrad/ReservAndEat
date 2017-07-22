package net.ramptors.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Projection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;

import java8.util.function.Function;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import static java8.util.stream.StreamSupport.stream;
import static net.ramptors.base.UtilObjetos.getParametroGenerico;
import static net.ramptors.datastore.Datastore.creaModelo;
import static net.ramptors.datastore.Datastore.getDatastoreService;
import static net.ramptors.datastore.Datastore.getKind;

public abstract class Consulta<E> {

    private final Class<E> tipo;
    private final Query query;

    public Consulta() {
        this.tipo = getParametroGenerico(this, 0);
        this.query = new Query(getKind(tipo));
    }

    public Consulta(Class<E> tipo) {
        this.tipo = tipo;
        this.query = new Query(getKind(tipo));
    }

    public Consulta<E> setAncestor(Key ancestor) {
        query.setAncestor(ancestor);
        return this;
    }

    public Consulta<E> setDistinct(boolean distinct) {
        query.setDistinct(distinct);
        return this;
    }

    public Consulta<E> setFilter(Filter filter) {
        query.setFilter(filter);
        return this;
    }

    public Consulta<E> addSort(String propertyName) {
        query.addSort(propertyName);
        return this;
    }

    public Consulta<E> addSort(String propertyName, SortDirection direction) {
        query.addSort(propertyName, direction);
        return this;
    }

    public Consulta<E> setKeysOnly() {
        query.setKeysOnly();
        return this;
    }

    public Consulta<E> clearKeysOnly() {
        query.clearKeysOnly();
        return this;
    }

    public Consulta<E> addProjection(Projection projection) {
        query.addProjection(projection);
        return this;
    }

    public Consulta<E> reverse() {
        query.reverse();
        return this;
    }

    public Stream<E> lista() {
        final PreparedQuery preparedQuery = getDatastoreService().prepare(query);
        final QueryResultList<Entity> entities
                = preparedQuery.asQueryResultList(FetchOptions.Builder.withDefaults());
        return stream(entities).map(new Function<Entity, E>() {
            @Override
            public E apply(Entity entity) {
                return creaModelo(tipo, entity);
            }
        });
    }
}
