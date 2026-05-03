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
import proyecto2.pkg0.modelo.GestionService;
import proyecto2.pkg0.modelo.Repuesto;
import proyecto2.pkg0.vistas.VentanaRepuestos;


public class ControladorRepuestos {
    private GestionService service;
    private VentanaRepuestos ventana;
    
    public ControladorRepuestos(GestionService service) {
        this.service = service;
        this.ventana = new VentanaRepuestos(this);
    }
    
    public void mostrarVentana() {
        ventana.setVisible(true);
    }
    
    public java.util.List<Repuesto> listarRepuestos() {
        return service.listarRepuestos();
    }   
    
    public void guardarRepuesto(String codigo, String nombre, int stock, double precio, String proveedor) {
        Repuesto repuesto = new Repuesto(codigo, nombre, stock, precio, proveedor);
        service.agregarRepuesto(repuesto);
    }
    
    public void eliminarRepuesto(String codigo) {
        service.eliminarRepuesto(codigo);
    }
    
    public Repuesto buscarRepuesto(String codigo) {
        return service.buscarRepuesto(codigo);
    }
    
    public void actualizarStock(String codigo, int nuevoStock) {
        Repuesto repuesto = service.buscarRepuesto(codigo);
        if (repuesto != null) {
            repuesto.setStock(nuevoStock);
        }
    }
}
