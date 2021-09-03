package modelo;

import java.util.ArrayList;
import java.util.List;

public class ProcesoEntrada {
    private int id;
    private String nombre;
    private long tamanio;
    private String privilegios;
    private int cantidadHilos;
    private List<Recurso> recursosNecesitados;

    public ProcesoEntrada(int id, String nombre, long tamanio, String privilegios, int cantidadHilos,List<Recurso> recursosNecesitados) {
        this.id =id;
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.privilegios = privilegios;
        this.cantidadHilos = cantidadHilos;
        this.recursosNecesitados = recursosNecesitados;
    }

    public ProcesoEntrada(int id, String nombre, long tamanio) {
        this.id = id;
        this.nombre = nombre;
        this.tamanio = tamanio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getTamanio() {
        return tamanio;
    }

    public void setTamanio(long tamanio) {
        this.tamanio = tamanio;
    }

    public String getPrivilegios() {
        return privilegios;
    }

    public void setPrivilegios(String privilegios) {
        this.privilegios = privilegios;
    }

    public int getCantidadHilos() {
        return cantidadHilos;
    }

    public void setCantidadHilos(int cantidadHilos) {
        this.cantidadHilos = cantidadHilos;
    }

    public List<Recurso> getRecursosNecesitados() {
        return recursosNecesitados;
    }

    public void setRecursosNecesitados(List<Recurso> recursosNecesitados) {
        this.recursosNecesitados = recursosNecesitados;
    }

    @Override
    public String toString() {
        return "ProcesoEntrada{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tamanio=" + tamanio +
                ", privilegios='" + privilegios + '\'' +
                ", cantidadHilos=" + cantidadHilos +
                ", recursosNecesitados=" + recursosNecesitados +
                '}';
    }
}
