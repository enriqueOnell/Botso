---
name: botso-ui
description: >-
  Úsala siempre que se cree, rediseñe o modifique cualquier pantalla, tarjeta,
  diálogo, estado vacío, badge, carrusel o componente de interfaz en Botso
  (Jetpack Compose + Material 3 Expressive). Aplica la paleta institucional y
  LocalBotsoColors, los tokens de MaterialTheme.shapes, la jerarquía tipográfica
  del Dashboard y las convenciones de BotsoMainApp para fondos transparentes y
  padding inferior frente a la HorizontalFloatingToolbar.
---

# Botso UI — Sistema visual "Expressive Academic Canvas"

## Cuándo aplicarla

Aplica esta skill antes de escribir o revisar UI en `com.onell.botso.ui`:

- Pantallas nuevas o existentes: `DashboardScreen`, `TasksScreen`, `SemestersScreen`, `CourseGradesScreen`, `SettingsScreen`.
- Tarjetas, filas, badges, chips, carruseles, estados vacíos y diálogos.
- Cualquier cambio de color, forma, tipografía, espaciado o estructura de una pantalla.

La fuente de verdad son los archivos reales del proyecto, no esta skill por sí sola:

| Tema | Archivo |
|---|---|
| Colores y semántica | `app/src/main/java/com/onell/botso/ui/theme/Color.kt` |
| Tema y `LocalBotsoColors` | `app/src/main/java/com/onell/botso/ui/theme/Theme.kt` |
| Tipografía | `app/src/main/java/com/onell/botso/ui/theme/Type.kt` |
| Formas | `app/src/main/java/com/onell/botso/ui/theme/Shape.kt` |
| Layout global | `app/src/main/java/com/onell/botso/navigation/BotsoMainApp.kt` |
| Referencia visual | `app/src/main/java/com/onell/botso/ui/screens/DashboardScreen.kt` y `ui/components/dashboard/` |

Si el código cambia, actualiza esta skill con el código, nunca al revés.

---

## Principio rector

Botso se ve como un **canvas académico expresivo**:

1. **Base neutra, acentos institucionales.** La mayoría de las secciones son superficies neutras (`surfaceContainerLow`). El color saturado se reserva para la tarjeta héroe, la prioridad y los accesos.
2. **Datos primero.** El número o título importante ocupa el estilo más grande de su tarjeta (`displayLarge`, `displayMedium`, `headlineSmall`); el resto es metadato pequeño.
3. **Formas redondeadas y generosas.** Tarjetas de sección con `shapes.large`; elementos destacados con `shapes.extraLarge`; badges con `shapes.extraSmall`.
4. **Un solo héroe por pantalla.** En el Dashboard es `PendingTasksCard`. No compitas con varias tarjetas gigantes y saturadas a la vez.
5. **Todo tokenizado.** Colores, formas y tipografía salen de `MaterialTheme` o de `LocalBotsoColors`. Nunca valores hexadecimales sueltos en una pantalla.

---

## 1. Color y armonía

### 1.1 Fuentes de color

- `BotsoLightColors` / `BotsoDarkColors`: esquemas Material 3 completos. El tema usa `MaterialExpressiveTheme` y, por defecto, **desactiva el color dinámico** (`dynamicColor = false`).
- `LocalBotsoColors.current`: colores semánticos de dominio. Hoy solo expone aprobado:
  - `passContainer` / `onPassContainer`.
  - Se provee automáticamente según `darkTheme`; no lo calcules a mano.
- `MaterialTheme.colorScheme.error` / `errorContainer` / `onErrorContainer`: reprobado, error y acciones destructivas.

### 1.2 Combinaciones observadas en el Dashboard

Estas son las parejas reales que producen la armonía actual. Úsalas como punto de partida y puedes combinarlas de otra forma siempre que respetes las reglas de 1.3.

