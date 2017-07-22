
package net.ramptors.si;

import net.ramptors.base.UtilBase;

import java.util.List;

import java8.util.Objects;
import java8.util.function.Predicate;
import java8.util.stream.RefStreams;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import static net.ramptors.base.UtilBase.isPresent;

/**
 * @author Gilberto Pacheco Gallegos
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Renglon {

    public static <R extends Renglon> Stream<R> filtra(String filtro,
            List<? extends R> lista) {
        if (!isPresent(lista)) {
            return RefStreams.empty();
        } else if (UtilBase.isNullOrEmpty(filtro)) {
            return StreamSupport.stream(lista);
        } else {
            final String upperFiltro = filtro.toUpperCase();
            return StreamSupport.stream(lista).sorted(new OrdenaRenglones<R>())
                    .filter(new Predicate<R>() {
                        @Override
                        public boolean test(R renglon) {
                            return renglon.getTexto1().toUpperCase()
                                    .contains(upperFiltro.toUpperCase())
                                    || (renglon.getTexto2() != null && renglon.getTexto2()
                                    .toUpperCase().contains(upperFiltro.toUpperCase()));
                        }
                    });
        }
    }
    private String detalleId;
    private String imagen;
    private String texto1;
    private String texto2;

    public Renglon() {
    }

    public Renglon(String detalleId, Object texto1) {
        this.detalleId = detalleId;
        this.texto1 = Objects.toString(texto1, null);
    }

    public Renglon(String detalleId, Object texto1, Object texto2) {
        this(detalleId, texto1);
        this.texto2 = Objects.toString(texto2, null);
    }

    public Renglon(String detalleId, String imagen, Object texto1,
            Object texto2) {
        this(detalleId, texto1, texto2);
        this.imagen = imagen;
    }

    public Renglon copia() {
        return new Renglon(getDetalleId(), getImagen(), getTexto1(),
                getTexto2());
    }

    public String getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(String detalleId) {
        this.detalleId = detalleId;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTexto1() {
        return texto1;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTexto2() {
        return texto2;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Renglon) {
            final Renglon other = (Renglon) o;
            return Objects.equals(getDetalleId(), other.getDetalleId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDetalleId());
    }
}
