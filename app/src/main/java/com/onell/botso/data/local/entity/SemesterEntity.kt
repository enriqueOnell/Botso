package com.onell.botso.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa un semestre académico.
 *
 * Las fechas se almacenan como texto ISO (`yyyy-MM-dd`) para mantener la BD
 * simple y se convierten a `LocalDate` en la capa de mappers.
 */
@Entity(tableName = "semesters")
data class SemesterEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "start_date")
    val startDate: String,
    @ColumnInfo(name = "end_date")
    val endDate: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true
)