| Rol visual | Contenedor | Contenido / acento | Dónde se ve |
|---|---|---|---|
| Sección neutra | `surfaceContainerLow` | Título `onPrimaryContainer`; cuerpo `onSurface` / `onSurfaceVariant` | `TodayClassSectionCard`, `PlatformSectionCard`, `PriorityTasksSectionCard`, `CourseGradeSectionCard` |
| Héroe primario | `primary` | Título `primaryContainer`; métrica `primary` sobre forma `primaryContainer` | `PendingTasksCard` |
| Clase del día | `primaryContainer` | Horario y materia `primary`; píldoras `primary` con texto `primaryContainer` | `ClassItem` |
| Acceso a plataforma | `secondary` | Texto e icono `secondaryContainer`; tile del icono `secondaryContainer` con tint `secondary` | `PlatformItem` |
| Tarea prioritaria | `tertiaryContainer` | Fecha y badge `tertiary`; badge con contenido `tertiaryContainer`; título `onPrimaryContainer` | `PriorityTaskItem` |
| Materia / promedio | `tertiaryContainer` | Nombre `tertiary`; promedio `onPassContainer` si aprueba, `error` si no | `CourseGradeItem` |
| Estado vacío | `primaryContainer` | Título `onSurface`; subtítulo `onSurfaceVariant`; tile del icono `secondary` | `SectionEmptyState` |
| Aprobado | `passContainer` (`LocalBotsoColors`) | `onPassContainer` | `CourseGradeItem`, `CircularGradeProgress` |
| Reprobado / destructivo | `errorContainer` / `error` | `onErrorContainer` / `onError` | `CourseGradeItem`, `ConfirmDeleteDialog` |

### 1.3 Reglas de combinación

1. **Pareja contenedor con su "on".** Para texto sobre un contenedor usa el token `on*` correspondiente (`primaryContainer` → `onPrimaryContainer`, `secondaryContainer` → `onSecondaryContainer`, etc.). No inventes pares nuevos sin comprobar contraste.
2. **Inversión expresiva permitida.** Puedes invertir un rol consigo mismo: `primary` ↔ `primaryContainer`, `secondary` ↔ `secondaryContainer`, `tertiary` ↔ `tertiaryContainer`. Eso es lo que hace `PendingTasksCard` (fondo `primary`, texto `primaryContainer`, forma `primaryContainer`, número `primary`) y `ClassItem` (fondo `primaryContainer`, texto `primary`, píldora `primary` con texto `primaryContainer`).
3. **No cruces roles sin verificar.** `primary` sobre `secondary`, `tertiary` sobre `primary` o `secondaryContainer` sobre `tertiaryContainer` suelen perder contraste y romper la jerarquía. Si necesitas un acento cruzado, primero comprueba AA.
4. **Un solo acento saturado por tarjeta.** Una tarjeta neutra puede tener un badge de acento; una tarjeta héroe puede tener una forma decorativa. No apiles primary + secondary + tertiary en la misma tarjeta.
5. **El texto principal nunca lleva alpha.** El alpha se usa solo para metadatos sobre un color de contenido ya establecido, como `onPrimaryContainer.copy(alpha = 0.8f)` en la descripción de `PriorityTaskItem`. Los números y títulos van a opacidad completa.
6. **Verde/rojo solo con semántica.** Aprobado = `LocalBotsoColors.passContainer` / `onPassContainer`. Reprobado/error = `error` / `errorContainer`. No escribas `Color(0xFF34D399)` ni `Color.Green` en una pantalla.
7. **Superficies nuevas al estilo Dashboard.** Usa `surfaceContainerLow` para secciones y `surfaceContainer` / `surfaceContainerHigh` para capas elevadas. `surfaceVariant` aparece en pantallas antiguas (`TasksScreen`, `SemestersScreen`); no lo introduzcas en código nuevo y migra a `surfaceContainer*` cuando toques ese componente.
8. **Modo oscuro automático.** No hagas `if (isSystemInDarkTheme())` en componentes. El tema ya cambia los tokens y `LocalBotsoColors`. Solo verifica el resultado con previews en ambos modos.
9. **Contraste mínimo.** Texto de cuerpo ≥ 4.5:1; texto grande (≥ 24 sp o ≥ 19 sp bold) ≥ 3:1. Si combinas tokens distintos a los de la tabla, verifica el contraste antes de dar la tarea por terminada.

