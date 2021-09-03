package simulador;

import modelo.ProcesoEntrada;
import modelo.Recurso;
import modelo.SistemaOperativo;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Recurso> recursosMocks = Arrays.asList(new Recurso(1, "numero 1", "e/s"),
                new Recurso(2, "numero 2", "datos"),
                new Recurso(3, "numero 3", "disco duro"),
                new Recurso(4, "numero 4", "Sonido"),
                new Recurso(5, "numero 5", "Pantalla"),
                new Recurso(6, "numero 6", "Ram"));

        SistemaOperativo so = new SistemaOperativo(recursosMocks.get(0), recursosMocks.get(1), recursosMocks.get(2), recursosMocks.get(3), recursosMocks.get(4), recursosMocks.get(5));

        so.crearProceso(
                new ProcesoEntrada(1,
                        "Oficee 1",
                        100,
                        "",
                        1,
                        Arrays.asList(recursosMocks.get(0), recursosMocks.get(1), recursosMocks.get(2))
                ));


        so.crearProceso(new ProcesoEntrada(2, "Oficee 2",
                100,
                "",
                1,
                Arrays.asList(recursosMocks.get(0), recursosMocks.get(1), recursosMocks.get(2))
        ));

        so.crearProceso(new ProcesoEntrada(3, "Oficee 3",
                100,
                "",
                1,
                Arrays.asList(recursosMocks.get(4), recursosMocks.get(1), recursosMocks.get(5))
        ));


        so.crearProceso(new ProcesoEntrada(4, "Oficee 4",
                100,
                "",
                1,
                Arrays.asList(recursosMocks.get(3), recursosMocks.get(1))
        ));


        so.crearProceso(new ProcesoEntrada(5, "Oficee 5",
                100,
                "",
                1,
                Arrays.asList(recursosMocks.get(4), recursosMocks.get(2))
        ));


        so.crearProceso(new ProcesoEntrada(6, "Oficee 6",
                100,
                "",
                1,
                Arrays.asList(recursosMocks.get(5))
        ));

        so.crearProceso(new ProcesoEntrada(7, "Oficee 7",
                100,
                "",
                1,
                Arrays.asList(recursosMocks.get(3))
        ));

        so.run();
    }
}
