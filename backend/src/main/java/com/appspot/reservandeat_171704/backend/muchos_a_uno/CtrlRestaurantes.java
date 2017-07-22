package com.appspot.reservandeat_171704.backend.muchos_a_uno;

import com.appspot.reservandeat_171704.backend.entidades.Restaurante;
import com.appspot.reservandeat_171704.backend.entidades.Rol;
import com.appspot.reservandeat_171704.backend.entidades.Usuario;
import com.appspot.reservandeat_171704.backend.mensajeria.Mensajeria;
import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.GERENTE;
import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.encripta;
import com.appspot.reservandeat_171704.muchos_a_uno.ModeloFormRestaurantes;
import static com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY;
import com.google.appengine.api.datastore.Query;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static java.util.Collections.singletonList;

import net.ramptors.datastore.Datastore;
import net.ramptors.si.Renglon;
import net.ramptors.si.backend.CtrlAbcHttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import java8.util.function.Consumer;
import java8.util.stream.Stream;
import static net.ramptors.base.UtilBase.isNullOrEmpty;
import static net.ramptors.base.UtilBase.isPresent;
import static net.ramptors.base.UtilBase.isTrue;

import static net.ramptors.base.UtilBase.texto;
import static net.ramptors.base.UtilBase.toUpperCase;
import net.ramptors.datastore.Consulta;

import static net.ramptors.datastore.Datastore.adaptaKey;
import static net.ramptors.datastore.Datastore.getKey;

public class CtrlRestaurantes
        extends CtrlAbcHttp<ModeloFormRestaurantes, Restaurante> {

    public CtrlRestaurantes() {
        super("Restaurante Nuevo", new Datastore<Restaurante>() {
        });
    }

    @Override
    protected String getTitulo() {
        return getModelo().get().getNombreRestaurante();
    }

    @Override
    protected Stream<Renglon> getRenglones(Stream<Restaurante> entidades) {
        return entidades.map(new FuncionRestauranteRenglon(getRequest().get()));
    }

    @Override
    protected void llenaModelo() {
        final ModeloFormRestaurantes modeloForm = getModeloForm().get();
        final String contrasena = modeloForm.getContrasena();
        final String correo = modeloForm.getKey();
        if (isTrue(modeloForm.getNuevo()) && isNullOrEmpty(correo)) {
            throw new RuntimeException("Falta el correo.");
        } else if (isTrue(modeloForm.getNuevo()) && isNullOrEmpty(contrasena)) {
            throw new RuntimeException("Falta la Contraseña.");
        } else if (isTrue(modeloForm.getDebeConfirmar())
                && !Objects.equals(contrasena, modeloForm.getConfirma())) {
            throw new RuntimeException("Las Contraseñas no coinciden.");
        } else if (isPresent(contrasena)
                && (contrasena.length() < 6 || contrasena.length() > 12)) {
            throw new RuntimeException(
                    "La Contraseña debe tener de 6 a 12 caracteres.");
        } else if (isTrue(modeloForm.getNuevo()) && new Consulta<Usuario>() {
        }.setFilter(new Query.FilterPredicate(KEY_RESERVED_PROPERTY, EQUAL,
                getKey(Restaurante.class, correo))).lista().count() > 0) {
            throw new RuntimeException(
                    "El correo proporcionado ya está reservado.");
        } else {
            getModelo().ifPresent(new Consumer<Restaurante>() {
                @Override
                public void accept(Restaurante modelo) {
                    if (isTrue(modeloForm.getNuevo())) {
                        modelo.setKey(modeloForm.getKey());
                        modelo.setContrasena(encripta(contrasena));
                    } else if (!isNullOrEmpty(contrasena)) {
                        modelo.setContrasena(encripta(contrasena));
                    }
                    modelo.setNombreRestaurante(modeloForm.getNombreRestaurante());
                    modelo.setTelefono(modeloForm.getTelefono());
                    modelo.setPuestoEncargado(modeloForm.getPuestoEncargado());
                    modelo.setRoles(singletonList(getKey(Rol.class, GERENTE)));
                    modelo.setUpperKey(toUpperCase(modelo.getKey()));
                }
                public void accept(Usuario modelo) {
                    if (isTrue(modeloForm.getNuevo())) {
                        modelo.setKey(modeloForm.getKey());
                        modelo.setContrasena(encripta(contrasena));
                    } else if (!isNullOrEmpty(contrasena)) {
                        modelo.setContrasena(encripta(contrasena));
                    }
                    modelo.setNombre(modeloForm.getNombreRestaurante());
                    modelo.setTelefono(modeloForm.getTelefono());
                    modelo.setRoles(singletonList(getKey(Rol.class,GERENTE )));
                    modelo.setUpperKey(toUpperCase(modelo.getKey()));
                }

            });
        }
    }

    @Override
    protected void muestraModelo() {
        final ModeloFormRestaurantes modeloForm = getModeloForm().get();
        getModelo().ifPresent(new Consumer<Restaurante>() {
            @Override
            public void accept(Restaurante modelo) {
                modeloForm.setKey(texto(modelo.getKey()));
                modeloForm.setCampoKeyMuestra(modeloForm.getNuevo());
                modeloForm.setContrasenaObligatoriaMuestra(modeloForm.getNuevo());
                modeloForm.setContrasena("");
                modeloForm.setConfirmaObligatoriaMuestra(modeloForm.getNuevo());
                modeloForm.setConfirma("");
                modeloForm.setNombreRestaurante(texto(modelo.getNombreRestaurante()));
                modeloForm.setTelefono(texto(modelo.getTelefono()));
                modeloForm.setPuestoEncargado(texto(modelo.getPuestoEncargado()));
            }
        });
    }

    @Override
    protected void agregaModelo() {
        super.agregaModelo();
        final Map<String, String> data = new HashMap<>();
        data.put("noticia", "Hay nuevos restaurantes.");
        Mensajeria.notifica(data);

    }

}
