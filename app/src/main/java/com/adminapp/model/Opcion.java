package com.adminapp.model;


public class Opcion {

    private Integer idOpcion;
    private String descripcionOpcion;

    public Opcion(Integer idOpcion, String descripcionOpcion){
        this.idOpcion = idOpcion;
        this.descripcionOpcion = descripcionOpcion;
    }

    public Integer getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Integer idOpcion) {
        this.idOpcion = idOpcion;
    }

    public String getDescripcionOpcion() {
        return descripcionOpcion;
    }

    public void setDescripcionOpcion(String descripcionOpcion) {
        this.descripcionOpcion = descripcionOpcion;
    }

    @Override
    public String toString() {
        return "Opcion{" +
                "idOpcion=" + idOpcion +
                ", descripcionOpcion='" + descripcionOpcion + '\'' +
                '}';
    }
}
