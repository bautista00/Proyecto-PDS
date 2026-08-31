# Mejoras de Code Smells - Procesos de Desarrollo de Software

## Alcance

Se revisó el proyecto hasta **"Nombre de método muy largo" inclusive**, que es el alcance indicado en clase. No se trabajó todavía sobre los smells posteriores del PDF (Código muerto, Data Class, Primitive Obsession, Intimidad inapropiada, etc.).

El criterio aplicado fue mantener cambios simples y justificables: un code smell es una señal de que algo puede mejorarse, no una regla mecánica que obligue a modificar cualquier caso que se parezca superficialmente.

---

## 1. Código duplicado

### Problema detectado

Se encontraron validaciones y fragmentos de lógica repetidos en distintos servicios, especialmente en:

- validación de IDs;
- validación de DNI;
- validación de nombre y apellido;
- validación de domicilio;
- validación del motivo de consulta;
- comprobación de turnos futuros;
- validaciones comunes al registrar y actualizar turnos;
- estructura repetida de `puedeAtender(...)` en las especialidades de Odontólogo.

### Cambios realizados

Se creó `ValidacionesClinica` para centralizar las validaciones reutilizadas por las distintas clases de servicio.

Se creó `TurnoHistorialUtil.tieneTurnosFuturos(...)` para evitar repetir la misma lógica en Paciente y Odontólogo.

En `TurnoValidador` se extrajo `validarDatosComunesTurno(...)`, de forma que las validaciones compartidas entre el alta y la actualización de un turno se definan una sola vez.

En `Odontologo` se agregó `motivoContieneAlgunaPalabra(...)` para reutilizar la estructura común de búsqueda de palabras dentro del motivo de consulta.

### Resultado

Las reglas repetidas quedaron centralizadas y cualquier modificación futura puede realizarse en un único lugar, reduciendo inconsistencias y mejorando el mantenimiento.

---

## 2. Métodos largos

### Problema detectado

Se encontraron métodos que concentraban demasiadas tareas dentro de una misma implementación, principalmente en `TurnoServiceImpl`, `TurnoPanel`, `PersistenciaServicio` y `Main`.

### Cambios realizados

En `TurnoPanel` se dividió la construcción de la interfaz en métodos más pequeños y descriptivos, separando el armado de filas, campos, botones y filtros.

En `TurnoServiceImpl`, la modificación de un turno quedó separada en operaciones de validación y aplicación de cambios, evitando concentrar todo el flujo dentro de un único método extenso.

En `PersistenciaServicio` se separó la lectura y reconstrucción de cada tipo de entidad mediante métodos como:

- `cargarPacienteDesdeLinea(...)`
- `crearOdontologo(...)`
- `cargarTurnoDesdeLinea(...)`

En `Main` se simplificó el armado inicial de la aplicación utilizando `DependenciasClinica`.

### Resultado

Los métodos principales expresan con mayor claridad qué tarea realizan y delegan los detalles en operaciones más pequeñas, facilitando la lectura, el mantenimiento y las pruebas.

---

## 3. God Class / Long Class

### Problema detectado

`TurnoServiceImpl` concentraba demasiadas responsabilidades:

- registrar turnos;
- actualizar y modificar turnos;
- cambiar estados;
- eliminar turnos;
- realizar búsquedas y filtros;
- validar datos;
- validar conflictos de horario;
- sincronizar relaciones entre entidades;
- calcular montos.

Esto hacía que una sola clase conociera y coordinara demasiadas partes del sistema.

### Cambios realizados

Las responsabilidades se distribuyeron de la siguiente forma:

- `TurnoServiceImpl`: operaciones principales de agenda, como registrar, actualizar, modificar, cambiar estado y eliminar.
- `TurnoConsultaService`: búsquedas, filtros y cálculo de monto.
- `TurnoValidador`: validaciones de los datos del turno, resolución de entidades existentes y conflictos de horario.
- `Turno`: comportamiento relacionado con su propia vinculación y desvinculación con los historiales de Paciente, Odontólogo y Secretaria.

### Resultado

`TurnoServiceImpl` dejó de concentrar toda la lógica del módulo y cada componente tiene una responsabilidad más específica, aumentando la cohesión y reduciendo el acoplamiento.

---

## 4. Parámetros largos

### Problema detectado

Se encontraron métodos que recibían una gran cantidad de datos individuales.

Los casos más claros aparecían al registrar o editar pacientes y turnos, donde los Controllers debían recibir muchos parámetros sueltos y del mismo tipo.

También `MainFrame` necesitaba recibir varias dependencias por separado.

### Cambios realizados

Se agregaron objetos que agrupan los datos relacionados:

- `PacienteRegistro`
- `PacienteEdicion`
- `TurnoRegistro`
- `TurnoEdicion`

De esta forma, el Controller recibe un único objeto representativo de la operación en lugar de 6, 7, 9 o 10 parámetros independientes.

También se agregó `DependenciasClinica`, que agrupa las dependencias necesarias para construir la interfaz principal.

Como resultado, `MainFrame` recibe un único objeto `DependenciasClinica` en lugar de múltiples servicios y Controllers separados.

### Casos no modificados

Los constructores de `Paciente` y `Turno` todavía tienen 6 parámetros.

