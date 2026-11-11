# Historial de cambios

Los cambios notables del proyecto se registran aquí.

## [1.0.0] - 2026-11-09

### Añadido

- Aplicación Android Pomodoro nativa con Kotlin, Jetpack Compose y Material 3.
- Temporizador con sesiones de trabajo, descansos cortos y descansos largos en ciclos de cuatro sesiones.
- Controles para iniciar/pausar y saltar al siguiente período.
- Configuración de duraciones, sonido, vibración, tono, tema e idioma, con persistencia local y restablecimiento.
- Interfaz en español e inglés, tema oscuro predeterminado y opción de tema claro.
- Icono vectorial con tomate y reloj.
- README con instrucciones de uso y compilación.

### Cambiado

- Se redujo el tamaño del APK al eliminar la biblioteca extendida de iconos y dibujar algunos controles con Compose.
- Se habilitó R8 y la reducción de recursos para Release.
- Se firmó Release con la clave debug para facilitar instalación local de pruebas.
- Se ajustó la interfaz para respetar las áreas seguras del sistema.
- Se engrosó el indicador semicircular, se puso en negrita el contador y se actualizaron los colores según el tema y Material 3.
- Se cambió el identificador de aplicación y namespace a `pe.com.dyd.app.pomodoro`.

### Documentación y mantenimiento

- Se añadió `.gitignore` para archivos de compilación, configuración local, IDE y claves.
- Se añadió este historial de cambios.
- Se consolidó el prompt del proyecto y las pautas de desarrollo en `AGENTS.md`.

### Actualizado

- Se reemplazó el icono de tomate por el gráfico de reloj rojo adjunto, convertido en icono adaptable vectorial para Android.

## [1.0.1] - 2026-11-11

### Corregido

- Se redujo el reloj del icono adaptable para que su contorno rojo quede dentro de la zona segura y no sea recortado por la máscara del lanzador.
- Se incrementó el número de versión para identificar esta actualización en el emulador.
