package dds.monedero.model;

import java.time.LocalDate;

//En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
//siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
public abstract class Movimiento {
  private LocalDate fecha;
  private double monto;

  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public boolean fueDepositado(LocalDate fecha) {
    return esDeposito() && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return !esDeposito() && esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public abstract boolean esDeposito();


  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

}
