package com.onell.botso.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.onell.botso.data.local.dao.*
import com.onell.botso.data.local.entity.*

/**
 * Base de datos local Room de Botso.
 *
 * Agrupa las cinco tablas del dominio académico: semestres, cursos, notas,
 * tareas y sesiones de clase. Las relaciones se resuelven con claves foráneas
 * en cascada (al borrar un semestre se borran sus cursos, y con ellos notas,
 * tareas y horarios).
 *
 * `getDatabase()` implementa un singleton thread-safe para evitar abrir varias
 * instancias de la base de datos.
 *
 * IMPORTANTE: la versión actual es 8 y no hay migraciones ni
 * `fallbackToDestructiveMigration()`. Cualquier incremento de versión debe
 * acompañarse de una `Migration`, o la app fallará al abrir la BD existente.
 */
@Database(
    entities = [
        SemesterEntity::class,
        CourseEntity::class,
        GradeEntity::class,
        TaskEntity::class,
        ClassSessionEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class BotsoDataBase : RoomDatabase() {
    abstract fun semesterDao(): SemesterDao
    abstract fun courseDao(): CourseDao
    abstract fun gradeDao(): GradeDao
    abstract fun taskDao(): TaskDao
    abstract fun classSessionDao(): ClassSessionDao

    companion object {
        @Volatile
        private var INSTANCE: BotsoDataBase? = null
        fun getDatabase(context: Context): BotsoDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BotsoDataBase::class.java,
                    "botso_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
