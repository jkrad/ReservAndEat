/*
 * Copyright 2017 Gilberto Pacheco Gallegos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appspot.reservandeat_171704.backend.multimedia;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;

import org.apache.commons.fileupload.util.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.channels.Channels;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CtrlArchivo extends HttpServlet {

    /**
     * Tamaño de los bloques de lectura. Debe ser > 1kb y < 10MB
     */
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    @Override
    public String getServletInfo() {
        return "Permite descargar archivos almacenados en Google Cloud Storage.";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Como el archivo devuelto puede cambiar, no se debe guardar en cache.
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        GcsFilename fileName = getFileName(request);
        GcsInputChannel readChannel = UtilMultimedia.gcsService
                .openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
        final InputStream inputStream = Channels.newInputStream(readChannel);
        final String contentType
                = URLConnection.guessContentTypeFromStream(inputStream);
        response.setContentType(contentType);
        Streams.copy(inputStream, response.getOutputStream(), true);
    }

    private GcsFilename getFileName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/", 4);
        if (!splits[0].equals("") || !splits[1].equals("gs")) {
            throw new IllegalArgumentException("The URL is not formed as expected. "
                    + "Expecting /gs/<bucket>/<object>");
        }
        return new GcsFilename(splits[2], splits[3]);
    }
}
