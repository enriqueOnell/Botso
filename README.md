# Botso

Aplicación Android para la gestión académica personal. Botso reúne en un solo lugar los semestres, las materias, los horarios de clase, las tareas y las calificaciones, ofreciendo un panel diario con todo lo que el estudiante necesita atender: clases del día, tareas pendientes, entregas prioritarias, accesos rápidos a plataformas y el promedio de cada materia.

> Interfaz y textos en español. Paquete base: `com.onell.botso`. Versión actual: **2.0** (`versionCode = 26`).

---

## ¿Qué resuelve?

| Necesidad | Solución en Botso |
| --- | --- |
| **Organizar el semestre** | Semestres con fecha de inicio/fin, cursos, profesor, aula/modalidad y horario por día |
| **No olvidar una clase** | Recordatorio 30 minutos antes del inicio (alarma exacta con `AlarmManager`, reprogramada tras reinicio) |
| **No olvidar una tarea** | Estados (`TODO`, `IN_PROGRESS`, `DONE`), filtro por estado, prioridad automática/manual y recordatorio periódico con `WorkManager` |
| **Conocer el rendimiento** | Promedio ponderado por curso y por corte (Cortes 1 y 2: 15% c/u, Corte 3: 20%) con notas Formativa/Cognitiva y teclado numérico integrado |
| **Acceso a la universidad** | Accesos directos a las plataformas EVA y SINU mediante AndroidX Browser (Custom Tabs) |
| **Personalización visual** | Selector de modo de tema (`Sistema`, `Claro`, `Oscuro`) y compatibilidad con color dinámico en Android 12+ |

### Pantallas

- **Dashboard** (`DashboardScreen`): Panel diario con carrusel de clases del día (`TodayClassSectionCard`), tarjeta héroe de tareas pendientes (`PendingTasksCard`), accesos a plataformas EVA y SINU (`PlatformSectionCard`), tareas con prioridad (`PriorityTasksSectionCard`) y carrusel de materias con promedios acumulados (`CourseGradeSectionCard`).
- **Tareas** (`TasksScreen`): Filtro por estado (`Por Hacer`, `En Progreso`, `Hecha`) mediante `TaskFilterSectionCard`, tarjetas `TaskItem` con cambio rápido de estado al tocar, menú contextual para editar o eliminar, banderas de prioridad y adjuntos.
- **Semestres** (`SemestersScreen`): Listado de semestres con sus materias y horarios (`SemesterSectionCard`, `SemesterCourseItem`); creación, edición y eliminación de semestres y cursos mediante hojas modales inferiores (`ModalBottomSheet`) y navegación directa a las notas de cada curso.
- **Notas del curso** (`CourseGradesScreen`): Resumen del curso con promedio general, porcentaje evaluado y barra de progreso (`CourseSummarySectionCard`), selector de cortes 1–3 con desglose Formativa/Cognitiva (`GradesSectionCard`) y editor con teclado numérico propio (`GradeEditorSectionCard` + `NumericPad`).
- **Ajustes** (`SettingsScreen`): Selector de modo de tema (`ThemeMode`: Sistema, Claro, Oscuro), interruptor de color dinámico (Android 12+), controles de notificaciones de tareas y clases con gestión del permiso `POST_NOTIFICATIONS` en tiempo real, aviso de permiso faltante y tarjeta "Acerca de" con el logo y la versión instalada.

---

## Arquitectura

El proyecto sigue una arquitectura **Clean Architecture** estructurada en capas y el patrón **MVVM** con flujo de datos unidireccional (UDF). La UI nunca accede directamente a Room ni a las APIs de Android: siempre se comunica a través de un ViewModel, casos de uso y contratos de repositorio.

```
┌─────────────────────────────────────────────────────────────────┐
│  UI (Compose + Material 3 Expressive)                           │
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
│  models · repository interfaces · use cases                     │
│  Reglas de negocio puras (sin dependencias Android/Room)        │
└──────────────────────────────┬──────────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────────┐
│  Data                                                           │
│  Room (entities + DAOs) · repositories · mappers · DataStore    │
│  WorkManager · AlarmManager · BootReceiver · NotificationService│
└─────────────────────────────────────────────────────────────────┘
```

