# Pomodoro

Aplicación Android nativa para organizar sesiones de enfoque y pausas con la técnica Pomodoro. Está escrita en Kotlin y Jetpack Compose, con componentes Material 3.

**Identificador de aplicación:** `pe.com.dyd.app.pomodoro`.

## Requisitos

- Android 10 (API 29) o posterior.
- Android Studio con el Android SDK 35.
- JDK 17 para compilar el proyecto.

## Funciones

- Iniciar y pausar el contador actual, o saltar al siguiente período.
- Sesión de trabajo inicial de 25 minutos.
- Descanso corto inicial de 5 minutos y descanso largo inicial de 15 minutos.
- Ciclo de cuatro sesiones de trabajo seguido de un descanso largo.
- Aviso sonoro y vibración al finalizar, ambos configurables.
- Selección de tono entre los sonidos predeterminados de notificación, llamada y alarma del dispositivo.
- Tema oscuro inicial y tema claro opcional.
- Interfaz en español inicial e inglés opcional.
- Preferencias guardadas localmente y opción para restaurar los valores iniciales.
- Icono adaptable vectorial basado en el gráfico de reloj rojo adjunto.
- Pantalla principal que respeta las áreas seguras del sistema, con progreso semicircular, tiempo destacado y controles de sesión.

## Estructura

```text
app/
├── build.gradle.kts                  # Configuración Android, dependencias y variantes
├── proguard-rules.pro                # Reglas adicionales de R8
└── src/main/
    ├── AndroidManifest.xml           # Aplicación, actividad y permiso de vibración
    ├── java/
    │   └── pe/com/dyd/app/pomodoro/
    │       └── MainActivity.kt        # Interfaz, temporizador y configuración
    └── res/
        ├── drawable/
        │   └── ic_launcher_foreground.xml # Gráfico vectorial del reloj
        ├── mipmap-anydpi-v26/
        │   └── ic_launcher.xml        # Icono adaptable de Android
        └── values/
            └── ic_launcher_background.xml # Fondo del icono adaptable
```

## Abrir y ejecutar

1. Abre la carpeta del proyecto en Android Studio.
2. Espera a que Gradle sincronice las dependencias.
3. Selecciona un emulador o dispositivo con Android 10 o posterior.
4. Ejecuta la configuración `app`.

## Generar APK

Desde la terminal de Windows, en la raíz del proyecto:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:assembleRelease
```

El APK de depuración se genera en `app/build/outputs/apk/debug/app-debug.apk`. El APK Release firmado para pruebas locales se genera en `app/build/outputs/apk/release/app-release.apk`.

La variante Release tiene activados R8 y la reducción de recursos. Actualmente usa la clave de depuración para permitir instalarla directamente durante las pruebas. Antes de distribuirla o publicarla, configura una clave de firma de lanzamiento propia; no publiques una versión firmada con la clave de depuración.

## Valores iniciales

| Ajuste | Valor |
| --- | --- |
| Trabajo | 25 minutos |
| Descanso corto | 5 minutos |
| Descanso largo | 15 minutos |
| Tema | Oscuro |
| Idioma | Español |
| Sonido al finalizar | Activado |
| Vibración al finalizar | Activada |

Los valores se guardan en las preferencias locales de la aplicación. El proyecto no requiere cuenta ni conexión de red.

## Documentación del proyecto

- [AGENTS.md](AGENTS.md): prompt consolidado y pautas para continuar el desarrollo.
- [CHANGELOG.md](CHANGELOG.md): historial de cambios por versión.


