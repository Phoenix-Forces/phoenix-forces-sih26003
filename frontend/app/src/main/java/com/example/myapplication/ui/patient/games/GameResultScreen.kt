package com.example.myapplication.ui.patient.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Text
import com.example.myapplication.ui.common.AppTopBar
import com.example.myapplication.ui.common.PrimaryActionButton
import com.example.myapplication.ui.common.SecondaryActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameResultScreen(
    uiState: GameResultUiState,
    onPlayAgain: () -> Unit,
    onBackToGames: () -> Unit,
    onViewProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val result = uiState.gameResult

    val feedbackMsg = when {
        result.score >= 80 -> "Fantastic work! Your cognitive focus and memory recall were sharp today!"
        result.score >= 50 -> "Good effort! You demonstrated strong concentration. Keep playing regularly!"
        else -> "Great attempt! Every game strengthens neural connections. Take your time and enjoy!"
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Game Performance")
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Trophy / Score Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = result.gameTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = result.cognitiveDomain,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${result.score}%",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = feedbackMsg,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Adaptive Difficulty Banner
            AdaptiveDifficultyNoticeBanner(
                action = uiState.adaptiveAction,
                oldDifficulty = uiState.oldDifficulty,
                newDifficulty = uiState.newDifficulty
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Metrics Breakdown Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Session Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ResultMetricItem(
                            icon = Icons.Default.CheckCircle,
                            iconColor = Color(0xFF2E7D32),
                            label = "Correct",
                            value = result.correctCount.toString()
                        )
                        ResultMetricItem(
                            icon = Icons.Default.Cancel,
                            iconColor = MaterialTheme.colorScheme.error,
                            label = "Incorrect",
                            value = result.incorrectCount.toString()
                        )
                        ResultMetricItem(
                            icon = Icons.Default.Timer,
                            iconColor = MaterialTheme.colorScheme.primary,
                            label = "Time",
                            value = "${result.timeTakenSeconds / 60}:${(result.timeTakenSeconds % 60).toString().padStart(2, '0')}"
                        )
                        ResultMetricItem(
                            icon = Icons.Default.Psychology,
                            iconColor = MaterialTheme.colorScheme.tertiary,
                            label = "Difficulty",
                            value = result.difficultyLevel
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            PrimaryActionButton(
                text = "Play Again",
                onClick = onPlayAgain,
                icon = Icons.Default.PlayArrow
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryActionButton(
                    text = "Games Hub",
                    onClick = onBackToGames,
                    modifier = Modifier.weight(1f)
                )

                SecondaryActionButton(
                    text = "View Progress",
                    onClick = onViewProgress,
                    icon = Icons.Default.ShowChart,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AdaptiveDifficultyNoticeBanner(
    action: AdaptiveAction,
    oldDifficulty: String,
    newDifficulty: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon, title, msg) = when (action) {
        AdaptiveAction.UPGRADED -> Tuple5(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            Icons.Default.AutoAwesome,
            "Adaptive Level Up!",
            "Excellent performance! Your adaptive game difficulty increased to $newDifficulty for future training."
        )
        AdaptiveAction.LOWERED -> Tuple5(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            Icons.Default.Psychology,
            "Comfort Calibration",
            "Game difficulty adjusted to $newDifficulty so you can practice comfortably at a relaxed pace."
        )
        AdaptiveAction.MAINTAINED -> Tuple5(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface,
            Icons.Default.Psychology,
            "Level Maintained",
            "Consistent performance! Your adaptive difficulty remains at $newDifficulty."
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = msg,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun ResultMetricItem(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private data class Tuple5<A, B, C, D, E>(
    val a: A, val b: B, val c: C, val d: D, val e: E
)

@Preview(showBackground = true)
@Composable
fun GameResultScreenPreview() {
    MyApplicationTheme {
        GameResultScreen(
            uiState = GameResultUiState(
                gameResult = Screen.GameResult(
                    gameTitle = "Memory Card Match",
                    score = 85,
                    correctCount = 6,
                    incorrectCount = 1,
                    timeTakenSeconds = 45,
                    difficultyLevel = "Medium",
                    cognitiveDomain = "Memory Domain"
                ),
                oldDifficulty = "Easy",
                newDifficulty = "Medium",
                adaptiveAction = AdaptiveAction.UPGRADED
            ),
            onPlayAgain = {},
            onBackToGames = {},
            onViewProgress = {}
        )
    }
}
