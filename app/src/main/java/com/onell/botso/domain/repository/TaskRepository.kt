package com.onell.botso.domain.repository

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    // Tasks
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksForCourse(courseId: String): Flow<List<Task>>
    fun getTasksWithCourse(courseId: String): Flow<List<TaskWithCourse>>
    fun getPendingTasksCount(): Flow<Int>
    fun getPriorityTasks(courseId: String): Flow<List<TaskWithCourse>>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}