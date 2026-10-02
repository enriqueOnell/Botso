# AGENTS.md — botso

Single-module Android app (`:app`, `com.onell.botso`). Compose + Hilt + Room. No README/CI.

## Commands
- Build debug: `./gradlew :app:assembleDebug`
- Unit tests (all): `./gradlew :app:testDebugUnitTest`
- Single test: `./gradlew :app:testDebugUnitTest --tests "com.onell.botso.CourseGradesViewModelTest"`
- Instrumented (needs emulator/device): `./gradlew :app:connectedDebugAndroidTest`
- Install debug: `./gradlew :app:installDebug` (package is `com.onell.botso.dev`, `-DEV` suffix)

## Architecture (`app/src/main/java/com/onell/botso/`)
- `MainActivity.kt` (`@AndroidEntryPoint`) → `BotsoTheme` → `navigation/BotsoMainApp.kt`
- `navigation/Routes.kt`: type-safe `@Serializable` sealed `Route` (Dashboard, Tasks, Semesters, CourseGrades(courseId))
- Layers: `data/` (Room `local/`, `repository/*Impl`, `mapper/`, `provider/`) → `domain/` (models, `repository/` interfaces, `usecase/<entity>/`, `provider/`) → `ui/` (`screens/`, `viewmodel/`, `uistate/`, `components/`)
- DI (`di/`): `DatabaseModule` provides `BotsoDataBase` + DAOs; `RepositoryModule` binds `*Impl` → interfaces. New repo = impl + interface + `@Binds` entry.
- ViewModels are `@HiltViewModel`, expose `StateFlow<*UiState>`; screens collect with `collectAsStateWithLifecycle()`.

## Gotchas
- `data/local/BotsoDataBase.kt`: v8, `exportSchema=false`, builder has **no migration / no `fallbackToDestructiveMigration()`** — bumping `version` without adding a migration crashes on existing installs.
- Use-case filenames are inconsistently capitalized (`getX` vs `Update…`, `Generate…`); match existing name when importing, don't "fix" casing in passing.
- UI strings/state are Spanish (Tareas, Semestres, Notas); keep it.
- Grade logic lives in `ui/viewmodel/CourseGradesViewModel.kt` (0.0–5.0 validation, term 3 weight 0.20 else 0.15, Formativa/Cognitiva name matching) — don't duplicate elsewhere.
- `org.gradle.configuration-cache=true`; prefer `./gradlew` over direct `gradle`.
