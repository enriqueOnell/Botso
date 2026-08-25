package com.onell.UniApp.domain.repository

import com.onell.UniApp.data.local.entity.*
import com.onell.UniApp.domain.model.ClassSessionWithCourse
import com.onell.UniApp.domain.model.TaskWithCourse
import kotlinx.coroutines.flow.Flow

interface UniRepository {
    // Semesters
    fun getAllSemesters(): Flow<List<Semester>>
    fun getActiveSemester(): Flow<Semester?>
    suspend fun insertSemester(semester: Semester): Long
    suspend fun updateSemester(semester: Semester)
    suspend fun deleteSemester(semester: Semester)
    
    // Courses
    fun getAllCourses(): Flow<List<Course>>
    fun getCoursesForSemester(semesterId: Long): Flow<List<Course>>
    fun getCourseById(courseId: Long): Flow<Course?>
    suspend fun insertCourse(course: Course): Long
    suspend fun updateCourse(course: Course)
    suspend fun deleteCourse(course: Course)
    
    // Grades
    fun getAllGrades(): Flow<List<Grade>>
    fun getGradesForCourse(courseId: Long): Flow<List<Grade>>
    suspend fun insertGrade(grade: Grade): Long
    suspend fun updateGrade(grade: Grade)
    suspend fun deleteGrade(grade: Grade)

    // Tasks
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksForCourse(courseId: Long): Flow<List<Task>>
    fun getTasksWithCourse(): Flow<List<TaskWithCourse>>
    fun getPendingTasksCount(): Flow<Int>
    fun getPriorityTasks(): Flow<List<TaskWithCourse>>
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)

    // Class Sessions
    fun getAllClassSessions(): Flow<List<ClassSessionWithCourse>>
    fun getSessionsForDay(dayOfWeek: Int): Flow<List<ClassSessionWithCourse>>
    fun getSessionsForCourse(courseId: Long): Flow<List<ClassSession>>
    suspend fun insertClassSession(session: ClassSession): Long
    suspend fun updateClassSession(session: ClassSession)
    suspend fun deleteClassSession(session: ClassSession)

    suspend fun initializeDatabaseIfEmpty()
}
