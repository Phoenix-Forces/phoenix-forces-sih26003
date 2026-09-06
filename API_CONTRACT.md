# API Contract — SIH26003

## Project

AI-Based Cognitive Gaming and Memory Assistance Platform for Elderly Dementia Patients in the North Eastern Region (NER)

## Purpose

This document defines the communication contract between the Kotlin Android application and the backend.

## Frontend

- Platform: Android
- Language: Kotlin
- IDE: Android Studio

## Backend

- API: REST
- Framework: FastAPI
- Database: PostgreSQL
- Deployment: AWS

## Base URL

Development: TBD

Production: TBD

---

# 1. Authentication

## Login

POST /auth/login

### Request

    {
      "email": "user@example.com",
      "password": "password"
    }

### Response

    {
      "success": true,
      "token": "JWT_TOKEN",
      "user_id": 101,
      "role": "patient",
      "patient_id": 101
    }

---

# 2. Patient

## Get Patient Profile

GET /patients/{patientId}

### Response

    {
      "patient_id": 101,
      "name": "Patient Name",
      "age": 72,
      "preferred_language": "en"
    }

## Update Patient Preferences

PATCH /patients/{patientId}

### Request

    {
      "preferred_language": "en",
      "voice_enabled": true
    }

### Response

    {
      "success": true,
      "patient_id": 101,
      "preferred_language": "en",
      "voice_enabled": true
    }

---

# 3. Games

## Get Available Games

GET /games

### Response

    {
      "games": [
        {
          "game_id": "memory_match",
          "name": "Memory Match",
          "category": "memory"
        },
        {
          "game_id": "pattern",
          "name": "Pattern Recognition",
          "category": "attention"
        },
        {
          "game_id": "routine_recall",
          "name": "Routine Recall",
          "category": "memory"
        },
        {
          "game_id": "spot_it",
          "name": "Spot It",
          "category": "attention"
        }
      ]
    }

## Get Game Configuration

GET /patients/{patientId}/games/{gameId}/config

### Response

    {
      "game_id": "memory_match",
      "difficulty": 2
    }

---

# 4. Game Results

## Save Game Result

POST /game-results

### Request

    {
      "patient_id": 101,
      "game_id": "memory_match",
      "session_id": "session-001",
      "score": 8,
      "accuracy": 80,
      "time_taken": 42,
      "attempts": 10,
      "difficulty": 2,
      "completed": true,
      "played_at": "2026-09-08T10:30:00Z",
      "client_result_id": "local-001"
    }

### Response

    {
      "success": true,
      "result_id": 501,
      "next_difficulty": 3
    }

## Get Game History

GET /patients/{patientId}/game-history

### Response

    {
      "patient_id": 101,
      "results": [
        {
          "result_id": 501,
          "game_id": "memory_match",
          "score": 8,
          "accuracy": 80,
          "time_taken": 42,
          "attempts": 10,
          "difficulty": 2,
          "played_at": "2026-09-08T10:30:00Z"
        }
      ]
    }

---

# 5. Adaptive Difficulty

The backend calculates the recommended difficulty using previous game performance.

General logic:

- High performance → increase difficulty
- Medium performance → maintain difficulty
- Low performance → decrease difficulty

## Get Current Difficulty

GET /patients/{patientId}/games/{gameId}/difficulty

### Response

    {
      "patient_id": 101,
      "game_id": "memory_match",
      "difficulty": 3
    }

The next recommended difficulty may also be returned after submitting a game result.

---

# 6. Caregiver Dashboard

## Get Dashboard Summary

GET /patients/{patientId}/dashboard

### Response

    {
      "patient_id": 101,
      "games_played": 14,
      "average_accuracy": 82,
      "average_score": 7.5,
      "last_activity": "2026-09-08T10:30:00Z",
      "current_difficulty": 3
    }

## Get Analytics

GET /patients/{patientId}/analytics

### Response

    {
      "patient_id": 101,
      "games_played": 14,
      "average_accuracy": 82,
      "daily_activity": []
    }

The frontend displays the data as charts.

The backend provides the stored data and calculated metrics.

---

# 7. Reminders

Supported reminder types:

- Medicine
- Hydration
- Daily activity
- Medical appointment

## Get Reminders

GET /patients/{patientId}/reminders

### Response

    {
      "reminders": [
        {
          "reminder_id": 301,
          "patient_id": 101,
          "type": "medicine",
          "title": "Morning medicine",
          "description": "Take prescribed medicine",
          "time": "08:00",
          "repeat": "daily",
          "enabled": true
        }
      ]
    }

## Create Reminder

POST /reminders

### Request

    {
      "patient_id": 101,
      "type": "medicine",
      "title": "Morning medicine",
      "description": "Take prescribed medicine",
      "time": "08:00",
      "repeat": "daily"
    }

