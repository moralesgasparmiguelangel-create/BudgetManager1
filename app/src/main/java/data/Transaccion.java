package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_transacciones")
public class Transaccion {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String concepto;
    private double monto;
    private String tipo;
    private String fechaHora;
    private String categoria;

    // CONSTRUCTOR 1: El constructor vacío que Room necesita para reconstruir los objetos desde la BD
    public Transaccion() {
    }

    // CONSTRUCTOR 2: El que tú usas en tus actividades (Asegúrate de que los nombres coincidan exactamente)
    public Transaccion(String concepto, double monto, String tipo, String fechaHora, String categoria) {
        this.concepto = concepto;
        this.monto = monto;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
        this.categoria = categoria;
    }

    // GETTERS Y SETTERS COMPLETOS (Room los necesita todos obligatoriamente si usas constructor vacío)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        String signo = tipo.equals("INGRESO") ? "+" : "-";
        return concepto + " [" + categoria + "] (" + fechaHora + ")\n" + signo + " $" + String.format("%.2f", monto);
    }
}