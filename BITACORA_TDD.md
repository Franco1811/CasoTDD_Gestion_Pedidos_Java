# Bitácora de Ciclos TDD - Sistema de Gestión de Pedidos

| Ciclo | Prueba escrita | Red: ¿por qué falló? | Green: ¿qué cambiaste? | Refactor realizado |
| :---: | :--- | :--- | :--- | :--- |
| **1** | `subtotalDeDosProductosSumaSusPrecios` | No compilaba: las clases `Producto` y `CalculadoraPedido` no existían. | Se creó `Producto` (record) y `CalculadoraPedido.calcularSubtotal` sumando precios con Streams. | Métodos auxiliares `soles()` y `producto()` en la clase de prueba para evitar repetir `new BigDecimal`. |