### 1.4 Jerarquía de acentos

- `primary` (azul): marca, héroe, horarios, acciones principales.
- `secondary` (menta): accesos externos, tile de estado vacío, confirmaciones positivas.
- `tertiary` (azul grisáceo): prioridad, fechas, utilidades y contenedores de materia.
- `error`: solo fallo, reprobado o acción destructiva.
- Formas decorativas: usa tokens de contenedor (`primaryContainer`, `secondaryContainer`) con `MaterialShapes.Pill` / `Circle`, como en `PendingTasksCard` y `PlatformItem`.

---

## 2. Formas y esquinas

### 2.1 Tokens disponibles

`BotsoShapes` en `ui/theme/Shape.kt`:

| Token | Radio | Uso |
|---|---:|---|
| `MaterialTheme.shapes.extraSmall` | 12 dp | Badges, chips compactos, etiquetas |
| `MaterialTheme.shapes.small` | 16 dp | Campos de formulario, filas internas, píldoras |
| `MaterialTheme.shapes.medium` | 24 dp | Diálogos, selectores, envolturas de sección |
| `MaterialTheme.shapes.large` | 32 dp | Tarjetas de sección y tarjetas internas del Dashboard |
| `MaterialTheme.shapes.extraLarge` | 40 dp | Tarjetas destacadas, estados vacíos, banners |
| `HeroShape` | 48 dp | Solo héroe principal o bienvenida |
| `FabShape` | 30 dp | Solo FAB con geometría propia |

### 2.2 Regla principal

**Prioriza siempre `MaterialTheme.shapes.*` para tarjetas, contenedores, badges, diálogos y botones.** No agregues `RoundedCornerShape(24.dp)` nuevo si existe `MaterialTheme.shapes.medium`.

Mapeo observado en el Dashboard:

- Contenedor de sección: `shapes.large`.
- Tarjeta interna de clase: `shapes.large` (aunque `ClassItem` aún usa `RoundedCornerShape(32.dp)`, equivale al token).
- Tarjeta de materia y de tarea prioritaria: `shapes.large` / `shapes.extraLarge`.
- Estado vacío: `shapes.extraLarge`; tile del icono: `shapes.medium`.
- Badge de materia en tarea: `shapes.extraSmall`.
- Diálogo: `shapes.medium` (hoy `ConfirmDeleteDialog` usa `RoundedCornerShape(24.dp)`; al tocarlo, migra al token).

Solo se permite un valor en `.dp` cuando:

- Es `HeroShape` o `FabShape` y no existe token equivalente.
- Es una máscara puntual de un carrusel o un detalle decorativo documentado.
- Material 3 Expressive exige una forma orgánica concreta.

En esos casos, deja un comentario breve explicando por qué no se usó un token.

### 2.3 Formas orgánicas de Material 3 Expressive

`MaterialShapes` está disponible en el proyecto y ya se usa:

- `MaterialShapes.Pill.toShape()` en `PendingTasksCard` para la forma decorativa de fondo.
- `MaterialShapes.Circle.toShape()` en `PlatformItem` para el tile del icono.

