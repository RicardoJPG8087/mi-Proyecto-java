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
import java.io.Serializable;

public class Repuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codigo;
    private String nombre;
    private int stock;
    private double precio;
    private String proveedor;
    
    public Repuesto(String codigo, String nombre, int stock, double precio, String proveedor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
        this.proveedor = proveedor;
    }
    
    public Repuesto(String codigo, String nombre, int stock) {
        this(codigo, nombre, stock, 0.0, "Desconocido");
    }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }
    
    @Override
    public String toString() {
        return nombre + " (Stock: " + stock + ") - $" + precio;
    }
}
