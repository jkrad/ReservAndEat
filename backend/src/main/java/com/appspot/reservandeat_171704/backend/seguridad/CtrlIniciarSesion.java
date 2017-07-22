package com.appspot.reservandeat_171704.backend.seguridad;

import com.appspot.reservandeat_171704.backend.entidades.Usuario;
import com.appspot.reservandeat_171704.seguridad.ModeloFormIniciarSesion;
import com.google.appengine.api.datastore.Key;

import net.ramptors.si.backend.CtrlBaseHttp;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.Cookie;

import java8.util.function.Consumer;
import java8.util.function.Function;
import java8.util.function.Predicate;
import java8.util.function.Supplier;
import java8.util.stream.Collectors;

import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.NOMBRE_DE_COOKIE;
import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.encripta;
import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.getCookie;
import static java8.util.stream.StreamSupport.stream;
import static net.ramptors.base.UtilBase.isNullOrEmpty;
import static net.ramptors.base.UtilBase.isPresent;
import static net.ramptors.datastore.Datastore.busca;

public class CtrlIniciarSesion extends CtrlBaseHttp<ModeloFormIniciarSesion> {

    @Override
    protected void procesa() {
        final ModeloFormIniciarSesion modeloForm = getModeloForm().get();
        final String accion = modeloForm.getAccion();
        if (!getSesion().isPresent() && isPresent(accion)) {
            switch (accion) {
                case "inicia":
                    inicia(modeloForm);
                    return;
                case "submit":
                    iniciaSesion(modeloForm);
                    return;
            }
        }
        modeloForm.setSiguienteForm("reserva-inicio");
    }

    private void inicia(ModeloFormIniciarSesion modeloForm) {
        modeloForm.setContrasena("");
        modeloForm.setCorreo("");
    }

    private void iniciaSesion(final ModeloFormIniciarSesion modeloForm) {
        final String correo = modeloForm.getCorreo();
        final String contrasena = modeloForm.getContrasena();
        
        if (isNullOrEmpty(correo)) {
            throw new RuntimeException("Falta el correo.");
        } else if (isNullOrEmpty(contrasena)) {
            throw new RuntimeException("Falta la contraseña.");
        } else {
            busca(Usuario.class, correo).ifPresentOrElse( new Consumer<Usuario>() {
                
                @Override
                public void accept(Usuario usuario) {
                    final String contrasenaEncriptada = encripta(contrasena);
                    if (Objects
                            .equals(usuario.getContrasena(), contrasenaEncriptada)) {
                        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                DataOutputStream daos = new DataOutputStream(baos)) {
                            daos.writeDouble(Math.random());
                            daos.writeUTF(correo);
                            daos.writeDouble(Math.random());
                            daos.writeUTF(stream(usuario.getRoles())
                                    .filter(new Predicate<Key>() {
                                        @Override
                                        public boolean test(Key key) {
                                            return isPresent(key);
                                        }
                                    })
                                    .map(new Function<Key, String>() {
                                        @Override
                                        public String apply(Key key) {
                                            return key.getName();
                                        }
                                    })
                                    .collect(Collectors.joining(",")));
                            daos.writeDouble(Math.random());
                            daos.flush();
                            final String value
                                    = Base64.toBase64String(baos.toByteArray());
                            final Cookie cookie
                                    = getCookie(getRequest().get(), NOMBRE_DE_COOKIE)
                                            .orElseGet(new Supplier<Cookie>() {
                                                @Override
                                                public Cookie get() {
                                                    return new Cookie(NOMBRE_DE_COOKIE, value);
                                                }
                                            });
                            cookie.setMaxAge(60 * 60 * 24 * 365);
                            cookie.setPath("/");
                            getResponse().get().addCookie(cookie);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        modeloForm.setSiguienteForm("reserva-inicio");
                    } else {
                        throw new RuntimeException(
                                "Combinación correo/contraseña incorrecta.");
                    }
                }
            },
                    new Runnable() {
                @Override
                public void run() {
                    throw new RuntimeException(
                            "Combinación correo/contraseña incorrecta.");
                }
            });
        }
    }
}