Patrón:

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BotsoAccentShape(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(MaterialShapes.Pill.toShape())
            .background(MaterialTheme.colorScheme.primaryContainer)
    )
}
```

Cuándo usarlas:

- Fondos decorativos, tiles de icono, badges y máscaras de carrusel.
- No reemplaces la forma base de una tarjeta de contenido por una forma orgánica: la tarjeta debe seguir `MaterialTheme.shapes.*` para mantener la alineación entre secciones.
- Si una forma orgánica recorta contenido dentro de un carrusel, aplica `Modifier.maskClip(MaterialTheme.shapes.large)` al item, como en `TodayClassSectionCard` y `CourseGradeSectionCard`.

---

## 3. Tipografía y jerarquía de datos

### 3.1 Escala real

`BotsoTypography` en `ui/theme/Type.kt`:

| Estilo | Tamaño / peso | Uso aprobado |
|---|---|---|
| `displayLarge` | 112 sp / ExtraBold | Métrica gigante: promedio total, contador de tareas |
| `displayMedium` | 56 sp / ExtraBold | Hora de clase, promedio de materia |
| `displaySmall` | 48 sp / ExtraBold | Métricas secundarias de impacto |
| `headlineLarge` | 44 sp / ExtraBold | Título de la top bar, encabezados de héroe |
| `headlineMedium` | 34 sp / ExtraBold | Título de tarjeta héroe (`PendingTasksCard`) |
| `headlineSmall` | 26 sp / ExtraBold | Título de sección (`Clases del Día`, `Plataformas`, `Mis materias`) |
| `titleLarge` | 20 sp / Bold | Título de item: materia, tarea, plataforma |
| `titleMedium` | 18 sp / Bold | Subtítulos y nombres secundarios |
| `bodyLarge` | 16 sp / Medium | Texto principal de párrafo |
| `bodyMedium` | 14 sp / Medium | Descripción, metadatos, texto de formulario |
| `labelLarge` | 14 sp / Bold | Botones de acción |
| `labelSmall` | 11 sp / Medium (fallback M3) | Badges, chips, fechas, metadatos pequeños |

### 3.2 Reglas de jerarquía

1. **Un dato principal por tarjeta.** El número o título que el usuario debe leer primero usa `display*` o `headline*`; no pongas dos `displayLarge` compitiendo.
2. **Título de sección = `headlineSmall`.** Es el patrón del Dashboard. No uses `titleLarge` para el encabezado de una sección completa.
3. **Título de item = `titleLarge`.** Materia, tarea, plataforma. Si el item es muy compacto, `titleMedium`.
4. **Metadato = `bodyMedium`, `bodySmall` o `labelSmall`.** Profesor, aula, fecha, descripción corta, badge.
5. **El peso ya está en el estilo.** `display*` y `headline*` ya son ExtraBold; `titleLarge` y `titleMedium` ya son Bold. No repitas `fontWeight` salvo que quieras una excepción deliberada.
6. **Números con formato estable.** Promedios con `String.format(Locale.US, "%.1f" / "%.2f")`; horas con `DateTimeFormatter.ofPattern("HH:mm")`; fechas en español con `Locale.forLanguageTag("es")`, como en `PriorityTaskItem`.
7. **Recorta, no rompas.** Títulos y metadatos de una línea llevan `maxLines` + `TextOverflow.Ellipsis`. Descripciones: máximo 2 líneas.
8. **Español de la app.** Títulos y estados en español (`Tareas`, `Semestres`, `Notas`, `¡Día libre!`). No introduzcas inglés en la UI.
9. **Alineación de métricas.** Los números grandes se alinean al final (`Alignment.BottomEnd`) cuando acompañan un texto, como en `CourseGradeItem` y `PendingTasksCard`.

### 3.3 Espaciado observado

- Padding interno de sección: `16.dp`.
- Separación título → contenido: `8.dp` o `16.dp`.
- Separación entre items de una lista: `12.dp` o `16.dp`.
- `LazyColumn` de pantalla: `contentPadding` lateral/top `16.dp` y `Arrangement.spacedBy(16.dp)`.
- Evita valores nuevos fuera de la escala `4 / 8 / 12 / 16 / 20 / 24`.

---

## 4. Arquitectura y convenciones del proyecto

### 4.1 Separación estado / UI

Cada pantalla sigue el patrón del Dashboard:

```kotlin
@Composable
fun XScreen(
    modifier: Modifier = Modifier,
    viewModel: XViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    XContent(uiState = uiState, modifier = modifier)
}

