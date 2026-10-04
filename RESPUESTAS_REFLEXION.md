# Preguntas de Reflexión - TDD Sistema de Gestión de Pedidos

### 1. En el ciclo 1 el código ignoraba la cantidad y aun así la prueba pasaba. ¿Qué te enseña esto sobre la elección de los datos de prueba?
**Respuesta:**  
Enseña que los datos de prueba deben ser cuidadosamente elegidos para desafiar exhaustivamente la lógica del sistema y evitar "falsos positivos" por coincidencia o simetría de datos. En el Ciclo 1, al definir los productos de prueba con `cantidad = 1`, la operación `precio * 1` daba exactamente el mismo valor que sumar solo los precios (`precio`). Un conjunto de pruebas riguroso debe emplear datos asimétricos, cantidades mayores a 1 y casos de frontera para forzar que el código de producción implemente el algoritmo completo y correcto, impidiendo que una solución incompleta sea aceptada erróneamente.

---

### 2. ¿Por qué se usa BigDecimal y no double para montos de dinero? ¿Por qué se crea desde un String y no desde un double?
**Respuesta:**  
- **BigDecimal vs double:** El tipo primitivo `double` utiliza aritmética de punto flotante binario bajo el estándar IEEE 754, lo cual produce imprecisiones de redondeo al representar fracciones decimales comunes (por ejemplo, `0.1 + 0.2` resulta en `0.30000000000000004`). En aplicaciones financieras y de facturación comercial, este error por acumulación de centésimas es inaceptable. `BigDecimal` garantiza una precisión decimal exacta con control total de la escala y de la política de redondeo.
- **Creación desde String vs double:** Debe crearse siempre mediante `String` (ej. `new BigDecimal("25.50")`) porque el constructor `new BigDecimal(double)` recibe un valor que ya perdió precisión en la memoria al convertirse a punto flotante (por ejemplo, `new BigDecimal(0.1)` almacena `0.1000000000000000055511151231257827021181583404541015625`). En contraste, el constructor con `String` garantiza que el valor interno sea exactamente el número de dígitos ingresados.

---

### 3. ¿Qué ocurrió con la escala de BigDecimal en los ciclos 2 y 4? ¿Qué alternativa a assertEquals existe para comparar solo el valor?
**Respuesta:**  
- **Comportamiento de la escala:** En el Ciclo 2, `BigDecimal.ZERO` posee una escala predeterminada de 0 (representado internamente como `"0"`), mientras que el caso de prueba exigía formato monetario a dos decimales (`"0.00"`). En el Ciclo 4, la multiplicación `90.00 * 0.18` generó un resultado con escala 4 (`16.2000`). Como el método `BigDecimal.equals` compara tanto el valor numérico como la escala, `16.20` y `16.2000` son evaluados como objetos diferentes. Por ello, fue necesario forzar explícitamente `.setScale(2, RoundingMode.HALF_UP)`.
- **Alternativa a assertEquals para comparar solo valor:** Si no se desea validar la escala sino únicamente la equivalencia del valor numérico, se utiliza `compareTo()` en conjunto con `assertTrue` (por ejemplo: `assertTrue(esperado.compareTo(actual) == 0)`), o la aserción de AssertJ: `assertThat(actual).isEqualByComparingTo(esperado);`.

---

### 4. ¿Qué valores límite del porcentaje de descuento probaste y por qué esos?
**Respuesta:**  
Se probaron los cuatro valores clave de la técnica de análisis de valores límite (BVA):
- **Límite inferior válido (`0%`):** Representa el caso de no aplicar descuento alguno; el subtotal permanece inalterado. Probado en el Ciclo 3.
- **Límite superior válido (`100%`):** Representa la exoneración total del monto; la base imponible resultante es `0.00`. Probado en el Ciclo 3.
- **Límite inferior inválido (`-1%`):** Inmediatamente por debajo del mínimo permitido. Se probó en el Ciclo 6 para asegurar que lance `IllegalArgumentException`.
- **Límite superior inválido (`101%`):** Inmediatamente por encima del máximo permitido. Se probó en el Ciclo 6 para asegurar que lance `IllegalArgumentException`.

---

### 5. La tienda decide que algunos productos estén exonerados de IGV. ¿Qué pruebas agregarías primero y qué parte del diseño cambiaría?
**Respuesta:**  
- **Pruebas a agregar primero (siguiendo TDD):**
  1. `totalDePedidoConProductoExoneradoNoAplicaIGV`: Probar un pedido con un único producto exonerado (ej. libros o insumos básicos) y verificar que el IGV sea `0.00` y el total coincida con el subtotal.
  2. `totalDePedidoMixtoAplicaIGVSoloAProductosGravados`: Probar un pedido con productos gravados y exonerados combinados, verificando que el 18% de IGV solo se calcule sobre la base imponible correspondiente a los productos gravados.
- **Cambios en el diseño:**
  1. **En `Producto`:** Incorporar una propiedad indicadora, ya sea un booleano `boolean exoneradoIGV` o un enum `TipoAfectacionIGV { GRAVADO, EXONERADO }`.
  2. **En `CalculadoraPedido`:** Modificar la lógica para calcular la base imponible gravada (filtrando productos no exonerados) y la base imponible exonerada; luego aplicar el impuesto únicamente a la base imponible gravada y sumarla a ambas bases para obtener el total final.
