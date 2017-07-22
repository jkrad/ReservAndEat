"use strict";
function texto(s) {
    return s ? s : "";
}
function arr(a) {
    return a ? a : [];
}
function getMensaje(e) {
    return e.message;
}
function buscaName(padre, name) {
    return padre.querySelector("[name='" + name + "']");

}
function setVisible(element, visible) {
    element.style.display = visible ? "" : "none";
}
function eliminaSufijo(texto, sufijo) {
    return texto ? texto.substring(0, texto.lastIndexOf(sufijo)) : "";
}
function registraElement(nombre, estructura) {
    return window.customElements.define(nombre, estructura);
}
function elementPrevio() {
    let currentScript = "currentScript" in document
            ? document.currentScript : document._currentScript;
    return currentScript.previousElementSibling;
}
function cargaTemplate(padre, template) {
    let content = document.importNode(template.content, true);
    padre.appendChild(content);
}
function borraTodosLosElementos(padre) {
    while (padre.firstChild) {
        padre.removeChild(padre.firstChild);
    }
}
function muestraExcepcion(e) {
    muestraError(getMensaje(e));
}
function muestraError(mensaje) {
    window.console.error(mensaje);
    muestraMensaje(mensaje, "img/error-flat.png");
}
function muestraMensaje(mensaje, icono) {
    if ("Notification" in window) {
        // Solicita que se autoricen las notificaciones.
        window.Notification.requestPermission(permission => {
            // Si el usuario acepta, se crea la notificación.
            if (permission === "granted") {
                let opciones = {
                    body: mensaje,
                    icon: icono
                };
                try {
                    new Notification("Reserv&&Eat", opciones);
                } catch (e) {
                    try {
                        navigator.serviceWorker.ready
                                .then(registro => registro.showNotification("Reserv&&Eat", opciones));
                    } catch (e) {
                        alert(mensaje);
                    }
                }
            } else {
                alert(mensaje);
            }
        });
    } else {
        alert(mensaje);
    }
}
function recuperaImagen(input, img) {
    if (input.files && input.files[0]) {
        let reader = new FileReader();
        reader.onload = function (e) {
            let i = document.getElementById(img);
            i.src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
}
function lanzaDetalle(padre, elementRenglon) {
    elementRenglon.addEventListener("click", evt => {
        let alFrente = buscaName(padre, "alFrente");
        if (alFrente.value === "maestro") {
            elementRenglon.dispatchEvent(
                    new CustomEvent("accion", {
                        detail: {
                            accion: "detalle",
                            detalleId: elementRenglon.value.detalleId
                        },
                        bubbles: true
                    }));
        }
    });
    setVisible(elementRenglon.boton, false);
}
function solicitaSincronizar() {
    window.dispatchEvent(new Event("sincronizar"));
}