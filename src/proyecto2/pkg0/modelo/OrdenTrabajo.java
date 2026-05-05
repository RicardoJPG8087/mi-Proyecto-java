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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class OrdenTrabajo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private Cliente cliente;
    private Equipo equipo;
    private Tecnico tecnicoAsignado;
    private LocalDate fechaIngreso;
    private LocalDate fechaEstimadaEntrega;
    private String diagnostico;
    private String estado;
    private Map<String, Integer> repuestosUsados;
    private boolean analisisRealizado;
    private List<Mejora> mejorasSolicitadas;
    private double costoMejoras;
    private String tipoServicio;
    
    public OrdenTrabajo(String id, Cliente cliente, Equipo equipo) {
        this.id = id;
        this.cliente = cliente;
        this.equipo = equipo;
        this.fechaIngreso = LocalDate.now();
        this.estado = "Pendiente";
        this.repuestosUsados = new HashMap<>();
        this.mejorasSolicitadas = new ArrayList<>();
        this.costoMejoras = 0.0;
        this.tipoServicio = "REPARACION";
        this.analisisRealizado = false;
    }
    
    public OrdenTrabajo(String id, Cliente cliente, Equipo equipo, Tecnico tecnico) {
        this(id, cliente, equipo);
        this.tecnicoAsignado = tecnico;
    }
    
    public void agregarRepuesto(String codigo, int cantidad) {
        repuestosUsados.put(codigo, repuestosUsados.getOrDefault(codigo, 0) + cantidad);
    }
    public void agregarRepuesto(String codigo) {
        agregarRepuesto(codigo, 1);
    }
    
    public void eliminarRepuesto(String codigo) {
        repuestosUsados.remove(codigo);
    }
    
    public double calcularCostoTotal(Map<String, Repuesto> inventario) {
        double total = 0;
        for (Map.Entry<String, Integer> entry : repuestosUsados.entrySet()) {
            Repuesto r = inventario.get(entry.getKey());
            if (r != null) {
                total += r.getPrecio() * entry.getValue();
            }
        }
        return total;
    }
    public void agregarMejora(Mejora mejora) {
        if (mejora == null) return;
        
        for (Mejora m : mejorasSolicitadas) {
            if (m.getCodigo().equals(mejora.getCodigo())) {
                return;
            }
        }
        
        mejorasSolicitadas.add(mejora);
        costoMejoras += mejora.getCosto();
        
        if (tipoServicio.equals("REPARACION")) {
            tipoServicio = "AMBOS";
        } else if (tipoServicio.equals("MEJORA")) {
            tipoServicio = "AMBOS";
        }
        
        if (mejora.getRepuestoNecesario() != null && !mejora.getRepuestoNecesario().isEmpty()) {
            agregarRepuesto(mejora.getRepuestoNecesario(), 1);
        }
    }
    
    public void eliminarMejora(String codigoMejora) {
        Mejora aEliminar = null;
        for (Mejora m : mejorasSolicitadas) {
            if (m.getCodigo().equals(codigoMejora)) {
                aEliminar = m;
                break;
            }
        }
        
        if (aEliminar != null) {
            costoMejoras -= aEliminar.getCosto();
            mejorasSolicitadas.remove(aEliminar);
            
            if (mejorasSolicitadas.isEmpty() && tipoServicio.equals("AMBOS")) {
                tipoServicio = "REPARACION";
            }
        }
    }
    
    public boolean tieneMejoras() {
        return !mejorasSolicitadas.isEmpty();
    }
    
    public double getCostoTotal() {
        return (repuestosUsados.size() * 5000) + 15000 + costoMejoras;
    }
    
    public List<Mejora> getMejorasSolicitadas() {
        return Collections.unmodifiableList(mejorasSolicitadas);
    }
    public void setMejorasSolicitadas(List<Mejora> mejorasSolicitadas) { 
        this.mejorasSolicitadas = mejorasSolicitadas;
        this.costoMejoras = 0;
        for (Mejora m : mejorasSolicitadas) {
            this.costoMejoras += m.getCosto();
        }
    }
    public double getCostoMejoras() { return costoMejoras; }
    public void setCostoMejoras(double costoMejoras) { this.costoMejoras = costoMejoras; }
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
    public Tecnico getTecnicoAsignado() { return tecnicoAsignado; }
    public void setTecnicoAsignado(Tecnico tecnicoAsignado) { this.tecnicoAsignado = tecnicoAsignado; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public LocalDate getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public void setFechaEstimadaEntrega(LocalDate fechaEstimadaEntrega) { this.fechaEstimadaEntrega = fechaEstimadaEntrega; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { 
        this.diagnostico = diagnostico;
        this.analisisRealizado = true;
    }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Map<String, Integer> getRepuestosUsados() {
        return Collections.unmodifiableMap(repuestosUsados);
    }
    public boolean isAnalisisRealizado() { return analisisRealizado; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("OT-%s | Cliente: %s | Estado: %s | Ingreso: %s", 
            id, cliente.getNombre(), estado, fechaIngreso.format(formatter));
    }
}
