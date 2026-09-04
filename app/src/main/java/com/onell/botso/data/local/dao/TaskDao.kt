package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.TaskEntity
import com.onell.botso.data.local.entity.TaskWithCourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE course_id = :courseId")
    fun getTasksForCourse(courseId: Long): Flow<List<TaskEntity>> // ¡Fuerza la entidad pura!

    @Transaction
    @Query("SELECT * FROM tasks WHERE course_id = :courseId")
    fun getTasksWithCourse(courseId: Long): Flow<List<TaskWithCourseEntity>>

    @Query("SELECT * FROM tasks WHERE status != 'DONE'")
    fun getPendingTasks(): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE status != 'DONE'")
    fun getPendingTasksCount(): Flow<Int>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}
