# AGENTS.md — botso

Single-module Android app (`:app`, `com.onell.botso`). Compose (Material 3 Expressive) + Hilt + Room. `README.md` in Spanish; no CI.

## Commands
- Build debug: `./gradlew :app:assembleDebug`
- Unit tests (all): `./gradlew :app:testDebugUnitTest`
- Single test: `./gradlew :app:testDebugUnitTest --tests "com.onell.botso.CourseGradesViewModelTest"`
- Instrumented (needs emulator/device): `./gradlew :app:connectedDebugAndroidTest`
- Install debug: `./gradlew :app:installDebug` (package is `com.onell.botso.dev`, `-DEV` suffix)

## Architecture (`app/src/main/java/com/onell/botso/`)
- `MainActivity.kt` (`@AndroidEntryPoint`) observes `SettingsViewModel` (`ThemeMode`, dynamic color) → `BotsoTheme` (`MaterialExpressiveTheme` + `LocalBotsoColors`) → `navigation/BotsoMainApp.kt`
- `navigation/Routes.kt`: type-safe `@Serializable` sealed `Route` (`Dashboard`, `Tasks`, `Semesters`, `CourseGrades(courseId)`, `Settings`)
- Layers: `data/` (Room `local/`, `repository/*Impl`, `mapper/`, `receiver/`, `service/`, `worker/`) → `domain/` (models, `repository/` interfaces, `usecase/<entity>/`) → `ui/` (`screens/`, `viewmodel/`, `uistate/`, `components/`, `theme/`)
- DI (`di/`): `DatabaseModule` provides `BotsoDataBase` + DAOs; `DataStoreModule` provides `DataStore<Preferences>`; `NotificationModule` provides `NotificationManagerCompat` + channel; `RepositoryModule` binds `*Impl` → domain interfaces. New repo = impl + interface + `@Binds` entry.
- ViewModels are `@HiltViewModel`, expose `StateFlow<*UiState>`; screens collect with `collectAsStateWithLifecycle()` and delegate pure UI to `*Content` composables.

## Gotchas
- `data/local/BotsoDataBase.kt`: v8, `exportSchema=false`, builder has **no migration / no `fallbackToDestructiveMigration()`** — bumping `version` without adding a migration crashes on existing installs.
- Use-case filenames are inconsistently capitalized (`getX` vs `Update…`, and package `domain/usecase/settigns/` has a legacy typo); match existing name/package when importing, don't "fix" casing or package names in passing.
- UI strings/state are Spanish (`Tareas`, `Semestres`, `Notas`, `Ajustes`); keep it.
- Grade logic lives in `ui/viewmodel/CourseGradesViewModel.kt` (0.0–5.0 validation, term 3 weight 0.20 else 0.15, Formativa/Cognitiva name matching) and `domain/model/CourseWithGrades.kt` (`averageGrade`) — don't duplicate elsewhere.
- `org.gradle.configuration-cache=true`; prefer `./gradlew` over direct `gradle`.
