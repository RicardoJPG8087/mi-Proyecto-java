/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2.pkg0.modelo;

/**
 *
 * @author nse15
 */

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import proyecto2.pkg0.modelo.excepciones.*;
import java.util.Collections;

public class GestionService {
    private Map<String, OrdenTrabajo> ordenes;
    private Map<String, Repuesto> inventario;
    private List<Tecnico> tecnicos;
    private List<Cliente> clientes;
    
    // Catálogo de mejoras (se carga desde CSV)
    private Map<String, Mejora> catalogoMejoras;
    
    private final String RUTA_TECNICOS = "tecnicos.csv";
    private final String RUTA_INVENTARIO = "inventario.csv";
    private final String RUTA_CLIENTES = "clientes.csv";
    private final String RUTA_MEJORAS = "mejoras.csv";

    public GestionService() {
        this.ordenes = new HashMap<>();
        this.inventario = new HashMap<>();
        this.tecnicos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.catalogoMejoras = new HashMap<>();
        
        // Cargar datos desde archivos CSV al iniciar
        cargarTecnicosCSV();
        cargarInventarioCSV();
        cargarClientesCSV();
        cargarMejorasCSV();  // ← Carga mejoras desde CSV
    }

    // ==========================================
    //      CARGA Y GUARDADO DE DATOS (CSV)
    // ==========================================
    
