package com.onell.botso.ui.components.grades

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.theme.LocalBotsoColors
import java.util.Locale

internal const val PASSING_SCORE = 3.0

/**
 * Resumen principal de la materia.
 *
 * Es el héroe de la pantalla: nombre de la materia, promedio actual en
 * `displayMedium`, porcentaje evaluado y promedio del corte seleccionado. El
 * contenedor usa la pareja semántica de aprobado (`LocalBotsoColors`) o de
 * error, nunca colores sueltos.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CourseSummarySectionCard(
    courseName: String,
    average: Double,
    evaluatedPercent: Int,
    termId: Int,
    termAverage: Double,
    modifier: Modifier = Modifier,
) {
    val semanticColors = LocalBotsoColors.current
    val isPassing = average >= PASSING_SCORE
    val containerColor = if (isPassing) {
        semanticColors.passContainer
    } else {
        MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = if (isPassing) {
        semanticColors.onPassContainer
    } else {
        MaterialTheme.colorScheme.onErrorContainer
    }
    val subtleContentColor = contentColor.copy(alpha = 0.8f)
    val evaluatedProgress = (evaluatedPercent / 100f).coerceIn(0f, 1f)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = containerColor,
        contentColor = contentColor
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = courseName.ifBlank { "Materia" },
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Promedio actual",
                        style = MaterialTheme.typography.labelLarge,
                        color = subtleContentColor
                    )

                    Text(
                        text = String.format(Locale.US, "%.1f", average),
                        style = MaterialTheme.typography.displayMedium
                    )
                }

                SummaryBadge(
                    text = if (isPassing) "Aprobando" else "Reprobando",
                    containerColor = contentColor.copy(alpha = 0.14f),
                    contentColor = contentColor
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Evaluado",
                        style = MaterialTheme.typography.labelLarge,
                        color = subtleContentColor
                    )
                    Text(
                        text = "$evaluatedPercent%",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                LinearProgressIndicator(
                    progress = { evaluatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = contentColor,
                    trackColor = contentColor.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
            }

            SummaryBadge(
                text = "Corte $termId · ${String.format(Locale.US, "%.1f", termAverage)}",
                containerColor = contentColor.copy(alpha = 0.14f),
                contentColor = contentColor
            )
        }
    }
}

@Composable
private fun SummaryBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        color = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

@Preview(name = "CourseSummary Passing Light", showBackground = true)
@Preview(
    name = "CourseSummary Passing Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CourseSummaryPassingPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CourseSummarySectionCard(
                courseName = "Bases de Datos",
                average = 4.2,
                evaluatedPercent = 100,
                termId = 1,
                termAverage = 4.3,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "CourseSummary Failing Light", showBackground = true)
@Preview(
    name = "CourseSummary Failing Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CourseSummaryFailingPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CourseSummarySectionCard(
                courseName = "Cálculo Diferencial",
                average = 2.2,
                evaluatedPercent = 60,
                termId = 2,
                termAverage = 2.4,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
