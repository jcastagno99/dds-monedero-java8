package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void PodemosDepositarUnMonto() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(),1500);
  }

  @Test
  void NoPodemosDepositarUnMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void PodemosDepositarTresVeces() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(),3856);
  }

  @Test
  void NoPodemosDepositarMasDeTresVeces() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void NoPodemosExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void NoPodemosExtraerMasDelLimitePermitido() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void NoPodemosExtraerUnMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  public void ElSaldoSeActualizaAlSacarDineroDeUnaCuenta() {
    cuenta.setSaldo(2000);
    cuenta.sacar(700);
    assertEquals(1300,cuenta.getSaldo());
  }

  @Test
  public void LosMovimientosSeRegistranCorrectamente() {
    cuenta.setSaldo(2000);
    cuenta.sacar(700);
    cuenta.poner(200);
    assertEquals(2, (long) cuenta.getMovimientos().size());
  }


}