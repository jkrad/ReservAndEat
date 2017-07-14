
"use strict";
class CtrlAvisos extends MedWeb {
    /**@param {HTMLFormElement} form
     * @param {Promise} infoPromesa */
    constructor(form, infoPromesa) {
        super(form, null);
        this.key = 0;
        Object.defineProperties(this, {
            infoPromesa: {value: infoPromesa},
            modeloForm: {value: {}},
            listenerSincronizado: {value: () => this.ejecuta("inicia")}
        });
    }
    inicia() {
        window.addEventListener("sincronizado", this.listenerSincronizado);
        super.inicia();
    }
    termina() {
        window.removeEventListener("sincronizado", this.listenerSincronizado);
    }
    enviaAlControlador() {
        switch (this.form.accion.value) {
            case "inicia":
            case "cerrar":
                this.inicializa();
                break;
            case "agregar":
                this.agrega();
                break;
            case "detalle":
                this.detalle(this.form.detalleId.value);
                break;
            case "submit":
                this.guarda();
                break;
            case "eliminar":
                this.elimina();
                break;
        }
    }
    inicializa() {
        this.modeloForm.alFrente = "maestro";
        this.infoPromesa
                .then(info => info.consulta())
                .then(lista => {
                    this.modeloForm.lista = lista;
                    this.finalizaEjecucion();
                });
    }
    finalizaEjecucion() {
        this.procesaModeloForm(this.modeloForm);
    }
    agrega() {
        this.configuraDetalle("true");
        this.modelo = {};
        this.modeloForm.detalleTitulo = "Aviso Nuevo";
        this.modeloForm.campoTituloMuestra = "true";
        this.modeloForm.campoFechaMuestra = "false";
        this.modeloForm.titulo = "";
        this.modeloForm.texto = "";
        this.finalizaEjecucion();
    }
    detalle(detalleId) {
        this.infoPromesa
                .then(info => info.busca(detalleId))
                .then(modelo => {
                    this.modelo = modelo;
                    this.configuraDetalle("false");
                    this.modeloForm.detalleTitulo = modelo.titulo;
                    this.modeloForm.campoTituloMuestra = "false";
                    this.modeloForm.campoFechaMuestra = "true";
                    this.modeloForm.fecha =
                            new Date(modelo.modificacion).toLocaleString();
                    this.modeloForm.texto = modelo.texto;
                    this.finalizaEjecucion();
                });
    }
    configuraDetalle(nuevo) {
        this.modeloForm.nuevo = nuevo;
        this.modeloForm.eliminarMuestra = nuevo === "true" ? "false" : "true";
        this.modeloForm.alFrente = "detalle";
    }
    guarda() {
        try {
            if (this.modeloForm.alFrente === "detalle") {
                this.llenaModelo();
                this.infoPromesa
                        .then(info => {
                            if (this.modeloForm.nuevo === "true") {
                                return info.agrega(this.modelo);
                            } else {
                                return info.modifica(this.modelo);
                            }
                        })
                        .then(() => this.inicializa());
            }
        } catch (e) {
            this.modeloForm.error = getMensaje(e);
            this.finalizaEjecucion();
        }
    }
    elimina() {
        if (this.modeloForm.nuevo !== "true") {
            this.infoPromesa
                    .then(info => info.elimina(this.modelo))
                    .then(() => this.inicializa());
        }
    }
    llenaModelo() {
        let error = false;
        if (this.modeloForm.nuevo === "true") {
            this.modelo.key = Date.now().toString() + Math.random() + this.key++;
            let titulo = this.form.titulo.value.trim();
            if (titulo) {
                this.modelo.titulo = titulo;
            } else {
                this.modeloForm.tituloError = "Falta el valor.";
                error = true;
            }
        }
        let texto = this.form.texto.value.trim();
        if (texto) {
            this.modelo.texto = texto;
        } else {
            this.modeloForm.textoError = "Falta el valor.";
            error = true;
        }
        if (error) {
            throw new Error("Error en los datos capturados.");
        }
    }
}