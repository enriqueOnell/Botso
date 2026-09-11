package com.onell.botso.data.mapper

import com.onell.botso.data.local.entity.ClassSessionEntity
import com.onell.botso.data.local.entity.ClassSessionWithCourseEntity
import com.onell.botso.data.local.entity.CourseEntity
import com.onell.botso.data.local.entity.CourseWithGradesEntity
import com.onell.botso.data.local.entity.GradeEntity
import com.onell.botso.data.local.entity.SemesterEntity
import com.onell.botso.data.local.entity.SemesterWithCourseEntity
import com.onell.botso.data.local.entity.TaskEntity
import com.onell.botso.data.local.entity.TaskWithCourseEntity
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse
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
        status = this.status,
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
        status = this.status,
        week = this.week,
        description = this.description
    )
}

fun SemesterWithCourseEntity.toDomain(): SemesterWithCourse {
    return SemesterWithCourse(
        semester = this.semester.toDomain(),
        courses = this.courses.map { it.toDomain() }
    )
}

fun TaskWithCourseEntity.toDomain(): TaskWithCourse {
    return TaskWithCourse(
        task = this.task.toDomain(),
        course = this.course.toDomain()
    )
}

fun ClassSessionWithCourseEntity.toDomain(): ClassSessionWithCourse {
    return ClassSessionWithCourse (
        session = this.classSession.toDomain(),
        course = this.course.toDomain()
    )
}

fun CourseWithGradesEntity.toDomain(): CourseWithGrades {
    return CourseWithGrades(
        course = this.course.toDomain(),
        grades = this.grades.map { it.toDomain() }
    )
}
