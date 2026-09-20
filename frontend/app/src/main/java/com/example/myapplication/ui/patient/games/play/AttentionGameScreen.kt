package com.example.myapplication.ui.patient.games.play

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.myapplication.ui.common.AppTopBar
import com.example.myapplication.ui.common.PrimaryActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

data class AttentionItem(
    val name: String,
    val symbol: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttentionGameScreen(
    difficulty: String = "Easy",
    onGameFinished: (Screen.GameResult) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allItems = listOf(
        AttentionItem("Hornbill Bird", "🦜"),
        AttentionItem("Bihu Dhol", "🥁"),
        AttentionItem("Assam Tea", "☕"),
        AttentionItem("Rhinoceros", "🦏"),
        AttentionItem("Eri Silk", "🧵"),
        AttentionItem("Loktak Hut", "🏠"),
        AttentionItem("Bamboo Basket", "🧺"),
        AttentionItem("Naga Shield", "🛡️")
    )

    val gridCount = when (difficulty.lowercase()) {
        "hard" -> 16
        "medium" -> 12
        else -> 9
    }

    var currentRound by remember { mutableIntStateOf(1) }
    val totalRounds = 3

    val targetItem = remember(currentRound) {
        allItems[(currentRound - 1) % allItems.size]
    }

    // Generate grid items for current round with 3 target items
    val gridItems = remember(currentRound, gridCount) {
        val targets = List(3) { targetItem }
        val distractors = List(gridCount - 3) {
            allItems.filter { it.name != targetItem.name }.random()
        }
        (targets + distractors).shuffled()
    }

    val selectedIndices = remember(currentRound) { mutableStateListOf<Int>() }

    var totalTargetsFound by remember { mutableIntStateOf(0) }
    var totalMistakes by remember { mutableIntStateOf(0) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(true) }

    // Timer effect
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000L)
            secondsElapsed++
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Attention & Concentration",
                onBack = onBack
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Target Prompt Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ROUND $currentRound OF $totalRounds",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "FIND ALL ${targetItem.symbol} ${targetItem.name.uppercase()}!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grid of Items
            val columns = if (gridCount >= 16) 4 else 3
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(gridItems) { index, item ->
                    val isSelected = selectedIndices.contains(index)
                    val isCorrect = isSelected && (item.name == targetItem.name)
                    val isWrong = isSelected && (item.name != targetItem.name)

                    val borderColor = when {
                        isCorrect -> Color(0xFF2E7D32)
                        isWrong -> MaterialTheme.colorScheme.error
                        else -> Color.Transparent
                    }

                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .border(3.dp, borderColor, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(enabled = !isSelected) {
                                selectedIndices.add(index)
                                if (item.name == targetItem.name) {
                                    totalTargetsFound++
                                } else {
                                    totalMistakes++
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isCorrect -> Color(0xFFC8E6C9)
                                isWrong -> MaterialTheme.colorScheme.errorContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.symbol,
                                fontSize = if (gridCount >= 16) 32.sp else 40.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer action & stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${secondsElapsed / 60}:${(secondsElapsed % 60).toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                PrimaryActionButton(
                    text = if (currentRound < totalRounds) "Next Round" else "Complete Game",
                    onClick = {
                        if (currentRound < totalRounds) {
                            currentRound++
                        } else {
                            // Finish game!
                            isTimerRunning = false
                            val totalPossible = totalRounds * 3
                            val score = maxOf(0, (totalTargetsFound.toFloat() / totalPossible * 100).toInt() - (totalMistakes * 10))

                            onGameFinished(
                                Screen.GameResult(
                                    gameId = "attention_game",
                                    gameTitle = "Attention & Concentration",
                                    score = score,
                                    correctCount = totalTargetsFound,
                                    incorrectCount = totalMistakes,
                                    timeTakenSeconds = secondsElapsed,
                                    difficultyLevel = difficulty,
                                    cognitiveDomain = "Attention"
                                )
                            )
                        }
                    },
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttentionGameScreenPreview() {
    MyApplicationTheme {
        AttentionGameScreen(
            difficulty = "Easy",
            onGameFinished = {},
            onBack = {}
        )
    }
}
