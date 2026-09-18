package com.onell.botso.data.mapper

import com.onell.botso.data.local.entity.ClassSessionEntity
import com.onell.botso.data.local.entity.CourseWithSessionEntity
import com.onell.botso.data.local.entity.CourseWithTaskEntity
import com.onell.botso.data.local.entity.CourseEntity
import com.onell.botso.data.local.entity.CourseWithGradesEntity
import com.onell.botso.data.local.entity.GradeEntity
import com.onell.botso.data.local.entity.SemesterEntity
import com.onell.botso.data.local.entity.SemesterWithCourseEntity
import com.onell.botso.data.local.entity.TaskEntity
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.CourseWithTask
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

fun ClassSessionEntity.toDomain(): ClassSession {
    return ClassSession(
        id = this.id,
        courseId = this.courseId,
        dayOfWeek = this.dayOfWeek,
        startTime = LocalTime.parse(this.startTime),
        endTime = LocalTime.parse(this.endTime),
        room = this.room,
        isRemote = this.isRemote
    )
}

fun ClassSession.toEntity(): ClassSessionEntity{
    return ClassSessionEntity(
        id = this.id.ifBlank { UUID.randomUUID().toString() },
        courseId = this.courseId,
        dayOfWeek = this.dayOfWeek,
        startTime = this.startTime.toString(),
        endTime = this.endTime.toString(),
        room = this.room,
        isRemote = this.isRemote
    )
}
fun CourseEntity.toDomain(): Course {
    return Course(
        id = this.id,
        semesterId = this.semesterId,
        name = this.name,
        professor = this.professor,
    )
}
fun Course.toEntity(): CourseEntity {
    return CourseEntity(
        id = this.id.ifBlank { UUID.randomUUID().toString() },
        semesterId = this.semesterId,
        name = this.name,
        professor = this.professor,
    )
}

fun GradeEntity.toDomain(): Grade{
    return Grade(
        id = this.id,
        courseId = this.courseId,
        name = this.name,
        score = this.score,
        weight = this.weight,
        termId = this.termId
    )
}

fun Grade.toEntity(): GradeEntity{
    return GradeEntity(
        id = this.id.ifBlank { UUID.randomUUID().toString() },
        courseId = this.courseId,
        name = this.name,
        score = this.score,
        weight = this.weight,
        termId = this.termId
    )
}

fun SemesterEntity.toDomain(): Semester {
    return Semester(
        id = this.id,
        name = this.name,
        startDate = LocalDate.parse(this.startDate),
        endDate = LocalDate.parse(this.endDate),
        isActive = this.isActive
    )
}

fun Semester.toEntity(): SemesterEntity {
    return SemesterEntity(
        id = this.id.ifBlank { UUID.randomUUID().toString() },
        name = this.name,
        startDate = this.startDate.toString(),
        endDate = this.endDate.toString(),
        isActive = this.isActive
    )
}

fun TaskEntity.toDomain(): Task{
    return Task(
        id = this.id,
        courseId = this.courseId,
        title = this.title,
        dueDate = LocalDate.parse(this.dueDate),
        isPriority = this.isPriority,
        hasAttachment = this.hasAttachment,
        status = TaskStatus.entries.find { it.key == this.status } ?: TaskStatus.TODO,
        week = this.week,
        description = this.description
    )
}

fun Task.toEntity(): TaskEntity{
    return TaskEntity(
        id = this.id.ifBlank { UUID.randomUUID().toString() },
        courseId = this.courseId,
        title = this.title,
        dueDate = this.dueDate.toString(),
        isPriority = this.isPriority,
        hasAttachment = this.hasAttachment,
        status = this.status.key,
        week = this.week,
        description = this.description
    )
}

fun SemesterWithCourseEntity.toDomain(): SemesterWithCourse {
    return SemesterWithCourse(
        semester = this.semester.toDomain(),
        courses = this.courses.flatMap { courseEntityWithSessions ->
            if (courseEntityWithSessions.sessions.isEmpty()) {
                listOf(
                    CourseWithSession(
                        course = courseEntityWithSessions.course.toDomain(),
                        session = ClassSession(
                            id = "",
                            courseId = courseEntityWithSessions.course.id,
                            dayOfWeek = 1,
                            startTime = LocalTime.of(8, 0),
                            endTime = LocalTime.of(10, 0),
                            room = "",
                            isRemote = false
                        )
                    )
                )
            } else {
                courseEntityWithSessions.sessions.map { sessionEntity ->
                    CourseWithSession(
                        course = courseEntityWithSessions.course.toDomain(),
                        session = sessionEntity.toDomain()
                    )
                }
            }
        }
    )
}

fun CourseWithTaskEntity.toDomain(): List<CourseWithTask> {
    return this.tasks.map { taskEntity ->
        CourseWithTask(
            course = this.course.toDomain(),
            task = taskEntity.toDomain()
        )
    }
}

fun CourseWithSessionEntity.toDomain(): List<CourseWithSession> {
    return if (this.sessions.isEmpty()) {
        listOf(
            CourseWithSession(
                course = this.course.toDomain(),
                session = ClassSession(
                    id = "",
                    courseId = this.course.id,
                    dayOfWeek = 1,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    room = "",
                    isRemote = false
                )
            )
        )
    } else {
        this.sessions.map { sessionEntity ->
            CourseWithSession(
                course = this.course.toDomain(),
                session = sessionEntity.toDomain()
            )
        }
    }
}

fun CourseWithGradesEntity.toDomain(): CourseWithGrades {
    return CourseWithGrades(
        course = this.course.toDomain(),
        grades = this.grades.map { it.toDomain() }
    )
}
