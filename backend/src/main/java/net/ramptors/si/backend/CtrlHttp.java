package net.ramptors.si.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CtrlHttp {
  void setRequest(HttpServletRequest request);
  void setResponse(HttpServletResponse response);
}