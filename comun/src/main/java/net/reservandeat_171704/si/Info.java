package net.reservandeat_171704.si;

import java8.util.Optional;
import java8.util.stream.Stream;

public interface Info<E> {
  public Stream<E> consulta();
  public Optional<E> busca(String id);
  public E agrega(E modelo);
  public E modifica(E modelo);
  public void elimina(E modelo);
}