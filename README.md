# Botso

Aplicación Android para la gestión académica personal. Botso reúne en un solo lugar los
semestres, las materias, los horarios de clase, las tareas y las calificaciones, y ofrece
un panel diario con lo que el estudiante necesita atender: clases de hoy, tareas
pendientes, tareas prioritarias y el promedio de cada materia.

> Interfaz y textos en español. Paquete base: `com.onell.botso`.

---

## ¿Qué resuelve?

| Necesidad | Solución en Botso |
| --- | --- |
| Organizar el semestre | Semestres con fecha de inicio/fin, cursos y horarios por día |
| No olvidar una clase | Recordatorio 30 minutos antes del inicio (alarma exacta cuando el sistema lo permite) |
| No olvidar una tarea | Estados (Por Hacer / En Progreso / Hecho), prioridad automática si vence en ≤ 3 días y recordatorio periódico |
| Conocer el rendimiento | Promedio ponderado por curso y por corte (Formativa + Cognitiva) |
| Compartir resultados | Exportación del semestre a PDF y hoja de compartir del sistema |
| Acceder rápido a la universidad | Accesos directos a las plataformas EVA y SINU mediante Custom Tabs |

### Pantallas

- **Dashboard**: plataformas académicas, clases del día, contador de tareas pendientes,
  tareas prioritarias y materias con su promedio.
- **Tareas**: listado filtrado por estado, cambio rápido de estado, edición y borrado.
- **Semestres**: semestres con sus materias y horarios; alta/edición/borrado y exportación a PDF.
- **Notas del curso**: promedio total animado, selector de corte y teclado numérico propio
  para las notas Formativa y Cognitiva.
- **Ajustes**: color dinámico, notificaciones de tareas y de clases, y versión de la app.

---

## Arquitectura

El proyecto sigue una arquitectura por capas inspirada en **Clean Architecture** y el patrón
**MVVM** con flujo de datos unidireccional (UDF). La UI nunca accede directamente a Room ni a
las APIs de Android: siempre pasa por un ViewModel, un caso de uso y una interfaz de repositorio.

```
┌─────────────────────────────────────────────────────────────────┐
│  UI (Compose)                                                   │
│  screens · components · theme · uistate                         │
│  Observa StateFlow y emite eventos al ViewModel                 │
└──────────────────────────────┬──────────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────────┐
│  Presentation (MVVM)                                            │
│  ViewModels @HiltViewModel · StateFlow<*UiState>                │
└──────────────────────────────┬──────────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────────┐
│  Domain                                                         │
│  models · repository interfaces · use cases · provider          │
│  Reglas de negocio puras (sin Android/Room)                     │
└──────────────────────────────┬──────────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────────┐
│  Data                                                           │
│  Room (entities + DAOs) · repositories · mappers · DataStore    │
│  WorkManager · AlarmManager · NotificationService · PDF         │
└─────────────────────────────────────────────────────────────────┘
```

### Decisiones clave

- **Estado unidireccional**: cada pantalla consume un `sealed interface *UiState`
  (`Loading`, `Success`, `Error`) expuesto como `StateFlow` y recogido con
  `collectAsStateWithLifecycle()`.
- **Reactividad**: los repositorios devuelven `Flow`; cualquier cambio en Room se propaga
  automáticamente hasta la UI sin recargas manuales.
- **Separación de modelos**: las entidades de Room (`*Entity`) se traducen a modelos de
  dominio mediante `data/mapper/Mappers.kt`. El dominio usa `LocalDate`, `LocalTime` y enums.
- **Inyección de dependencias**: Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`,
  `@HiltViewModel`) con módulos en `di/`.
- **Navegación type-safe**: Navigation Compose con rutas `@Serializable` en `navigation/Routes.kt`.
- **Persistencia de ajustes**: Preferences DataStore (`settings_prefs`), separado de Room.
- **Trabajo en segundo plano**: WorkManager para el recordatorio de tareas y AlarmManager
  para los avisos de clase; `BootReceiver` reprograma las alarmas tras un reinicio.

### Reglas de negocio relevantes

- Escala de notas **0.0 – 5.0**; promedio aprobatorio desde **3.0**.
- Peso por corte: **corte 3 = 20%**, cortes 1 y 2 = **15%** cada uno.
- Las notas se identifican por nombre dentro del corte: `Nota Formativa` y `Nota Cognitiva`.
- Una tarea que vence en **0 a 3 días** se marca automáticamente como prioritaria.
- El aviso de clase se programa **30 minutos antes** de la hora de inicio.

---

## Stack tecnológico

| Categoría | Tecnología / versión |
| --- | --- |
| Lenguaje | Kotlin 2.4.10 |
| UI | Jetpack Compose + Material 3 Expressive (BOM 2026.09.00) |
| Arquitectura | MVVM + Clean Architecture por capas |
| DI | Hilt 2.60.1 + Hilt Navigation Compose |
| Persistencia | Room 2.8.5 (KSP) |
| Preferencias | Preferences DataStore 1.2.1 |
| Navegación | Navigation Compose con rutas `@Serializable` |
| Asincronía | Kotlin Coroutines + Flow |
| Trabajo en segundo plano | WorkManager 2.12.0 + AlarmManager |
| Navegador | AndroidX Browser (Custom Tabs) |
| Build | AGP 9.5.1, Gradle Kotlin DSL, Version Catalog (`libs.versions.toml`) |
| SDK | `minSdk 29`, `targetSdk 37`, `compileSdk 37` |

Dependencias declaradas en `gradle/libs.versions.toml` y `app/build.gradle.kts`.

---

## Estructura del proyecto

```
botso/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/onell/botso/
│       │   │   ├── MainActivity.kt              # Entrada + tema
│       │   │   ├── BotsoApplication.kt          # @HiltAndroidApp + WorkManager
│       │   │   ├── navigation/
│       │   │   │   ├── BotsoMainApp.kt          # Scaffold, barra inferior, NavHost
│       │   │   │   └── Routes.kt                # Rutas type-safe
│       │   │   ├── data/
│       │   │   │   ├── local/                   # Room: BD, DAOs y entidades
│       │   │   │   ├── mapper/                  # Entity <-> Domain
│       │   │   │   ├── repository/              # Implementaciones de repositorios
│       │   │   │   ├── provider/                # Generación de PDF
│       │   │   │   ├── receiver/                # Alarmas y arranque del dispositivo
│       │   │   │   ├── service/                 # Notificaciones
│       │   │   │   └── worker/                  # WorkManager
│       │   │   ├── domain/
│       │   │   │   ├── model/                   # Modelos puros
│       │   │   │   ├── repository/              # Contratos
│       │   │   │   ├── provider/                # Contrato del PDF
│       │   │   │   └── usecase/                 # Casos de uso por entidad
│       │   │   ├── di/                          # Módulos Hilt
│       │   │   └── ui/
│       │   │       ├── screens/                 # 5 pantallas
│       │   │       ├── components/              # Componentes reutilizables
│       │   │       ├── viewmodel/               # ViewModels
│       │   │       ├── uistate/                 # Estados de UI
│       │   │       └── theme/                   # Color, tipografía, formas, tema
│       │   └── res/                             # Recursos Android
│       ├── test/                                # Pruebas unitarias
│       └── androidTest/                         # Pruebas instrumentadas
├── gradle/libs.versions.toml                    # Catálogo de versiones
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

