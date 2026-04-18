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
import proyecto2.pkg0.modelo.*;
import proyecto2.pkg0.controlador.ControladorRepuestos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaRepuestos extends JFrame {
    private static final long serialVersionUID = 1L;
    private ControladorRepuestos controlador;
    private JTable tablaRepuestos;
    private DefaultTableModel modeloTabla;
    private JTextField txtCodigo, txtNombre, txtStock, txtPrecio, txtProveedor;
    
    public VentanaRepuestos(ControladorRepuestos controlador) {
        this.controlador = controlador;
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setTitle("Gestión de Repuestos/Inventario");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{"Código", "Nombre", "Stock", "Precio", "Proveedor"}, 0) {
            private static final long serialVersionUID = 1L;
        };
        tablaRepuestos = new JTable(modeloTabla);
        add(new JScrollPane(tablaRepuestos), BorderLayout.CENTER);
        
        // Panel de edición
        JPanel panelEdicion = new JPanel(new GridLayout(5, 2, 5, 5));
        panelEdicion.setBorder(BorderFactory.createTitledBorder("Datos del Repuesto"));
        
        panelEdicion.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panelEdicion.add(txtCodigo);
        
        panelEdicion.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelEdicion.add(txtNombre);
        
        panelEdicion.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        panelEdicion.add(txtStock);
        
        panelEdicion.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelEdicion.add(txtPrecio);
        
        panelEdicion.add(new JLabel("Proveedor:"));
        txtProveedor = new JTextField();
        panelEdicion.add(txtProveedor);
        
        add(panelEdicion, BorderLayout.SOUTH);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar/Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar Tabla");
        
        btnAgregar.addActionListener(e -> guardarRepuesto());
        btnEliminar.addActionListener(e -> eliminarRepuesto());
        btnActualizar.addActionListener(e -> cargarDatos());
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        add(panelBotones, BorderLayout.NORTH);
        
        // Seleccionar fila
        tablaRepuestos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarRepuestoSeleccionado();
            }
        });
    }
    
    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (Repuesto r : controlador.listarRepuestos()) {
            modeloTabla.addRow(new Object[]{
                r.getCodigo(), 
                r.getNombre(), 
                r.getStock(), 
                r.getPrecio(), 
                r.getProveedor()
            });
        }
    }
    
    private void cargarRepuestoSeleccionado() {
        int fila = tablaRepuestos.getSelectedRow();
        if (fila >= 0) {
            txtCodigo.setText((String) modeloTabla.getValueAt(fila, 0));
            txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
            txtStock.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
            txtPrecio.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
            txtProveedor.setText((String) modeloTabla.getValueAt(fila, 4));
        }
    }
    
    private void guardarRepuesto() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            int stock = Integer.parseInt(txtStock.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            String proveedor = txtProveedor.getText().trim();
            
            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código y nombre son obligatorios");
                return;
            }
            
            controlador.guardarRepuesto(codigo, nombre, stock, precio, proveedor);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Repuesto guardado exitosamente");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock y precio deben ser números válidos");
        }
    }
    
    private void eliminarRepuesto() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un repuesto para eliminar");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el repuesto " + codigo + "?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            controlador.eliminarRepuesto(codigo);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Repuesto eliminado");
        }
    }
    
    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtStock.setText("");
        txtPrecio.setText("");
        txtProveedor.setText("");
    }
}
