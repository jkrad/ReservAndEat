package net.ramptors.si.backend;

import net.ramptors.si.CtrlBase;
import net.ramptors.si.ModeloForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java8.util.Optional;

public abstract class CtrlBaseHttp<MF extends ModeloForm>
        extends CtrlBase<MF> implements CtrlHttp {

    private Optional<HttpServletRequest> request = Optional.empty();
    private Optional<HttpServletResponse> response = Optional.empty();

    protected CtrlBaseHttp(String... rolesPermitidos) {
        super(rolesPermitidos);
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
