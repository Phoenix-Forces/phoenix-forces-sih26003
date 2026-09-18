package com.example.myapplication.ui.patient.games.play

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

data class RoutineStep(
    val stepOrder: Int,
    val title: String,
    val symbol: String
)

data class RoutineScenario(
    val title: String,
    val steps: List<RoutineStep>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyRoutineGameScreen(
    difficulty: String = "Easy",
    onGameFinished: (Screen.GameResult) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scenarios = remember {
        listOf(
            RoutineScenario(
                title = "Morning Health Care Routine",
                steps = listOf(
                    RoutineStep(1, "Wake Up & Drink Fresh Water", "🌅"),
                    RoutineStep(2, "Enjoy Warm Assam Tea", "☕"),
                    RoutineStep(3, "Take Morning Prescription Medicine", "💊"),
                    RoutineStep(4, "Morning Walk in Neighborhood Park", "🚶")
                )
            ),
            RoutineScenario(
                title = "Afternoon Meal & Rest Routine",
                steps = listOf(
                    RoutineStep(1, "Wash Hands Cleanly", "🧼"),
                    RoutineStep(2, "Eat Healthy Balanced Lunch", "🍲"),
                    RoutineStep(3, "Take Post-Lunch Hydration", "🥛"),
                    RoutineStep(4, "Short Afternoon Rest", "🛋️")
                )
            )
        )
    }

    var currentScenarioIndex by remember { mutableIntStateOf(0) }
    val currentScenario = scenarios[currentScenarioIndex]

    val currentSteps = remember(currentScenarioIndex) {
        mutableStateListOf<RoutineStep>().apply {
            addAll(currentScenario.steps.shuffled())
        }
    }

    var isChecked by remember { mutableStateOf(false) }
    var scoreSum by remember { mutableIntStateOf(0) }
    var totalCorrectSteps by remember { mutableIntStateOf(0) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000L)
            secondsElapsed++
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Routine Recall", fontWeight = FontWeight.Bold) },
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
            // Scenario Title Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Routine ${currentScenarioIndex + 1} of ${scenarios.size}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentScenario.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Use Up / Down arrows to arrange activities in proper daily order (1 to 4).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Draggable/Shiftable steps list
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(currentSteps) { index, step ->
                    val isCorrectPosition = isChecked && (step.stepOrder == index + 1)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isChecked) {
                                if (isCorrectPosition) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Step ${index + 1}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(text = step.symbol, fontSize = 28.sp)

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )

                            if (!isChecked) {
                                Column {
                                    if (index > 0) {
                                        IconButton(
                                            onClick = {
                                                val prev = currentSteps[index - 1]
                                                currentSteps[index - 1] = step
                                                currentSteps[index] = prev
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up")
                                        }
                                    }
                                    if (index < currentSteps.size - 1) {
                                        IconButton(
                                            onClick = {
                                                val next = currentSteps[index + 1]
                                                currentSteps[index + 1] = step
                                                currentSteps[index] = next
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.ArrowDownward, contentDescription = "Move Down")
                                        }
                                    }
                                }
                            } else {
                                if (isCorrectPosition) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Correct Order",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button
            if (!isChecked) {
                Button(
                    onClick = {
                        isChecked = true
                        var correctCount = 0
                        currentSteps.forEachIndexed { idx, step ->
                            if (step.stepOrder == idx + 1) correctCount++
                        }
                        totalCorrectSteps += correctCount
                        scoreSum += (correctCount * 25)
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Check Order", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            } else {
                Button(
                    onClick = {
                        if (currentScenarioIndex < scenarios.size - 1) {
                            currentScenarioIndex++
                            isChecked = false
                        } else {
                            // Finish
                            isTimerRunning = false
                            val totalPossible = scenarios.size * 4
                            val finalScore = (totalCorrectSteps.toFloat() / totalPossible * 100).toInt()

                            onGameFinished(
                                Screen.GameResult(
                                    gameId = "daily_routine",
                                    gameTitle = "Daily Routine Recall",
                                    score = finalScore,
                                    correctCount = totalCorrectSteps,
                                    incorrectCount = totalPossible - totalCorrectSteps,
                                    timeTakenSeconds = secondsElapsed,
                                    difficultyLevel = difficulty,
                                    cognitiveDomain = "Routine Recall"
                                )
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = if (currentScenarioIndex < scenarios.size - 1) "Next Routine" else "Finish Routine Game",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyRoutineGameScreenPreview() {
    MyApplicationTheme {
        DailyRoutineGameScreen(
            difficulty = "Easy",
            onGameFinished = {},
            onBack = {}
        )
    }
}
