
// ESTE ARCHIVO SIMPRE DEBE ESTAR EN LA RAIZ DE LA APLICACION.

"use strict";
const CACHE_RESERVA = "cache-reserva-v3.00";
// Archivos requeridos para que la aplicación funcione fuera de línea.
const REQUIRED_FILES = [
    "comp/rmpt/rmpt-boton-menu.html",
    "comp/rmpt/rmpt-busqueda.html",
    "comp/rmpt/rmpt-herramientas-detalle.html",
    "comp/rmpt/rmpt-indica-campos-obligatorios.html",
    "comp/rmpt/rmpt-indicador.html",
    "comp/rmpt/rmpt-lista.html",
    "comp/rmpt/rmpt-renglon.html",
    "comp/rmpt/rmpt-selecciona-muchos.html",
    "comp/rmpt/rmpt-selecciona-uno.html",
    "comp/rmpt/rmpt-selector.html",
    "comp/reserva-footer.html",
    "comp/reserva-inicio.html",
    "comp/reserva-main.html",
    "comp/reserva-panel-maestro.html",
    "comp/reserva-panel-secundario.html",
    "comp/form-tipo-comida.html",
    "comp/form-colonia.html",
    "comp/form-registrar.html",
    "comp/form-registrar-restaurante.html",
    "comp/form-iniciar-sesion.html",
    "comp/form-sesion.html",
    "comp/form-usuarios.html",
    "comp/form-servicio.html",
    "comp/form-rol",
    "css/codepoints",
    "css/colores.css",
    "css/estilos.css",
    "css/material-icons.css",
    "css/MaterialIcons-Regular.eot",
    "css/MaterialIcons-Regular.ijmap",
    "css/MaterialIcons-Regular.svg",
    "css/MaterialIcons-Regular.ttf",
    "css/MaterialIcons-Regular.woff",
    "css/MaterialIcons-Regular.woff2",
    "css/roboto.css",
    "css/tipografia.css",
    "fonts/roboto-v16-latin-300.eot",
    "fonts/roboto-v16-latin-300.svg",
    "fonts/roboto-v16-latin-300.ttf",
    "fonts/roboto-v16-latin-300.woff",
    "fonts/roboto-v16-latin-300.woff2",
    "fonts/roboto-v16-latin-500.eot",
    "fonts/roboto-v16-latin-500.svg",
    "fonts/roboto-v16-latin-500.ttf",
    "fonts/roboto-v16-latin-500.woff",
    "fonts/roboto-v16-latin-500.woff2",
    "fonts/roboto-v16-latin-regular.eot",
    "fonts/roboto-v16-latin-regular.svg",
    "fonts/roboto-v16-latin-regular.ttf",
    "fonts/roboto-v16-latin-regular.woff",
    "fonts/roboto-v16-latin-regular.woff2",
    "img/error-flat.png",
    "img/ic_launcher_48px.png",
    "img/ic_launcher_72px.png",
    "img/ic_launcher_96px.png",
    "img/ic_launcher_144px.png",
    "img/ic_launcher_192px.png",
    "img/mosaicocompro.png",
    "img/shopping-basket-full256.png",
    "img/vacio.png",
    "js/libs/webcomponentsjs/custom-elements-es5-adapter.js",
    "js/libs/webcomponentsjs/webcomponents-hi.js",
    "js/libs/webcomponentsjs/webcomponents-hi.js.map",
    "js/libs/webcomponentsjs/webcomponents-hi-ce.js",
    "js/libs/webcomponentsjs/webcomponents-hi-ce.js.map",
    "js/libs/webcomponentsjs/webcomponents-hi-sd-ce.js",
    "js/libs/webcomponentsjs/webcomponents-hi-sd-ce.js.map",
    "js/libs/webcomponentsjs/webcomponents-lite.js",
    "js/libs/webcomponentsjs/webcomponents-lite.js.map",
    "js/libs/webcomponentsjs/webcomponents-loader.js",
    "js/libs/webcomponentsjs/webcomponents-sd-ce.js",
    "js/libs/webcomponentsjs/webcomponents-sd-ce.js.map",
    "js/libs/MedWeb.js",
    "js/libs/util.js",
    "js/bdAbierta.js",
    "js/reserva.js",
    "js/CtrlAvisos.js",
    "js/InfoAviso.js",
    "js/sincronizacion.js",
    'index.html',
    "manifest.json",
    '/' // Separa esta URL de index.html
];
self.addEventListener('install', evt => {
    console.log("[Service Worker] Install.");
    // Realiza la instalación: carga los archivos requeridos en la cache.
    evt.waitUntil(
            caches.open(CACHE_RESERVA)
            .then(function (cache) {
                // Añade todas las dependencias fuera de línea al cache.
                return cache.addAll(REQUIRED_FILES);
            }));
});
self.addEventListener('activate', evt => {
    console.log("[Service Worker] Activo.");
});
self.addEventListener("push", evt => {
    let text = evt.data.text();
    console.log("[Service Worker] Push Recibido.");
    console.log(text);
    let data = JSON.parse(text);
    if (data.noticia && self.registration && self.registration.showNotification) {
        const options = {body: data.noticia, icon: "img/ic_launcher_72px.png"};
        evt.waitUntil(self.registration.showNotification("Reserva", options));
    } else {
        self.clients.claim()
                .then(() => self.clients.matchAll())
                .then(clientes => clientes.forEach(c => c.postMessage(data)));
    }
});
self.addEventListener('notificationclick', evt => {
    console.log('[Service Worker] Notification click Recibida.');
    evt.notification.close();
    evt.waitUntil(clients.openWindow('https://reserveatut.appspot.com/'));
});
// Toma de la caché archivos solicitados. Los otros son descargados.
self.addEventListener('fetch', function (evt) {
    if (evt.request.method === "GET") {
        evt.respondWith(
                caches.match(evt.request)
                .then(response => response || fetch(evt.request))
                );
    }
});