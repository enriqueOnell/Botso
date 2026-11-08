package com.onell.botso.domain.repository

import com.onell.botso.domain.model.CourseWithTask
import com.onell.botso.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de acceso a tareas, prioridades y conteo de pendientes.
 */
interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksForCourse(courseId: String): Flow<List<Task>>

    fun getPendingTasksCount(): Flow<Int>
    fun getAllTasksWithCourse(): Flow<List<CourseWithTask>>
    fun getAllPriorityTasks(): Flow<List<Task>>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}
