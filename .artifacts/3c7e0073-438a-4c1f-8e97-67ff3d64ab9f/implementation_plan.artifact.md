# Implementation Plan - UniApp Foundation

Initialize the project with Hilt, Room Database, Academic Organic Theme, and Navigation 3.

## User Review Required
- The design images `input_file_0.png` through `input_file_4.png` were not found in the project. I will rely on the text description provided in the prompt. If these images are critical, please ensure they are available or provide more details.
- I will use the latest stable versions of Hilt and Room.

## Proposed Changes

### [Hilt Setup]
- Add Hilt dependencies to `libs.versions.toml` and `build.gradle.kts`.
- Create `UniApplication` and annotate with `@HiltAndroidApp`.
- Update `AndroidManifest.xml`.

### [Room Database]
- [NEW] Entities: `Semester`, `Course`, `Grade`, `Task`, `ClassSession`.
- [NEW] DAOs for each entity.
- [NEW] `UniDatabase` class.
- [NEW] `DatabaseModule` for Hilt.

### [Theme - Academic Organic]
- [MODIFY] `Color.kt`: Set `Primary` to #4f378a and `SecondaryContainer` to #a0f399.
- [MODIFY] `Shape.kt`: 24dp rounded corners for cards and large containers.
- [MODIFY] `Type.kt`: Roboto Flex configuration.
- [MODIFY] `Theme.kt`: Material 3 theme with dynamic color support and fallbacks.

### [Navigation & UI Structure]
- [NEW] `Navigation.kt`: Setup Navigation 3 with routes for Dashboard, Schedule, Kanban, Semesters, and CourseGrades.
- [NEW] `UniBottomNavigationBar`: Custom bottom bar with pill-shaped indicator.
- [NEW] Screen placeholders for each route.

### [Architecture]
- Organize files into `data`, `domain`, `ui`, `navigation`, and `theme` packages.

## Verification Plan
### Automated Tests
- Build the project using `./gradlew assembleDebug`.
- Run Room database tests if time permits.

### Manual Verification
- Verify the bottom bar and theme in Compose Previews.
