package net.reservandeat_171704.si.backend;

import net.reservandeat_171704.si.CtrlAbc;
import net.reservandeat_171704.si.Info;
import net.reservandeat_171704.si.ModeloFormAbc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java8.util.Optional;

public abstract class CtrlAbcHttp<MF extends ModeloFormAbc, E>
        extends CtrlAbc<MF, E> implements CtrlHttp {

    private Optional<HttpServletRequest> request = Optional.empty();
    private Optional<HttpServletResponse> response = Optional.empty();

    public CtrlAbcHttp(String tituloDeNuevo, Info<E> info,
            String... rolesPermitidos) {
        super(tituloDeNuevo, info, rolesPermitidos);
    }

    public Optional<HttpServletRequest> getRequest() {
        return request;
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        this.request = Optional.of(request);
    }

    public Optional<HttpServletResponse> getResponse() {
        return response;
    }

    @Override
    public void setResponse(HttpServletResponse response) {
        this.response = Optional.of(response);
    }
}
