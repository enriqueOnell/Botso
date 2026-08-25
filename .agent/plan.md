# Project Plan

UniApp: An academic organization app for university students. Features include a daily dashboard, weekly schedule, Kanban task board, semester tracking, and grade prediction with weighted averages. The design follows an 'Academic Organic' style with Material 3, rounded corners (24dp), and a specific color palette (Primary: #4f378a, Secondary Container: #a0f399). Architecture: MVVM + Clean Architecture. Tech: Kotlin, Jetpack Compose, Hilt, Room, Navigation Compose.

## Project Brief

# Project Brief: UniApp

## Features
*   **Daily Dashboard**: A centralized view for students to quickly see their upcoming classes and urgent tasks for the day.
*   **Weekly Schedule**: An interactive visual timetable for managing university lectures, seminars, and laboratory sessions.
*   **Kanban Task Board**: A productivity-focused board for tracking assignments and projects through various stages of completion.
*   **Semester Tracking**: A management module to organize course details, credits, and academic progress throughout the semester.
*   **Grade Prediction**: A specialized calculator that helps students estimate their final marks using weighted averages and current performance data.

## High-Level Tech Stack
*   **Kotlin**: The foundational language for the application logic.
*   **Jetpack Compose**: Utilized with **Material 3** to implement the "Academic Organic" design style.
*   **Jetpack Navigation 3**: A modern, state-driven navigation framework for the app's flow.
*   **Compose Material Adaptive**: Used to create responsive layouts that work seamlessly across phones, tablets, and foldables.
*   **Hilt**: For streamlined dependency injection across the MVVM and Clean Architecture layers.
*   **Room**: To provide reliable local persistence for student data and academic records.
*   **Coroutines & Flow**: To handle asynchronous operations and reactive data updates efficiently.

## Implementation Steps
**Total Duration:** 56m 33s

### Task_1_Foundation_Navigation: Setup Hilt DI, Room database (Semester, Course, Grade, Task, Schedule), and Navigation with 'Academic Organic' Theme (24dp corners, BottomBar with green pill indicator).
- **Status:** COMPLETED
- **Updates:** Initialized Hilt DI, created Room database entities and DAOs (Semester, Course, Grade, Task, ClassSession), implemented 'Academic Organic' Theme with #4f378a Primary and #a0f399 SecondaryContainer, and setup Navigation Compose with the UniBottomNavigationBar. Project structure follows MVVM/Clean Architecture.
- **Acceptance Criteria:**
  - Project structure (MVVM+Clean) created
  - Hilt initialized
  - Room entities and DAOs defined
  - Theme with 24dp rounded corners and BottomBar navigation working
- **Duration:** 20m

### Task_2_Academic_Management: Implement Semester and CourseGrades screens, including weighted average calculation logic and Room persistence.
- **Status:** COMPLETED
- **Updates:** Implemented SemestersScreen and CourseGradesScreen with Room persistence. CourseGradesScreen includes tabbed cuts (Corte 1, 2, 3), real-time weighted average calculation, and the CircularGradeProgress component. SemestersScreen displays semester cards with status chips and metrics.
- **Acceptance Criteria:**
  - Semester listing and creation implemented
  - Course management with weighted grade calculation working
  - Data persists across app restarts
- **Duration:** 7m 43s

### Task_3_Academic_Dashboard_Tools: Implement Dashboard, Schedule, and Kanban board screens.
- **Status:** COMPLETED
- **Updates:** Implemented Dashboard, Schedule, and Kanban screens with full UI fidelity to mockups. Dashboard shows daily summaries and priorities. Schedule features a weekly timetable with color-coded classes. Kanban board manages tasks with status columns. Integrated with Room database and populated with realistic mock data. 24dp corners and 'Academic Organic' theme applied throughout.
- **Acceptance Criteria:**
  - Dashboard provides academic overview
  - Schedule displays weekly timetable
  - Kanban board manages tasks effectively
- **Duration:** 25m 3s

### Task_4_Run_Verify: Run the application and verify all features, UI fidelity, and stability.
- **Status:** COMPLETED
- **Updates:** Application verified by critic_agent. Stability, UI fidelity (24dp corners, pill buttons, color palette), and core functionality (Dashboard, Schedule, Kanban, CourseGrades logic) are all confirmed. App is demo-ready and persists data correctly.
- **Acceptance Criteria:**
  - Make sure all existing tests pass
  - Build pass
  - App does not crash
  - UI follows 'Academic Organic' design requirements (24dp corners, pill buttons)
  - Critic agent confirms alignment with requirements
- **Duration:** 3m 47s

