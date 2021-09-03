package modelo;

public class RecursoTiempoEjecucion extends Recurso {
    private int idProcesoEjecucion;

    public RecursoTiempoEjecucion(int id, String nombre, String tipo) {
        super(id, nombre, tipo);
        this.idProcesoEjecucion = 0;
    }

    public RecursoTiempoEjecucion(Recurso recurso) {
        super(recurso.getId(), recurso.getNombre(), recurso.getTipo());
    }

    public int getIdProcesoEjecucion() {
        return idProcesoEjecucion;
    }

    public void setIdProcesoEjecucion(int idProcesoEjecucion) {
        this.idProcesoEjecucion = idProcesoEjecucion;
    }

    @Override
    public String toString() {
        return " RecursoTiempoEjecucion { " +
                " recurso =  " + this.getNombre() +
                " idProceso =" + idProcesoEjecucion +
                " } " ;
    }
}
