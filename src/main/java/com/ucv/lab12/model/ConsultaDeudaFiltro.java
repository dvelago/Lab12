package com.ucv.lab12.model;

import java.time.LocalDate;

public class ConsultaDeudaFiltro {

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String tipoDeuda;
    private String situacionLaboral;
    private String estado;
    private String texto;

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getTipoDeuda() {
        return tipoDeuda;
    }

    public void setTipoDeuda(String tipoDeuda) {
        this.tipoDeuda = tipoDeuda;
    }

    public String getSituacionLaboral() {
        return situacionLaboral;
    }

    public void setSituacionLaboral(String situacionLaboral) {
        this.situacionLaboral = situacionLaboral;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean tieneTexto() {
        return texto != null && !texto.trim().isEmpty();
    }

    public String resumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("Desde: ").append(fechaDesde == null ? "todos" : fechaDesde);
        sb.append(" | Hasta: ").append(fechaHasta == null ? "todos" : fechaHasta);
        sb.append(" | Tipo: ").append(orTodos(tipoDeuda));
        sb.append(" | Situacion: ").append(orTodos(situacionLaboral));
        sb.append(" | Estado: ").append(orTodos(estado));
        sb.append(" | Texto: ").append(orTodos(texto));
        return sb.toString();
    }

    private String orTodos(String value) {
        return value == null || value.trim().isEmpty() || "Todos".equalsIgnoreCase(value.trim())
                ? "Todos"
                : value.trim();
    }
}
