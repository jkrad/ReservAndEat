package com.appspot.reservandeat_171704.backend.muchos_a_muchos;

import com.appspot.reservandeat_171704.backend.entidades.Rol;
import com.appspot.reservandeat_171704.backend.entidades.Usuario;
import com.appspot.reservandeat_171704.backend.mensajeria.Mensajeria;
import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.COMENSAL;
import com.appspot.reservandeat_171704.muchos_a_muchos.ModeloFormUsuarios;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import net.ramptors.datastore.Consulta;
import net.ramptors.datastore.Datastore;
import net.ramptors.si.Renglon;
import net.ramptors.si.backend.CtrlAbcHttp;

import java.util.Objects;

import java8.util.function.Consumer;
import java8.util.stream.Stream;

import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.encripta;
import static com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.Map;
import static net.ramptors.base.UtilBase.isNullOrEmpty;
import static net.ramptors.base.UtilBase.isPresent;
import static net.ramptors.base.UtilBase.isTrue;
import static net.ramptors.base.UtilBase.texto;
import static net.ramptors.base.UtilBase.toUpperCase;
import static net.ramptors.datastore.Datastore.adaptaKeys;
import static net.ramptors.datastore.Datastore.getKey;

public class CtrlUsuarios
        extends CtrlAbcHttp<ModeloFormUsuarios, Usuario> {

    public CtrlUsuarios() {
        super("Nuevo Comensal", new Datastore<Usuario>() {
        });
    }

    @Override
    protected String getTitulo() {
        return getModelo().get().getKey();
    }

    @Override
    protected Stream<Renglon> getRenglones(Stream<Usuario> entidades) {
        return entidades.map(new FuncionUsuarioRenglon());
    }

    @Override
    protected void llenaModelo() {
        final ModeloFormUsuarios modeloForm = getModeloForm().get();
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
        }
                .setFilter(new FilterPredicate(KEY_RESERVED_PROPERTY, EQUAL,
                        getKey(Usuario.class, correo))).lista().count() > 0) {
            throw new RuntimeException(
                    "El correo proporcionado ya está reservado.");
        } else {
            getModelo().ifPresent(new Consumer<Usuario>() {
                @Override
                public void accept(Usuario modelo) {
                    if (isTrue(modeloForm.getNuevo())) {
                        modelo.setKey(modeloForm.getKey());
                        modelo.setContrasena(encripta(contrasena));
                    } else if (!isNullOrEmpty(contrasena)) {
                        modelo.setContrasena(encripta(contrasena));
                    }
                    modelo.setNombre(modeloForm.getNombre());
                    modelo.setTelefono(modeloForm.getTelefono());
                    modelo.setRoles(singletonList(getKey(Rol.class, COMENSAL)));
                    modelo.setUpperKey(toUpperCase(modelo.getKey()));
                }
            });
        }
    }

    @Override
    protected void muestraModelo() {
        final ModeloFormUsuarios modeloForm = getModeloForm().get();
        getModelo().ifPresent(new Consumer<Usuario>() {
            @Override
            public void accept(Usuario modelo) {
                modeloForm.setKey(texto(modelo.getKey()));
                modeloForm.setCampoKeyMuestra(modeloForm.getNuevo());
                modeloForm.setContrasenaObligatoriaMuestra(modeloForm.getNuevo());
                modeloForm.setContrasena("");
                modeloForm.setConfirmaObligatoriaMuestra(modeloForm.getNuevo());
                modeloForm.setConfirma("");
                modeloForm.setNombre(texto(modelo.getNombre()));
                modeloForm.setTelefono(texto(modelo.getTelefono()));
                modeloForm.setRoles(adaptaKeys(modelo.getRoles()));
            }
        });
    }

    @Override
    protected void agregaModelo() {
        super.agregaModelo();
        final Map<String, String> data = new HashMap<>();
        data.put("noticia", "Tu registro fue exitoso!.");
        Mensajeria.notifica(data);

    }
}
