package com.appspot.reservandeat_171704.backend.mensajeria;

import com.appspot.reservandeat_171704.backend.entidades.Subscripcion;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.ramptors.datastore.Datastore.agregaEntidad;
import static net.ramptors.datastore.Datastore.busca;
import static net.ramptors.datastore.Datastore.getKey;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class InfoSubscripcion extends HttpServlet {

    @Override
    public String getServletInfo() {
        return "Mantiene los dispositivos web para notificaciones.";
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        final Subscripcion s
                = JSON.std.beanFrom(Subscripcion.class, request.getInputStream());
        if (!busca(Subscripcion.class, getKey(s)).isPresent()) {
            agregaEntidad(s);
            log("Subscripcion registrada: " + s.getKey());
        }
    }
}