## Update Reminder

PATCH /reminders/{reminderId}

### Request

    {
      "time": "08:30",
      "enabled": true
    }

## Delete Reminder

DELETE /reminders/{reminderId}

## Mark Reminder Complete

POST /reminders/{reminderId}/complete

### Response

    {
      "success": true,
      "reminder_id": 301,
      "completed_at": "2026-09-08T08:05:00Z"
    }

---

# 8. Caregiver Alerts

## Get Alerts

GET /patients/{patientId}/alerts

### Response

    {
      "alerts": [
        {
          "alert_id": 1001,
          "type": "inactivity",
          "severity": "medium",
          "message": "No activity for 3 days",
          "read": false
        }
      ]
    }

## Mark Alert as Read

PATCH /alerts/{alertId}

### Request

    {
      "read": true
    }

---

# 9. Offline Synchronization

When there is no internet connection, the Android application stores relevant data locally.

When connectivity returns, pending data is synchronized with the backend.

## Sync Data

POST /sync

### Request

    {
      "device_id": "device-001",
      "operations": [
        {
          "operation_id": "op-001",
          "type": "game_result",
          "client_result_id": "local-001",
          "data": {
            "patient_id": 101,
            "game_id": "memory_match",
            "score": 8,
            "accuracy": 80,
            "time_taken": 42,
            "difficulty": 2
          }
        }
      ]
    }

### Response

    {
      "success": true,
      "synced": 1,
      "failed": 0
    }

The backend must prevent the same offline operation from being stored more than once.

---

# 10. Voice Assistance

The application supports voice-enabled multilingual interaction.

General flow:

    Patient speaks
        ↓
    Speech-to-Text (STT)
        ↓
    Text / command
        ↓
    Application action
        ↓
    Text-to-Speech (TTS)
        ↓
    Patient hears response

## Get Supported Languages

GET /voice/languages

### Response

    {
      "languages": [
        {
          "code": "en",
          "name": "English",
          "stt": true,
          "tts": true
        },
        {
          "code": "hi",
          "name": "Hindi",
          "stt": true,
          "tts": true
        }
      ]
    }

The initial prototype may support a limited number of languages.

The exact STT/TTS provider and audio format will be finalized during implementation.

---

# 11. Authentication and Security

Authenticated endpoints use:

    Authorization: Bearer <JWT_TOKEN>

The backend must ensure:

- Patients can access only their own data.
- Caregivers can access only assigned patients.
- Sensitive credentials and API keys are never stored in the Android application.
- Patient data is securely stored on the backend.

---

# 12. Standard Error Format

    {
      "success": false,
      "error": "ERROR_CODE",
      "message": "Description of the error"
    }

Common HTTP status codes:

- 200 - Success
- 201 - Created
- 400 - Bad Request
- 401 - Unauthorized
- 403 - Forbidden
- 404 - Not Found
- 409 - Conflict
- 422 - Validation Error
- 500 - Server Error
- 503 - Service Unavailable

---

# 13. Responsibility Split

## Frontend — Kotlin / Android Studio

Responsible for:

- Android UI/UX
- Patient screens
- Caregiver screens
- Navigation
- Cognitive game UI and basic game logic
- Result collection
- Multilingual UI
- Voice interaction UI
- Local/offline storage
- Local sync queue
- Charts and visualizations
- Calling backend APIs
- Displaying backend data

## Backend

Responsible for:

- REST APIs
- Authentication and authorization
- Database
- Game result storage
- Game history
- Adaptive difficulty logic
- Analytics calculations
- Reminders
- Caregiver alerts
- Synchronization API
- Voice service integration
- Security
- Deployment

---

# 14. Integration Flow

## Main Flow

    Kotlin Android App
            ↓
        Login
            ↓
    Play Cognitive Game
            ↓
      Generate Result
            ↓
    POST /game-results
            ↓
         Backend
            ↓
        Database
            ↓
    Adaptive Difficulty
            ↓
    Return next_difficulty
            ↓
    Caregiver Dashboard
            ↓
    GET /dashboard
    GET /game-history
    GET /analytics

## Offline Flow

    No Internet
        ↓
    Store result locally
        ↓
    Sync Queue
        ↓
    Internet Returns
        ↓
    POST /sync
        ↓
    Backend
        ↓
    Database

## Voice Flow

    Patient speaks
        ↓
        STT
        ↓
    Text / command
        ↓
    Application action
        ↓
        TTS
        ↓
    Patient hears response

---

# 15. Contract Change Rule

Any change to the following must be discussed between the frontend and backend teams and updated in this document:

- Endpoint
- HTTP method
- Request fields
- Response fields
- Field names
- Field types
- Authentication requirements
