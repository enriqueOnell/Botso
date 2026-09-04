package com.onell.botso

import com.onell.botso.data.local.entity.*
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.ui.viewmodel.CourseGradesViewModel
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
            GradeEntity(courseId = 1, termId = 1, name = "Nota Formativa", score = 4.0, weight = 0.15),
            GradeEntity(courseId = 1, termId = 1, name = "Nota Cognitiva", score = 3.0, weight = 0.15),
            GradeEntity(courseId = 1, termId = 2, name = "Nota Formativa", score = 5.0, weight = 0.15),
            GradeEntity(courseId = 1, termId = 2, name = "Nota Cognitiva", score = 5.0, weight = 0.15),
            GradeEntity(courseId = 1, termId = 3, name = "Nota Formativa", score = 2.0, weight = 0.20),
            GradeEntity(courseId = 1, termId = 3, name = "Nota Cognitiva", score = 2.0, weight = 0.20)
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
        private val _grades = MutableStateFlow<List<GradeEntity>>(emptyList())
        
        fun setGrades(grades: List<GradeEntity>) {
            _grades.value = grades
        }

        override fun getAllSemesters(): Flow<List<SemesterEntity>> = flowOf(emptyList())
        override fun getActiveSemester(): Flow<SemesterEntity?> = flowOf(null)
        override suspend fun insertSemester(semester: SemesterEntity): Long = 0
        override suspend fun updateSemester(semester: SemesterEntity) {}
        override suspend fun deleteSemester(semester: SemesterEntity) {}
        
        override fun getAllCourses(): Flow<List<CourseEntity>> = flowOf(emptyList())
        override fun getCoursesForSemester(semesterId: Long): Flow<List<CourseEntity>> = flowOf(emptyList())
        override fun getCourseById(courseId: Long): Flow<CourseEntity?> = flowOf(null)
        override suspend fun insertCourse(course: CourseEntity): Long = 0
        override suspend fun updateCourse(course: CourseEntity) {}
        override suspend fun deleteCourse(course: CourseEntity) {}
        
        override fun getAllGrades(): Flow<List<GradeEntity>> = _grades.asStateFlow()
        override fun getGradesForCourse(courseId: Long): Flow<List<GradeEntity>> = _grades.asStateFlow()
        override suspend fun insertGrade(grade: GradeEntity): Long {
            val current = _grades.value.toMutableList()
            current.add(grade)
            _grades.value = current
            return 0
        }
        override suspend fun updateGrade(grade: GradeEntity) {
            val current = _grades.value.toMutableList()
            val index = current.indexOfFirst { it.id == grade.id || (it.courseId == grade.courseId && it.termId == grade.termId && it.name == grade.name) }
            if (index != -1) {
                current[index] = grade
            } else {
                current.add(grade)
            }
            _grades.value = current
        }
        override suspend fun deleteGrade(grade: GradeEntity) {}

        override fun getAllTasks(): Flow<List<TaskEntity>> = flowOf(emptyList())
        override fun getTasksForCourse(courseId: Long): Flow<List<TaskEntity>> = flowOf(emptyList())
        override fun getTasksWithCourse(): Flow<List<TaskWithCourse>> = flowOf(emptyList())
        override fun getPendingTasksCount(): Flow<Int> = flowOf(0)
        override fun getPriorityTasks(): Flow<List<TaskWithCourse>> = flowOf(emptyList())
        override suspend fun insertTask(task: TaskEntity): Long = 0
        override suspend fun updateTask(task: TaskEntity) {}
        override suspend fun deleteTask(task: TaskEntity) {}

        override fun getAllClassSessions(): Flow<List<ClassSessionWithCourse>> = flowOf(emptyList())
        override fun getSessionsForDay(dayOfWeek: Int): Flow<List<ClassSessionWithCourse>> = flowOf(emptyList())
        override fun getSessionsForCourse(courseId: Long): Flow<List<ClassSessionEntity>> = flowOf(emptyList())
        override suspend fun insertClassSession(session: ClassSessionEntity): Long = 0
        override suspend fun updateClassSession(session: ClassSessionEntity) {}
        override suspend fun deleteClassSession(session: ClassSessionEntity) {}

        override suspend fun initializeDatabaseIfEmpty() {}
    }
}
