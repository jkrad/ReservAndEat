package com.appspot.reservandeat_171704.backend.mensajeria;

import com.appspot.reservandeat_171704.backend.entidades.Dispositivo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java8.util.function.Consumer;

import static net.ramptors.base.UtilIO.leeString;
import static net.ramptors.datastore.Datastore.agregaEntidad;
import static net.ramptors.datastore.Datastore.busca;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class InfoDispositivo extends HttpServlet {

    @Override
    public String getServletInfo() {
        return "Mantiene los dispositivos para notificaciones.";
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        leeString(request.getInputStream()).ifPresent(new Consumer<String>() {
            @Override
            public void accept(String key) {
                if (!busca(Dispositivo.class, key).isPresent()) {
                    agregaEntidad(new Dispositivo(key));
                }
            }
        });
    }
}
