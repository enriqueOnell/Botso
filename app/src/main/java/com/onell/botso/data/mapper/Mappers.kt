package com.onell.botso.data.mapper

import com.onell.botso.data.local.entity.ClassSessionEntity
import com.onell.botso.data.local.entity.ClassSessionWithCourseEntity
import com.onell.botso.data.local.entity.CourseEntity
import com.onell.botso.data.local.entity.GradeEntity
import com.onell.botso.data.local.entity.SemesterEntity
import com.onell.botso.data.local.entity.SemesterWithCourseEntity
import com.onell.botso.data.local.entity.TaskEntity
import com.onell.botso.data.local.entity.TaskWithCourseEntity
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import com.onell.botso.domain.model.SemesterWithStats
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse
import java.util.UUID

fun ClassSessionEntity.toDomain(): ClassSession {
    return ClassSession(
        id = this.id,
        courseId = this.courseId,
        dayOfWeek = this.dayOfWeek,
        startTime = this.startTime,
        endTime = this.endTime,
        room = this.room
    )
}

fun ClassSession.toEntity(): ClassSessionEntity{
    return ClassSessionEntity(
        id = if (this.id.isBlank()) UUID.randomUUID().toString() else this.id,
        courseId = this.courseId,
        dayOfWeek = this.dayOfWeek,
        startTime = this.startTime,
        endTime = this.endTime,
        room = this.room
    )
}
fun CourseEntity.toDomain(): Course {
    return Course(
        id = this.id,
        semesterId = this.semesterId,
        name = this.name,
        code = this.code,
        colorHex = this.colorHex,
        location = this.location,
        professor = this.professor,
        dayOfWeek = this.dayOfWeek,
        startTime = this.startTime,
        endTime = this.endTime,
        isRemote = this.isRemote
    )
}
fun Course.toEntity(): CourseEntity {
    return CourseEntity(
        id = if (this.id.isBlank()) UUID.randomUUID().toString() else this.id,
        semesterId = this.semesterId,
        name = this.name,
        code = this.code,
        colorHex = this.colorHex,
        location = this.location,
        professor = this.professor,
        dayOfWeek = this.dayOfWeek,
        startTime = this.startTime,
        endTime = this.endTime,
        isRemote = this.isRemote
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
        id = if (this.id.isBlank()) UUID.randomUUID().toString() else this.id,
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
        startDate = this.startDate,
        endDate = this.endDate,
        isActive = this.isActive
    )
}

fun Semester.toEntity(): SemesterEntity {
    return SemesterEntity(
        id = if (this.id.isBlank()) UUID.randomUUID().toString() else this.id,
        name = this.name,
        startDate = this.startDate,
        endDate = this.endDate,
        isActive = this.isActive
    )
}

fun TaskEntity.toDomain(): Task{
    return Task(
        id = this.id,
        courseId = this.courseId,
        title = this.title,
        dueDate = this.dueDate,
        isPriority = this.isPriority,
        hasAttachment = this.hasAttachment,
        status = this.status,
        week = this.week,
        description = this.description
    )
}

fun Task.toEntity(): TaskEntity{
    return TaskEntity(
        id = if (this.id.isBlank()) UUID.randomUUID().toString() else this.id,
        courseId = this.courseId,
        title = this.title,
        dueDate = this.dueDate,
        isPriority = this.isPriority,
        hasAttachment = this.hasAttachment,
        status = this.status,
        week = this.week,
        description = this.description
    )
}

fun SemesterWithCourseEntity.toDomain(): SemesterWithCourse {
    return SemesterWithCourse(
        semester = this.semester.toDomain(), // Llama al mapper base de SemesterEntity
        courses = this.courses.map { it.toDomain() } // Mapea la lista entera de CourseEntity
    )
}

fun TaskWithCourseEntity.toDomain(): TaskWithCourse {
    return TaskWithCourse(
        task = this.task.toDomain(), // Mapper base de TaskEntity
        course = this.course.toDomain() // Mapper base de CourseEntity
    )
}

fun ClassSessionWithCourseEntity.toDomain(): ClassSessionWithCourse { // O el nombre que le des en Dominio
    return ClassSessionWithCourse (
        session = this.classSession.toDomain(),
        course = this.course.toDomain()
    )
}