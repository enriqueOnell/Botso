# Update Grading Weights for CourseGrades

Update the grading logic to follow the 15%/15% (Corte 1 & 2) and 20%/20% (Corte 3) distribution.

## Proposed Changes

### [CourseGradesViewModel](file:///home/onell/AndroidStudioProjects/UniApp/app/src/main/java/com/onell/UniApp/ui/viewmodel/CourseGradesViewModel.kt)

- Update `currentTermAverage` to use equal weights (50/50) for all terms, as both Formativa and Cognitiva are equal within their respective cuts (15/15 and 20/20).
- Update `saveGrades` to use weights:
    - Corte 1 & 2: 0.15 for both.
    - Corte 3: 0.20 for both.
- Update `calculateTotalAverage` to simply sum all `score * weight` from the grades list.

### [CourseGradesScreen](file:///home/onell/AndroidStudioProjects/UniApp/app/src/main/java/com/onell/UniApp/ui/screens/CourseGradesScreen.kt)

- Update `GradeInputSection` to accept `termId`.
- Dynamically update labels in `GradeInputField` based on `termId`:
    - 1 or 2: "Nota Formativa (15%)", "Nota Cognitiva (15%)".
    - 3: "Nota Formativa (20%)", "Nota Cognitiva (20%)".

### [UniRepositoryImpl](file:///home/onell/AndroidStudioProjects/UniApp/app/src/main/java/com/onell/UniApp/data/repository/UniRepositoryImpl.kt)

- Verify if any seeding logic needs update. (Currently empty, no changes needed unless mock data is requested).

## Verification Plan

### Automated Tests
- Build the project: `./gradlew :app:assembleDebug`

### Manual Verification
- Check UI labels for each tab (Corte 1, 2, 3).
- Verify "Promedio Total" calculation logic by entering sample grades.
- Example:
    - Corte 1: 5.0, 5.0 -> Corte Avg 5.0, Contribution 1.5
    - Corte 2: 5.0, 5.0 -> Corte Avg 5.0, Contribution 1.5
    - Corte 3: 5.0, 5.0 -> Corte Avg 5.0, Contribution 2.0
    - Total: 5.0
