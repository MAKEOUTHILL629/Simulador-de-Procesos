package modelo;

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
                notifyObservers(procesoTiempoEjecucion);
            }
        } catch (InterruptedException e) {
            System.out.println("Por alguna razon el bloque no funciona");
            Thread.currentThread().interrupt();
        }
    }
}
