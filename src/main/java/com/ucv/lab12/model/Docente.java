package com.ucv.lab12.model;

public class Docente {
    private int idDocente;
    private String dni;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;

    // Constructor vacío
    public Docente() {}

    // Constructor completo
    public Docente(int idDocente, String dni, String nombres, String apellidos, String correo, String telefono) {
        this.idDocente = idDocente;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}