package net.reservandeat_171704.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ejecutor {
  private static final int NUMERO_DE_NUCLEOS =
      Runtime.getRuntime().availableProcessors();
  public static final ExecutorService SERVICIO =
      Executors.newFixedThreadPool(NUMERO_DE_NUCLEOS);
  private Ejecutor() {}
}
