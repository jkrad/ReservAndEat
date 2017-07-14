package net.reservandeat_171704.base;

import org.mini2Dx.android.beans.BeanInfo;
import org.mini2Dx.android.beans.IntrospectionException;
import org.mini2Dx.android.beans.Introspector;
import org.mini2Dx.android.beans.PropertyDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import java8.util.J8Arrays;
import java8.util.Optional;
import java8.util.function.Consumer;
import java8.util.function.Function;
import java8.util.function.Supplier;

import static net.reservandeat_171704.base.UtilBase.isPresent;

public class Property {

    private static final Map<Class<?>, Map<String, Property>> properties
            = new HashMap<>();

    public static Map<String, Property> getProperties(final Class<?> tipo) {
        return Optional.ofNullable(properties.get(tipo)).orElseGet(
                new Supplier<Map<String, Property>>() {
            @Override
            public Map<String, Property> get() {
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(tipo);
                    final Map<String, Property> map = new HashMap<>();
                    J8Arrays.stream(beanInfo.getPropertyDescriptors()).forEach(
                            new Consumer<PropertyDescriptor>() {
                        @Override
                        public void accept(PropertyDescriptor pd) {
                            map.put(pd.getName(), new Property(pd));
                        }
                    });
                    properties.put(tipo, map);
                    return map;
                } catch (IntrospectionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static Optional<Property> getProperty(final Object obj,
            final String name) {
        return Optional.ofNullable(obj).map(new Function<Object, Property>() {
            @Override
            public Property apply(Object o) {
                return getProperties(o.getClass()).get(name);
            }
        });
    }

    public static Optional<Object> get(final Object obj, String name) {
        return getProperty(obj, name).map(new Function<Property, Object>() {
            @Override
            public Object apply(Property property) {
                return property.get(obj);
            }
        });
    }

    public static void set(final Object obj, String name, final String valor) {
        getProperty(obj, name).ifPresent(new Consumer<Property>() {
            @Override
            public void accept(Property property) {
                property.set(obj, valor);
            }
        });
    }
    private final PropertyDescriptor propertyDescriptor;

    private Property(PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
    }

    public String getName() {
        return propertyDescriptor.getName();
    }

    public Class<?> getType() {
        return propertyDescriptor.getPropertyType();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public Object get(Object obj) {
        try {
            final Method readMethod = propertyDescriptor.getReadMethod();
            return isPresent(readMethod) ? readMethod.invoke(obj) : null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public void set(Object obj, Object valor) {
        try {
            final Method writeMethod = propertyDescriptor.getWriteMethod();
            if (isPresent(writeMethod)) {
                writeMethod.invoke(obj, valor);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