### Sistema de Diseño ("Expressive Academic Canvas")

- **`MaterialExpressiveTheme`**: Implementación de las especificaciones de Material 3 Expressive (`material3:1.5.0`) con `MotionScheme.expressive()` en `ui/theme/Theme.kt`.
- **Paleta Institucional (`Color.kt`)**: Esquemas claro (`BotsoLightColors`) y oscuro (`BotsoDarkColors`) inspirados en el logo de Botso (Azul `#0060A9` / `#A2C9FF`, Verde Menta `#006C4B` / `#45DFA4`, Azul Grisáceo `#4E5F79` / `#B6C8E5`), además de `LocalBotsoColors` (`BotsoSemanticColors`) para estados semánticos de aprobación (`passContainer` / `onPassContainer`).
- **Tipografía y Formas (`Type.kt`, `Shape.kt`)**: Escala tipográfica `BotsoTypography` (`displayLarge` de `112.sp` hasta `labelLarge`) y tokens `BotsoShapes` (`12.dp` a `40.dp`), junto con `HeroShape` (`48.dp`), `FabShape` (`30.dp`) y formas orgánicas `MaterialShapes` (`Pill`, `Circle`).
- **Contenedor Global (`BotsoMainApp.kt`)**: `Scaffold` con `CenterAlignedTopAppBar` transparente (`enterAlwaysScrollBehavior`), `HorizontalFloatingToolbar` inferior (`exitAlwaysScrollBehavior`) con FAB contextual y `NavHost` type-safe.
- **Estados Vacíos y Carruseles**: `SectionEmptyState` reutilizable en todas las pantallas con listas vacías y `HorizontalUncontainedCarousel` para clases del día y materias.

---

## Stack tecnológico

| Categoría | Tecnología / versión |
| --- | --- |
| **Lenguaje** | Kotlin `2.4.21` |
| **UI** | Jetpack Compose (BOM `2026.11.01`) + Material 3 Expressive (`material3:1.5.0`) |
| **Arquitectura** | MVVM + Clean Architecture por capas |
| **DI** | Hilt `2.60.1` + Hilt Navigation Compose `1.4.0` + Hilt Work `1.4.0` |
| **Persistencia** | Room `2.8.5` (KSP `2.3.10`), base de datos v8 |
| **Preferencias** | Preferences DataStore `1.3.0` |
| **Navegación** | Navigation Compose con rutas `@Serializable` (`navigation/Routes.kt`, Kotlinx Serialization `1.13.0`) |
| **Asincronía** | Kotlin Coroutines `1.11.0` + `Flow` / `StateFlow` |
| **Segundo plano** | WorkManager `2.12.0` (`DailyTaskWorker` cada 3 h) + `AlarmManager` (`ClassAlarmSchedulerImpl`) |
| **Navegador** | AndroidX Browser `1.10.0` (Custom Tabs) |
| **Build** | AGP `9.5.3`, Gradle Kotlin DSL, Version Catalog (`libs.versions.toml`), Java `11` |
| **SDK** | `minSdk 29`, `targetSdk 37`, `compileSdk 37` (`minorApiLevel = 1`) |

---

## Estructura del proyecto

