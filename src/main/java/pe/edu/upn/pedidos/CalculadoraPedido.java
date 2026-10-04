package pe.edu.upn.pedidos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculadoraPedido {

    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");

    public BigDecimal calcularSubtotal(List<Producto> productos) {
        for (Producto p : productos) {
            if (p.precio().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }
            if (p.cantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
            }
        }
        return productos.stream()
                .map(p -> p.precio().multiply(BigDecimal.valueOf(p.cantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal aplicarDescuento(BigDecimal subtotal, BigDecimal porcentaje) {
        if (porcentaje.compareTo(BigDecimal.ZERO) < 0 || porcentaje.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        }
        BigDecimal descuento = subtotal.multiply(porcentaje)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return subtotal.subtract(descuento).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularImpuesto(BigDecimal baseImponible) {
        return baseImponible.multiply(TASA_IGV).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularTotal(List<Producto> productos, BigDecimal porcentaje) {
        BigDecimal subtotal = calcularSubtotal(productos);
        BigDecimal baseImponible = aplicarDescuento(subtotal, porcentaje);
        BigDecimal impuesto = calcularImpuesto(baseImponible);
        return baseImponible.add(impuesto).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularTotal(List<Producto> productos) {
        return calcularTotal(productos, BigDecimal.ZERO);
    }
}
