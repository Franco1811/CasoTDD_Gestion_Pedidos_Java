# Caso TDD: Sistema de Gestión de Pedidos

Proyecto práctico del curso **Calidad y Pruebas de Software (Ciclo VIII - UPN)**, enfocado en el desarrollo guiado por pruebas (**TDD**) utilizando **Java 17**, **JUnit 5** y **Maven**.

---

## Integrantes (Grupo 24020)
* Franco Emerson Gonzalez Poma
* Jesus Jeanfranco Medina Cabello
* Nicole Darlene Tito Huacho
* Angelo Zair Zavala Matute

---

## Descripción del Proyecto
El objetivo del proyecto es implementar la lógica de cálculo del total de un pedido para una tienda en línea peruana (subtotal, descuentos, cálculo del IGV al 18% y total final con redondeo comercial).

Todo el desarrollo se construyó aplicando de manera estricta el ciclo **Red 🔴 -> Green 🟢 -> Refactor 🔵**, asegurando que cada funcionalidad naciera a partir de una prueba unitaria previa.

## Puntos clave del diseño:
* **Uso de `BigDecimal`:** Se evitaron los tipos `double` o `float` para prevenir errores de precisión en operaciones financieras.
* **Escala y Redondeo:** Manejo explícito de 2 decimales con `RoundingMode.HALF_UP` en todas las operaciones monetarias.
* **Técnicas de Prueba:** Aplicación de pruebas parametrizadas (`@ParameterizedTest`), fuentes CSV (`@CsvSource`), conjuntos de valores (`@ValueSource`) y análisis de valores límite (BVA).

---

## Ciclos TDD Implementados
1. **Ciclo 1:** Cálculo básico de subtotal sumando precios.
2. **Ciclo 2:** Multiplicación de precio por cantidad y ajuste de escala para pedidos vacíos (`0.00`).
3. **Ciclo 3:** Aplicación de cupones de descuento (0% a 100%) sobre el subtotal.
4. **Ciclo 4:** Cálculo del IGV (18%) sobre la base imponible después del descuento.
5. **Ciclo 5:** Cálculo del total del pedido y verificación del redondeo a 2 decimales.
6. **Ciclo 6:** Validaciones de negocio con valores límite y excepciones (`IllegalArgumentException`).
7. **Ciclo 7:** Refactorización final (extracción de constantes, métodos privados de validación y método centralizado de redondeo).

---

## Ejecución de Pruebas
Para compilar el proyecto y ejecutar la suite completa de pruebas unitarias (15 casos de prueba):

```powershell
./mvnw test
```

---

## Convención de Commits
El historial de Git refleja de forma transparente la progresión del laboratorio, utilizando los prefijos requeridos:
* `red:` Registro de la prueba fallando o error de compilación.
* `green:` Implementación mínima para que la prueba pase.
* `refactor:` Mejoras de código, eliminación de duplicidad o documentación sin alterar el comportamiento.
