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
    
    private static int leerOpcion(Scanner scanner){
        while (true){
            try{ return Integer.parseInt(scanner.nextLine());
                
            } catch(NumberFormatException e){
                System.err.print("Entrada invalida. Ingrese un número: ");
            }
        }
    }
    
    private static double leerDouble(Scanner scanner){
        while (true){
            try{ 
                return Double.parseDouble(scanner.nextLine());
            } catch(NumberFormatException e){
                System.err.print("Entrada inválida. Ingrese un número (puede usar decimales): ");
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SISTEMA DE GESTIÓN DE SERVICIO TÉCNICO ===");
        System.out.println("Seleccione modo de ejecución:");
        System.out.println("1. Interfaz Gráfica (Ventanas)");
        System.out.println("2. Interfaz de Consola");
        System.out.print("Opción: ");
        
        
        if (leerOpcion(scanner) == 1) {
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
            
            int opcion = leerOpcion(scanner);
            
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
        for (OrdenTrabajo ot : service.listarOrdenes()) {
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
        for (Cliente c : service.listarClientes()) {
            if (c.getRut().equals(rut)) {
                cliente = c;
                break;
            }
        }
        
        if (cliente == null) {
            System.out.print("Cliente no existe. Crear nuevo - Nombre: ");
            String nombre = scanner.nextLine();
            cliente = new Cliente(rut, nombre);
            service.agregarCliente(cliente);
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

    while (true) {
        System.out.println("\n--- EDITAR ORDEN ---");
        System.out.println("1. Cambiar diagnóstico");
        System.out.println("2. Cambiar estado");
        System.out.println("3. Agregar repuesto");
        System.out.println("4. Eliminar repuesto");
        System.out.println("5. Agregar mejora");
        System.out.println("6. Volver");
        System.out.print("Opción: ");

        int op = leerOpcion(scanner);

        switch (op) {

            case 1:
                System.out.print("Nuevo diagnóstico: ");
                String diag = scanner.nextLine();
                orden.setDiagnostico(diag);
                break;

            case 2:
                System.out.print("Nuevo estado: ");
                String estado = scanner.nextLine();
                orden.setEstado(estado);
                break;

            case 3:
                System.out.print("Código repuesto: ");
                String cod = scanner.nextLine();

                System.out.print("Cantidad: ");
                int cant = leerOpcion(scanner);

                orden.agregarRepuesto(cod, cant);
                System.out.println("Repuesto agregado a la orden.");
                break;

            case 4:
                System.out.print("Código repuesto a eliminar: ");
                String codDel = scanner.nextLine();

                orden.getRepuestosUsados().remove(codDel);
                System.out.println("Repuesto eliminado.");
                break;
                
            case 5: 
                System.out.println("Mejoras disponibles:");
                for (Mejora m : service.getCatalogoMejoras().values()) {
                    System.out.println(m.getCodigo() + " - " + m.getNombre());
                }

                System.out.print("Código de mejora: ");
                String codMejora = scanner.nextLine();

                service.agregarMejoraAOrden(id, codMejora);
                break;

            case 6:
                service.actualizarOrden(id, orden);
                System.out.println("Cambios guardados.");
                return;
        }
    }
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
        System.out.println("4. Buscar repuesto");    
        System.out.println("5. Eliminar repuesto");  
        System.out.print("Opción: ");
        
        int op = leerOpcion(scanner);
        
        if (op == 1) {
            for (Repuesto r : service.listarRepuestos()) {
                System.out.println(r.getCodigo() + " - " + r.getNombre() + " - Stock: " + r.getStock() + " - $" + r.getPrecio());
            }
        } else if (op == 2) {
            System.out.print("Código: ");
            String cod = scanner.nextLine();
            System.out.print("Nombre: ");
            String nom = scanner.nextLine();
            System.out.print("Stock inicial: ");
            int stock = leerOpcion(scanner);
            System.out.print("Precio: ");
            double precio = leerDouble(scanner);
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
                int nuevoStock = leerOpcion(scanner);
                r.setStock(nuevoStock);
                System.out.println("Stock actualizado");
            } else {
                System.out.println("Repuesto no encontrado");
            }
        } else if (op == 4){
            System.out.print("Código del repuesto: ");
            String cod = scanner.nextLine();

            Repuesto r = service.buscarRepuesto(cod);
            if (r != null) {
                System.out.println("Encontrado: " + r.getNombre() + 
                " | Stock: " + r.getStock() + 
                " | Precio: $" + r.getPrecio());
            } else {
                System.out.println("Repuesto no encontrado");
            }
                
        } else if(op == 5){
            System.out.print("Código del repuesto a eliminar: ");
            String cod = scanner.nextLine();

            service.eliminarRepuesto(cod);
            System.out.println("Repuesto eliminado");
        }
        
    }
    
    private static void calcularFecha(GestionService service, Scanner scanner) throws Exception {
        System.out.print("ID de orden: ");
        String id = scanner.nextLine();

        OrdenTrabajo orden = service.buscarOrden(id);

        LocalDate fecha = service.calcularFechaEstimada(orden);
        orden.setFechaEstimadaEntrega(fecha);

        System.out.println("Fecha estimada de entrega: " + fecha);

        System.out.print("¿Desea confirmar orden y descontar stock? (s/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("s")) {
            service.confirmarOrden(id);
            System.out.println("Orden confirmada y stock actualizado.");
        }
    }   
}
