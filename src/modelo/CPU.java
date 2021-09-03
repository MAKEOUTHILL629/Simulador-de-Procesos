package modelo;

import java.util.Observable;

public class CPU extends Observable implements Runnable {
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

                if(this.idProcesoEjecucion != 0){
                    System.out.println("El proceso " + this.getIdProcesoEjecucion() + " esta pasando por cpu");
                    Thread.sleep(tiempoEnCPU);
                    System.out.println("El proceso " + this.getIdProcesoEjecucion() + " termino de pasar por cpu");
                    setChanged();
                    notifyObservers(this.idProcesoEjecucion);

                }else{

                }
            }
        }catch (InterruptedException e){
            System.out.println("Por alguna razon el bloque no funciona");
            Thread.currentThread().interrupt();
        }

        System.out.println("Salio del metodo run en la cpu");
    }
}
