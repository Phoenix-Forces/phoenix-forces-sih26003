package com.example.myapplication.ui.patient.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.common.AppTopBar
import com.example.myapplication.ui.common.GameCard
import com.example.myapplication.ui.theme.MyApplicationTheme

data class GameInfo(
    val id: String,
    val title: String,
    val domain: String,
    val description: String,
    val icon: ImageVector,
    val containerColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesListScreen(
    uiState: GamesUiState,
    onGameSelected: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val games = listOf(
        GameInfo(
            id = "memory_flip",
            title = "Memory Card Match",
            domain = "Memory Domain",
            description = "Flip cards and match regional North-East cultural icons like Bihu Dhol and Assam Tea.",
            icon = Icons.Default.Extension,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        GameInfo(
            id = "pattern_memory",
            title = "Pattern Memory",
            domain = "Pattern Recognition",
            description = "Remember and repeat the flashing sequence of regional symbols.",
            icon = Icons.Default.Grid4x4,
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        GameInfo(
            id = "attention_game",
            title = "Attention & Concentration",
            domain = "Attention Domain",
            description = "Spot target regional objects quickly against distraction icons.",
            icon = Icons.Default.AdsClick,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        GameInfo(
            id = "daily_routine",
            title = "Daily Routine Recall",
            domain = "Routine Recall",
            description = "Arrange daily activities and medication routines in their correct step order.",
            icon = Icons.Default.FormatListNumbered,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        GameInfo(
            id = "object_recognition",
            title = "Object Recognition",
            domain = "Object Domain",
            description = "Identify heritage crafts, garments, and traditional items from visual clues.",
            icon = Icons.Default.Category,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        GameInfo(
            id = "emotion_recognition",
            title = "Emotion Recognition",
            domain = "Emotion Domain",
            description = "Match social scenarios and expressions with encouraging voice guidance.",
            icon = Icons.Default.SentimentSatisfiedAlt,
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Cognitive Games Hub",
                onBack = onBack
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AdaptiveDifficultyHeaderCard(
                    currentDifficulty = uiState.currentDifficulty,
                    patientName = uiState.patientName
                )
            }

            item {
                Text(
                    text = "Select a Game",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(games, key = { it.id }) { game ->
                GameCard(
                    title = game.title,
                    description = game.description,
                    difficulty = uiState.currentDifficulty.uppercase(),
                    duration = "5 min",
                    icon = game.icon,
                    onClick = { onGameSelected(game.id) }
                )
            }
        }
    }
}

@Composable
fun AdaptiveDifficultyHeaderCard(
    currentDifficulty: String,
    patientName: String,
    modifier: Modifier = Modifier
) {
    val badgeColor = when (currentDifficulty.lowercase()) {
        "hard" -> MaterialTheme.colorScheme.errorContainer
        "medium" -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val badgeTextColor = when (currentDifficulty.lowercase()) {
        "hard" -> MaterialTheme.colorScheme.onErrorContainer
        "medium" -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Adaptive AI Training",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Personalized for $patientName",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = badgeColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = badgeTextColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentDifficulty.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Difficulty automatically adjusts after each game based on your performance to support optimal memory health.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Removed local GameCard

@Preview(showBackground = true, device = "spec:width=412dp,height=915dp,dpi=480")
@Composable
fun GamesListScreenPreview() {
    MyApplicationTheme {
        GamesListScreen(
            uiState = GamesUiState(currentDifficulty = "Medium", patientName = "Amiya"),
            onGameSelected = {},
            onBack = {}
        )
    }
}
