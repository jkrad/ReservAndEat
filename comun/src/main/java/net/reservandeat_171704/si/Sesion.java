package net.reservandeat_171704.si;

import java.io.Serializable;
import java.util.Set;

import java8.util.Objects;

import static java.util.Collections.unmodifiableSet;

@SuppressWarnings("WeakerAccess")
public class Sesion implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String id;
    private final Set<String> roles;

    public Sesion(String id, Set<String> roles) {
        this.id = Objects.requireNonNull(id);
        this.roles = unmodifiableSet(roles);
    }

    public String getId() {
        return id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean tieneRol(String rol) {
        return roles.contains(rol);
    }
}
