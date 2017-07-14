package com.appspot.reservandeat_171704.backend.seguridad;

import com.appspot.reservandeat_171704.backend.entidades.Dispositivo;
import com.appspot.reservandeat_171704.backend.entidades.Rol;
import com.appspot.reservandeat_171704.backend.entidades.Subscripcion;
import com.appspot.reservandeat_171704.backend.entidades.Usuario;
import com.google.appengine.api.datastore.DatastoreServiceConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java8.util.function.Consumer;
import java8.util.function.Predicate;
import java8.util.stream.RefStreams;
import java8.util.stream.Stream;

import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.encripta;
import static java.util.Collections.singletonList;
import static net.reservandeat_171704.datastore.Datastore.agregaEntidad;
import static net.reservandeat_171704.datastore.Datastore.busca;
import static net.reservandeat_171704.datastore.Datastore.eliminaTodos;
import static net.reservandeat_171704.datastore.Datastore.getKey;

/**
 * Escucha el ciclo de vida de la aplicación web.
 *
 * @author Gilberto Pacheco Gallegos
 */
public class CtrlInicio implements ServletContextListener {

    //Por el momento solo tenemos 3 tipos de roles 
    public static final String ADMINISTRADOR = "Administrador";
    public static final String GERENTE = "Gerente";
    public static final String COMENSAL = "Comensal";

    private static final Stream<Rol> ROLES = RefStreams.of(
            new Rol(ADMINISTRADOR, "Administra el sistema."),
            new Rol(GERENTE, "Administra el restaurante"),
            new Rol(COMENSAL, "Cliente que realiza reservaciones"));

    private static final String ID_ADMINISTRADOR = "zentuskun@outlook.com";
    private static final String CONTRASENA_ADMINISTRADOR = "juanito";
    private static final String NOMBRE_ADMINISTRADOR = "JuanHC";


    /**
     * Se lista cuando la aplicación se iniciaTransaccion.
     *
     * @param sce servicios y recursos que ofrece la aplicación.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty(DatastoreServiceConfig.DATASTORE_EMPTY_LIST_SUPPORT,
                Boolean.TRUE.toString());
        ROLES.filter(new Predicate<Rol>() {
            @Override
            public boolean test(Rol rol) {
                return !busca(Rol.class, rol.getKey()).isPresent();
            }
        }).forEach(new Consumer<Rol>() {
            @Override
            public void accept(Rol rol) {
                agregaEntidad(rol);
            }
        });
        if (!busca(Usuario.class, ID_ADMINISTRADOR).isPresent()) {
            final Usuario usuario = new Usuario();
            usuario.setKey(ID_ADMINISTRADOR);
            usuario.setContrasena(encripta(CONTRASENA_ADMINISTRADOR));
            usuario.setNombre(NOMBRE_ADMINISTRADOR);
            usuario.setRoles(singletonList(getKey(Rol.class, ADMINISTRADOR)));
            agregaEntidad(usuario);
        }
        eliminaTodos(Dispositivo.class);
        eliminaTodos(Subscripcion.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
