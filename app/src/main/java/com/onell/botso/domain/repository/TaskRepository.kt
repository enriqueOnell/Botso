package com.onell.botso.domain.repository

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    // Tasks
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksForCourse(courseId: Long): Flow<List<Task>>
    fun getTasksWithCourse(courseId: Long): Flow<List<TaskWithCourse>>
    fun getPendingTasksCount(): Flow<Int>
    fun getPriorityTasks(courseId: Long): Flow<List<TaskWithCourse>>
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}