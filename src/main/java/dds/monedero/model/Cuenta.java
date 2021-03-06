package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    this.validarMontoNoNegativo(cuanto);
    this.validarMaximaCantidadDepositos();
    this.agregarMovimiento(new Deposito(LocalDate.now(),cuanto));
    this.modificarSaldo(cuanto);
  }

  public void sacar(double cuanto) {
    this.validarMontoNoNegativo(cuanto);
    this.validarSaldoNoMenor(cuanto);
    this.validarMaximaExtraccionDiaria(cuanto);
    this.agregarMovimiento(new Extraccion(LocalDate.now(),cuanto));
    this.modificarSaldo(-cuanto);

  }

  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha)).mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public void validarMontoNoNegativo(double monto){
    if(monto <= 0){
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarMaximaCantidadDepositos(){
    if(this.cantidadDeDepositos() >= 3){
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void validarSaldoNoMenor(double monto){
    if((saldo - monto) < 0){
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  public void validarMaximaExtraccionDiaria(double monto){
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if(monto > limite){
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

  public long cantidadDeDepositos(){
    return movimientos.stream().filter(Movimiento::esDeposito).count();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  void modificarSaldo(double monto){
    this.saldo += monto;
  }

}
