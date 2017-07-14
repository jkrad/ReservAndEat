
"use strict";
/** Maneja el almacenamiento local usando IndexedDb con borrado lógico. */
class InfoAviso {
    constructor(base) {
        Object.defineProperty(this, "base", {
            enumerable: true,
            value: base
        });
    }
    busca(key) {
        return new Promise((resolve) => {
            this.avisoParaConsultas().get(key).onsuccess = evt => {
                let modelo = evt.target.result;
                if (modelo && !modelo.eliminado) {
                    resolve(modelo);
                } else {
                    resolve();
                }
            };
        });
    }
    /** lista los objetos registrados que no tienen borrado lógico. */
    consulta() {
        return new Promise((resolve) => {
            let lista = [];
            this.avisoParaConsultas().openCursor().onsuccess = evt => {
                let cursor = evt.target.result;
                if (cursor) {
                    let modelo = cursor.value;
                    if (!modelo.eliminado) {
                        lista.push({
                            detalleId: modelo.key,
                            texto1: new Date(modelo.modificacion).toLocaleString(),
                            texto2: modelo.titulo
                        });
                    }
                    cursor.continue();
                } else {
                    resolve(lista);
                }
            };
        });
    }
    /** Lista todos los objetos para sincronización, incluyendo los que tienen
     * borrado lógico. */
    consultaTodos() {
        return new Promise((resolve) => {
            let lista = [];
            this.avisoParaConsultas().openCursor().onsuccess = evt => {
                let cursor = evt.target.result;
                if (cursor) {
                    lista.push(cursor.value);
                    cursor.continue();
                } else {
                    resolve(lista);
                }
            };
        });
    }
    /** Agrega un objeto.
     * @param {Object} modelo */
    agrega(modelo) {
        return new Promise((resolve) => {
            modelo.modificacion = new Date().getTime();
            modelo.eliminado = false;
            let Aviso = this.avisoParaCambios(resolve, true);
            // Estas son las operaciones de la transacción.
            Aviso.add(modelo);
        });
    }
    /** Modifica un objeto.
     * @param {Object} modelo */
    modifica(modelo) {
        return new Promise((resolve) => {
            modelo.modificacion = new Date().getTime();
            let Aviso = this.avisoParaCambios(resolve, true);
            Aviso.put(modelo);
        });
    }
    /** Elimina un objeto
     * @param {Object} modelo */
    elimina(modelo) {
        return new Promise((resolve) => {
            modelo.modificacion = new Date().getTime();
            modelo.eliminado = true;
            let Aviso = this.avisoParaCambios(resolve, true);
            Aviso.put(modelo);
        });
    }
    /** Borra el contenido del almacén y guarda el contenido del listado.
     * @param {Array<Object>} lista */
    reemplaza(lista) {
        return new Promise((resolve) => {
            let Aviso = this.avisoParaCambios(resolve);
            Aviso.clear();
            lista.forEach(aviso => Aviso.add(aviso));
        });
    }
    /** @returns {IDBObjectStore} */
    avisoParaConsultas() {
        // Inicia una transacción de lectura indicando los almacenes que usa.
        let tx = this.base.transaction(["Aviso"], "readonly");
        return tx.objectStore("Aviso");
    }
    /** @returns {IDBObjectStore}
     * @param {function} resolve 
     * @param {function} sincroniza */
    avisoParaCambios(resolve, sincroniza) {
        /* Inicia una transacción de lectura y escritura indicando los almacenes que
         * usa. */
        let tx = this.base.transaction(["Aviso"], "readwrite");
        let store = tx.objectStore("Aviso");
        // Función después de terminar.
        tx.oncomplete = () => {
            resolve();
            if (sincroniza) {
                solicitaSincronizar();
            }
        };
        return store;
    }
}
Object.defineProperty(window, "infoAvisoListo", {
    value: window.bdAbierta
            .then(base => window.Promise.resolve(new InfoAviso(base)))
});