/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2.pkg0.vistas;

/**
 *
 * @author nse15
 */
import javax.swing.*;
import proyecto2.pkg0.controlador.ControladorPrincipal;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private ControladorPrincipal controlador;
    private JButton btnOrdenes, btnRepuestos, btnReportes, btnSalir;
    
    public VentanaPrincipal(ControladorPrincipal controlador) {
        this.controlador = controlador;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Servicio Técnico - Gestión de Órdenes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("Sistema de Gestión de Servicio Técnico", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);
        
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        btnOrdenes = new JButton("Gestionar Órdenes de Trabajo");
        btnRepuestos = new JButton("Gestionar Repuestos/Inventario");
        btnReportes = new JButton("Generar Reportes");
        btnSalir = new JButton("Salir");
        
        btnOrdenes.addActionListener(e -> controlador.mostrarVentanaOrdenes());
        btnRepuestos.addActionListener(e -> controlador.mostrarVentanaRepuestos());
        btnReportes.addActionListener(e -> controlador.generarReporte());
        btnSalir.addActionListener(e -> System.exit(0));
        
        panelBotones.add(btnOrdenes);
        panelBotones.add(btnRepuestos);
        panelBotones.add(btnReportes);
        panelBotones.add(btnSalir);
        
        add(panelBotones, BorderLayout.CENTER);
    }
}