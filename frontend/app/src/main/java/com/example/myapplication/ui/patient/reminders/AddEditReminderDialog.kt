package com.example.myapplication.ui.patient.reminders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.local.entity.ReminderEntity
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun AddEditReminderDialog(
    initialReminder: ReminderEntity? = null,
    onDismiss: () -> Unit,
    onSave: (title: String, category: String, timeString: String, repeatInterval: String, notes: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(initialReminder?.title ?: "") }
    var selectedCategory by remember { mutableStateOf(initialReminder?.category ?: "Medicine") }
    var repeatInterval by remember { mutableStateOf(initialReminder?.repeatInterval ?: "Daily") }
    var notes by remember { mutableStateOf(initialReminder?.notes ?: "") }

    // Time parsing or defaults
    val initialTime = initialReminder?.timeString ?: "08:00 AM"
    var hourText by remember { mutableStateOf(initialTime.take(2).ifBlank { "08" }) }
    var minuteText by remember { mutableStateOf(initialTime.drop(3).take(2).ifBlank { "00" }) }
    var isAm by remember { mutableStateOf(!initialTime.contains("PM", ignoreCase = true)) }

    val categories = listOf("Medicine", "Hydration", "Daily Activity", "Medical Appointment")
    val repeatOptions = listOf("Daily", "Weekdays", "Weekends", "Once")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialReminder == null) "Add New Reminder" else "Edit Reminder",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Reminder Title") },
                    placeholder = { Text("e.g., Take Morning BP Medicine") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Category Selection
                Column {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.take(2).forEach { category ->
                            FilterChip(
                                selected = selectedCategory.equals(category, ignoreCase = true),
                                onClick = { selectedCategory = category },
                                label = { Text(category, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.drop(2).forEach { category ->
                            FilterChip(
                                selected = selectedCategory.equals(category, ignoreCase = true),
                                onClick = { selectedCategory = category },
                                label = { Text(category, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Time Picker Input
                Column {
                    Text(
                        text = "Scheduled Time",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = hourText,
                            onValueChange = { if (it.length <= 2) hourText = it },
                            label = { Text("Hour") },
                            placeholder = { Text("08") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )

                        Text(":", fontWeight = FontWeight.Bold, fontSize = 24.sp)

                        OutlinedTextField(
                            value = minuteText,
                            onValueChange = { if (it.length <= 2) minuteText = it },
                            label = { Text("Minute") },
                            placeholder = { Text("00") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = { isAm = !isAm },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text(if (isAm) "AM" else "PM", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Repeat Interval
                Column {
                    Text(
                        text = "Repeat Schedule",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeatOptions.forEach { opt ->
                            FilterChip(
                                selected = repeatInterval.equals(opt, ignoreCase = true),
                                onClick = { repeatInterval = opt },
                                label = { Text(opt, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    placeholder = { Text("e.g. Take with warm water after breakfast") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val formattedHour = hourText.padStart(2, '0')
                        val formattedMinute = minuteText.padStart(2, '0')
                        val amPm = if (isAm) "AM" else "PM"
                        val timeStr = "$formattedHour:$formattedMinute $amPm"

                        onSave(title, selectedCategory, timeStr, repeatInterval, notes)
                    }
                },
                enabled = title.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Reminder", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun AddEditReminderDialogPreview() {
    MyApplicationTheme {
        AddEditReminderDialog(
            onDismiss = {},
            onSave = { _, _, _, _, _ -> }
        )
    }
}
