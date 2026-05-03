/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2.pkg0.persistencia;

/**
 *
 * @author nse15
 */
import proyecto2.pkg0.modelo.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatosIniciales {
    
    public static void exportarReporte(GestionService service, String rutaArchivo) throws IOException {
        List<String[]> lineas = new ArrayList<>();
        
        lineas.add(new String[]{"ID Orden", "Cliente", "Equipo", "Diagnóstico", "Estado", "Fecha Ingreso", "Fecha Estimada"});
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (OrdenTrabajo ot : service.listarOrdenes()) {
            lineas.add(new String[]{
                ot.getId(),
                ot.getCliente().getNombre(),
                ot.getEquipo().toString(),
                ot.getDiagnostico() != null ? ot.getDiagnostico() : "Sin diagnóstico",
                ot.getEstado(),
                ot.getFechaIngreso().format(formatter),
                ot.getFechaEstimadaEntrega() != null ? ot.getFechaEstimadaEntrega().format(formatter) : "No calculada"
            });
        }
        
        ArchivoUtils.guardarCSV(lineas, rutaArchivo);
    }
}
