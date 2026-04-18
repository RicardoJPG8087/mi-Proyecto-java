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
import proyecto2.pkg0.controlador.ControladorOrdenes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class VentanaOrdenes extends JFrame {
    private static final long serialVersionUID = 1L;
    private ControladorOrdenes controlador;
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar, txtId, txtDiagnostico;
    private JComboBox<String> cbEstado, cbCliente, cbTecnico;
    private JTextArea taRepuestos;
    
    // ========== NUEVOS COMPONENTES PARA MEJORAS ==========
    private JComboBox<String> cbMejoras;
    private JTextArea taMejoras;
    private JButton btnAgregarMejora;
    // ====================================================
    
    public VentanaOrdenes(ControladorOrdenes controlador) {
        this.controlador = controlador;
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setTitle("Gestión de Órdenes de Trabajo");
        setSize(950, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Panel de búsqueda (Norte)
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(15);
        panelBusqueda.add(txtBuscar);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarOrden(txtBuscar.getText()));
        panelBusqueda.add(btnBuscar);
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        panelBusqueda.add(btnActualizar);
        
        // Panel de botones de acción arriba junto a búsqueda
        JButton btnNuevo = new JButton("Nueva Orden");
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCalcularFecha = new JButton("Calcular Fecha Estimada");

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarOrden());
        btnEliminar.addActionListener(e -> eliminarOrden());
        btnCalcularFecha.addActionListener(e -> controlador.calcularFechaEstimada(txtId.getText()));

        panelBusqueda.add(btnNuevo);
        panelBusqueda.add(btnGuardar);
        panelBusqueda.add(btnEliminar);
        panelBusqueda.add(btnCalcularFecha);
        
        add(panelBusqueda, BorderLayout.NORTH);

        // 2. Tabla (Centro)
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Cliente", "Equipo", "Estado", "Diagnóstico", "Tipo Servicio", "Fecha Ingreso"}, 0);
        tablaOrdenes = new JTable(modeloTabla);
        add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        // 3. Preparación de componentes de Cliente
        cbCliente = new JComboBox<>();
        JButton btnNuevoCliente = new JButton("+");
        btnNuevoCliente.setPreferredSize(new Dimension(45, 25));
        btnNuevoCliente.addActionListener(e -> {
            String rut = JOptionPane.showInputDialog(this, "Ingrese RUT del cliente:");
            if (rut != null && !rut.trim().isEmpty()) {
                String nombre = JOptionPane.showInputDialog(this, "Ingrese Nombre completo:");
                if (nombre != null && !nombre.trim().isEmpty()) {
                    controlador.registrarCliente(rut, nombre);
                    cargarCombos();
                    cbCliente.setSelectedItem(rut + " - " + nombre);
                }
            }
        });

        JPanel panelComboCliente = new JPanel(new BorderLayout(5, 0));
        panelComboCliente.add(cbCliente, BorderLayout.CENTER);
        panelComboCliente.add(btnNuevoCliente, BorderLayout.EAST);

        // ========== PREPARACIÓN DE COMPONENTES DE MEJORAS ==========
        cbMejoras = new JComboBox<>();
        btnAgregarMejora = new JButton("+ Agregar Mejora");
        btnAgregarMejora.addActionListener(e -> agregarMejora());
        
        JPanel panelComboMejoras = new JPanel(new BorderLayout(5, 0));
        panelComboMejoras.add(cbMejoras, BorderLayout.CENTER);
        panelComboMejoras.add(btnAgregarMejora, BorderLayout.EAST);
        
        taMejoras = new JTextArea(3, 20);
        taMejoras.setEditable(false);
        taMejoras.setBackground(new Color(240, 240, 240));
        // ===========================================================

        // 4. Panel de edición (Sur) - CON MEJORAS
        JPanel panelEdicion = new JPanel(new GridLayout(8, 2, 5, 5));
        panelEdicion.setBorder(BorderFactory.createTitledBorder("Detalles de Orden"));

        panelEdicion.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelEdicion.add(txtId);

        panelEdicion.add(new JLabel("Cliente:"));
        panelEdicion.add(panelComboCliente);

        panelEdicion.add(new JLabel("Técnico:"));
        cbTecnico = new JComboBox<>();
        panelEdicion.add(cbTecnico);

        panelEdicion.add(new JLabel("Estado:"));
        cbEstado = new JComboBox<>(new String[]{"Pendiente", "En Reparación", "Completada", "Entregada", "Finalizada"});
        panelEdicion.add(cbEstado);

        panelEdicion.add(new JLabel("Diagnóstico:"));
        txtDiagnostico = new JTextField();
        panelEdicion.add(txtDiagnostico);

        panelEdicion.add(new JLabel("Repuestos (código:cantidad):"));
        taRepuestos = new JTextArea(3, 20);
        panelEdicion.add(new JScrollPane(taRepuestos));
        
        // NUEVAS FILAS PARA MEJORAS
        panelEdicion.add(new JLabel("Catálogo de Mejoras:"));
        panelEdicion.add(panelComboMejoras);
        
        panelEdicion.add(new JLabel("Mejoras solicitadas:"));
        panelEdicion.add(new JScrollPane(taMejoras));

        add(panelEdicion, BorderLayout.SOUTH);

        // Selección de fila
        tablaOrdenes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarOrdenSeleccionada();
            }
        });
    }
    
    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (OrdenTrabajo ot : controlador.listarOrdenes()) {
            modeloTabla.addRow(new Object[]{
                ot.getId(),
                ot.getCliente().getNombre(),
                ot.getEquipo().toString(),
                ot.getEstado(),
                ot.getDiagnostico() != null ? ot.getDiagnostico() : "Sin diagnóstico",
                ot.getTipoServicio(),
                ot.getFechaIngreso()
            });
        }
        cargarCombos();
    }
    
    private void cargarCombos() {
        // Limpiar para no duplicar
        cbTecnico.removeAllItems();
        cbCliente.removeAllItems();
        cbMejoras.removeAllItems();  // NUEVO

        // Cargar Técnicos
        for (Tecnico t : controlador.getTecnicos()) {
            cbTecnico.addItem(t.getId() + " - " + t.getNombre() + " (Carga: " + t.getCargaTrabajo() + ")");
        }

        // Cargar Clientes
        for (Cliente c : controlador.getClientes()) {
            cbCliente.addItem(c.getRut() + " - " + c.getNombre());
        }
        
        // ========== Cargar Catálogo de Mejoras ==========
        for (Mejora m : controlador.getCatalogoMejoras().values()) {
            cbMejoras.addItem(m.getCodigo() + " - " + m.getNombre() + " ($" + m.getCosto() + ")");
        }
        // ================================================
    }
    
    private void cargarOrdenSeleccionada() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila >= 0) {
            String id = (String) modeloTabla.getValueAt(fila, 0);
            OrdenTrabajo ot = controlador.buscarOrdenPorId(id);
            if (ot != null) {
                txtId.setText(ot.getId());
                txtDiagnostico.setText(ot.getDiagnostico());
                cbEstado.setSelectedItem(ot.getEstado());
                
                cbCliente.setSelectedItem(ot.getCliente().getRut() + " - " + ot.getCliente().getNombre());
                if (ot.getTecnicoAsignado() != null) {
                    cbTecnico.setSelectedItem(ot.getTecnicoAsignado().getId() + " - " + ot.getTecnicoAsignado().getNombre() + " (Carga: " + ot.getTecnicoAsignado().getCargaTrabajo() + ")");
                }
                
                // Mostrar repuestos
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Integer> entry : ot.getRepuestosUsados().entrySet()) {
                    sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
                }
                taRepuestos.setText(sb.toString());
                
                // ========== NUEVO: Mostrar mejoras ==========
                mostrarMejorasEnTextArea(ot);
                // ============================================
            }
        }
    }
    
    // ========== NUEVO MÉTODO: Mostrar mejoras en el TextArea ==========
    private void mostrarMejorasEnTextArea(OrdenTrabajo ot) {
        StringBuilder sb = new StringBuilder();
        if (ot.getMejorasSolicitadas() != null && !ot.getMejorasSolicitadas().isEmpty()) {
            for (Mejora m : ot.getMejorasSolicitadas()) {
                sb.append(m.getCodigo()).append(": ").append(m.getNombre());
                sb.append(" - $").append(m.getCosto()).append("\n");
            }
            sb.append("---\nCosto total mejoras: $").append(ot.getCostoMejoras());
        } else {
            sb.append("Sin mejoras solicitadas");
        }
        taMejoras.setText(sb.toString());
    }
    // ==================================================================
    
    // ========== NUEVO MÉTODO: Agregar mejora a la orden ==========
    private void agregarMejora() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero seleccione o cree una orden");
            return;
        }
        
        String mejoraSeleccionada = (String) cbMejoras.getSelectedItem();
        if (mejoraSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "No hay mejoras disponibles en el catálogo");
            return;
        }
        
        String codigoMejora = mejoraSeleccionada.split(" - ")[0];
        
        boolean exito = controlador.agregarMejoraAOrden(id, codigoMejora);
        if (exito) {
            cargarOrdenSeleccionada();
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Mejora agregada exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar la mejora");
        }
    }
    // ==================================================================
    
    private void guardarOrden() {
        try {
            String id = txtId.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID de la orden es obligatorio");
                return;
            }
            
            String clienteStr = (String) cbCliente.getSelectedItem();
            String tecnicoStr = (String) cbTecnico.getSelectedItem();
            String estado = (String) cbEstado.getSelectedItem();
            String diagnostico = txtDiagnostico.getText();
            String repuestosText = taRepuestos.getText();
            
            controlador.guardarOrden(id, clienteStr, tecnicoStr, estado, diagnostico, repuestosText);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Orden guardada exitosamente");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void eliminarOrden() {
        String id = txtId.getText();
        if (!id.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Eliminar orden " + id + "?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.eliminarOrden(id);
                cargarDatos();
                limpiarFormulario();
            }
        }
    }
    
    private void buscarOrden(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            cargarDatos();
            return;
        }
        
        modeloTabla.setRowCount(0);
        for (OrdenTrabajo ot : controlador.listarOrdenes()) {
            if (ot.getId().toLowerCase().contains(criterio.toLowerCase()) ||
                ot.getCliente().getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                modeloTabla.addRow(new Object[]{
                    ot.getId(),
                    ot.getCliente().getNombre(),
                    ot.getEquipo().toString(),
                    ot.getEstado(),
                    ot.getDiagnostico() != null ? ot.getDiagnostico() : "Sin diagnóstico",
                    ot.getTipoServicio(),
                    ot.getFechaIngreso()
                });
            }
        }
    }
    
    private void limpiarFormulario() {
        txtId.setText("");
        txtDiagnostico.setText("");
        taRepuestos.setText("");
        taMejoras.setText("");
        cbEstado.setSelectedIndex(0);
    }
}