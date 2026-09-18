package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.TaskEntity
import com.onell.botso.data.local.entity.CourseWithTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE course_id = :courseId")
    fun getTasksForCourse(courseId: String): Flow<List<TaskEntity>>

    @Transaction
    @Query("SELECT * FROM courses")
    fun getAllTasksWithCourse(): Flow<List<CourseWithTaskEntity>>

    @Query("SELECT * FROM tasks WHERE status != 'DONE'")
    fun getPendingTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE is_priority = 1 AND status != 'DONE'")
    fun getPriorityTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}
