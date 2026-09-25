package com.onell.botso.ui.components.tasks

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import java.time.LocalDate

@Composable
fun StatusChip(task: Task){
    Surface(
        color = when(task.status){
            TaskStatus.TODO -> Color(0xFFA0F399)
            TaskStatus.IN_PROGRESS -> Color(0xFFE5D980)
            TaskStatus.DONE -> Color(0xFFFFDAD6)

        },
        shape = CircleShape
    ) {
        Text(
            text = task.status.label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview
@Composable
fun StatusChipPreview1() {
    StatusChip(Task(
        id = "id",
        courseId = "courseId",
        title = "title",
        dueDate = LocalDate.now(),
        isPriority = true,
        hasAttachment = true,
        status = TaskStatus.TODO,
        week = 3
    ))
}

@Preview
@Composable
fun StatusChipPreview2() {
    StatusChip(Task(
        id = "id",
        courseId = "courseId",
        title = "title",
        dueDate = LocalDate.now(),
        isPriority = true,
        hasAttachment = true,
        status = TaskStatus.IN_PROGRESS,
        week = 3
    ))
}

@Preview
@Composable
fun StatusChipPreview3() {
    StatusChip(Task(
        id = "id",
        courseId = "courseId",
        title = "title",
        dueDate = LocalDate.now(),
        isPriority = true,
        hasAttachment = true,
        status = TaskStatus.DONE,
        week = 3
    ))
}