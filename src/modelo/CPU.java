package modelo;

import java.util.Observable;
import java.util.Observer;

public class CPU extends Observable implements Runnable, Observer {
    private int idProcesoEjecucion;
    private final Long tiempoEnCPU;
    private boolean parar;

    public CPU(Long tiempoEnCPU) {
        this.tiempoEnCPU = tiempoEnCPU;
        this.parar= false;
    }

    public int getIdProcesoEjecucion() {
        return idProcesoEjecucion;
    }

    public synchronized void setIdProcesoEjecucion(int idProcesoEjecucion) {
        System.out.println("SE USO EL SET DE LA CPU");
        this.idProcesoEjecucion = idProcesoEjecucion;
    }

    public Long getTiempoEnCPU() {
        return tiempoEnCPU;
    }

    public boolean isParar() {
        return parar;
    }

    public void setParar(boolean parar) {
        this.parar = parar;
    }

    public void stop(){
        this.parar = true;
    }

    @Override
    public void run() {
        System.out.println("Cpu en ejecucion");
        try{
            while(!parar){
                this.idProcesoEjecucion = this.getIdProcesoEjecucion();
                if(this.idProcesoEjecucion != 0){
                    System.out.println("El proceso " + this.getIdProcesoEjecucion() + " esta pasando por cpu");
                    Thread.sleep(tiempoEnCPU);
                    System.out.println("El proceso " + this.getIdProcesoEjecucion() + " termino de pasar por cpu");
                    setChanged();
                    notifyObservers(this.idProcesoEjecucion);

                }else{
                    System.out.println("No hay id en la cpu");
                }
            }
        }catch (InterruptedException e){
            System.out.println("Por alguna razon el bloque no funciona");
            Thread.currentThread().interrupt();
        }

        System.out.println("Salio del metodo run en la cpu");
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("SE USO EL SET DE LA CPU" + this);
        this.idProcesoEjecucion = (int) arg;

    }
}
