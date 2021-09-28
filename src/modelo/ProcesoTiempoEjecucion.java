package modelo;

public class ProcesoTiempoEjecucion {
    private ProcesoEntrada proceso;
    private Estado estado;
    private Long tamanio;
    private Long tiempoEnElSistema;
    private Long tiempoInicio;
    private Long tiempoFinal;

    public ProcesoTiempoEjecucion(ProcesoEntrada proceso) {
        this.proceso = proceso;
        this.tamanio = proceso.getTamanio();
        this.tiempoEnElSistema = 0l;
        this.estado = Estado.NUEVO;
        this.tiempoInicio = 0l;
        this.tiempoFinal = 0l;
    }

    public ProcesoEntrada getProceso() {
        return proceso;
    }

    public Estado getEstado() {
        return estado;
    }

    public Long getTamanio() {
        return tamanio;
    }

    public Long getTiempoEnElSistema() {
        return tiempoEnElSistema;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setTamanio(Long tamanio) {
        this.tamanio = tamanio;
    }

    public void setTiempoEnElSistema(Long tiempoEnElSistema) {
        this.tiempoEnElSistema = tiempoEnElSistema;
    }

    public void descontarTiempoSistema(Long tiempoCPU) {
        this.tamanio -= tiempoCPU;
    }
    public void setTiempoInicio(){

        if(this.tiempoInicio == 0){
            this.tiempoInicio = System.currentTimeMillis();
        }

    }

    public void setTiempoFinal(){
        this.tiempoFinal =  System.currentTimeMillis();
    }

    public Long getTiempoSistiempoSistem(){
        return this.tiempoFinal == 0 ?  System.currentTimeMillis() - this.tiempoInicio : this.tiempoFinal - this.tiempoInicio;
    }

    public void aniadirTiempoSistema(Long tiempoEnElSistema) {
        this.tiempoEnElSistema += tiempoEnElSistema;
    }

    @Override
    public String toString() {
        return " ProcesoTiempoEjecucion { " +
                " proceso = " + proceso.getNombre() +
                ", estado = " + estado +
                ", tamanio = " + tamanio +
                ", tiempoEnElSistema = " + tiempoEnElSistema +
                " } ";
    }
}

