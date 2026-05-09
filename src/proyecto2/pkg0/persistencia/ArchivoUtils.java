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
import java.io.*;
import java.util.*;

public class ArchivoUtils {
    
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> void guardarDatos(Map<String, T> datos, String nombreArchivo) 
            throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(datos);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> Map<String, T> cargarDatos(String nombreArchivo) 
            throws IOException, ClassNotFoundException {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            return (Map<String, T>) ois.readObject();
        }
    }
    
    public static void guardarCSV(List<String[]> datos, String nombreArchivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (String[] fila : datos) {
                pw.println(String.join(";", fila));
            }
        }
    }
    public static List<String[]> leerCSV(String nombreArchivo) throws IOException {
        List<String[]> datos = new ArrayList<>();
        File archivo = new File(nombreArchivo);
        
        if (!archivo.exists()) {
            return datos; // Retorna lista vacía si el archivo no existe aún
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    // Dividimos la línea por el punto y coma ";" que usas al guardar
                    datos.add(linea.split(";"));
                }
            }
        }
        return datos;
    }
}
