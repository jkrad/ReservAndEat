package net.ramptors.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

import net.ramptors.base.Property;
import net.ramptors.si.Info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import java8.util.Optional;
import java8.util.function.Consumer;
import java8.util.function.Function;
import java8.util.function.Predicate;
import java8.util.stream.Collectors;
import java8.util.stream.Stream;

import static com.google.appengine.api.datastore.KeyFactory.createKey;
import static com.google.appengine.api.datastore.KeyFactory.keyToString;
import static com.google.appengine.api.datastore.KeyFactory.stringToKey;
import static java8.util.stream.StreamSupport.stream;
import static net.ramptors.base.Property.getProperties;
import static net.ramptors.base.UtilBase.isNullOrEmpty;
import static net.ramptors.base.UtilBase.isPresent;
import static net.ramptors.base.UtilObjetos.getParametroGenerico;

public abstract class Datastore<E> implements Info<E> {

    private static final Validator validator
            = Validation.buildDefaultValidatorFactory().getValidator();
    private static final DatastoreService datastoreService
            = DatastoreServiceFactory.getDatastoreService();

    static DatastoreService getDatastoreService() {
        return datastoreService;
    }

    public static List<Key> adaptaStrings(List<String> ids) {
        return isNullOrEmpty(ids) ? Collections.<Key>emptyList()
                : stream(ids)
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) {
                                return isPresent(s);
                            }
                        })
                        .map(new Function<String, Key>() {
                            @Override
                            public Key apply(String s) {
                                return stringToKey(s);
                            }
                        }).collect(Collectors.<Key>toList());
    }

    public static List<String> adaptaKeys(Collection<Key> ids) {
        return isNullOrEmpty(ids) ? new ArrayList<String>()
                : stream(ids).map(new Function<Key, String>() {
                    @Override
                    public String apply(Key key) {
                        return keyToString(key);
                    }
                }).collect(Collectors.<String>toList());
    }

    public static Key adaptaString(String id) {
        return isNullOrEmpty(id) ? null : stringToKey(id);
    }

    public static String adaptaKey(Key key) {
        return isPresent(key) ? keyToString(key) : "";
    }

    public static Key getKey(Class<?> tipo, String name) {
        return createKey(getKind(tipo), name);
    }

    public static Key getKey(Key key, Class<?> tipo, String name) {
        return createKey(key, getKind(tipo), name);
    }

    public static Key getKey(Class<?> tipo, long id) {
        return createKey(getKind(tipo), id);
    }

    public static Key getKey(Key key, Class<?> tipo, long id) {
        return createKey(key, getKind(tipo), id);
    }

    public static <T> Optional<T> busca(Class<T> tipo, String name) {
        return busca(tipo, createKey(getKind(tipo), name));
    }

    public static <T> Optional<T> busca(Key key, Class<T> tipo, String name) {
        return busca(tipo, createKey(key, getKind(tipo), name));
    }

    public static <T> Optional<T> busca(Key key, Class<T> tipo, long id) {
        return busca(tipo, createKey(key, getKind(tipo), id));
    }

    public static <T> Optional<T> busca(Class<T> tipo, long id) {
        return busca(tipo, createKey(getKind(tipo), id));
    }

    public static Key getKey(Object modelo) {
        final Object key = Property.get(modelo, "key").get();
        final String kind = getKind(modelo.getClass());
        final Key parent = adaptaKey(Property.get(modelo, "parent")).orElse(null);
        return key instanceof String ? createKey(parent, kind, (String) key)
                : createKey(parent, kind, (Long) key);
    }

    public static <T> Optional<T> busca(final Class<T> tipo, Key key) {
        return Optional.ofNullable(key).map(new Function<Key, T>() {
            @Override
            public T apply(Key key) {
                if (!getKind(tipo).equals(key.getKind())) {
                    throw new IllegalArgumentException("Kind de la llave incorrecto.");
                } else {
                    try {
                        final Entity entity = getDatastoreService().get(key);
                        return creaModelo(tipo, entity);
                    } catch (EntityNotFoundException ignored) {
                        return null;
                    }
                }
            }
        });
    }

    public static <E> E agregaEntidad(final E modelo) {
        final Set<ConstraintViolation<E>> violaciones = validator.validate(modelo);
        if (violaciones.size() > 0) {
            throw new ConstraintViolationException(
                    Collections.<ConstraintViolation<?>>unmodifiableSet(violaciones));
        }
        final Property propertyKey = Property.getProperty(modelo, "key").get();
        final String kind = getKind(modelo.getClass());
        final Optional<Key> parent = adaptaKey(Property.get(modelo, "parent"));
        final Entity entity;
        if (String.class.equals(propertyKey.getType())) {
            entity = new Entity(kind, (String) propertyKey.get(modelo),
                    parent.orElse(null));
        } else if (parent.isPresent()) {
            entity = new Entity(kind, parent.get());
        } else {
            entity = new Entity(kind);
        }
        llenaEntity(modelo, entity);
        final Key key = getDatastoreService().put(entity);
        if (Long.class.equals(propertyKey.getType())) {
            propertyKey.set(modelo, key.getId());
        }
        return modelo;
    }

    public static <E> E modificaEntidad(E modelo) {
        final Set<ConstraintViolation<E>> violaciones = validator.validate(modelo);
        if (violaciones.size() > 0) {
            throw new ConstraintViolationException(
                    Collections.<ConstraintViolation<?>>unmodifiableSet(violaciones));
        }
        try {
            final Entity entity = getDatastoreService().get(getKey(modelo));
            llenaEntity(modelo, entity);
            getDatastoreService().put(entity);
            return modelo;
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void eliminaEntidad(Object modelo) {
        getDatastoreService().delete(getKey(modelo));
    }

    public static void eliminaTodos(final Class<?> tipo) {
        final Query query = new Query(getKind(tipo));
        query.setKeysOnly();
        getDatastoreService().delete(
                stream(getDatastoreService().prepare(query).asQueryResultList(
                        FetchOptions.Builder.withDefaults())).map(
                        new Function<Entity, Key>() {
                    @Override
                    public Key apply(Entity entity) {
                        return entity.getKey();
                    }
                }).collect(Collectors.<Key>toList()));
    }

    static <E> E creaModelo(Class<E> tipo, final Entity entity) {
        try {
            final E modelo = tipo.newInstance();
            final Key parent = entity.getParent();
            final Key key = entity.getKey();
            final String keyName = key.getName();
            final long id = key.getId();
            stream(getProperties(tipo).values())
                    .forEach(new Consumer<Property>() {
                        @Override
                        public void accept(Property p) {
                            final String name = p.getName();
                            switch (name) {
                                case "key":
                                    if (String.class.equals(p.getType())) {
                                        p.set(modelo, keyName);
                                    } else if (Long.class.equals(p.getType())) {
                                        p.set(modelo, id);
                                    }
                                    break;
                                case "parent":
                                    if (String.class.equals(p.getType()) && isPresent(parent)) {
                                        p.set(modelo, keyToString(parent));
                                    } else {
                                        p.set(modelo, parent);
                                    }
                                    break;
                                default:
                                    p.set(modelo, entity.getProperty(name));
                            }
                        }
                    });
            return modelo;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void llenaEntity(final Object modelo, final Entity entity) {
        final Map<String, Property> propertyDescriptors
                = getProperties(modelo.getClass());
        stream(propertyDescriptors.values())
                .forEach(new Consumer<Property>() {
                    @Override
                    public void accept(Property p) {
                        final String name = p.getName();
                        switch (name) {
                            case "class":
                            case "key":
                            case "parent":
                                break;
                            default:
                                entity.setProperty(name, p.get(modelo));
                        }
                    }
                });
    }

    static String getKind(Class<?> tipo) {
        return tipo.getSimpleName();
    }

    private static Optional<Key> adaptaKey(Optional<?> key) {
        return key.map(new Function<Object, Key>() {
            @Override
            public Key apply(Object parent) {
                return parent instanceof String ? stringToKey((String) parent)
                        : (Key) parent;
            }
        });
    }
    private Class<E> tipoEntidad = getParametroGenerico(this, 0);

    @Override
    public Stream<E> consulta() {
        return new Consulta<E>(tipoEntidad) {
        }.lista();
    }

    @Override
    public Optional<E> busca(String id) {
        return busca(tipoEntidad, isNullOrEmpty(id) ? null : stringToKey(id));
    }

    public E agrega(final E modelo) {
        return agregaEntidad(modelo);
    }

    public E modifica(E modelo) {
        return modificaEntidad(modelo);
    }

    public void elimina(E modelo) {
        eliminaEntidad(modelo);
    }
}
