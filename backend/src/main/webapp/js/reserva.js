"use strict";
// Esconde el body para prevenir FOUC
document.body.style.opacity = 0;
if ("serviceWorker" in navigator) {
    navigator.serviceWorker.addEventListener("message", evt => {
        let data = evt.data;
        if (data.id === document.querySelector("[name='sesionId']").value
                && data.texto) {
            muestraMensaje(data.texto, "img/ic_launcher_72px.png");
        } else if (data.noticia) {
            muestraMensaje(data.noticia, "img/ic_launcher_72px.png");
        } else {
            switch (data.sincronizacion) {
                case "terminada":
                    window.dispatchEvent(new Event("sincronizacionTerminada"));
                    break;
                case "procesando":
                    solicitaSincronizar();
            }
        }
    });
    navigator.serviceWorker.register('sw.js')
            .then(registro => {
                console.log("Service Worker registrado");
                console.log(registro);
            })
            .catch(window.muestraExcepcion);
    navigator.serviceWorker.ready
            .then(registro => {
                if ("PushManager" in window) {
                    registro.pushManager.getSubscription()
                            .then(subscription => {
                                if (subscription) {
                                    return window.Promise.resolve(subscription);
                                } else {
                                    return registro.pushManager
                                            .subscribe({userVisibleOnly: true});
                                }
                            })
                            .then(subscripcion => {
                                console.log("Subscrito.");
                                console.log(subscripcion);
                                let s = {
                                    key: subscripcion.endpoint,
                                    llave: btoa(String.fromCharCode.apply(null,
                                            new Uint8Array(subscripcion.getKey('p256dh')))),
                                    auth: btoa(String.fromCharCode.apply(null,
                                            new Uint8Array(subscripcion.getKey('auth'))))
                                };
                                return window.fetch("infoSubscripcion", {method: "POST",
                                    credentials: "include", body: JSON.stringify(s)});
                            })
                            .then(resp => {
                                solicitaSincronizar();
                                if (!resp.ok) {
                                    throw new Error(resp.statusText);
                                }
                            })
                            .catch(window.muestraExcepcion);
                }
            })
            .catch(window.muestraExcepcion);
}
window.addEventListener("WebComponentsReady", function () {
    // Muestra el body ahora que todos los componentes están listos.
    document.body.style.opacity = 1;
});