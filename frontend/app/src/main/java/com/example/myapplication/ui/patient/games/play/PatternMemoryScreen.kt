package com.example.myapplication.ui.patient.games.play

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
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

data class PatternPad(
    val id: Int,
    val name: String,
    val symbol: String,
    val baseColor: Color,
    val activeColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternMemoryScreen(
    difficulty: String = "Easy",
    onGameFinished: (Screen.GameResult) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pads = remember {
        listOf(
            PatternPad(0, "Bihu Dhol", "🥁", Color(0xFFC62828), Color(0xFFFF5252)),
            PatternPad(1, "Assam Tea", "☕", Color(0xFF2E7D32), Color(0xFF66BB6A)),
            PatternPad(2, "Hornbill", "🦜", Color(0xFFE65100), Color(0xFFFF9800)),
            PatternPad(3, "Naga Shield", "🛡️", Color(0xFF1565C0), Color(0xFF42A5F5))
        )
    }

    val targetSequenceLength = when (difficulty.lowercase()) {
        "hard" -> 6
        "medium" -> 5
        else -> 3
    }

    // Sequence generated
    val generatedSequence = remember(difficulty) {
        List(targetSequenceLength) { (0..3).random() }
    }

    val userInputs = remember { mutableStateListOf<Int>() }

    var activePadIndex by remember { mutableIntStateOf(-1) }
    var isDemonstrating by remember { mutableStateOf(false) }
    var isUserTurn by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("Tap 'Start Pattern' when ready!") }
    var mistakes by remember { mutableIntStateOf(0) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // Timer effect
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000L)
            secondsElapsed++
        }
    }

    // Function to trigger pattern playback
    suspend fun playSequence() {
        isDemonstrating = true
        isUserTurn = false
        userInputs.clear()
        statusText = "Watch carefully! Pattern is flashing..."

        for (padId in generatedSequence) {
            delay(400L)
            activePadIndex = padId
            delay(600L)
            activePadIndex = -1
        }

        delay(300L)
        isDemonstrating = false
        isUserTurn = true
        statusText = "Your turn! Tap the pads in order (${userInputs.size}/$targetSequenceLength)"
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Pattern Memory",
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
            // Header card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sequence Target: $targetSequenceLength Steps  |  Difficulty: $difficulty",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pads grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                itemsIndexed(pads) { index, pad ->
                    val isIlluminated = (activePadIndex == pad.id)
                    val colorAnim by animateColorAsState(
                        targetValue = if (isIlluminated) pad.activeColor else pad.baseColor,
                        animationSpec = tween(durationMillis = 150),
                        label = "padColor"
                    )

                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable(enabled = isUserTurn && !isDemonstrating) {
                                userInputs.add(pad.id)
                                activePadIndex = pad.id

                                // Check input
                                val currentStepIndex = userInputs.size - 1
                                if (generatedSequence[currentStepIndex] != pad.id) {
                                    mistakes++
                                    statusText = "Oops! Wrong pad. Watch again!"
                                    isUserTurn = false
                                } else if (userInputs.size == targetSequenceLength) {
                                    // Complete!
                                    isTimerRunning = false
                                    statusText = "Perfect! Pattern completed!"
                                    val score = maxOf(0, 100 - (mistakes * 20))

                                    onGameFinished(
                                        Screen.GameResult(
                                            gameId = "pattern_memory",
                                            gameTitle = "Pattern Memory",
                                            score = score,
                                            correctCount = targetSequenceLength,
                                            incorrectCount = mistakes,
                                            timeTakenSeconds = secondsElapsed,
                                            difficultyLevel = difficulty,
                                            cognitiveDomain = "Pattern Recognition"
                                        )
                                    )
                                } else {
                                    statusText = "Good! Keep going (${userInputs.size}/$targetSequenceLength)"
                                }
                            },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = colorAnim),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = pad.symbol,
                                    fontSize = 42.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = pad.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action button
            if (!isTimerRunning && userInputs.isEmpty()) {
                PrimaryActionButton(
                    text = "Start Pattern Game",
                    onClick = {
                        isTimerRunning = true
                        secondsElapsed = 0
                        mistakes = 0
                    },
                    icon = Icons.Default.PlayArrow
                )
            } else if (!isUserTurn && !isDemonstrating && mistakes > 0) {
                PrimaryActionButton(
                    text = "Replay Pattern",
                    onClick = {
                        userInputs.clear()
                    },
                    icon = Icons.Default.Psychology
                )
            } else {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }

    // Effect to play sequence on start or replay
    LaunchedEffect(isTimerRunning, userInputs.size) {
        if (isTimerRunning && userInputs.isEmpty() && !isDemonstrating && !isUserTurn) {
            playSequence()
        }
    }

    // Flash reset effect when user clicks pad
    LaunchedEffect(userInputs.size) {
        if (userInputs.isNotEmpty() && activePadIndex != -1) {
            delay(200L)
            activePadIndex = -1
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatternMemoryScreenPreview() {
    MyApplicationTheme {
        PatternMemoryScreen(
            difficulty = "Easy",
            onGameFinished = {},
            onBack = {}
        )
    }
}
