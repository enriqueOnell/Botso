package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE courseId = :courseId")
    fun getTasksForCourse(courseId: Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE status != 'DONE'")
    fun getPendingTasks(): Flow<List<Task>>

    @Query("SELECT COUNT(*) FROM tasks WHERE status != 'DONE'")
    fun getPendingTasksCount(): Flow<Int>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}
