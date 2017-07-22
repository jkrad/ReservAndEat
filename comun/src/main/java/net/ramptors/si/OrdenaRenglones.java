package net.ramptors.si;

import java.util.Comparator;

public class OrdenaRenglones<R extends Renglon> implements Comparator<R> {
  @Override public int compare(R r1, R r2) {
    return r1.getTexto1().toUpperCase().compareTo(r2.getTexto1().toUpperCase());
  }
}
