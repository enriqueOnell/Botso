package com.onell.botso.domain.repository

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.CourseWithTask
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksForCourse(courseId: String): Flow<List<Task>>
    fun getAllTasksWithCourse(): Flow<List<CourseWithTask>>
    fun getAllPriorityTasks(): Flow<List<Task>>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}
