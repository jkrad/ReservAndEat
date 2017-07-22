package com.appspot.reservandeat_171704.backend.seguridad;

import com.appspot.reservandeat_171704.seguridad.ModeloFormSesion;

import net.ramptors.si.Sesion;
import net.ramptors.si.backend.CtrlBaseHttp;

import javax.servlet.http.Cookie;

import java8.util.function.Consumer;

import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.ADMINISTRADOR;
import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.GERENTE;
import static com.appspot.reservandeat_171704.backend.seguridad.CtrlInicio.COMENSAL;
import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.NOMBRE_DE_COOKIE;
import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.getCookie;

public class CtrlSesion extends CtrlBaseHttp<ModeloFormSesion> {

    @Override
    protected void procesa() {
        final ModeloFormSesion modeloForm = getModeloForm().get();
        switch (modeloForm.getAccion()) {
            case "inicia":
                inicia();
                break;
            case "iniciarSesion":
                modeloForm.setSiguienteForm("form-iniciar-sesion");
                break;
            case "terminarSesion":
                terminaSesion();
        }
    }

    private void inicia() {
        getSesion().ifPresentOrElse(
                new Consumer<Sesion>() {
            @Override
            public void accept(Sesion sesion) {

                final String esAdministrador = sesion.tieneRol(ADMINISTRADOR) ? "true" : "false";
                final String esGerente = sesion.tieneRol(GERENTE) ? "true" : "false";
                final String esComensal = sesion.tieneRol(COMENSAL) ? "true" : "false";
                final ModeloFormSesion modeloForm = getModeloForm().get();

                modeloForm.setSesionId(sesion.getId());
                modeloForm.setIniciarSesionMuestra("false");
                modeloForm.setTerminarSesionMuestra("true");
                modeloForm.setNavTipoComidaMuestra(esAdministrador);
                modeloForm.setNavColoniasMuestra(esAdministrador);
                modeloForm.setNavMesasMuestra(esAdministrador);
                modeloForm.setNavServiciosMuestra(esAdministrador);
                modeloForm.setNavRolMuestra(esAdministrador);
                modeloForm.setNavColoniasMuestra(esAdministrador);
            }
        },
                new Runnable() {
            @Override
            public void run() {
                sesionTerminada();
            }
        });
        getModeloForm().get().setSiguienteForm(null);
    }

    private void terminaSesion() {
        final ModeloFormSesion modeloForm = getModeloForm().get();
        getCookie(getRequest().get(), NOMBRE_DE_COOKIE).ifPresent(
                new Consumer<Cookie>() {
            @Override
            public void accept(Cookie cookie) {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                getResponse().get().addCookie(cookie);
            }
        });
        sesionTerminada();
        modeloForm.setSiguienteForm("reserva-inicio");
    }

    private void sesionTerminada() {
        final ModeloFormSesion modeloForm = getModeloForm().get();
        modeloForm.setSesionId("");
        modeloForm.setIniciarSesionMuestra("true");
        modeloForm.setTerminarSesionMuestra("false");
        modeloForm.setNavTipoComidaMuestra("false");
        modeloForm.setNavColoniasMuestra("false");
        modeloForm.setNavMesasMuestra("false");
        modeloForm.setNavServiciosMuestra("false");
        modeloForm.setNavRolMuestra("false");
        modeloForm.setNavRegistrarResta("true");
        modeloForm.setNavRegistrarComen("true");

    }
}
