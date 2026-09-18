package com.example.myapplication.ui.patient.games.play

import android.speech.tts.TextToSpeech
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import java.util.Locale

data class EmotionQuestion(
    val emoji: String,
    val scenario: String,
    val targetEmotion: String,
    val options: List<String>,
    val encouragementMsg: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionRecognitionGameScreen(
    difficulty: String = "Easy",
    onGameFinished: (Screen.GameResult) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Text to Speech
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val ttsEngine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
            }
        }
        tts = ttsEngine
        onDispose {
            ttsEngine.stop()
            ttsEngine.shutdown()
        }
    }

    fun speakText(text: String) {
        if (isTtsReady && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "EMOTION_TTS")
        }
    }

    val questions = remember {
        listOf(
            EmotionQuestion(
                emoji = "😊",
                scenario = "A grandmother receiving a warm surprise visit from her children and grandchildren.",
                targetEmotion = "Happy",
                options = listOf("Happy", "Peaceful", "Sad", "Surprised"),
                encouragementMsg = "Wonderful job! Joy and warmth express happiness!"
            ),
            EmotionQuestion(
                emoji = "😌",
                scenario = "Sitting quietly by a scenic river viewing a serene sunrise in Assam.",
                targetEmotion = "Peaceful",
                options = listOf("Peaceful", "Happy", "Focused", "Sad"),
                encouragementMsg = "Excellent! Quiet moments bring a feeling of peace."
            ),
            EmotionQuestion(
                emoji = "😲",
                scenario = "Opening a door and seeing an unexpected anniversary party organized by family!",
                targetEmotion = "Surprised",
                options = listOf("Surprised", "Sad", "Focused", "Peaceful"),
                encouragementMsg = "Great observation! Unexpected joy brings surprise."
            ),
            EmotionQuestion(
                emoji = "🧐",
                scenario = "Carefully painting intricate traditional bamboo patterns on crafts.",
                targetEmotion = "Focused",
                options = listOf("Focused", "Surprised", "Happy", "Sad"),
                encouragementMsg = "Spot on! Detailed work requires deep focus."
            ),
            EmotionQuestion(
                emoji = "😟",
                scenario = "Missing a long-time childhood friend who moved far away.",
                targetEmotion = "Sad",
                options = listOf("Sad", "Peaceful", "Focused", "Happy"),
                encouragementMsg = "Compassionate effort! Missing loved ones is a sad feeling."
            )
        )
    }

    var questionIndex by remember { mutableIntStateOf(0) }
    val currentQuestion = questions[questionIndex]

    var selectedOption by remember { mutableStateOf<String?>(null) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(true) }

    // Speak prompt on new question
    LaunchedEffect(questionIndex, isTtsReady) {
        if (isTtsReady) {
            speakText("How does this person feel? ${currentQuestion.scenario}")
        }
    }

    // Timer effect
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000L)
            secondsElapsed++
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emotion Recognition", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { speakText(currentQuestion.scenario) }) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Read Scenario Out Loud", tint = MaterialTheme.colorScheme.primary)
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
                    text = "Scenario ${questionIndex + 1} of ${questions.size}",
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

            // Scenario Emotion Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
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
                            .size(100.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentQuestion.emoji, fontSize = 60.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "\"${currentQuestion.scenario}\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Identify the emotion being felt:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Emotion Answer Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                currentQuestion.options.forEach { option ->
                    val isSelected = selectedOption == option
                    val isCorrect = option == currentQuestion.targetEmotion

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
                                if (option == currentQuestion.targetEmotion) {
                                    correctAnswersCount++
                                    speakText(currentQuestion.encouragementMsg)
                                } else {
                                    speakText("Great effort! The answer is ${currentQuestion.targetEmotion}")
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
                                fontSize = 18.sp
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
                Button(
                    onClick = {
                        if (questionIndex < questions.size - 1) {
                            questionIndex++
                            selectedOption = null
                        } else {
                            // Finish
                            isTimerRunning = false
                            val finalScore = (correctAnswersCount.toFloat() / questions.size * 100).toInt()

                            onGameFinished(
                                Screen.GameResult(
                                    gameId = "emotion_recognition",
                                    gameTitle = "Emotion Recognition",
                                    score = finalScore,
                                    correctCount = correctAnswersCount,
                                    incorrectCount = questions.size - correctAnswersCount,
                                    timeTakenSeconds = secondsElapsed,
                                    difficultyLevel = difficulty,
                                    cognitiveDomain = "Emotion Domain"
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
                        text = if (questionIndex < questions.size - 1) "Next Scenario" else "View Game Results",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmotionRecognitionGameScreenPreview() {
    MyApplicationTheme {
        EmotionRecognitionGameScreen(
            difficulty = "Easy",
            onGameFinished = {},
            onBack = {}
        )
    }
}