    public void cargarTecnicosCSV() {
        File archivo = new File(RUTA_TECNICOS);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            tecnicos.clear();
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length >= 4) {
                    tecnicos.add(new Tecnico(p[0], p[1], p[2], Integer.parseInt(p[3])));
                }
            }
        } catch (Exception e) {
            System.err.println("Error técnicos: " + e.getMessage());
        }
    }
    
    public void guardarTecnicosCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_TECNICOS))) {
            for (Tecnico t : tecnicos) {
                pw.println(String.format("%s;%s;%s;%d", t.getId(), t.getNombre(), t.getEspecialidad(), t.getCargaTrabajo()));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar técnicos: " + e.getMessage());
        }
    }
    
    public void cargarInventarioCSV() {
        File archivo = new File(RUTA_INVENTARIO);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length >= 5) {
                    try {
                        String codigo = p[0];
                        String nombre = p[1];
                        double precio = Double.parseDouble(p[2]);
                        int stock = Integer.parseInt(p[3]);
                        String proveedor = p[4];
                    
                        Repuesto r = new Repuesto(codigo, nombre, stock, precio, proveedor);
                        inventario.put(codigo, r);
                    } catch (NumberFormatException e) {
                        System.err.println("Error en formato de número: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar inventario: " + e.getMessage());
        }
    }
    
    public void guardarInventarioCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_INVENTARIO))) {
            for (Repuesto r : inventario.values()) {
                pw.println(String.format(Locale.US, "%s;%s;%.2f;%d;%s", 
                    r.getCodigo(), r.getNombre(), r.getPrecio(), r.getStock(), r.getProveedor()));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar inventario: " + e.getMessage());
        }
    }

    public void cargarClientesCSV() {
        File archivo = new File(RUTA_CLIENTES);
        if (!archivo.exists()) return;
        clientes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length >= 2) {
                    clientes.add(new Cliente(p[0], p[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error clientes: " + e.getMessage());
        }
    }
    
    public void guardarClientesCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_CLIENTES))) {
            for (Cliente c : clientes) {
                pw.println(c.getRut() + ";" + c.getNombre());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
        }
    }
    
    // ========== CARGA DE MEJORAS DESDE CSV ==========
    private void cargarMejorasCSV() {
        File archivo = new File(RUTA_MEJORAS);
        if (!archivo.exists()) {
            System.out.println("Archivo mejoras.csv no encontrado. Creando por defecto...");
            crearMejorasPorDefecto();
            return;
        }
        
        catalogoMejoras.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // Saltar cabecera
                }
                String[] p = linea.split(";");
                if (p.length >= 6) {
                    try {
                        String codigo = p[0].trim();
                        String nombre = p[1].trim();
                        String descripcion = p[2].trim();
                        double costo = Double.parseDouble(p[3].trim());
                        int tiempoHoras = Integer.parseInt(p[4].trim());
                        String repuestoNecesario = p[5].trim();
                        if (repuestoNecesario.isEmpty()) repuestoNecesario = null;
                        
                        Mejora mejora = new Mejora(codigo, nombre, descripcion, costo, tiempoHoras, repuestoNecesario);
                        catalogoMejoras.put(codigo, mejora);
                    } catch (NumberFormatException e) {
                        System.err.println("Error en formato de número en mejoras.csv: " + linea);
                    }
                }
            }
            System.out.println("✓ Mejoras cargadas desde CSV: " + catalogoMejoras.size());
        } catch (IOException e) {
            System.err.println("Error al cargar mejoras: " + e.getMessage());
        }
    }
    
    private void crearMejorasPorDefecto() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_MEJORAS))) {
            pw.println("codigo;nombre;descripcion;costo;tiempoHoras;repuestoNecesario");
            pw.println("RAM-UPGRADE;Ampliación de RAM;Aumentar la memoria RAM del equipo para mejor rendimiento;45000;1;RAM-003");
            pw.println("RAM-UPGRADE-16;Ampliación de RAM a 16GB;Aumentar la memoria RAM a 16GB para rendimiento óptimo;85000;1;RAM-004");
            pw.println("SSD-UPGRADE;Disco SSD;Reemplazar disco duro por SSD para mayor velocidad;65000;2;SSD-001");
            pw.println("GPU-UPGRADE;Tarjeta Gráfica;Agregar tarjeta gráfica dedicada;150000;2;GPU-005");
            pw.println("PSU-UPGRADE;Fuente de Poder;Actualizar fuente de poder para mayor estabilidad;88000;1;PSU-004");
            System.out.println("✓ Archivo mejoras.csv creado por defecto");
        } catch (IOException e) {
            System.err.println("Error al crear mejoras.csv: " + e.getMessage());
        }
        // Recargar después de crear
        cargarMejorasCSV();
    }
    
    public void guardarMejorasCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_MEJORAS))) {
            pw.println("codigo;nombre;descripcion;costo;tiempoHoras;repuestoNecesario");
            for (Mejora m : catalogoMejoras.values()) {
                String repuesto = m.getRepuestoNecesario() != null ? m.getRepuestoNecesario() : "";
                pw.println(String.format(Locale.US, "%s;%s;%s;%.0f;%d;%s",
                    m.getCodigo(), m.getNombre(), m.getDescripcion(), 
                    m.getCosto(), m.getTiempoEstimadoHoras(), repuesto));
            }
            System.out.println("✓ Mejoras guardadas en CSV: " + catalogoMejoras.size());
        } catch (IOException e) {
            System.err.println("Error al guardar mejoras: " + e.getMessage());
        }
    }
    
    public void agregarNuevaMejora(Mejora mejora) {
        catalogoMejoras.put(mejora.getCodigo(), mejora);
        guardarMejorasCSV();
    }
    // ==================================================
    
    // ==========================================
    //    MÉTODOS QUE FALTABAN (SOLUCIÓN ERRORES)
    // ==========================================

    public void agregarCliente(Cliente cliente) {
        if (buscarCliente(cliente.getRut()) == null) {
            clientes.add(cliente);
            guardarClientesCSV();
        }
    }
    
    public Cliente buscarCliente(String rut) {
        return clientes.stream()
                .filter(c -> c.getRut().equals(rut))
                .findFirst()
                .orElse(null);
    }

    public void eliminarOrden(String id) throws OrdenNoEncontradaException {
        if (!ordenes.containsKey(id)) {
            throw new OrdenNoEncontradaException("La orden " + id + " no existe.");
        }
        ordenes.remove(id);
    }

    public void agregarRepuesto(Repuesto r) {
        inventario.put(r.getCodigo(), r);
        guardarInventarioCSV(); 
    }

    public void eliminarRepuesto(String codigo) {
        inventario.remove(codigo);
        guardarInventarioCSV();
    }

    public Repuesto buscarRepuesto(String codigo) {
        return inventario.get(codigo);
    }

    // ==========================================
    //   LÓGICA DE ÓRDENES Y TÉCNICOS
    // ==========================================

    public void asignarTecnicoAOrden(String idOrden, Tecnico tecnico) throws OrdenNoEncontradaException {
        OrdenTrabajo orden = buscarOrden(idOrden);
        orden.setTecnicoAsignado(tecnico);
        tecnico.incrementarCarga(); 
        guardarTecnicosCSV();
    }

    public void finalizarOrden(String idOrden) throws OrdenNoEncontradaException,StockInsuficienteException  {
        OrdenTrabajo orden = buscarOrden(idOrden);
        confirmarOrden(idOrden);
        orden.setEstado("Finalizada");
        if (orden.getTecnicoAsignado() != null) {
            orden.getTecnicoAsignado().deincrementarCarga();
        }
        guardarTecnicosCSV();
    }

    public OrdenTrabajo buscarOrden(String id) throws OrdenNoEncontradaException {
        OrdenTrabajo orden = ordenes.get(id);
        if (orden == null) throw new OrdenNoEncontradaException("Orden no encontrada: " + id);
        return orden;
    }

    public void agregarOrden(OrdenTrabajo orden) {
        ordenes.put(orden.getId(), orden);
    }
    
    public void actualizarOrden(String id, OrdenTrabajo ordenActualizada) {
        ordenes.put(id, ordenActualizada);
    }
    
    public void verificarStock(OrdenTrabajo orden) throws StockInsuficienteException {
        for (Map.Entry<String, Integer> entry : orden.getRepuestosUsados().entrySet()) {
            Repuesto r = inventario.get(entry.getKey());

            if (r == null || r.getStock() < entry.getValue()) {
                throw new StockInsuficienteException("Stock insuficiente: " + entry.getKey());
            }
        }
    }
    
    public void descontarStock(OrdenTrabajo orden) {
        for (Map.Entry<String, Integer> entry : orden.getRepuestosUsados().entrySet()) {
            Repuesto r = inventario.get(entry.getKey());
            r.setStock(r.getStock() - entry.getValue());
        }
        guardarInventarioCSV();
    }
    
    public void confirmarOrden(String idOrden) throws StockInsuficienteException,OrdenNoEncontradaException  {
        OrdenTrabajo orden = buscarOrden(idOrden);

        verificarStock(orden);
        descontarStock(orden);
    }


    public LocalDate calcularFechaEstimada(OrdenTrabajo orden) throws StockInsuficienteException {
        // Validación de stock antes de calcular
        verificarStock(orden);
        
        int dias = 3 + orden.getRepuestosUsados().size();
        
        // Agregar días por mejoras
        if (orden.tieneMejoras()) {
            dias += orden.getMejorasSolicitadas().size();
        }
        
        long pendientes = ordenes.values().stream().filter(ot -> ot.getEstado().equals("Pendiente")).count();
        if (pendientes > 5) dias += 2;
        return LocalDate.now().plusDays(dias);
    }
    
    // ========== MÉTODOS PARA MEJORAS ==========
    
    public Map<String, Mejora> getCatalogoMejoras() {
        return catalogoMejoras;
    }
    
    public Mejora buscarMejora(String codigo) {
        return catalogoMejoras.get(codigo);
    }
    
    public void agregarMejoraAOrden(String idOrden, String codigoMejora) throws OrdenNoEncontradaException {
        OrdenTrabajo orden = buscarOrden(idOrden);
        Mejora mejora = catalogoMejoras.get(codigoMejora);
        if (mejora != null) {
            orden.agregarMejora(mejora);
            System.out.println("Mejora " + mejora.getNombre() + " agregada a orden " + idOrden);
        }
    }
    
    public void eliminarMejoraDeOrden(String idOrden, String codigoMejora) throws OrdenNoEncontradaException {
        OrdenTrabajo orden = buscarOrden(idOrden);
        orden.eliminarMejora(codigoMejora);
        System.out.println("Mejora eliminada de orden " + idOrden);
    }
    
    // ==================================================

    // ==========================================
    //                GETTERS
    // ==========================================
    
    public List<OrdenTrabajo> listarOrdenes() {
        return new ArrayList<>(ordenes.values());
    }
    
    public List<Tecnico> listarTecnicos() {
    return Collections.unmodifiableList(tecnicos);
}

    public List<Cliente> listarClientes() {
        return Collections.unmodifiableList(clientes);
    }

    public List<Repuesto> listarRepuestos() {
        return new ArrayList<>(inventario.values());
    }
}