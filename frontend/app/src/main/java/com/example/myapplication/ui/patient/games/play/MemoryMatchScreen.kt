package com.example.myapplication.ui.patient.games.play

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

data class MemoryCard(
    val id: Int,
    val symbol: String,
    val name: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryMatchScreen(
    difficulty: String = "Easy",
    onGameFinished: (Screen.GameResult) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allThemes = listOf(
        MemoryCard(0, "🥁", "Bihu Dhol", Color(0xFFE57373)),
        MemoryCard(1, "☕", "Assam Tea", Color(0xFF81C784)),
        MemoryCard(2, "🦜", "Hornbill", Color(0xFFFFB74D)),
        MemoryCard(3, "🦏", "Rhinoceros", Color(0xFFBA68C8)),
        MemoryCard(4, "🧵", "Eri Silk", Color(0xFF4DD0E1)),
        MemoryCard(5, "🏠", "Loktak Hut", Color(0xFFAED581)),
        MemoryCard(6, "🎋", "Bamboo Craft", Color(0xFFFFD54F)),
        MemoryCard(7, "🛡️", "Naga Shield", Color(0xFF90A4AE))
    )

    val pairsCount = when (difficulty.lowercase()) {
        "hard" -> 8
        "medium" -> 6
        else -> 4
    }

    val cardsList = remember(difficulty) {
        val selected = allThemes.take(pairsCount)
        val duplicated = (selected + selected).mapIndexed { index, card ->
            card.copy(id = index)
        }
        duplicated.shuffled()
    }

    val isFlipped = remember(difficulty) { mutableStateListOf(*Array(cardsList.size) { false }) }
    val isMatched = remember(difficulty) { mutableStateListOf(*Array(cardsList.size) { false }) }

    var selectedFirstIndex by remember { mutableIntStateOf(-1) }
    var selectedSecondIndex by remember { mutableIntStateOf(-1) }
    var isChecking by remember { mutableStateOf(false) }

    var mistakes by remember { mutableIntStateOf(0) }
    var matches by remember { mutableIntStateOf(0) }
    var flips by remember { mutableIntStateOf(0) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(true) }

    // Timer effect
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000L)
            secondsElapsed++
        }
    }

    // Check finished condition
    LaunchedEffect(matches) {
        if (matches == pairsCount && pairsCount > 0) {
            isTimerRunning = false
            val score = maxOf(0, 100 - (mistakes * 10))
            delay(500L)
            onGameFinished(
                Screen.GameResult(
                    gameId = "memory_flip",
                    gameTitle = "Memory Card Match",
                    score = score,
                    correctCount = pairsCount,
                    incorrectCount = mistakes,
                    timeTakenSeconds = secondsElapsed,
                    difficultyLevel = difficulty,
                    cognitiveDomain = "Memory"
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Memory Card Match", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Metrics bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MetricItem(
                        icon = Icons.Default.Timer,
                        label = "Time",
                        value = "${secondsElapsed / 60}:${(secondsElapsed % 60).toString().padStart(2, '0')}"
                    )
                    MetricItem(
                        icon = Icons.Default.Refresh,
                        label = "Flips",
                        value = flips.toString()
                    )
                    MetricItem(
                        icon = Icons.Default.CheckCircle,
                        label = "Matches",
                        value = "$matches / $pairsCount"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tap cards to find regional matching pairs!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Grid of cards
            val columns = if (pairsCount > 6) 4 else 3
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(cardsList) { index, card ->
                    val faceUp = isFlipped[index] || isMatched[index]

                    MemoryCardItem(
                        card = card,
                        isFaceUp = faceUp,
                        isMatched = isMatched[index],
                        onClick = {
                            if (!faceUp && !isChecking) {
                                isFlipped[index] = true
                                flips++

                                if (selectedFirstIndex == -1) {
                                    selectedFirstIndex = index
                                } else {
                                    selectedSecondIndex = index
                                    isChecking = true

                                    val firstCard = cardsList[selectedFirstIndex]
                                    val secondCard = cardsList[index]

                                    if (firstCard.name == secondCard.name) {
                                        isMatched[selectedFirstIndex] = true
                                        isMatched[index] = true
                                        matches++
                                        selectedFirstIndex = -1
                                        selectedSecondIndex = -1
                                        isChecking = false
                                    } else {
                                        mistakes++
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Auto flip back effect if non-match
            LaunchedEffect(selectedSecondIndex) {
                if (selectedFirstIndex != -1 && selectedSecondIndex != -1) {
                    val first = cardsList[selectedFirstIndex]
                    val second = cardsList[selectedSecondIndex]
                    if (first.name != second.name) {
                        delay(1000L)
                        isFlipped[selectedFirstIndex] = false
                        isFlipped[selectedSecondIndex] = false
                        selectedFirstIndex = -1
                        selectedSecondIndex = -1
                        isChecking = false
                    }
                }
            }
        }
    }
}

@Composable
fun MemoryCardItem(
    card: MemoryCard,
    isFaceUp: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFaceUp) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "flip"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable(enabled = !isMatched && !isFaceUp, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFaceUp) card.color else MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation > 90f) {
                // Face UP (mirror graphic back to normal)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                ) {
                    Text(
                        text = card.symbol,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            } else {
                // Face DOWN
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🌿",
                        fontSize = 28.sp
                    )
                    Text(
                        text = "Arogya",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun MetricItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MemoryMatchScreenPreview() {
    MyApplicationTheme {
        MemoryMatchScreen(
            difficulty = "Easy",
            onGameFinished = {},
            onBack = {}
        )
    }
}
