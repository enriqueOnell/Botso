package com.onell.botso.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "courses",
    foreignKeys = [
        ForeignKey(
            entity = Semester::class,
            parentColumns = ["id"],
            childColumns = ["semesterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["semesterId"])]
)
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val semesterId: Long,
    val name: String,
    val code: String = "",
    val colorHex: String = "",
    val location: String,
    val professor: String,
    val dayOfWeek: Int = 1,
    val startTime: String = "08:00",
    val endTime: String = "10:00",
    val isRemote: Boolean = false
)
