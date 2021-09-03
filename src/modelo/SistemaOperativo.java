package modelo;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SistemaOperativo extends Observable implements Observer {
    private List<ProcesoTiempoEjecucion> procesosSistema;
    private List<Integer> nuevos;
    private List<Integer> listos;
    private CPU cpu;
    private List<Bloqueo> bloqueados;
    private List<RecursoTiempoEjecucion> recursos;
    private Long tiempoCPUOcupado;
    private final Long tiempoEnCpu = Long.valueOf(10000);
    private final Long tiempoEnBloqueado = Long.valueOf(10000);
    Thread hiloCpu;

    public SistemaOperativo(Recurso... recursos) {
        System.out.println("Se inicio el sistema operativo");
        this.tiempoCPUOcupado = 0l;
        this.recursos = new ArrayList<>();
        for (Recurso recurso :
                recursos) {
            this.agregarRecurso(recurso);
        }
        this.nuevos = new ArrayList<>();
        this.listos = new ArrayList<>();
        this.bloqueados = new ArrayList<>();
        this.procesosSistema = new ArrayList<>();
        this.cpu = new CPU(tiempoEnCpu);
        cpu.addObserver(this);
        this.addObserver(this.cpu);
        this.hiloCpu = new Thread(this.cpu, "Cpu");

    }

    public void agregarRecurso(Recurso recurso) {
        System.out.println("Se agrego un recurso " + recurso.getNombre());
        this.recursos.add(new RecursoTiempoEjecucion(recurso));
    }

    public ProcesoTiempoEjecucion obtenerProceso(int id) {

        return this.procesosSistema.stream().filter(proceso -> proceso.getProceso().getId() == id).findAny().orElseGet(() -> new ProcesoTiempoEjecucion(new ProcesoEntrada(0, "no existe", 0)));
    }

    public void crearProceso(ProcesoEntrada procesoEntrada) {
        System.out.println("Se ingreso un proceso en el sistema " + procesoEntrada);
        ProcesoTiempoEjecucion procesoNuevo = new ProcesoTiempoEjecucion(procesoEntrada);
        procesoNuevo.setEstado(Estado.NUEVO);
        this.procesosSistema.add(procesoNuevo);
        this.nuevos.add(procesoEntrada.getId());
    }

    private void agregarNuevoProceso(int id) {

        ProcesoTiempoEjecucion proceso = this.obtenerProceso(id);
        proceso.setEstado(Estado.LISTO);
        System.out.println("se agrego a listo el proceso " + proceso);
        this.listos.add(proceso.getProceso().getId());
    }

    private boolean asignarRecursosAlProceso(int idProceso) {
        ProcesoTiempoEjecucion procesoTiempoEjecucion = this.obtenerProceso(idProceso);
        AtomicInteger contador = new AtomicInteger();
        procesoTiempoEjecucion.getProceso().getRecursosNecesitados().forEach(recurso -> {
            this.recursos.forEach(recursosSistema -> {
                if (recurso.getId() == recursosSistema.getId() && recursosSistema.getIdProcesoEjecucion() == 0) {
                    recursosSistema.setIdProcesoEjecucion(procesoTiempoEjecucion.getProceso().getId());
                    contador.getAndIncrement();
                }
            });
        });

        if (contador.get() == procesoTiempoEjecucion.getProceso().getRecursosNecesitados().size()) {
            System.out.println("Se le asignaron recursos " + procesoTiempoEjecucion.getProceso().getNombre());
            return true;
        } else {
            this.recursos.stream()
                    .filter(recursosSistema -> recursosSistema.getIdProcesoEjecucion() == procesoTiempoEjecucion.getProceso().getId())
                    .forEach(recursoTiempoEjecucion -> recursoTiempoEjecucion.setIdProcesoEjecucion(0));
            System.out.println("No se pudo asignar recursos");
            return false;
        }
    }

    private void quitarTodosLosRecursos(int idProceso){
        System.out.println("Se le quitaron todos los recurso a "+ idProceso );
        this.recursos.forEach(recursoTiempoEjecucion -> {
            if (recursoTiempoEjecucion.getIdProcesoEjecucion() == idProceso) {
                    recursoTiempoEjecucion.setIdProcesoEjecucion(0);
            }
        });
    }

    private void quitarRecursos(int idProceso){
        this.recursos.forEach(recursoTiempoEjecucion -> {
            if (recursoTiempoEjecucion.getIdProcesoEjecucion() == idProceso) {
                if (this.correrRandom()) {
                    recursoTiempoEjecucion.setIdProcesoEjecucion(0);
                    System.out.println("Se le quito el recurso al recurso con id " + idProceso );
                }
            }
        });
    }

    private boolean correrRandom() {
        System.out.println("corrio un random");
        if (Math.random() * 10 > 5) {
            return true;
        } else {
            return false;
        }
    }


    public void run() {
        System.out.println("Metodo run del sistema operativo");
        hiloCpu.start();
        hiloCpu.suspend();
            while (this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.TERMINADO).count() < this.procesosSistema.size()){

                this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.NUEVO).forEach(proceso -> {
                    this.agregarNuevoProceso(proceso.getProceso().getId());
                });

                if (this.cpu.getIdProcesoEjecucion() == 0) {
                    ProcesoTiempoEjecucion proceso = this.procesosSistema.stream().filter(procesoEncontrar -> procesoEncontrar.getEstado() == Estado.LISTO).findFirst().get();
                    if (this.asignarRecursosAlProceso(proceso.getProceso().getId())) {
                        System.out.println("El proceso " + proceso.getProceso().getNombre() + " entro en ejecucion");
                        proceso.setEstado(Estado.EJECUCION);
                        System.out.println("El id del proceso en cpu es  " + this.cpu.getIdProcesoEjecucion());
                        //this.cpu.setIdProcesoEjecucion(proceso.getProceso().getId());
                        this.setChanged();
                        this.notifyObservers(proceso.getProceso().getId());
                        this.hiloCpu.resume();
                        System.out.println("La memoria de la cpu es " + this.cpu);
                        this.listos.remove(proceso.getProceso().getId());
                    } else {
                        System.out.println("El proceso " + proceso + " entro se bloqueo");
                        proceso.setEstado(Estado.BLOQUEADO);
                        Bloqueo procesoBloqueado = new Bloqueo(proceso.getProceso().getId(), this.tiempoEnBloqueado);
                        procesoBloqueado.addObserver(this);
                        Thread hiloBloque = new Thread(procesoBloqueado);
                        hiloBloque.start();
                        this.bloqueados.add(procesoBloqueado);
                        System.out.println("Se bloqueo el proceso " + proceso.getProceso().getNombre());
                        this.listos.remove(proceso.getProceso().getId());
                    }
                    System.out.println("Se supone que se le asigno un proceso a la cpu");
                }
            }


    }

    @Override
    public void update(Observable o, Object arg) {
        int id = (int) arg;
        ProcesoTiempoEjecucion procesoTiempoEjecucion = this.obtenerProceso(id);
        switch (procesoTiempoEjecucion.getEstado()) {
            case BLOQUEADO:
                procesoTiempoEjecucion.setEstado(Estado.LISTO);
                procesoTiempoEjecucion.aniadirTiempoSistema(this.tiempoEnCpu);
                System.out.println("Salido de bloqueo " + procesoTiempoEjecucion.getProceso().getNombre());
                this.listos.add(id);
                this.bloqueados.remove(this.bloqueados.indexOf(this.bloqueados.stream().filter(bloqueo -> bloqueo.getProcesoTiempoEjecucion() == id).findFirst().get()));
                break;
            case EJECUCION:

                if(procesoTiempoEjecucion.getTiempoEnElSistema() < 1){
                    procesoTiempoEjecucion.setEstado(Estado.TERMINADO);
                    this.quitarTodosLosRecursos(id);
                }else{
                    procesoTiempoEjecucion.setEstado(Estado.LISTO);
                    procesoTiempoEjecucion.descontarTiempoSistema(this.tiempoEnCpu);
                    procesoTiempoEjecucion.aniadirTiempoSistema(this.tiempoEnCpu);
                    this.quitarRecursos(id);
                }
                this.hiloCpu.suspend();
                this.cpu.setIdProcesoEjecucion(0);
                System.out.println("Salido de ejecucion " + procesoTiempoEjecucion.getProceso().getNombre());
                break;
            default:

        }

    }

}
