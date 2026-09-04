package com.onell.botso.data.repository

import com.onell.botso.data.local.dao.CourseDao
import com.onell.botso.data.local.dao.TaskDao
import com.onell.botso.data.mapper.toDomain
import com.onell.botso.data.mapper.toEntity
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.domain.repository.TaskRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { list -> list.map { it.toDomain() } }

    override fun getTasksForCourse(courseId: Long): Flow<List<Task>> =
        taskDao.getTasksForCourse(courseId).map { list -> list.map { it.toDomain() } }

    override fun getTasksWithCourse(courseId: Long): Flow<List<TaskWithCourse>> {
        return taskDao.getTasksWithCourse(courseId).map { list ->
            list.map { entity -> entity.toDomain() }
        }
    }

    override fun getPendingTasksCount(): Flow<Int> = taskDao.getPendingTasksCount()

    override fun getPriorityTasks(courseId: Long): Flow<List<TaskWithCourse>> {
        return getTasksWithCourse(courseId).map { tasks ->
            tasks.filter { it.task.isPriority && it.task.status != "DONE" }
        }
    }

    override suspend fun insertTask(task: Task): Long = taskDao.insertTask(task.toEntity())

    override suspend fun updateTask(task: Task) = taskDao.updateTask(task.toEntity())

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task.toEntity())
}