@Composable
fun XContent(
    uiState: XUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is XUiState.Loading -> { /* LoadingIndicator centrado */ }
        is XUiState.Error -> { /* mensaje con colorScheme.error */ }
        is XUiState.Success -> { /* LazyColumn + secciones */ }
    }
}
```

Reglas:

- El `UiState` vive en `ui/uistate/` como `sealed interface` con `Loading`, `Success` y `Error`.
- `XContent` es pura: recibe estado y callbacks, no conoce el ViewModel ni el repositorio.
- La lógica de negocio no va en la UI. Las notas y validaciones siguen en `CourseGradesViewModel`.
- Los previews llaman a `XContent` con datos falsos, nunca al `XScreen` con Hilt.

### 4.2 Nombres observados

| Sufijo | Significado | Ejemplo |
|---|---|---|
| `XScreen` | Entrada con ViewModel / navegación | `DashboardScreen` |
| `XContent` | UI pura testeable y previsualizable | `DashboardContent` |
| `XSectionCard` | Contenedor de una sección completa | `TodayClassSectionCard` |
| `XItem` | Elemento dentro de una lista o carrusel | `ClassItem`, `CourseGradeItem` |
| `XCard` | Tarjeta independiente | `PendingTasksCard` |
| `SectionEmptyState` | Estado vacío reutilizable | `SectionEmptyState` |

- Componentes por feature en `ui/components/<feature>/`.
- Componentes compartidos entre features: `ui/components/` (hoy `dashboard/SectionEmptyState` es el estado vacío de referencia).
- Firma típica: datos/estado, callbacks, `modifier: Modifier = Modifier` al final.
- Usa `remember` para formatters y mapas derivados; usa `key(item.id)` en listas dinámicas.

### 4.3 Sección estándar

```kotlin
@Composable
fun XSectionCard(
    items: List<X>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Título de la sección",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(Modifier.height(8.dp))

            if (items.isEmpty()) {
                SectionEmptyState(
                    title = "Sin datos",
                    subtitle = "Describe el siguiente paso para el usuario",
                    icon = Icons.Rounded.School
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items.forEach { item ->
                        key(item.id) { XItem(item = item) }
                    }
                }
            }
        }
    }
}
```

### 4.4 Estados vacíos

- Reutiliza `SectionEmptyState(title, subtitle, icon)` para listas vacías dentro de tarjetas.
- No inventes un `Card` vacío nuevo. `TasksScreen` y `SemestersScreen` todavía tienen estados vacíos propios; al tocarlos, migra a `SectionEmptyState` o a un componente equivalente en su feature.
- El icono debe comunicar el estado (`EventAvailable` para día libre, `TaskAlt` para todo al día, `School` para sin materias).
- El subtítulo debe decir qué hacer, no solo repetir el título.

### 4.5 Carruseles

Patrón del Dashboard:

```kotlin
HorizontalUncontainedCarousel(
    state = rememberCarouselState { items.size },
    modifier = Modifier.fillMaxWidth(),
    itemWidth = 280.dp,
    itemSpacing = 12.dp,
    contentPadding = PaddingValues(horizontal = 2.dp)
) { index ->
    XItem(
        item = items[index],
        modifier = Modifier.maskClip(MaterialTheme.shapes.large)
    )
}
```

- Usa carrusel Material 3 cuando haya 2+ elementos horizontales; no inventes un `LazyRow` con decoraciones propias.
- `itemWidth` coherente con el contenido: 280 dp para clases, 180 dp para materias.
- Aplica `maskClip` con el token de forma del item.
- Si el carrusel está dentro de una tarjeta de sección, el padding externo ya lo aporta la sección; no dupliques márgenes grandes.

---

## 5. Integración con `BotsoMainApp`

### 5.1 Estructura global

`BotsoMainApp` ya provee:

- `CenterAlignedTopAppBar` transparente con el título dinámico de la ruta.
- `HorizontalFloatingToolbar` inferior con navegación y FAB contextual.
- `NavHost` con las rutas type-safe.
- Diálogos globales de tareas, semestres, cursos y confirmación de borrado.

Por lo tanto, una pantalla nueva **no debe**:

- Crear su propio `Scaffold`.
- Agregar otra top bar o un FAB de navegación.
- Pintar un fondo opaco de pantalla completa.
- Duplicar la lógica de diálogos globales.

### 5.2 Fondo transparente

El `Scaffold` y la top bar son transparentes; el fondo base lo pinta el tema. La pantalla debe dejar ver ese fondo:

```kotlin
LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(
        start = 16.dp,
        top = 16.dp,
        end = 16.dp,
        bottom = 112.dp
    ),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) { /* secciones */ }
