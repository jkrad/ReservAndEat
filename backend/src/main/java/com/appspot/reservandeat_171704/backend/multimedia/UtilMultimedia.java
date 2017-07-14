/* Copyright 2015 Gilberto Pacheco Gallegos Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. */
package com.appspot.reservandeat_171704.backend.multimedia;

import com.appspot.reservandeat_171704.backend.entidades.Archivo;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import org.apache.commons.fileupload.util.Streams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import java8.util.Optional;

public class UtilMultimedia {
  @SuppressWarnings("FieldCanBeLocal")
  private static String SEGMENTO_GCS = "rmptcompro.appspot.com";
  private static final ImagesService imagesService = ImagesServiceFactory
      .getImagesService();
  /**
   * Servicio GCS. Las transferencias se reintentan hasta 10 veces sin exceder
   * 15 segundos.
   */
  public static final GcsService gcsService = GcsServiceFactory
      .createGcsService(new RetryParams.Builder().initialRetryDelayMillis(10)
          .retryMaxAttempts(10).totalRetryPeriodMillis(15000).build());
  public static String getArchivoUrl(HttpServletRequest request,
      Archivo archivo) {
    final int localPort = request.getLocalPort();
    return localPort == 0 || localPort == 80
        ?
        request.getScheme() + "://" + request.getServerName() + "/gs/" + archivo
            .getBucket() + "/" + archivo.getNombre()
        :
        request.getScheme() + "://" + request.getServerName() + ":" + localPort
            + "/gs/" + archivo.getBucket() + "/" + archivo.getNombre();
  }
  @SuppressWarnings("UnusedParameters")
  public static String getMiniaturaUrl(HttpServletRequest request,
      Archivo archivo) {
    final String servingUrl = archivo.getServingUrl();
    return (!servingUrl.startsWith("http://localhost")
        ? servingUrl.replace("http://", "https://") : servingUrl) + "=s150";
  }
  public static Archivo creaArchivo(byte[] bytes) {
    try {
      final GcsFilename fileName = cargaArchivoGcs(bytes).get();
      final Archivo archivo = new Archivo();
      archivo.setNombre(fileName.getObjectName());
      archivo.setBucket(fileName.getBucketName());
      archivo.setServingUrl(imagesService
          .getServingUrl(ServingUrlOptions.Builder
              .withGoogleStorageFileName("/gs/" + fileName.getBucketName()
                  + '/' + fileName.getObjectName())));
      return archivo;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  private static Optional<GcsFilename> cargaArchivoGcs(byte[] bytes)
      throws IOException {
    final ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
    final GcsFilename fileName = creaFileName();
    final GcsOutputChannel outputChannel = gcsService
        .createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
    final long longitud =
        Streams.copy(stream, Channels.newOutputStream(outputChannel), true);
    if (longitud > 0) {
      return Optional.of(fileName);
    } else {
      gcsService.delete(fileName);
      return Optional.empty();
    }
  }
  private static GcsFilename creaFileName() {
    UUID uuid = UUID.randomUUID();
    return new GcsFilename(SEGMENTO_GCS, uuid.toString());
  }
}