Se consideran candidatos secundarios, pero no se agregó Builder ni otra abstracción solamente para reducir un parámetro. En el estado actual del proyecto, hacerlo agregaría más complejidad que beneficio. Puede revisarse nuevamente si las entidades crecen.

---

## 5. Ruptura de encapsulamiento

### Problema detectado

Se encontraron cadenas de getters donde una clase necesitaba conocer la estructura interna de otra.

Ejemplos:

```java
paciente.getDomicilio().getCalle()
turno.getPaciente().getNombre()
turno.getOdontologo().getId()
```

Esto generaba dependencia directa sobre cómo estaban compuestas internamente las entidades.

### Cambios realizados

Se agregaron operaciones de mayor nivel para que las clases clientes pidan directamente la información que necesitan.

Ejemplos:

```java
paciente.getCalleDomicilio()
turno.getNombrePaciente()
turno.getIdOdontologo()
persona.getNombreCompleto()
```

También la lógica de vincular y desvincular un turno con los historiales de sus actores quedó encapsulada dentro de `Turno`.

### Resultado

Las vistas, repositorios, consultas y servicios necesitan conocer menos detalles internos de las entidades, reduciendo el acoplamiento estructural.

---

## 6. Herencia rechazada

### Resultado del análisis

**No se detectó un caso claro de herencia rechazada que requiera refactor.**

`Odontologo` define un contrato abstracto mediante métodos como:

- `getEspecialidad()`
- `getTarifaBase()`
- `puedeAtender()`

Las clases `OdontologoGeneral`, `Endodoncista` y `Ortodoncista` implementan ese contrato mediante `@Override`.

Esto no se considera herencia rechazada porque las subclases no están descartando o anulando un comportamiento concreto definido por el padre. Simplemente implementan un comportamiento que `Odontologo` dejó abstracto.

Además, conceptualmente las clases mantienen una relación válida de tipo **"es un Odontólogo"**.

---

## 7. Lazy Class

### Resultado del análisis

**No se detectó en el proyecto final una clase que deba eliminarse por hacer poco o nada.**

Las clases auxiliares creadas durante la refactorización tienen responsabilidades concretas:

- `TurnoValidador` centraliza reglas de validación.
- `TurnoConsultaService` concentra búsquedas y filtros.
- `ValidacionesClinica` evita repetición de reglas comunes.
- `TurnoHistorialUtil` concentra la comprobación de turnos futuros.

Por lo tanto, aunque algunas clases sean pequeñas, su existencia está justificada por una responsabilidad específica y reutilizable.

---

## 8. Complejidad artificial

### Resultado del análisis

No se incorporaron patrones de diseño ni capas adicionales únicamente "por las dudas".

Las soluciones se mantuvieron simples:

- métodos auxiliares para separar responsabilidades;
- DTOs para agrupar parámetros relacionados;
- clases específicas cuando existía una responsabilidad claramente diferenciada;
- comportamiento llevado al dominio cuando correspondía al propio objeto.

### Resultado

La refactorización busca reducir complejidad y no reemplazar un problema simple por una arquitectura innecesariamente más difícil de entender.

---

## 9. Nombre de variable muy corto

### Problema detectado

Se encontraron nombres de variables poco expresivos, especialmente en vistas, persistencia y recorridos de colecciones.

Ejemplos:

```text
p
o
s
t
d
sb
c
x
n
```

### Cambios realizados

Se reemplazaron por nombres que expresan qué representa cada variable.

Ejemplos:

```text
p  -> paciente
o  -> odontologo
s  -> secretaria
t  -> turno
d  -> domicilio
sb -> resultado / linea / campoActual
```

También se reemplazaron variables como `c`, `x` y `n` cuando el contexto requería un nombre más descriptivo.

Se mantuvieron nombres convencionales y locales como `i` para índices de bucles cortos y `e` para eventos o excepciones, porque su significado es inmediato dentro de ese contexto.

### Resultado

El código puede entenderse con menor necesidad de seguir mentalmente qué representa cada variable.

---

## 10. Nombre de método muy largo

### Resultado del análisis

**No se detectó un caso claro que deba modificarse solamente por la longitud del nombre.**

Existen métodos con nombres extensos, por ejemplo:

```java
buscarPorOdontologoYPaciente(...)
validarConflictoHorarioExcluyendoTurno(...)
```

Sin embargo, cada uno representa una única responsabilidad.

Acortar estos nombres únicamente para reducir su cantidad de caracteres haría que el código fuera menos expresivo.

El smell se consideraría más evidente si el propio nombre indicara que el método realiza varias acciones diferentes, por ejemplo mediante expresiones del tipo `hacerXyY(...)`.

---

## Verificación realizada

Se verificó el funcionamiento general del proyecto después de aplicar las mejoras:

- Compilación completa de todos los `.java` con `javac`: OK.
- Alta, modificación, filtros y eliminación de turnos: OK.
- Sincronización de historiales al cambiar Odontólogo o Secretaria: OK.
- Actualización de un turno inexistente: rechazada correctamente.
- Registro y edición mediante DTO de Paciente: OK.
- Guardado y carga de persistencia, incluyendo campos escapados con `;`: OK.
