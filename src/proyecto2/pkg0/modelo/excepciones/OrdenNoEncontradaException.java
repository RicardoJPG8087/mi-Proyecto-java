/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2.pkg0.modelo.excepciones;

/**
 *
 * @author nse15
 */
public class OrdenNoEncontradaException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public OrdenNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