```
botso/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── keepRules/rules.keep
│       │   ├── java/com/onell/botso/
│       │   │   ├── MainActivity.kt              # Entrada edge-to-edge + observa ThemeMode y color dinámico
│       │   │   ├── BotsoApplication.kt          # @HiltAndroidApp + Configuration.Provider (WorkManager)
│       │   │   ├── navigation/
│       │   │   │   ├── BotsoMainApp.kt          # Scaffold, TopAppBar, HorizontalFloatingToolbar, NavHost y diálogos
│       │   │   │   └── Routes.kt                # Rutas type-safe @Serializable (Dashboard, Tasks, Semesters, CourseGrades, Settings)
│       │   │   ├── data/
│       │   │   │   ├── local/                   # Room: BotsoDataBase (v8), DAOs y entidades
│       │   │   │   ├── mapper/                  # Mappers Entity <-> Domain
│       │   │   │   ├── repository/              # Implementaciones de repositorios (Room y DataStore)
│       │   │   │   ├── receiver/                # ClassAlarmSchedulerImpl, ClassAlarmReceiver, BootReceiver
│       │   │   │   ├── service/                 # NotificationServiceImpl
│       │   │   │   └── worker/                  # DailyTaskWorker y TaskNotificationSchedulerImpl
│       │   │   ├── domain/
│       │   │   │   ├── model/                   # Modelos puros de dominio (incluye TaskStatus y ThemeMode)
│       │   │   │   ├── repository/              # Contratos de repositorio, alarmas y notificaciones
│       │   │   │   └── usecase/                 # Casos de uso por entidad (course, grade, notification, semester, session, settigns, task)
│       │   │   ├── di/                          # Módulos Hilt (Database, DataStore, Notification, Repository)
│       │   │   └── ui/
│       │   │       ├── screens/                 # Dashboard, Tasks, Semesters, CourseGrades, Settings
│       │   │       ├── components/              # Componentes por feature (dashboard, dialogs, grades, semesters, settings, tasks)
│       │   │       ├── viewmodel/               # ViewModels @HiltViewModel
│       │   │       ├── uistate/                 # Estados de UI (sealed interfaces y SettingsUiState)
│       │   │       └── theme/                   # Color, Shape, Type, Theme (MaterialExpressiveTheme + LocalBotsoColors)
│       │   └── res/                             # Recursos (botso_logo, drawables, mipmaps, strings, XML)
│       ├── test/                                # Pruebas unitarias
│       └── androidTest/                         # Pruebas instrumentadas
├── gradle/libs.versions.toml                    # Catálogo de versiones
├── build.gradle.kts
├── settings.gradle.kts
├── AGENTS.md                                    # Guía rápida de arquitectura y convenciones para agentes
├── LICENSE                                      # Licencia Apache 2.0
└── README.md
```

---

## Compilar y ejecutar

### 1. Clonar

```bash
git clone <URL-del-repositorio>
cd botso
```

### 2. Compilar por línea de comandos

```bash
# Build de depuración
./gradlew :app:assembleDebug

# Instalar en dispositivo conectado (paquete com.onell.botso.dev, sufijo -DEV)
./gradlew :app:installDebug

# Pruebas unitarias
./gradlew :app:testDebugUnitTest

# Una prueba específica
./gradlew :app:testDebugUnitTest --tests "com.onell.botso.CourseGradesViewModelTest"

# Pruebas instrumentadas (requiere emulador o dispositivo)
./gradlew :app:connectedDebugAndroidTest
```

El APK de depuración se genera en `app/build/outputs/apk/debug/`.

---

## Permisos

| Permiso | Uso |
| --- | --- |
| `INTERNET` | Abrir las plataformas académicas EVA y SINU en Custom Tabs |
| `POST_NOTIFICATIONS` | Mostrar recordatorios periódicos de tareas y avisos de clase (runtime en Android 13+) |
| `SCHEDULE_EXACT_ALARM` | Programar avisos exactos 30 minutos antes de cada clase (Android 12+) |
| `RECEIVE_BOOT_COMPLETED` | Reprogramar las alarmas de clase tras reiniciar el dispositivo (`BootReceiver`) |

---

## Mantenimiento y Licencia

- **Migraciones de Room**: La base de datos está en la versión **8** con `exportSchema = false` y sin `fallbackToDestructiveMigration()`. Al modificar entidades o incrementar la versión de BD, se debe añadir una `Migration` explícita.
- **Reglas de negocio de notas**: Centralizadas en `CourseGradesViewModel` (escala `0.0`–`5.0`, corte 3 con peso `0.20` y cortes 1–2 con `0.15`, umbral de aprobación `3.0` y cálculo ponderado en `CourseWithGrades.averageGrade`).
- **Localización en español**: Toda la interfaz y los mensajes están en español.
- **KDoc y Previews**: Los componentes principales incluyen documentación KDoc y vistas previas `@Preview` en modo claro y oscuro envolviendo el componente puro (`*Content` / item) en `BotsoTheme`.
- **Licencia**: Distribuido bajo la licencia [Apache 2.0](LICENSE).
