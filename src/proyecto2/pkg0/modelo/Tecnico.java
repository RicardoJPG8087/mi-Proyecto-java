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

public class Tecnico implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String nombre;
    private String especialidad;
    private int cargaTrabajo;
    
    public Tecnico(String id, String nombre, String especialidad, int cargaTrabajo) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.cargaTrabajo = cargaTrabajo;
    }
    
    public void incrementarCarga() { this.cargaTrabajo++; }
    public void deincrementarCarga() { if(cargaTrabajo > 0) this.cargaTrabajo--; }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public int getCargaTrabajo() { return cargaTrabajo; }
    public void setCargaTrabajo(int cargaTrabajo) { this.cargaTrabajo = cargaTrabajo; }
    
    @Override
    public String toString() {
        return nombre + " (" + especialidad + ") - Pedidos: " + cargaTrabajo;
    }
}
