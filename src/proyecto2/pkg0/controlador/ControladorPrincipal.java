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
import proyecto2.pkg0.persistencia.DatosIniciales;
import proyecto2.pkg0.vistas.*;

import javax.swing.*;
import java.io.IOException;

public class ControladorPrincipal {
    private GestionService service;
    private VentanaPrincipal ventanaPrincipal;
    
    public ControladorPrincipal() {
        this.service = new GestionService();
        this.ventanaPrincipal = new VentanaPrincipal(this);
    }
    
    public void iniciar() {
        ventanaPrincipal.setVisible(true);
    }
    
    public void mostrarVentanaOrdenes() {
        ControladorOrdenes controladorOrdenes = new ControladorOrdenes(service);
        controladorOrdenes.mostrarVentana();
    }
    
    public void mostrarVentanaRepuestos() {
    // 1. Creamos el controlador de repuestos pasando el servicio
    ControladorRepuestos controladorRepuestos = new ControladorRepuestos(service);
    
    // 2. Le decimos que muestre la ventana
    controladorRepuestos.mostrarVentana();
    }
    
    public void generarReporte() {
        try {
            DatosIniciales.exportarReporte(service, "reporte_ordenes.csv");
            JOptionPane.showMessageDialog(ventanaPrincipal, "Reporte generado: reporte_ordenes.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(ventanaPrincipal, "Error al generar reporte: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ControladorPrincipal().iniciar();
        });
    }
}
