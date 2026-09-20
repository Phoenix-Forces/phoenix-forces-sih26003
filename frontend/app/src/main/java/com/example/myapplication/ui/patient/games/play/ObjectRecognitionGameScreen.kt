package com.example.myapplication.ui.patient.games.play

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.myapplication.ui.common.AppTopBar
import com.example.myapplication.ui.common.PrimaryActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

data class ObjectQuestion(
    val symbol: String,
    val description: String,
    val correctAnswer: String,
    val options: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectRecognitionGameScreen(
    difficulty: String = "Easy",
    onGameFinished: (Screen.GameResult) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember {
        listOf(
            ObjectQuestion(
                symbol = "🧣",
                description = "Traditional hand-woven white cotton cloth with distinct red embroidered motifs, given as a revered mark of honor and respect.",
                correctAnswer = "Gamocha",
                options = listOf("Gamocha", "Saree", "Eri Silk", "Woolen Stole")
            ),
            ObjectQuestion(
                symbol = "🏠",
                description = "Traditional floating hut built on organic phumdi biomass over Loktak Lake in Manipur.",
                correctAnswer = "Loktak Floating Hut",
                options = listOf("Loktak Floating Hut", "Bamboo Bridge", "Tea Factory", "Houseboat")
            ),
            ObjectQuestion(
                symbol = "🛡️",
                description = "Handcrafted indigenous wooden shield with woven cane, traditionally used in heritage tribal armor.",
                correctAnswer = "Naga Shield",
                options = listOf("Naga Shield", "Bamboo Basket", "Pottery Jar", "Clay Lamp")
            ),
            ObjectQuestion(
                symbol = "🎺",
                description = "Traditional musical horn/flute crafted from buffalo horn and bamboo, played during festive celebrations.",
                correctAnswer = "Bihu Flute (Pepa)",
                options = listOf("Bihu Flute (Pepa)", "Violin", "Tabla", "Harmonium")
            ),
            ObjectQuestion(
                symbol = "🧵",
                description = "Eco-friendly, warm thermal silk fabric woven in Assam known for its soft natural texture.",
                correctAnswer = "Eri Silk",
                options = listOf("Eri Silk", "Cotton Towel", "Polyester Cloth", "Jute Fiber")
            )
        )
    }

    var questionIndex by remember { mutableIntStateOf(0) }
    val currentQuestion = questions[questionIndex]

    var selectedOption by remember { mutableStateOf<String?>(null) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
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
            AppTopBar(
                title = "Object Recognition",
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
            // Header Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${questionIndex + 1} of ${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Score: ${correctAnswersCount * 20}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cultural Visual Clue Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentQuestion.symbol, fontSize = 52.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentQuestion.description,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Answer Choice Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                currentQuestion.options.forEach { option ->
                    val isSelected = selectedOption == option
                    val isCorrect = option == currentQuestion.correctAnswer

                    val btnColor = when {
                        selectedOption != null && isCorrect -> Color(0xFFC8E6C9)
                        selectedOption != null && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceContainerHigh
                    }

                    val contentColor = when {
                        selectedOption != null && isCorrect -> Color(0xFF1B5E20)
                        selectedOption != null && isSelected && !isCorrect -> MaterialTheme.colorScheme.onErrorContainer
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    Button(
                        onClick = {
                            if (selectedOption == null) {
                                selectedOption = option
                                if (option == currentQuestion.correctAnswer) {
                                    correctAnswersCount++
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = btnColor,
                            contentColor = contentColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            if (selectedOption != null) {
                                if (isCorrect) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF2E7D32))
                                } else if (isSelected) {
                                    Icon(Icons.Default.Error, contentDescription = "Incorrect", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next button
            if (selectedOption != null) {
                PrimaryActionButton(
                    text = if (questionIndex < questions.size - 1) "Next Question" else "View Game Results",
                    onClick = {
                        if (questionIndex < questions.size - 1) {
                            questionIndex++
                            selectedOption = null
                        } else {
                            // Finished
                            isTimerRunning = false
                            val finalScore = (correctAnswersCount.toFloat() / questions.size * 100).toInt()

                            onGameFinished(
                                Screen.GameResult(
                                    gameId = "object_recognition",
                                    gameTitle = "Object Recognition",
                                    score = finalScore,
                                    correctCount = correctAnswersCount,
                                    incorrectCount = questions.size - correctAnswersCount,
                                    timeTakenSeconds = secondsElapsed,
                                    difficultyLevel = difficulty,
                                    cognitiveDomain = "Object Domain"
                                )
                            )
                        }
                    }
                )
            } else {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ObjectRecognitionGameScreenPreview() {
    MyApplicationTheme {
        ObjectRecognitionGameScreen(
            difficulty = "Easy",
            onGameFinished = {},
            onBack = {}
        )
    }
}
