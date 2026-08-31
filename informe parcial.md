# Mejoras de Code Smells - Procesos de Desarrollo de Software

## Alcance

Se revisó el proyecto hasta **"Nombre de método muy largo" inclusive**, que es el alcance indicado en clase. No se trabajó todavía sobre los smells posteriores del PDF (Código muerto, Data Class, Primitive Obsession, Intimidad inapropiada, etc.).

La idea aplicada fue mantener cambios simples y justificables: un code smell es una señal de mejora, no una regla mecánica que obligue a modificar todo.

## 1. Código duplicado

### Cambios mantenidos/mejorados
- Se mantiene `ValidacionesClinica` para centralizar validaciones repetidas.
- Se mantiene `TurnoHistorialUtil.tieneTurnosFuturos(...)` para evitar la misma lógica en Paciente y Odontólogo.
- `TurnoValidador` ahora concentra la validación común de turno en `validarDatosComunesTurno(...)`, evitando repetir casi el mismo bloque entre alta y actualización.
- Se extrajo a `Odontologo.motivoContieneAlgunaPalabra(...)` la estructura repetida de `puedeAtender(...)` de las especialidades.

## 2. Métodos largos

### Cambios
- Se conserva la separación de los métodos de armado de `TurnoPanel`.
- Se eliminaron handlers de una sola línea como `onGuardarAction() -> guardar()` porque agregaban indirección sin aportar una abstracción real.
- `TurnoServiceImpl.modificarTurno(...)` quedó dividido en validación y aplicación de cambios.
- `PersistenciaServicio` divide la lectura de registros mediante métodos como `cargarPacienteDesdeLinea(...)`, `crearOdontologo(...)` y `cargarTurnoDesdeLinea(...)`.
- `Main` quedó reducido al armado inicial mediante `DependenciasClinica`.

## 3. God Class / Long Class

### Problema original
`TurnoServiceImpl` concentraba altas, modificaciones, consultas, validaciones, relaciones y facturación.

### Solución final
- `TurnoServiceImpl`: operaciones principales de agenda (registrar, actualizar, modificar, cambiar estado y eliminar).
- `TurnoConsultaService`: búsquedas, filtros y cálculo de monto.
- `TurnoValidador`: validaciones y resolución de entidades existentes.
- `Turno`: mantiene su propia vinculación/desvinculación con los historiales de sus actores.

Se eliminó `TurnoAgendaService` de la versión parcial porque dejaba a `TurnoServiceImpl` como un delegador casi puro, creando un posible caso de Lazy Class por exceso de delegación.

También se eliminó `TurnoRelacionador`: la sincronización de un turno con sus propios actores se llevó al dominio (`Turno`) para evitar una capa adicional innecesaria.

## 4. Parámetros largos

### Cambios
Se agregaron objetos que agrupan los datos relacionados:
- `PacienteRegistro`
- `PacienteEdicion`
- `TurnoRegistro`
- `TurnoEdicion`

Por ejemplo, el Controller ya no recibe 9 o 10 datos sueltos para un paciente ni 6 o 7 datos sueltos para un turno.

También se agregó `DependenciasClinica`, por lo que `MainFrame` pasó de recibir 9 parámetros a recibir una única dependencia agrupadora.

### Casos no modificados intencionalmente
Los constructores de `Paciente` y `Turno` todavía tienen 6 parámetros. Se consideran candidatos secundarios, pero no se agregó Builder ni otra capa solamente para reducir un parámetro, porque en este estado generaría más complejidad que beneficio. Se puede revisar más adelante si esas entidades crecen.

## 5. Ruptura de encapsulamiento

### Cambios
Se eliminaron las cadenas de acceso al dominio que aparecían en UI, persistencia, repositorios y consultas.

Ejemplos anteriores:
- `paciente.getDomicilio().getCalle()`
- `turno.getPaciente().getNombre()`
- `turno.getOdontologo().getId()`

Ahora los objetos exponen operaciones de mayor nivel, por ejemplo:
- `paciente.getCalleDomicilio()`
- `turno.getNombrePaciente()`
- `turno.getIdOdontologo()`
- `persona.getNombreCompleto()`

La lógica de vincular/desvincular un turno con los historiales también quedó dentro de `Turno`, evitando que un servicio externo recorra su estructura interna.

## 6. Herencia rechazada

### Resultado
**No se detectó un caso claro que requiera refactor.**

`Odontologo` define un contrato abstracto (`getEspecialidad()`, `getTarifaBase()`, `puedeAtender()`) y `OdontologoGeneral`, `Endodoncista` y `Ortodoncista` implementan ese contrato con `@Override`.

Esto no es herencia rechazada: las subclases no están descartando un comportamiento concreto que no les corresponda, sino implementando comportamiento que el padre dejó abstracto. Además, conceptualmente todas mantienen la relación "es un Odontólogo".

## 7. Lazy Class

### Cambio principal
La versión parcial dejaba `TurnoServiceImpl` casi exclusivamente reenviando llamadas a `TurnoAgendaService` y `TurnoConsultaService`.

Se simplificó esa estructura:
- se eliminó `TurnoAgendaService`;
- `TurnoServiceImpl` recuperó las operaciones de agenda;
- se conserva `TurnoConsultaService` porque sí tiene una responsabilidad diferenciada;
- se eliminó `TurnoRelacionador` al poder ubicar ese comportamiento naturalmente en `Turno`.

## 8. Complejidad artificial

### Cambios
- Se eliminaron wrappers de eventos de una sola línea en `TurnoPanel`.
- Se redujo la cadena de delegación en el servicio de turnos.
- No se agregaron patrones adicionales donde un método u objeto simple era suficiente.

## 9. Nombre de variable muy corto

Se reemplazaron nombres poco expresivos, especialmente en vistas y persistencia:
- `p` -> `paciente`
- `o` -> `odontologo`
- `s` -> `secretaria`
- `t` -> `turno`
- `d` -> `domicilio`
- `sb` -> `resultado`, `linea`, `campoActual`
- `c`, `x`, `n` -> nombres que explican su función

Se conservaron nombres convencionales y locales como `i` para índices de bucles cortos y `e` para eventos/excepciones, porque su significado es inmediato en ese contexto.

## 10. Nombre de método muy largo

### Resultado
**No se detectó un caso claro que deba modificarse solamente por la longitud del nombre.**

Métodos como `buscarPorOdontologoYPaciente(...)` o `validarConflictoHorarioExcluyendoTurno(...)` tienen nombres extensos, pero expresan una única responsabilidad. Acortarlos de forma artificial haría el código menos expresivo.

## Corrección funcional detectada durante el refactor

En la versión parcial, `TurnoAgendaService.actualizar(...)` había perdido la comprobación de existencia del turno. Como `TurnoRepository.actualizar(...)` usa `put`, actualizar un turno inexistente terminaba insertándolo.

La versión actual vuelve a comprobar la existencia antes de actualizar:

```java
turnoValidador.obtenerTurnoExistente(turno.getId());
```

De esta forma el refactor mantiene el comportamiento esperado del Proyecto V0.

## Verificación realizada

- Compilación completa de todos los `.java` con `javac`: OK.
- Alta, modificación, filtros y eliminación de turnos: OK.
- Sincronización de historiales al cambiar Odontólogo/Secretaria: OK.
- Actualización de turno inexistente: rechazada correctamente.
- Registro y edición mediante DTO de Paciente: OK.
- Guardado/carga de persistencia, incluyendo campos escapados con `;`: OK.
