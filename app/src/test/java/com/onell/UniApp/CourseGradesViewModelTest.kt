package com.onell.UniApp

import com.onell.UniApp.data.local.entity.*
import com.onell.UniApp.domain.model.ClassSessionWithCourse
import com.onell.UniApp.domain.model.TaskWithCourse
import com.onell.UniApp.domain.repository.UniRepository
import com.onell.UniApp.ui.viewmodel.CourseGradesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CourseGradesViewModelTest {

    private lateinit var viewModel: CourseGradesViewModel
    private lateinit var fakeRepository: FakeUniRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeUniRepository()
        viewModel = CourseGradesViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `calculate average correctly with mixed grades`() = runTest {
        val grades = listOf(
            Grade(courseId = 1, termId = 1, name = "Nota Formativa", score = 4.0, weight = 0.15),
            Grade(courseId = 1, termId = 1, name = "Nota Cognitiva", score = 3.0, weight = 0.15),
            Grade(courseId = 1, termId = 2, name = "Nota Formativa", score = 5.0, weight = 0.15),
            Grade(courseId = 1, termId = 2, name = "Nota Cognitiva", score = 5.0, weight = 0.15),
            Grade(courseId = 1, termId = 3, name = "Nota Formativa", score = 2.0, weight = 0.20),
            Grade(courseId = 1, termId = 3, name = "Nota Cognitiva", score = 2.0, weight = 0.20)
        )
        fakeRepository.setGrades(grades)
        
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.averageScore.collect {}
        }

        viewModel.setCourseId(1)

        // Corte 1: 4.0 * 0.15 + 3.0 * 0.15 = 0.6 + 0.45 = 1.05
        // Corte 2: 5.0 * 0.15 + 5.0 * 0.15 = 0.75 + 0.75 = 1.5
        // Corte 3: 2.0 * 0.20 + 2.0 * 0.20 = 0.4 + 0.4 = 0.8
        // Total Average: 1.05 + 1.5 + 0.8 = 3.35

        assertEquals(3.35, viewModel.averageScore.value, 0.0001)
    }

    @Test
    fun `saveGrades updates repository correctly`() = runTest {
        viewModel.setCourseId(1)
        viewModel.setTermId(1)
        viewModel.updateStagedFormativa("4.5")
        viewModel.updateStagedCognitiva("3.5")
        
        viewModel.saveGrades()
        
        val savedGrades = fakeRepository.getGradesForCourse(1).first()
        val formativa = savedGrades.find { it.name == "Nota Formativa" && it.termId == 1 }
        val cognitiva = savedGrades.find { it.name == "Nota Cognitiva" && it.termId == 1 }
        
        assertEquals(4.5, formativa?.score ?: 0.0, 0.0)
        assertEquals(3.5, cognitiva?.score ?: 0.0, 0.0)
    }

    @Test
    fun `isValidInput rejects values outside 0 to 5`() = runTest {
        viewModel.updateStagedFormativa("6.0")
        assertEquals("", viewModel.stagedFormativa.value)
        
        viewModel.updateStagedFormativa("-1.0")
        assertEquals("", viewModel.stagedFormativa.value)
        
        viewModel.updateStagedFormativa("4.2")
        assertEquals("4.2", viewModel.stagedFormativa.value)
    }

    class FakeUniRepository : UniRepository {
        private val _grades = MutableStateFlow<List<Grade>>(emptyList())
        
        fun setGrades(grades: List<Grade>) {
            _grades.value = grades
        }

        override fun getAllSemesters(): Flow<List<Semester>> = flowOf(emptyList())
        override fun getActiveSemester(): Flow<Semester?> = flowOf(null)
        override suspend fun insertSemester(semester: Semester): Long = 0
        override suspend fun updateSemester(semester: Semester) {}
        override suspend fun deleteSemester(semester: Semester) {}
        
        override fun getAllCourses(): Flow<List<Course>> = flowOf(emptyList())
        override fun getCoursesForSemester(semesterId: Long): Flow<List<Course>> = flowOf(emptyList())
        override fun getCourseById(courseId: Long): Flow<Course?> = flowOf(null)
        override suspend fun insertCourse(course: Course): Long = 0
        override suspend fun updateCourse(course: Course) {}
        override suspend fun deleteCourse(course: Course) {}
        
        override fun getAllGrades(): Flow<List<Grade>> = _grades.asStateFlow()
        override fun getGradesForCourse(courseId: Long): Flow<List<Grade>> = _grades.asStateFlow()
        override suspend fun insertGrade(grade: Grade): Long {
            val current = _grades.value.toMutableList()
            current.add(grade)
            _grades.value = current
            return 0
        }
        override suspend fun updateGrade(grade: Grade) {
            val current = _grades.value.toMutableList()
            val index = current.indexOfFirst { it.id == grade.id || (it.courseId == grade.courseId && it.termId == grade.termId && it.name == grade.name) }
            if (index != -1) {
                current[index] = grade
            } else {
                current.add(grade)
            }
            _grades.value = current
        }
        override suspend fun deleteGrade(grade: Grade) {}

        override fun getAllTasks(): Flow<List<Task>> = flowOf(emptyList())
        override fun getTasksForCourse(courseId: Long): Flow<List<Task>> = flowOf(emptyList())
        override fun getTasksWithCourse(): Flow<List<TaskWithCourse>> = flowOf(emptyList())
        override fun getPendingTasksCount(): Flow<Int> = flowOf(0)
        override fun getPriorityTasks(): Flow<List<TaskWithCourse>> = flowOf(emptyList())
        override suspend fun insertTask(task: Task): Long = 0
        override suspend fun updateTask(task: Task) {}
        override suspend fun deleteTask(task: Task) {}

        override fun getAllClassSessions(): Flow<List<ClassSessionWithCourse>> = flowOf(emptyList())
        override fun getSessionsForDay(dayOfWeek: Int): Flow<List<ClassSessionWithCourse>> = flowOf(emptyList())
        override fun getSessionsForCourse(courseId: Long): Flow<List<ClassSession>> = flowOf(emptyList())
        override suspend fun insertClassSession(session: ClassSession): Long = 0
        override suspend fun updateClassSession(session: ClassSession) {}
        override suspend fun deleteClassSession(session: ClassSession) {}

        override suspend fun initializeDatabaseIfEmpty() {}
    }
}
