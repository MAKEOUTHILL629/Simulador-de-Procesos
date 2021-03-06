package modelo;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SistemaOperativo extends Observable implements Observer, Runnable {
    private List<ProcesoTiempoEjecucion> procesosSistema;
    private List<Integer> nuevos;
    private List<Integer> listos;
    private CPU cpu;
    private List<Bloqueo> bloqueados;
    private List<RecursoTiempoEjecucion> recursos;
    private Long tiempoCPUOcupado;
    private Long tiempoPromedioProcesos;
    private Long tiempoBloqueado;
    private final Long tiempoEnCpu = Long.valueOf(3);
    private final Long tiempoEnBloqueado = Long.valueOf(3);
    private Thread hiloCpu;

    public List<ProcesoTiempoEjecucion> getProcesosSistema() {
        return procesosSistema;
    }

    public List<RecursoTiempoEjecucion> getRecursos() {
        return recursos;
    }

    public SistemaOperativo(Recurso... recursos) {

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
        this.tiempoBloqueado = 0l;
        this.cpu = new CPU(tiempoEnCpu * 1000);
        cpu.addObserver(this);
        this.hiloCpu = new Thread(this.cpu, "Cpu");
        hiloCpu.start();
        hiloCpu.suspend();
    }

    public void agregarRecurso(Recurso recurso) {
        this.recursos.add(new RecursoTiempoEjecucion(recurso));
    }

    public ProcesoTiempoEjecucion obtenerProceso(int id) {
        return this.procesosSistema.stream()
                .filter(proceso -> proceso.getProceso().getId() == id)
                .findAny()
                .orElseGet(() -> new ProcesoTiempoEjecucion(new ProcesoEntrada(0, "no existe", 0)));
    }

    public void crearProceso(ProcesoEntrada procesoEntrada) {
        ProcesoTiempoEjecucion procesoNuevo = new ProcesoTiempoEjecucion(procesoEntrada);
        procesoNuevo.setEstado(Estado.NUEVO);
        this.procesosSistema.add(procesoNuevo);
        this.nuevos.add(procesoEntrada.getId());
    }

    private void agregarNuevoProceso(int id) {
        ProcesoTiempoEjecucion proceso = this.obtenerProceso(id);

        proceso.setEstado(Estado.LISTO);
        proceso.setTiempoInicio();

        this.listos.add(proceso.getProceso().getId());
    }

    private boolean asignarRecursosAlProceso(int idProceso) {
        ProcesoTiempoEjecucion procesoTiempoEjecucion = this.obtenerProceso(idProceso);

        AtomicInteger contador = new AtomicInteger();

        procesoTiempoEjecucion.getProceso().getRecursosNecesitados().forEach(recurso -> {
            this.recursos.forEach(recursosSistema -> {
                if (recurso.getId() == recursosSistema.getId() && (recursosSistema.getIdProcesoEjecucion() == 0 || recursosSistema.getIdProcesoEjecucion() == procesoTiempoEjecucion.getProceso().getId())) {
                    contador.getAndIncrement();
                }
            });
        });

        if (contador.get() == procesoTiempoEjecucion.getProceso().getRecursosNecesitados().size()) {

            procesoTiempoEjecucion.getProceso().getRecursosNecesitados().forEach(recurso -> {
                this.recursos.forEach(recursosSistema -> {
                    if (recurso.getId() == recursosSistema.getId()) {
                        recursosSistema.setIdProcesoEjecucion(procesoTiempoEjecucion.getProceso().getId());
                    }
                });
            });

            return true;
        } else {
//            this.recursos.stream()
//                    .filter(recursosSistema -> recursosSistema.getIdProcesoEjecucion() == procesoTiempoEjecucion.getProceso().getId())
//                    .forEach(recursoTiempoEjecucion -> recursoTiempoEjecucion.setIdProcesoEjecucion(0));

            return false;
        }
    }

    private void quitarTodosLosRecursos(int idProceso) {

        this.recursos.forEach(recursoTiempoEjecucion -> {
            if (recursoTiempoEjecucion.getIdProcesoEjecucion() == idProceso) {
                recursoTiempoEjecucion.setIdProcesoEjecucion(0);
            }
        });
    }

    private void quitarRecursos(int idProceso) {

        this.recursos.forEach(recursoTiempoEjecucion -> {
            if (recursoTiempoEjecucion.getIdProcesoEjecucion() == idProceso) {
                if (this.correrRandom()) {
                    recursoTiempoEjecucion.setIdProcesoEjecucion(0);
                }
            }
        });

    }

    private boolean correrRandom() {
        if (Math.random() * 10 > 5) {

            return false;
        } else {
            return true;

        }
    }

    public boolean terminaronLosProcesos() {
        return this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.TERMINADO).count() < this.procesosSistema.size();
    }


    public List<ProcesoTiempoEjecucion> obtenerProcesosEstadoNuevo() {
        return this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.NUEVO).collect(Collectors.toList());
    }

    public List<ProcesoTiempoEjecucion> obtenerProcesosEstadoListo() {
        return this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.LISTO).collect(Collectors.toList());
    }

    public List<ProcesoTiempoEjecucion> obtenerProcesosEstadoEjecucion() {
        return this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.EJECUCION).collect(Collectors.toList());
    }

    public List<ProcesoTiempoEjecucion> obtenerProcesosEstadoTerminado() {
        return this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.TERMINADO).collect(Collectors.toList());
    }

    public List<ProcesoTiempoEjecucion> obtenerProcesosEstadoBloqueado() {
        return this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.BLOQUEADO).collect(Collectors.toList());
    }


    public void run() {

        long tiempoOcupado = System.currentTimeMillis();
        while (terminaronLosProcesos()) {

            this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.NUEVO).forEach(proceso -> {

                this.agregarNuevoProceso(proceso.getProceso().getId());
                this.nuevos.remove(this.nuevos.indexOf(proceso.getProceso().getId()));
            });

            this.procesosSistema.stream().filter(proceso -> proceso.getEstado() == Estado.LISTO).forEach(proceso -> {
                if (!this.listos.contains(proceso.getProceso().getId())) {
                    this.listos.add(proceso.getProceso().getId());
                }
            });

            if (this.cpu.getIdProcesoEjecucion() == 0 && !this.listos.isEmpty()) {
                if (this.listos.size() > 0) {
                    //ProcesoTiempoEjecucion proceso = this.procesosSistema.stream().filter(procesoEncontrar -> procesoEncontrar.getEstado() == Estado.LISTO).findFirst().get();
                    ProcesoTiempoEjecucion proceso = this.obtenerProceso(this.listos.stream().findFirst().orElseGet(() -> 0));

//                    System.out.println("Lista de procesos nuevos: \n " + this.nuevos);
//                    System.out.println();
//                    System.out.println();
//                    System.out.println("Lista de procesos listos: \n " + this.listos);
//                    System.out.println();
//                    System.out.println();
//                    System.out.println("Procesos CPU: \n " + this.cpu.getIdProcesoEjecucion());
//                    System.out.println();
//                    System.out.println();
//                    System.out.println("Lista de bloqueos CPU: \n " + this.bloqueados.toString());
//                    System.out.println();
//                    System.out.println();


                    if (this.asignarRecursosAlProceso(proceso.getProceso().getId())) {

                        proceso.setEstado(Estado.EJECUCION);
                        this.cpu.setIdProcesoEjecucion(proceso.getProceso().getId());
                        this.listos.remove(this.listos.indexOf(proceso.getProceso().getId()));
                        this.hiloCpu.resume();
                    } else {

                        proceso.setEstado(Estado.BLOQUEADO);
                        Bloqueo procesoBloqueado = new Bloqueo(proceso.getProceso().getId(), this.tiempoEnBloqueado * 1000);
                        procesoBloqueado.addObserver(this);
                        Thread hiloBloque = new Thread(procesoBloqueado);
                        hiloBloque.start();
                        this.bloqueados.add(procesoBloqueado);

                        this.listos.remove(this.listos.indexOf(procesoBloqueado.getProcesoTiempoEjecucion()));
                    }
                }
                System.out.println();
                System.out.println();
                System.out.println("Lista de procesos : \n " + this.procesosSistema);
                System.out.println("Lista de recursos : \n " + this.recursos);
                System.out.println();
                this.setChanged();
                this.notifyObservers();

            }
        }
        this.tiempoCPUOcupado = (System.currentTimeMillis() - tiempoOcupado) / 1000;
        this.setChanged();
        this.notifyObservers();

    }

    private void calcularTiempoPromedio() {
        AtomicInteger acumulador = new AtomicInteger();
        AtomicInteger cantidad = new AtomicInteger();
        this.procesosSistema.forEach(proceso -> {
            if (proceso.getEstado() == Estado.TERMINADO) {
                acumulador.addAndGet(Math.toIntExact(proceso.getTiempoSistiempoSistem()));
                cantidad.getAndIncrement();
            }
        });

        this.tiempoPromedioProcesos = Long.valueOf(acumulador.get() / cantidad.get());
    }


    public Long getTiempoBloqueado() {
        return tiempoBloqueado;
    }

    public Long getTiempoCPUOcupado() {
        return tiempoCPUOcupado;
    }

    public Long getTiempoPromedioProcesos() {
        return tiempoPromedioProcesos;
    }

    @Override
    public void update(Observable o, Object arg) {
        int id = (int) arg;
        ProcesoTiempoEjecucion procesoTiempoEjecucion = this.obtenerProceso(id);
        switch (procesoTiempoEjecucion.getEstado()) {
            case BLOQUEADO:

                procesoTiempoEjecucion.setEstado(Estado.LISTO);
                procesoTiempoEjecucion.aniadirTiempoSistema(this.tiempoEnCpu);

                //this.bloqueados.remove(this.bloqueados.indexOf(new Bloqueo(id, this.tiempoEnBloqueado)) == -1 ? 0 : this.bloqueados.indexOf(new Bloqueo(id, this.tiempoEnBloqueado)));
                this.bloqueados.remove(new Bloqueo(id, this.tiempoEnBloqueado));
                this.tiempoBloqueado += this.tiempoEnBloqueado;
                break;
            case EJECUCION:
                System.out.println("Se esta validando que el proceso paso por ejecucion " + id);
                if (procesoTiempoEjecucion.getTamanio() < 1) {
                    procesoTiempoEjecucion.setEstado(Estado.TERMINADO);
                    calcularTiempoPromedio();
                    procesoTiempoEjecucion.setTiempoFinal();
                    this.quitarTodosLosRecursos(id);
                    System.out.println("Se termino de ejecutar el proceso " + id);
                } else {
                    System.out.println("Ingreso en la validacion de que no termino el proceso " + id);
                    procesoTiempoEjecucion.descontarTiempoSistema(this.tiempoEnCpu);
                    procesoTiempoEjecucion.aniadirTiempoSistema(this.tiempoEnCpu);
                    if (procesoTiempoEjecucion.getTamanio() > 0) {
                        procesoTiempoEjecucion.setEstado(Estado.LISTO);
                        this.quitarRecursos(id);
                        System.out.println("El proceso " + id + " No termino por lo tanto pasa a cola");
                    } else {
                        procesoTiempoEjecucion.setEstado(Estado.TERMINADO);
                        calcularTiempoPromedio();
                        procesoTiempoEjecucion.setTiempoFinal();
                        this.quitarTodosLosRecursos(id);
                        System.out.println("El proceso termino en esta ultima ejecucion " + id);
                        this.cpu.setIdProcesoEjecucion(0);
                    }

                }
                this.cpu.setIdProcesoEjecucion(0);
                this.hiloCpu.suspend();
                System.out.println("El proceso " + id + " termino de pasar por cpu");
                break;
            default:

        }


    }

}
