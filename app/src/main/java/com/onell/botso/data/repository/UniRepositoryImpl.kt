package com.onell.botso.data.repository

import com.onell.botso.data.local.dao.*
import com.onell.botso.data.local.entity.*
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.domain.repository.UniRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UniRepositoryImpl @Inject constructor(
    private val semesterDao: SemesterDao,
    private val courseDao: CourseDao,
    private val gradeDao: GradeDao,
    private val taskDao: TaskDao,
    private val sessionDao: ClassSessionDao
) : UniRepository {
    // Semesters
    override fun getAllSemesters(): Flow<List<Semester>> = semesterDao.getAllSemesters()
    
    override fun getActiveSemester(): Flow<Semester?> = semesterDao.getActiveSemester()
    
    override suspend fun insertSemester(semester: Semester): Long = semesterDao.insertSemester(semester)
    
    override suspend fun updateSemester(semester: Semester) = semesterDao.updateSemester(semester)
    
    override suspend fun deleteSemester(semester: Semester) = semesterDao.deleteSemester(semester)
    
    // Courses
    override fun getAllCourses(): Flow<List<Course>> = courseDao.getAllCourses()

    override fun getCoursesForSemester(semesterId: Long): Flow<List<Course>> = courseDao.getCoursesForSemester(semesterId)
    
    override fun getCourseById(courseId: Long): Flow<Course?> = courseDao.getCourseById(courseId)

    override suspend fun insertCourse(course: Course): Long = courseDao.insertCourse(course)
    
    override suspend fun updateCourse(course: Course) = courseDao.updateCourse(course)
    
    override suspend fun deleteCourse(course: Course) = courseDao.deleteCourse(course)
    
    // Grades
    override fun getAllGrades(): Flow<List<Grade>> = gradeDao.getAllGrades()

    override fun getGradesForCourse(courseId: Long): Flow<List<Grade>> = gradeDao.getGradesForCourse(courseId)
    
    override suspend fun insertGrade(grade: Grade): Long = gradeDao.insertGrade(grade)
    
    override suspend fun updateGrade(grade: Grade) = gradeDao.updateGrade(grade)
    
    override suspend fun deleteGrade(grade: Grade) = gradeDao.deleteGrade(grade)

    // Tasks
    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    override fun getTasksForCourse(courseId: Long): Flow<List<Task>> = taskDao.getTasksForCourse(courseId)

    override fun getTasksWithCourse(): Flow<List<TaskWithCourse>> {
        return combine(taskDao.getAllTasks(), courseDao.getAllCourses()) { tasks, courses ->
            tasks.map { task ->
                TaskWithCourse(task, courses.firstOrNull { it.id == task.courseId } ?: Course(id = 0, semesterId = 0, name = "Unknown", code = "", colorHex = "", location = "", professor = "", dayOfWeek = 1, startTime = "", endTime = "", isRemote = false))
            }
        }
    }

    override fun getPendingTasksCount(): Flow<Int> = taskDao.getPendingTasksCount()

    override fun getPriorityTasks(): Flow<List<TaskWithCourse>> {
        return getTasksWithCourse().map { tasks ->
            tasks.filter { it.task.isPriority && it.task.status != "DONE" }
        }
    }

    override suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    override suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    // Class Sessions
    override fun getAllClassSessions(): Flow<List<ClassSessionWithCourse>> {
        return combine(sessionDao.getAllSessions(), courseDao.getAllCourses()) { sessions, courses ->
            sessions.map { session ->
                ClassSessionWithCourse(session, courses.firstOrNull { it.id == session.courseId } ?: Course(id = 0, semesterId = 0, name = "Unknown", code = "", colorHex = "", location = "", professor = "", dayOfWeek = 1, startTime = "", endTime = "", isRemote = false))
            }
        }
    }

    override fun getSessionsForDay(dayOfWeek: Int): Flow<List<ClassSessionWithCourse>> {
        return combine(
            sessionDao.getSessionsForDay(dayOfWeek),
            courseDao.getAllCourses(),
            sessionDao.getAllSessions()
        ) { sessionsForDay, allCourses, allSessions ->
            val coursesWithAnySession = allSessions.map { it.courseId }.toSet()

            val actualSessions = sessionsForDay.map { session ->
                ClassSessionWithCourse(
                    session = session,
                    course = allCourses.firstOrNull { it.id == session.courseId } ?: Course(id = 0, semesterId = 0, name = "Unknown", code = "", colorHex = "", location = "", professor = "", dayOfWeek = 1, startTime = "", endTime = "", isRemote = false)
                )
            }

            val courseVirtualSessions = allCourses.filter {
                it.dayOfWeek == dayOfWeek && it.id !in coursesWithAnySession && it.startTime.isNotBlank()
            }.map { course ->
                ClassSessionWithCourse(
                    session = ClassSession(
                        courseId = course.id,
                        dayOfWeek = course.dayOfWeek,
                        startTime = course.startTime,
                        endTime = course.endTime,
                        room = course.location
                    ),
                    course = course
                )
            }

            actualSessions + courseVirtualSessions
        }
    }

    override fun getSessionsForCourse(courseId: Long): Flow<List<ClassSession>> = sessionDao.getSessionsForCourse(courseId)

    override suspend fun insertClassSession(session: ClassSession): Long = sessionDao.insertSession(session)

    override suspend fun updateClassSession(session: ClassSession) = sessionDao.updateSession(session)

    override suspend fun deleteClassSession(session: ClassSession) = sessionDao.deleteSession(session)

    override suspend fun initializeDatabaseIfEmpty() {
        // No mock data as per requirements
    }
}
