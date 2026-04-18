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

public class Mejora implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codigo;
    private String nombre;
    private String descripcion;
    private double costo;
    private int tiempoEstimadoHoras;
    private String repuestoNecesario;
    
    public Mejora(String codigo, String nombre, String descripcion, 
                  double costo, int tiempoEstimadoHoras, String repuestoNecesario) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.tiempoEstimadoHoras = tiempoEstimadoHoras;
        this.repuestoNecesario = repuestoNecesario;
    }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public int getTiempoEstimadoHoras() { return tiempoEstimadoHoras; }
    public void setTiempoEstimadoHoras(int tiempoEstimadoHoras) { this.tiempoEstimadoHoras = tiempoEstimadoHoras; }
    public String getRepuestoNecesario() { return repuestoNecesario; }
    public void setRepuestoNecesario(String repuestoNecesario) { this.repuestoNecesario = repuestoNecesario; }
    
    @Override
    public String toString() {
        return nombre + " - $" + costo + " (" + tiempoEstimadoHoras + " hrs)";
    }
}