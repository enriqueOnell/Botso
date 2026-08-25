# Fix Dashboard not reflecting newly added courses

Newly added courses are not appearing in the "Clases de Hoy" section of the Dashboard because the Dashboard only fetches data from the `class_sessions` table via `repository.getSessionsForDay(dayOfWeek)`. When a course is added, it stores its schedule in the `courses` table, but doesn't automatically create a `ClassSession` entry. The `ScheduleViewModel` already handles this by combining both sources, but the `DashboardViewModel` (via the repository) does not.

## Proposed Changes

### [Data Layer]

#### [MODIFY] [UniRepositoryImpl.kt](file:///home/onell/AndroidStudioProjects/UniApp/app/src/main/java/com/onell/UniApp/data/repository/UniRepositoryImpl.kt)
Update `getSessionsForDay(dayOfWeek: Int)` to include courses that match the specified day and do not have any explicit class sessions defined. This matches the logic used in the schedule view.

## Verification Plan

### Automated Tests
- I will check if there are existing tests for `UniRepositoryImpl` and add a test case if possible to verify `getSessionsForDay` returns both explicit sessions and courses without sessions.

### Manual Verification
- I will simulate adding a course for "Today" and verify it appears in the Dashboard. (Since I can't run the app, I'll rely on code analysis and potentially unit tests).
