package com.ucv.lab12.model;

import java.time.LocalDateTime;

public class Usuario {

    private int idUsuario;
    private String usuario;
    private String nombreCompleto;
    private String hashClave;
    private String saltClave;
    private String rol;
    private boolean activo = true;
    private LocalDateTime ultimoAcceso;
    private LocalDateTime fechaCreacion;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getHashClave() {
        return hashClave;
    }

    public void setHashClave(String hashClave) {
        this.hashClave = hashClave;
    }

    public String getSaltClave() {
        return saltClave;
    }

    public void setSaltClave(String saltClave) {
        this.saltClave = saltClave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return usuario + " - " + nombreCompleto;
    }
}
