package com.adminapp.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String cedula;
    private String nombre;
    private String pass;
    private long telefono;

    public Usuario(){
        super();
    }

    public Usuario(String cedula, String nombre, String pass, long telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.pass = pass;
        this.telefono = telefono;
    }

    public Usuario(String cedula, String nombre, long telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }
}
