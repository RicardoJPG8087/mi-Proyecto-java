/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2.pkg0.controlador;

/**
 *
 * @author nse15
 */

import proyecto2.pkg0.modelo.*;
import proyecto2.pkg0.modelo.excepciones.*;
import proyecto2.pkg0.vistas.VentanaOrdenes;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Map;

public class ControladorOrdenes {
    private GestionService service;
    private VentanaOrdenes ventana;
    
    public ControladorOrdenes(GestionService service) {
        this.service = service;
        this.ventana = new VentanaOrdenes(this);
    }
    
    public void mostrarVentana() {
        ventana.setVisible(true);
    }
    
    public boolean existeOrden(String id) {
        try {
            service.buscarOrden(id);
            return true;
        } catch (OrdenNoEncontradaException e) {
            return false;
        }
    }
    
    public java.util.List<OrdenTrabajo> listarOrdenes() {
        return service.listarOrdenes();
    }
    
    public OrdenTrabajo buscarOrdenPorId(String id) {
        try {
            return service.buscarOrden(id);
        } catch (OrdenNoEncontradaException e) {
            JOptionPane.showMessageDialog(ventana, e.getMessage());
            return null;
        }
    }

    public void guardarOrden(String id, String clienteInfo, String tecnicoInfo, 
                              String estado, String diagnostico, String repuestosStr) {
        try {
            Cliente cliente = null;
            if (clienteInfo != null && clienteInfo.contains(" - ")) {
                String rutCliente = clienteInfo.split(" - ")[0];
                cliente = service.buscarCliente(rutCliente);
            }

            Tecnico tecnico = null;
            if (tecnicoInfo != null && tecnicoInfo.contains(" - ")) {
                String idTecnico = tecnicoInfo.split(" - ")[0];
                tecnico = service.listarTecnicos().stream()
                    .filter(t -> t.getId().equals(idTecnico))
                    .findFirst().orElse(null);
            }

            OrdenTrabajo orden;
            boolean esNueva = false;
            try {
                orden = service.buscarOrden(id);
            } catch (OrdenNoEncontradaException e) {
                orden = new OrdenTrabajo(id, cliente, 
                    new Equipo("SN-" + id, "Genérico", "Modelo Base", "Desktop"));
                service.agregarOrden(orden);
                esNueva = true;
            }

            orden.setEstado(estado);
            orden.setDiagnostico(diagnostico);

            if (tecnico != null) {
                service.asignarTecnicoAOrden(id, tecnico);
            }

            if (repuestosStr != null && !repuestosStr.trim().isEmpty()) {
                String[] lineas = repuestosStr.split("\n");
                for (String linea : lineas) {
                    if (linea.contains(":")) {
                        String[] partes = linea.split(":");
                        try {
                            orden.agregarRepuesto(partes[0].trim(), Integer.parseInt(partes[1].trim()));
                        } catch (NumberFormatException nfe) {
                            // Ignorar líneas mal formateadas
                        }
                    }
                }
            }

            if ("Finalizada".equalsIgnoreCase(estado)) {
                service.finalizarOrden(id);
            }

            JOptionPane.showMessageDialog(ventana, "Orden " + (esNueva ? "creada" : "actualizada") + " con éxito.");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ventana, "Error al procesar la orden: " + ex.getMessage());
        }
    }
    
    public void calcularFechaEstimada(String id) {
        try {
            OrdenTrabajo orden = service.buscarOrden(id);
            LocalDate fechaEstimada = service.calcularFechaEstimada(orden);
            orden.setFechaEstimadaEntrega(fechaEstimada);
            
            JOptionPane.showMessageDialog(ventana, 
                "Fecha estimada de entrega: " + fechaEstimada);
        } catch (OrdenNoEncontradaException | StockInsuficienteException e) {
            JOptionPane.showMessageDialog(ventana, "Error: " + e.getMessage());
        }
    }
    
    public void registrarCliente(String rut, String nombre) {
        service.agregarCliente(new Cliente(rut, nombre));
    }
    
    public void eliminarOrden(String id) {
        try {
            service.eliminarOrden(id);
            JOptionPane.showMessageDialog(ventana, "Orden eliminada");
        } catch (OrdenNoEncontradaException e) {
            JOptionPane.showMessageDialog(ventana, e.getMessage());
        }
    }
    
    // ========== NUEVOS MÉTODOS PARA MEJORAS ==========
    
    public Map<String, Mejora> getCatalogoMejoras() {
        return service.getCatalogoMejoras();
    }
    
    public boolean agregarMejoraAOrden(String idOrden, String codigoMejora) {
        try {
            service.agregarMejoraAOrden(idOrden, codigoMejora);
            return true;
        } catch (OrdenNoEncontradaException e) {
            JOptionPane.showMessageDialog(ventana, "Error: " + e.getMessage());
            return false;
        }
    }
    // =================================================
    
    public java.util.List<Cliente> getClientes() {
        return service.listarClientes();
    }

    public java.util.List<Tecnico> getTecnicos() {
        return service.listarTecnicos();
    }
}