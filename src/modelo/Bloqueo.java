package modelo;

import java.util.Objects;
import java.util.Observable;

public class Bloqueo extends Observable implements Runnable {
    private int procesoTiempoEjecucion;
    private final Long tiempoEnBloqueado;
    private boolean parar;

    public Bloqueo(int procesoTiempoEjecucion, Long tiempoEnBloqueado) {
        this.procesoTiempoEjecucion = procesoTiempoEjecucion;
        this.tiempoEnBloqueado = tiempoEnBloqueado;
    }


    public int getProcesoTiempoEjecucion() {
        return procesoTiempoEjecucion;
    }

    public void setProcesoTiempoEjecucion(int procesoTiempoEjecucion) {
        this.procesoTiempoEjecucion = procesoTiempoEjecucion;
    }

    public void stop() {
        this.parar = true;
    }

    @Override
    public void run() {
        try {
            while (!parar) {
                Thread.sleep(tiempoEnBloqueado);
                setChanged();
                notifyObservers(this.procesoTiempoEjecucion);
                parar = true;
            }
        } catch (InterruptedException e) {
            System.out.println("Por alguna razon el bloque no funciona");
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bloqueo bloqueo = (Bloqueo) o;
        return procesoTiempoEjecucion == bloqueo.procesoTiempoEjecucion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(procesoTiempoEjecucion, tiempoEnBloqueado, parar);
    }

    @Override
    public String toString() {
        return "Bloqueo { " +
                " procesoTiempoEjecucion = " + procesoTiempoEjecucion +
                ", tiempoEnBloqueado = " + tiempoEnBloqueado +
                " } ";
    }
}
