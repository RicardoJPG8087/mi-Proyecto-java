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

public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String serie;
    private String marca;
    private String modelo;
    private String tipo;
    
    public Equipo(String serie, String marca, String modelo, String tipo) {
        this.serie = serie;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }
    
    public Equipo(String serie, String marca, String modelo) {
        this(serie, marca, modelo, "Desktop");
    }
    
    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    @Override
    public String toString() {
        return marca + " " + modelo + " (" + tipo + ")";
    }
}
