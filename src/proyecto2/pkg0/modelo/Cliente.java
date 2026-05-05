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

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String rut;
    private String nombre;
    private String telefono;
    private String email;
    
    public Cliente(String rut, String nombre, String telefono, String email) {
        this.rut = rut;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }
    
    public Cliente(String rut, String nombre) {
        this(rut, nombre, "Sin teléfono", "Sin email");
    }
    
    
//------------------------------------------------------------------------------
    public String getRut() { 
        return rut; 
    }public void setRut(String rut) { 
        this.rut = rut; 
    }
    
    public String getNombre() { 
        return nombre; 
    }public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getTelefono() { 
        return telefono; 
    }public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    
    public String getEmail() { 
        return email; 
    }public void setEmail(String email) { 
        this.email = email; 
    }
//------------------------------------------------------------------------------   
    @Override
    public String toString() {
        return nombre + " (" + rut + ")";
    }
}