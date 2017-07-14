
package net.reservandeat_171704.si.backend;

import com.fasterxml.jackson.jr.ob.JSON;

import net.reservandeat_171704.base.Property;
import net.reservandeat_171704.si.CtrlBase;
import net.reservandeat_171704.si.ModeloForm;
import net.reservandeat_171704.si.Sesion;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java8.util.Optional;
import java8.util.function.Consumer;
import java8.util.function.Supplier;

import static com.appspot.reservandeat_171704.backend.seguridad.UtilSeguridad.recuperaSesion;
import static java.util.Objects.requireNonNull;
import static net.reservandeat_171704.base.UtilBase.isPresent;
import static net.reservandeat_171704.base.UtilIO.leeBytes;
import static net.reservandeat_171704.base.UtilIO.leeString;
import static net.reservandeat_171704.base.UtilObjetos.castList;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class ServletBase extends HttpServlet {

    private Optional<? extends Class<?>> controladorTipo = Optional.empty();

    @Override
    public void init() throws ServletException {
        try {
            final ServletConfig servletConfig = getServletConfig();
            final String controladorNombre
                    = servletConfig.getInitParameter("controlador");
            requireNonNull(controladorNombre,
                    "Falta el servlet-param controlador en el web.xml");
            controladorTipo = Optional.of(Class.forName(controladorNombre));
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(final HttpServletRequest req,
            final HttpServletResponse resp) throws ServletException, IOException {
        controladorTipo.ifPresent(new Consumer<Class<?>>() {
            @Override
            public void accept(Class<?> controladorTipo) {
                try {
                    final CtrlBase controlador = (CtrlBase) controladorTipo.newInstance();
                    final ModeloForm modeloForm
                            = (ModeloForm) controlador.getTipoModeloForm().newInstance();
                    try {
                        final Optional<Sesion> sesion = recuperaSesion(req);
                        lee(req, modeloForm);
                        if (controlador instanceof CtrlHttp) {
                            final CtrlHttp ctrlHttp = (CtrlHttp) controlador;
                            ctrlHttp.setRequest(req);
                            ctrlHttp.setResponse(resp);
                        }
                        controlador.procesa(sesion, modeloForm);
                    } catch (Exception e) {
                        controlador.procesaError("Error procesando forma.", e);
                    }
                    JSON.std.write(modeloForm, resp.getOutputStream());
                } catch (InstantiationException | IllegalAccessException
                        | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void lee(HttpServletRequest request, final ModeloForm modeloForm)
            throws IOException, ServletException, FileUploadException {
        final Map<String, Property> map
                = Property.getProperties(modeloForm.getClass());
        ServletFileUpload upload = new ServletFileUpload();
        FileItemIterator iter = upload.getItemIterator(request);
        while (iter.hasNext()) {
            final FileItemStream item = iter.next();
            final InputStream stream = item.openStream();
            final Property p = map.get(item.getFieldName());
            if (isPresent(p)) {
                final Class<?> tipo = p.getType();
                if (String.class.isAssignableFrom(tipo)) {
                    p.set(modeloForm, leeString(stream).orElse(null));
                } else if (List.class.isAssignableFrom(tipo)) {
                    leeString(stream).ifPresent(new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            final List<String> lista
                                    = castList(String.class, p.get(modeloForm)).orElseGet(
                                            new Supplier<List<String>>() {
                                        @Override
                                        public List<String> get() {
                                            final List<String> lista = new ArrayList<String>() {
                                            };
                                            p.set(modeloForm, lista);
                                            return lista;
                                        }
                                    });
                            lista.add(s);
                        }
                    });
                } else if (byte[].class.isAssignableFrom(tipo)) {
                    p.set(modeloForm, leeBytes(stream).orElse(null));
                }
            }
        }
    }
}
