# Pomodoro: especificación y pautas del proyecto

## Prompt consolidado

> Crea y mantén una aplicación Android nativa de tipo Pomodoro usando Kotlin, Jetpack Compose y componentes Material 3. La aplicación debe ser compatible con Android 10 (API 29) o posterior. El identificador de aplicación y namespace son `pe.com.dyd.app.pomodoro`; usa SDK de compilación y destino 35 y JVM 17.
>
> Usa como referencia la pantalla adjunta: interfaz sencilla, oscura y centrada, con un indicador de progreso semicircular, el tiempo restante en el centro, el nombre de la fase debajo y controles para iniciar/pausar y saltar al siguiente contador. El trazo del semicírculo debe ser grueso. El contador debe mostrarse en negrita. Usa blanco como color principal en tema oscuro y negro en tema claro. Los botones deben usar el color primario predeterminado de Material 3. Respeta las áreas seguras para evitar superposiciones con la barra de estado y el recorte de cámara.
>
> Implementa sesiones de trabajo de 25 minutos, descansos cortos de 5 minutos y descansos largos de 15 minutos como valores iniciales. Después de cuatro sesiones de trabajo, sigue un descanso largo; los descansos restantes son cortos. Permite iniciar o pausar el temporizador actual y avanzar manualmente al siguiente período.
>
> Incluye una pantalla de configuración que permita ajustar las tres duraciones, activar o desactivar sonido y vibración al terminar, escoger un tipo de sonido disponible en el dispositivo, seleccionar tema claro u oscuro, seleccionar español o inglés y restablecer los valores predeterminados. Guarda las preferencias localmente. Los valores iniciales son tema oscuro, idioma español, sonido y vibración activados.
>
> Crea un icono vectorial propio con un tomate rojo y un reloj más pequeño al costado. Mantén las dependencias ligeras; evita incorporar catálogos completos de iconos si no son necesarios. Activa la reducción de código y recursos en Release.

## Pautas de desarrollo

- Mantén Kotlin, Compose y Material 3 como tecnologías principales.
- Mantén `applicationId` y `namespace` en `pe.com.dyd.app.pomodoro`.
- Conserva Android 10 (API 29) como versión mínima y respeta las áreas seguras del sistema.
- Mantén español e inglés y persiste las preferencias en almacenamiento local.
- Evita dependencias grandes si un elemento se puede dibujar directamente con Compose.
- La firma Release usa actualmente la clave debug para instalación local. No usar esa clave para distribución: configurar una clave de lanzamiento propia antes de publicar.
- Actualiza `README.md` y `CHANGELOG.md` cuando cambien las funciones, requisitos o instrucciones de compilación.
