package com.ucv.lab12.model;

import java.time.LocalDate;

public class Deuda {
    private int idDeuda;
    private int idDocente; // Relación con el docente
    private double monto;
    private String tipoDeuda; // Ej. "Administrativa", "Judicial", "Reintegro"
    private String estado;     // Ej. "Pendiente", "Pagada", "En Proceso"
    private LocalDate fechaRegistro;

    // Constructor vacío
    public Deuda() {}

    // Constructor completo
    public Deuda(int idDeuda, int idDocente, double monto, String tipoDeuda, String estado, LocalDate fechaRegistro) {
        this.idDeuda = idDeuda;
        this.idDocente = idDocente;
        this.monto = monto;
        this.tipoDeuda = tipoDeuda;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getIdDeuda() { return idDeuda; }
    public void setIdDeuda(int idDeuda) { this.idDeuda = idDeuda; }

    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getTipoDeuda() { return tipoDeuda; }
    public void setTipoDeuda(String tipoDeuda) { this.tipoDeuda = tipoDeuda; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}