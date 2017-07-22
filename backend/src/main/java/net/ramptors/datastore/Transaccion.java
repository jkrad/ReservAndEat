package net.ramptors.datastore;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

public class Transaccion implements AutoCloseable {
  private final Transaction tx;
  public Transaccion() {
    tx = Datastore.getDatastoreService().beginTransaction();
  }
  public Transaccion(TransactionOptions options) {
    tx = Datastore.getDatastoreService().beginTransaction(options);
  }
  @Override public void close() throws Exception {
    try {
      tx.commit();
    } catch (Exception e) {
      try {
        if (tx.isActive()) {
          tx.rollback();
        }
      } catch (Exception e1) {
        e.addSuppressed(e1);
      }
      throw e;
    }
  }
}