```

- No uses `Modifier.background(MaterialTheme.colorScheme.surface)` en la raíz de la pantalla.
- No agregues `Color.White` / `Color.Black` como fondo.
- Si necesitas una superficie, que sea una tarjeta con token, no la pantalla completa.

### 5.3 Padding inferior y `HorizontalFloatingToolbar`

`BotsoMainApp` pasa al `NavHost` solo el padding superior:

```kotlin
NavHost(
    modifier = Modifier
        .fillMaxSize()
        .padding(top = paddingValues.calculateTopPadding())
)
```

La toolbar flota sobre el contenido y se oculta al hacer scroll (`exitAlwaysScrollBehavior`). Por eso:

- Todo `LazyColumn` / `LazyRow` de pantalla debe reservar **al menos la altura de la toolbar + margen** en su `contentPadding` inferior. Usa `112.dp` como valor de referencia del proyecto (toolbar + navigation bar + 16 dp).
- Si la pantalla usa `Column` con `verticalScroll`, agrega un `Spacer(Modifier.height(112.dp))` al final o un `padding(bottom = 112.dp)` en el contenedor desplazable.
- No confíes en el `paddingValues` del `Scaffold` para el borde inferior: no se está aplicando al `NavHost`.
- Verifica visualmente que el último item se pueda desplazar por encima de la toolbar y no quede tapado.

### 5.4 Navegación y estado

- Las rutas nuevas se declaran en `navigation/Routes.kt` como `@Serializable`.
- La navegación se dispara desde `BotsoMainApp` o desde callbacks de la pantalla; no navegues dentro de un componente de presentación.
- Mantén `launchSingleTop`, `restoreState` y `saveState` como en las rutas existentes.

---

## 6. Checklist operativo para una pantalla nueva

Antes de dar por terminada cualquier pantalla, tarjeta, diálogo, estado vacío o componente:

### Formas

- [ ] Todas las tarjetas usan `MaterialTheme.shapes.small/medium/large/extraLarge`; no hay `RoundedCornerShape(...)` nuevo sin justificar.
- [ ] Los badges usan `shapes.extraSmall`; los diálogos, `shapes.medium`.
- [ ] Las formas orgánicas usan `MaterialShapes.*.toShape()` con `@OptIn(ExperimentalMaterial3ExpressiveApi::class)`.
- [ ] Los items de carrusel usan `maskClip` con el token de forma correspondiente.

### Color y contraste

- [ ] No hay colores hexadecimales, `Color.White`, `Color.Black` ni verdes/rojos hardcodeados en la UI.
- [ ] Cada texto usa el token `on*` de su contenedor, o una inversión `rol` ↔ `rolContainer` verificada.
- [ ] Aprobado usa `LocalBotsoColors`; reprobado usa `error`.
- [ ] El modo claro y el oscuro se ven legibles; no hay texto que desaparezca.
- [ ] El preview existe en ambos modos:

```kotlin
@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun XPreview() {
    BotsoTheme { XContent(uiState = fakeState) }
}
```

### Tipografía y datos

- [ ] Hay un solo dato principal por tarjeta.
- [ ] Título de sección = `headlineSmall`; título de item = `titleLarge`; metadato = `bodyMedium` / `labelSmall`.
- [ ] Los números usan formato estable (`Locale.US`, 1–2 decimales; horas `HH:mm`).
- [ ] Títulos y metadatos largos tienen `maxLines` + `TextOverflow.Ellipsis`.
- [ ] Los textos están en español y el estado vacío dice qué hacer.

### Arquitectura

- [ ] Existe `XScreen` + `XContent`; `XContent` no conoce el ViewModel.
- [ ] El estado viene de `XUiState` (`Loading` / `Success` / `Error`).
- [ ] La pantalla no crea `Scaffold`, top bar ni FAB propios.
- [ ] La raíz es transparente y no pinta un fondo opaco.
- [ ] El `contentPadding` inferior es ≥ `112.dp` (o hay spacer equivalente) para no chocar con la `HorizontalFloatingToolbar`.
- [ ] Las listas vacías reutilizan `SectionEmptyState` o el estado vacío de su feature.
- [ ] Los carruseles usan `HorizontalUncontainedCarousel` + `rememberCarouselState`.
- [ ] No se duplica lógica de negocio (notas, validaciones, cálculos) en la UI.

### Verificación final

- [ ] `./gradlew :app:assembleDebug` compila sin errores.
- [ ] Si tocaste lógica de notas o estados, `./gradlew :app:testDebugUnitTest` pasa.
- [ ] Revisaste `TasksScreen`, `SemestersScreen`, `SettingsScreen` o `CourseGradesScreen` si el cambio era transversal.
- [ ] El resultado se parece al Dashboard: base neutra, un acento por tarjeta, datos grandes y formas tokenizadas.

---

## 7. Errores comunes

| Error | Por qué rompe el sistema | Corrección |
|---|---|---|
| `RoundedCornerShape(24.dp)` nuevo | Ignora `BotsoShapes` y desincroniza radios | `MaterialTheme.shapes.medium` |
| `Color(0xFF...)` en una pantalla | Rompe modo oscuro y color dinámico | Token de `MaterialTheme.colorScheme` o `LocalBotsoColors` |
| Texto `primary` sobre `secondary` | Contraste y jerarquía impredecibles | Usa `onSecondary` o invierte el rol completo |
| `Color.Green` para aprobado | No respeta la paleta ni el modo oscuro | `LocalBotsoColors.passContainer` / `onPassContainer` |
| Fondo `surface` en la raíz | Tapa el fondo base y la top bar transparente | Deja la raíz sin `background` |
| `contentPadding = PaddingValues(16.dp)` en una pantalla con toolbar | El último item queda debajo de la toolbar | Añade `bottom = 112.dp` |
| `displayLarge` en cada item | Comprime la jerarquía y rompe la lectura | Reserva `display*` para la métrica principal |
| Estado vacío con `Card` propio | Duplica estilos y pierde el patrón | Reutiliza `SectionEmptyState` |
| `fontWeight = FontWeight.Black` extra | El estilo ya define el peso | Usa el estilo tal cual |
| `Scaffold` dentro de una pantalla | Duplica top bar/toolbar y descuadra el layout | Usa el `Scaffold` global de `BotsoMainApp` |

---

## 8. Evidencia de la que sale esta skill

- `ui/theme/Color.kt`: paleta institucional, semántica de aprobado y `LocalBotsoColors`.
- `ui/theme/Theme.kt`: `MaterialExpressiveTheme`, `MotionScheme.expressive()`, `LocalBotsoColors`.
- `ui/theme/Type.kt`: escala tipográfica y usos documentados.
- `ui/theme/Shape.kt`: `BotsoShapes`, `HeroShape`, `FabShape`.
- `ui/screens/DashboardScreen.kt`: `LazyColumn` con `contentPadding`, secciones y previews de éxito/vacío.
- `ui/components/dashboard/`: `SectionCard`, `Item`, `SectionEmptyState`, carruseles y parejas de color reales.
- `navigation/BotsoMainApp.kt`: top bar transparente, `HorizontalFloatingToolbar`, `NavHost` y padding superior únicamente.
