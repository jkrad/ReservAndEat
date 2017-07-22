"use strict";
(function () {
    const url = "infoSincronizacion";
    window.addEventListener("sincronizacionTerminada", () => {
        window.fetch(url, {method: "GET", credentials: "include"}).
                then(respuesta => {
                    if (respuesta.ok) {
                        return respuesta.json();
                    } else {
                        throw new Error(respuesta.statusText);
                    }
                })
                .then(lista => window.infoAvisoListo
                            .then(info => info.reemplaza(lista || []))
                            .then(() => window.dispatchEvent(new Event("sincronizado")))
                )
                .catch(window.muestraExcepcion);
    });
    window.addEventListener("sincronizar", () => {
        window.infoAvisoListo
                .then(info => info.consultaTodos())
                .then(lista => window.fetch(url, {method: "POST",
                        credentials: "include", body: JSON.stringify(lista)}))
                .then(respuesta => {
                    if (!respuesta.ok) {
                        throw new Error(respuesta.statusText);
                    }
                })
                .catch(window.muestraExcepcion);
    });
})();