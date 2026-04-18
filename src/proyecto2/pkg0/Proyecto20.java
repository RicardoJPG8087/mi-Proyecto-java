/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2.pkg0;

/**
 *
 * @author nse15
 */
import proyecto2.pkg0.controlador.ControladorPrincipal;
import proyecto2.pkg0.modelo.*;
import proyecto2.pkg0.modelo.excepciones.*;
import proyecto2.pkg0.persistencia.DatosIniciales;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

public class Proyecto20 {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SISTEMA DE GESTIÓN DE SERVICIO TÉCNICO ===");
        System.out.println("Seleccione modo de ejecución:");
        System.out.println("1. Interfaz Gráfica (Ventanas)");
        System.out.println("2. Interfaz de Consola");
        System.out.print("Opción: ");
        
        int opcion = scanner.nextInt();
        
        if (opcion == 1) {
            SwingUtilities.invokeLater(() -> {
                new ControladorPrincipal().iniciar();
            });
        } else {
            ejecutarConsola();
        }
    }
    
    private static void ejecutarConsola() {
        GestionService service = new GestionService();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Listar órdenes de trabajo");
            System.out.println("2. Agregar orden de trabajo");
            System.out.println("3. Buscar orden de trabajo");
            System.out.println("4. Editar orden de trabajo");
            System.out.println("5. Eliminar orden de trabajo");
            System.out.println("6. Gestionar repuestos");
            System.out.println("7. Calcular fecha estimada de entrega");
            System.out.println("8. Generar reporte");
            System.out.println("9. Salir");
            System.out.print("Opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (opcion) {
                    case 1:
                        listarOrdenes(service);
                        break;
                    case 2:
                        agregarOrden(service, scanner);
                        break;
                    case 3:
                        buscarOrden(service, scanner);
                        break;
                    case 4:
                        editarOrden(service, scanner);
                        break;
                    case 5:
                        eliminarOrden(service, scanner);
                        break;
                    case 6:
                        gestionarRepuestos(service, scanner);
                        break;
                    case 7:
                        calcularFecha(service, scanner);
                        break;
                    case 8:
                        DatosIniciales.exportarReporte(service, "reporte_consola.csv");
                        System.out.println("Reporte generado: reporte_consola.csv");
                        break;
                    case 9:
                        System.out.println("¡Hasta luego!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static void listarOrdenes(GestionService service) {
        System.out.println("\n=== LISTADO DE ÓRDENES ===");
        for (OrdenTrabajo ot : service.getOrdenes().values()) {
            System.out.println(ot);
            System.out.println("  Diagnóstico: " + (ot.getDiagnostico() != null ? ot.getDiagnostico() : "Pendiente"));
            System.out.println("  Estado: " + ot.getEstado());
            System.out.println("  Repuestos: " + ot.getRepuestosUsados());
        }
    }
    
    private static void agregarOrden(GestionService service, Scanner scanner) throws Exception {
        System.out.print("ID de orden: ");
        String id = scanner.nextLine();
        System.out.print("RUT del cliente: ");
        String rut = scanner.nextLine();
        
        Cliente cliente = null;
        for (Cliente c : service.getClientes()) {
            if (c.getRut().equals(rut)) {
                cliente = c;
                break;
            }
        }
        
        if (cliente == null) {
            System.out.print("Cliente no existe. Crear nuevo - Nombre: ");
            String nombre = scanner.nextLine();
            cliente = new Cliente(rut, nombre);
            service.getClientes().add(cliente);
        }
        
        System.out.print("Marca del equipo: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo del equipo: ");
        String modelo = scanner.nextLine();
        
        OrdenTrabajo orden = new OrdenTrabajo(id, cliente, 
            new Equipo("SN" + id, marca, modelo, "Desktop"));
        service.agregarOrden(orden);
        System.out.println("Orden creada exitosamente");
    }
    
    private static void buscarOrden(GestionService service, Scanner scanner) {
        System.out.print("ID de orden a buscar: ");
        String id = scanner.nextLine();
        try {
            OrdenTrabajo orden = service.buscarOrden(id);
            System.out.println("Orden encontrada: " + orden);
            System.out.println("  Diagnóstico: " + orden.getDiagnostico());
            System.out.println("  Estado: " + orden.getEstado());
        } catch (OrdenNoEncontradaException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void editarOrden(GestionService service, Scanner scanner) throws Exception {
        System.out.print("ID de orden a editar: ");
        String id = scanner.nextLine();
        OrdenTrabajo orden = service.buscarOrden(id);
        
        System.out.print("Nuevo diagnóstico (Enter para mantener): ");
        String diag = scanner.nextLine();
        if (!diag.isEmpty()) {
            orden.setDiagnostico(diag);
        }
        
        System.out.print("Nuevo estado (Pendiente/En Reparación/Completada/Entregada): ");
        String estado = scanner.nextLine();
        if (!estado.isEmpty()) {
            orden.setEstado(estado);
        }
        
        service.actualizarOrden(id, orden);
        System.out.println("Orden actualizada");
    }
    
    private static void eliminarOrden(GestionService service, Scanner scanner) throws Exception {
        System.out.print("ID de orden a eliminar: ");
        String id = scanner.nextLine();
        service.eliminarOrden(id);
        System.out.println("Orden eliminada");
    }
    
    private static void gestionarRepuestos(GestionService service, Scanner scanner) {
        System.out.println("\n=== GESTIÓN DE REPUESTOS ===");
        System.out.println("1. Listar repuestos");
        System.out.println("2. Agregar repuesto");
        System.out.println("3. Actualizar stock");
        System.out.print("Opción: ");
        
        int op = scanner.nextInt();
        scanner.nextLine();
        
        if (op == 1) {
            for (Repuesto r : service.getInventario().values()) {
                System.out.println(r.getCodigo() + " - " + r.getNombre() + " - Stock: " + r.getStock() + " - $" + r.getPrecio());
            }
        } else if (op == 2) {
            System.out.print("Código: ");
            String cod = scanner.nextLine();
            System.out.print("Nombre: ");
            String nom = scanner.nextLine();
            System.out.print("Stock inicial: ");
            int stock = scanner.nextInt();
            System.out.print("Precio: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Proveedor: ");
            String prov = scanner.nextLine();
            
            service.agregarRepuesto(new Repuesto(cod, nom, stock, precio, prov));
            System.out.println("Repuesto agregado");
        } else if (op == 3) {
            System.out.print("Código del repuesto: ");
            String cod = scanner.nextLine();
            Repuesto r = service.buscarRepuesto(cod);
            if (r != null) {
                System.out.print("Nuevo stock: ");
                int nuevoStock = scanner.nextInt();
                r.setStock(nuevoStock);
                System.out.println("Stock actualizado");
            } else {
                System.out.println("Repuesto no encontrado");
            }
        }
    }
    
    private static void calcularFecha(GestionService service, Scanner scanner) throws Exception {
        System.out.print("ID de orden: ");
        String id = scanner.nextLine();
        OrdenTrabajo orden = service.buscarOrden(id);
        
        System.out.print("Ingrese repuestos necesarios (código:cantidad, separados por coma): ");
        String repuestosInput = scanner.nextLine();
        String[] repuestos = repuestosInput.split(",");
        for (String r : repuestos) {
            String[] partes = r.split(":");
            if (partes.length == 2) {
                try {
                    orden.agregarRepuesto(partes[0].trim(), Integer.parseInt(partes[1].trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Cantidad inválida para " + partes[0]);
                }
            }
        }
        
        LocalDate fecha = service.calcularFechaEstimada(orden);
        orden.setFechaEstimadaEntrega(fecha);
        System.out.println("Fecha estimada de entrega: " + fecha);
        
        System.out.print("¿Desea confirmar y descontar stock? (s/n): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("s")) {
            service.verificarYDescontarStock(orden);
            System.out.println("Stock actualizado. Orden en proceso.");
        }
    }
}
