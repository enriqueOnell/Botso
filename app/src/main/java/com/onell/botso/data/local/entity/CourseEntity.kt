package com.onell.botso.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

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
    @PrimaryKey val id:String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "semester_id") val semesterId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "code") val code: String = "",
    @ColumnInfo(name = "color_hex") val colorHex: String = "",
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "professor") val professor: String,
    @ColumnInfo(name = "day_of_week") val dayOfWeek: Int = 1,
    @ColumnInfo(name = "start_time") val startTime: String = "08:00",
    @ColumnInfo(name = "end_time") val endTime: String = "10:00",
    @ColumnInfo(name = "is_remote") val isRemote: Boolean = false
)
