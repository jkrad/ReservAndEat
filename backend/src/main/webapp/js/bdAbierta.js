
"use strict";
Object.defineProperty(window, "bdAbierta", {
    value: new window.Promise((resolve, reject) => {
        let solicitud = window.indexedDB.open("reserva", 2);
        solicitud.onerror = evt => reject(new Error(evt.target.error.name));
        // Al crear la base o hacerle cambios hay que crear las tablas.
        solicitud.onupgradeneeded = evt => {
            let base = evt.target.result;
            // Automáticamente se inicia una transacción para el cambio de versión.
            evt.target.transaction.onerror = evt =>
                reject(new Error(evt.target.error.name));
            // Como hay cambio de versión borra el almacén si la bd existe.
            if (base.objectStoreNames.contains("Aviso")) {
                base.deleteObjectStore("Aviso");
            }
            // Crea el almacén "Aviso" con el campo llave "key".
            base.createObjectStore("Aviso", {keyPath: "key"});
        };
        // Función al abrir la base.
        solicitud.onsuccess = evt => {
            let base = evt.target.result;
            // Función en caso de error.
            base.onerror = evt => reject(evt.target.error.name);
            console.log("BD abierta");
            resolve(base);
        };
    })
});