### Flujo de datos (ejemplo: guardar una nota)

```
CourseGradesScreen
   └─ CourseGradesViewModel.saveGrades()
        └─ InsertGradeUseCase / UpdateGradeUseCase
             └─ GradeRepository (interfaz)
                  └─ GradeRepositoryImpl
                       └─ GradeDao (Room)
                            └─ Flow<List<...>> → ViewModel → UiState → Compose
```

---

## Requisitos

- **Android Studio** reciente con soporte para AGP 9.x.
- **JDK 17 o superior** (AGP 9 lo requiere; el proyecto compila con `sourceCompatibility 11`).
- **Android SDK 37** instalado.
- Dispositivo o emulador con **Android 10 (API 29)** o superior.

---

## Compilar y ejecutar

### 1. Clonar

```bash
git clone <URL-del-repositorio>
cd botso
```

### 2. Compilar en Android Studio

1. Abrir la carpeta raíz del proyecto con **File → Open**.
2. Esperar la sincronización de Gradle (el proyecto usa Version Catalog).
3. Seleccionar el módulo `app` y un dispositivo/emulador.
4. Ejecutar con **Run ▶** o `Shift + F10`.

### 3. Compilar por línea de comandos

```bash
# Build de depuración
./gradlew :app:assembleDebug

# Instalar en el dispositivo conectado
./gradlew :app:installDebug

# Pruebas unitarias
./gradlew :app:testDebugUnitTest

# Una prueba concreta
./gradlew :app:testDebugUnitTest --tests "com.onell.botso.CourseGradesViewModelTest"

# Pruebas instrumentadas (requiere emulador/dispositivo)
./gradlew :app:connectedDebugAndroidTest
```

El APK de depuración se genera en `app/build/outputs/apk/debug/`.

> La variante `debug` usa el `applicationId` **`com.onell.botso.dev`** y el sufijo de versión
> `-DEV`, por lo que puede convivir instalada junto a la versión de producción.

---

## Permisos

| Permiso | Uso |
| --- | --- |
| `INTERNET` | Abrir EVA/SINU en Custom Tabs |
| `POST_NOTIFICATIONS` | Recordatorios de tareas y clases (Android 13+) |
| `SCHEDULE_EXACT_ALARM` | Avisos de clase a la hora exacta (Android 12+) |
| `RECEIVE_BOOT_COMPLETED` | Reprogramar alarmas tras reiniciar el dispositivo |

La app solicita `POST_NOTIFICATIONS` en tiempo de ejecución desde Ajustes y explica el
caso en el que el permiso queda bloqueado.

---

## Notas técnicas y mantenimiento

- **Migraciones de Room**: la base de datos está en la versión **8** con
  `exportSchema = false` y **sin** `fallbackToDestructiveMigration()`. Antes de subir la
  versión hay que añadir una `Migration`; de lo contrario la app fallará al abrir una BD
  existente.
- **Nombres de casos de uso**: algunos archivos usan minúscula inicial (`getX.kt`,
  `insertX.kt`) y otros mayúscula (`UpdateX.kt`, `GenerateX.kt`). Es intencional en el
  estado actual del proyecto; al añadir imports conviene respetar el nombre existente.
- **Textos en español**: los strings visibles están escritos directamente en los
  composables (Tareas, Semestres, Notas). Si se planea internacionalizar, conviene
  moverlos a `res/values/strings.xml`.
- **Material 3 Expressive**: se usan APIs experimentales (`HorizontalFloatingToolbar`,
  `LoadingIndicator`, `MaterialShapes`, `MotionScheme.expressive()`); pueden cambiar entre
  versiones de la librería.
- **Pruebas**: actualmente solo existe una prueba unitaria de ejemplo; se recomienda
  ampliar la cobertura de `CourseGradesViewModel` y de los casos de uso.
- **Documentación del código**: los archivos principales incluyen KDoc con la
  responsabilidad de cada componente y las reglas de negocio relevantes.

---

## Estado del proyecto

- Versión: **1.6.0** (`versionCode 25`).
- Módulo único `:app`.
- Sin CI configurado.
- Interfaz completamente en Jetpack Compose.
