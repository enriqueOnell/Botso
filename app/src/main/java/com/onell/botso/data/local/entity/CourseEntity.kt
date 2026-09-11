package com.onell.botso.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "courses",
    foreignKeys = [
        ForeignKey(
            entity = SemesterEntity::class,
            parentColumns = ["id"],
            childColumns = ["semester_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["semester_id"])]
)
data class CourseEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "semester_id")
    val semesterId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "professor")
    val professor: String = ""
)