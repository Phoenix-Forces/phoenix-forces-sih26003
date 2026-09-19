<p alig![alt text](image.png)n="center">
  <img src="https://img.shields.io/badge/🧠_Arogya_Cognitive-Adaptive_Cognitive_Support_Platform-6C63FF?style=for-the-badge&labelColor=1a1a2e" alt="Arogya Cognitive" />
</p>

<p align="center">
  <em>An AI-assisted cognitive support platform for elderly users, combining adaptive cognitive games, offline-first functionality, personalized performance tracking, and caregiver monitoring.</em>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/SIH-2026-3b82f6?style=flat-square&logo=hackthebox&logoColor=white" alt="SIH 2026" />
  <img src="https://img.shields.io/badge/PS-SIH26003-8b5cf6?style=flat-square&logo=target&logoColor=white" alt="PS SIH26003" />
  <img src="https://img.shields.io/badge/Platform-Android-34a853?style=flat-square&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Compose" />
  <img src="https://img.shields.io/badge/Backend-FastAPI-009688?style=flat-square&logo=fastapi&logoColor=white" alt="FastAPI" />
  <img src="https://img.shields.io/badge/Cloud-AWS-FF9900?style=flat-square&logo=amazonaws&logoColor=white" alt="AWS" />
  <img src="https://img.shields.io/badge/Database-DynamoDB-4053D6?style=flat-square&logo=amazondynamodb&logoColor=white" alt="DynamoDB" />
  <img src="https://img.shields.io/badge/Auth-Cognito-DD344C?style=flat-square&logo=amazonaws&logoColor=white" alt="Cognito" />
  <img src="https://img.shields.io/badge/License-MIT-22c55e?style=flat-square&logo=opensourceinitiative&logoColor=white" alt="MIT" />
</p>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Problem Statement](#-problem-statement)
- [Solution](#-solution)
- [How It Works](#-how-it-works)
- [Key Features](#-key-features)
- [Cognitive Modules](#-cognitive-modules)
- [Tech Stack](#-tech-stack)
- [System Architecture](#-system-architecture)
- [Project Structure](#-project-structure)
- [Current Prototype](#-current-prototype)
- [Getting Started](#-getting-started)
- [Backend Configuration](#%EF%B8%8F-backend-configuration)
- [API Overview](#-api-overview)
- [Offline Synchronization](#-offline-synchronization)
- [Adaptive Difficulty](#-adaptive-difficulty)
- [Database Schema](#%EF%B8%8F-database-schema)
- [Security](#-security)
- [Design & UX](#-design--ux)
- [Project Status](#-project-status)
- [Team](#-team)
- [Smart India Hackathon](#-smart-india-hackathon)
- [Testing](#-testing)
- [Future Scope](#-future-scope)
- [Disclaimer](#%EF%B8%8F-disclaimer)
- [License](#-license)

---

## 🌟 Overview

**Arogya Cognitive** is an Android-based cognitive support platform designed for elderly users experiencing memory decline and cognitive difficulties.

The platform combines **cognitive games, performance tracking, adaptive difficulty, reminders, offline-first storage, cloud synchronization, and caregiver monitoring** into a single accessible system.

The project is developed for **Smart India Hackathon 2026** under:

| Field | Details |
|---|---|
| 🏷️ **Problem Statement** | SIH26003 |
| 📝 **Title** | AI-Based Cognitive Gaming and Memory Assistance Platform for Elderly Dementia Patients in the North Eastern Region (NER) |
| 📂 **Category** | Software |
| 🏥 **Theme** | MedTech / BioTech / HealthTech |
| 👥 **Team** | Phoenix Forces |

The platform is designed with the realities of remote and low-connectivity regions in mind, where continuous internet access cannot always be assumed.

---

## 🎯 Problem Statement

Dementia and age-related cognitive decline can affect:

- 🧠 Memory
- 🎯 Attention
- 👁️ Recognition
- 🧩 Reasoning
- 📅 Daily routines
- 💬 Communication
- 🏃 Independent activities

Existing digital cognitive solutions may depend heavily on continuous connectivity, may not be localized for regional users, and may provide limited caregiver visibility.

For elderly users, the interface must also remain:

- ✅ Simple
- ✅ Accessible
- ✅ Low cognitive load
- ✅ Easy to navigate
- ✅ Suitable for repeated daily use

---

## 💡 Solution

Arogya Cognitive provides a unified platform consisting of:

| Component | Description |
|-----------|-------------|
| 🧩 **Adaptive Cognitive Games** | 6 interactive games across distinct cognitive domains |
| 📊 **Performance Tracking** | Score, accuracy, response time, difficulty, and domain-level progress |
| 🔄 **Offline-First Storage** | Room database ensures full functionality without internet |
| 👨‍👩‍👧 **Caregiver Monitoring** | Dashboard with patient overview, game history, and alert system |
| 🔔 **Reminders & Alerts** | AlarmManager-powered medication, hydration, and appointment reminders |
| 🌐 **Multilingual Support** | English, Hindi (हिन्दी), Assamese (অসমীয়া), Bengali (বাংলা) |
| 🧠 **AI Personalization** | Grand Finale roadmap — AI-assisted cognitive recommendations |
| ☁️ **Serverless Backend** | AWS API Gateway → Lambda → DynamoDB architecture |
| 🔐 **Secure Auth** | Amazon Cognito JWT-based identity and access management |

The platform follows a continuous cycle:

```
     ┌─────────┐
     │  PLAY   │ ← Cognitive Games
     └────┬────┘
          ▼
     ┌─────────┐
     │ ANALYZE │ ← Score · Accuracy · Time
     └────┬────┘
          ▼
     ┌─────────┐
     │  ADAPT  │ ← Difficulty Adjustment
     └────┬────┘
          ▼
     ┌─────────┐
     │  SYNC   │ ← Cloud Synchronization
     └────┬────┘
          ▼
     ┌─────────┐
     │ MONITOR │ ← Caregiver Dashboard
     └────┬────┘
          │
          └──────── 🔄 ──────── back to PLAY
```

---

## 🔄 How It Works

```
┌────────────────────────┐
│     🧓 Elderly User    │
│      Android App       │
└───────────┬────────────┘
            │
            ▼
┌────────────────────────┐
│   🧩 Cognitive Games   │
│  Memory · Focus ·      │
│  Pattern · Recognition │
└───────────┬────────────┘
            │
            ▼
┌────────────────────────┐
│  📊 Performance        │
│     Analysis           │
│  Score · Accuracy ·    │
│  Time · Difficulty     │
└───────────┬────────────┘
            │
            ▼
┌────────────────────────┐
│  🧠 Adaptive           │
│     Difficulty         │
│  Adjust next session   │
└───────────┬────────────┘
            │
            ├────────────────┐
            │                │
       📴 Offline       🌐 Online
            │                │
            ▼                ▼
      ┌──────────┐    ┌─────────────┐
      │ 💾 Room  │───▶│ 🌐 API      │
      │ Database │    │   Gateway   │
      └──────────┘    └──────┬──────┘
                             │
                             ▼
                      ┌─────────────┐
                      │ ⚡ AWS      │
                      │   Lambda    │
                      │   FastAPI   │
                      └──────┬──────┘
                             │
                             ▼
                      ┌─────────────┐
                      │ 🗄️ DynamoDB │
                      └──────┬──────┘
                             │
                             ▼
                      ┌─────────────┐
                      │ 👨‍⚕️ Caregiver │
                      │  Dashboard  │
                      └─────────────┘
```

---

## ✨ Key Features

| Feature | Description | Status |
|---------|-------------|--------|
| 🧩 **Cognitive Games** | 6 interactive activities: memory, attention, recognition, pattern, routine, emotion | ✅ Implemented |
| 🧠 **Adaptive Difficulty** | Rule-based engine adjusts difficulty per performance (Easy → Medium → Hard) | ✅ Implemented |
| 📊 **Performance Tracking** | Score, accuracy, response time, difficulty level, cognitive domain per session | ✅ Implemented |
| 💾 **Offline-First** | All data persisted to Room database; app fully functional without internet | ✅ Implemented |
| 🔄 **Cloud Sync API** | `POST /sync` endpoint for batch offline operation synchronization | ✅ Backend Ready |
| 👨‍👩‍👧 **Caregiver Dashboard** | Patient summary, recent games, domain progress, alert management | ✅ Implemented |
| 🔔 **Reminders** | AlarmManager + BroadcastReceiver for medication, hydration, appointments | ✅ Implemented |
| 🔐 **Cognito Auth** | JWT verification via JWKS (RS256) on backend | ✅ Backend Ready |
| ☁️ **Serverless Backend** | FastAPI + Mangum on AWS Lambda behind API Gateway | ✅ Deployed |
| 🌐 **Multilingual UI** | 4 languages with full string translations (EN, HI, AS, BN) | ✅ Implemented |
| 🗣️ **Voice Assistant** | On-device Android STT/TTS with voice command routing | ✅ Implemented |
| 📱 **Elderly-Friendly UI** | Material 3, large touch targets, simple navigation, low cognitive load | ✅ Implemented |
| 🧠 **AI Personalization** | ML-based cognitive trend analysis and personalized recommendations | 🔮 Grand Finale |
| 🗺️ **NER Cultural Content** | Region-specific imagery, proverbs, and culturally relevant game content | 🔮 Grand Finale |

---

## 🧩 Cognitive Modules

The application contains **6 cognitive game modules**, each targeting a distinct cognitive domain:

| # | Module | Cognitive Focus | Game ID | Description |
|---|--------|----------------|---------|-------------|
| 🧠 | **Memory Match** | Memory & Recognition | `memory_flip` | Flip cards and match pairs from memory |
| 🔢 | **Pattern Memory** | Short-term Visual Memory | `pattern_memory` | Memorize and reproduce visual grid patterns |
| 🎯 | **Focus Test** | Attention & Concentration | `attention_game` | Identify targets under time pressure |
| 📅 | **Daily Schedule** | Routine & Sequencing | `daily_routine` | Arrange daily activities in correct order |
| 🖼️ | **Identify Item** | Object Recognition & Recall | `object_recognition` | Identify objects from visual cues |
| 😊 | **Identify Feeling** | Emotion Recognition | `emotion_recognition` | Recognize emotions from expressions |

Each game records:
- ✅ Score (0–100)
- ✅ Correct / Incorrect count
- ✅ Time taken (seconds)
- ✅ Difficulty level
- ✅ Cognitive domain
- ✅ Timestamp

---

## 🛠️ Tech Stack

### 📱 Android Frontend

| Technology | Purpose |
|-----------|---------|
| <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" /> | Application development |
| <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" /> | Modern declarative UI framework |
| <img src="https://img.shields.io/badge/Material_3-757575?style=flat-square&logo=materialdesign&logoColor=white" /> | Design system & theming |
| <img src="https://img.shields.io/badge/Room-4285F4?style=flat-square&logo=sqlite&logoColor=white" /> | Local SQLite persistence (offline-first) |
| <img src="https://img.shields.io/badge/Retrofit-48B983?style=flat-square&logo=square&logoColor=white" /> | REST API client |
| <img src="https://img.shields.io/badge/Moshi-000000?style=flat-square&logo=json&logoColor=white" /> | JSON serialization |
| <img src="https://img.shields.io/badge/DataStore-34A853?style=flat-square&logo=android&logoColor=white" /> | Preferences storage |
| <img src="https://img.shields.io/badge/Navigation_3-FF6F00?style=flat-square&logo=android&logoColor=white" /> | Type-safe navigation (latest) |
| <img src="https://img.shields.io/badge/Coroutines-7F52FF?style=flat-square&logo=kotlin&logoColor=white" /> | Asynchronous operations |
| <img src="https://img.shields.io/badge/ViewModel-4285F4?style=flat-square&logo=android&logoColor=white" /> | UI state management (12 ViewModels) |

### ☁️ Backend & Cloud

| Technology | Purpose |
|-----------|---------|
| <img src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white" /> | Backend development |
| <img src="https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=fastapi&logoColor=white" /> | REST API framework |
| <img src="https://img.shields.io/badge/Pydantic-E92063?style=flat-square&logo=pydantic&logoColor=white" /> | Request/response validation |
| <img src="https://img.shields.io/badge/Mangum-000000?style=flat-square&logo=serverless&logoColor=white" /> | ASGI → Lambda adapter |
| <img src="https://img.shields.io/badge/Boto3-FF9900?style=flat-square&logo=amazonaws&logoColor=white" /> | AWS SDK for Python |
| <img src="https://img.shields.io/badge/API_Gateway-FF4F8B?style=flat-square&logo=amazonapigateway&logoColor=white" /> | HTTPS API entry point |
| <img src="https://img.shields.io/badge/Lambda-FF9900?style=flat-square&logo=awslambda&logoColor=white" /> | Serverless compute |
| <img src="https://img.shields.io/badge/DynamoDB-4053D6?style=flat-square&logo=amazondynamodb&logoColor=white" /> | NoSQL database (single-table design) |
| <img src="https://img.shields.io/badge/Cognito-DD344C?style=flat-square&logo=amazonaws&logoColor=white" /> | User authentication (JWT/RS256) |

---

## 🏗️ System Architecture

```
                    ┌──────────────────────────┐
                    │    📱 Android App         │
                    │   Kotlin + Compose + M3   │
                    │                           │
                    │  ┌────────┐ ┌──────────┐ │
                    │  │Room DB │ │DataStore  │ │
                    │  │(SQLite)│ │(Prefs)    │ │
                    │  └───┬────┘ └────┬─────┘ │
                    │      │  Offline   │       │
                    │      │  First     │       │
                    │  ┌───┴───────────┴─────┐ │
                    │  │  Retrofit + Moshi    │ │
                    │  └──────────┬───────────┘ │
                    └─────────────┬─────────────┘
                                  │
                             HTTPS / JSON
                                  │
                                  ▼
                    ┌──────────────────────────┐
                    │  🌐 Amazon API Gateway    │
                    │     REST API              │
                    └─────────────┬────────────┘
                                  │
                    ┌─────────────┴────────────┐
                    │                          │
                    ▼                          ▼
          ┌─────────────────┐      ┌────────────────────┐
          │ 🔐 Amazon       │      │ ⚡ AWS Lambda       │
          │    Cognito      │      │    FastAPI +        │
          │ Authentication  │      │    Mangum           │
          └─────────────────┘      └─────────┬──────────┘
                                             │
                               ┌─────────────┼─────────────┐
                               │             │             │
                               ▼             ▼             ▼
                         ┌──────────┐  ┌──────────┐  ┌──────────┐
                         │ 📊 Game  │  │ 🧠 Adapt │  │ 🔄 Data  │
                         │ Results  │  │ Difficulty│  │   Sync   │
                         │   APIs   │  │ Analysis │  │   APIs   │
                         └──────────┘  └──────────┘  └──────────┘
                                             │
                                             ▼
                                   ┌─────────────────┐
                                   │ 🗄️ DynamoDB     │
                                   │  Single Table    │
                                   │  Design          │
                                   └────────┬────────┘
                                            │
                                            ▼
                                   ┌─────────────────┐
                                   │ 👨‍⚕️ Caregiver    │
                                   │    Dashboard     │
                                   └─────────────────┘
```

### Architecture Principles

| Principle | Implementation |
|-----------|---------------|
| 📴 **Offline-First** | Room DB as primary store; API sync is secondary |
| ⚡ **Serverless** | Lambda + API Gateway — zero server management |
| 🧱 **Modular** | Clean separation: data → repository → ViewModel → UI |
| 📈 **Scalable** | DynamoDB + Lambda auto-scale with demand |
| 🔐 **Secure** | Cognito JWT + Pydantic validation + IAM |
| 🔌 **API-Driven** | RESTful JSON contract between frontend and backend |
| ☁️ **Cloud-Synced** | Batch sync endpoint for offline operations |

---

## 📁 Project Structure

```
phoenix-forces-sih26003/
│
├── 📄 API_CONTRACT.md                    # Frontend ↔ Backend API contract
├── 📄 README.md
├── 📄 .gitignore
│
├── 📱 frontend/                          # Android Application
│   ├── app/
│   │   ├── build.gradle.kts              # Dependencies (Compose, Room, Retrofit, Moshi, etc.)
│   │   └── src/main/
│   │       ├── AndroidManifest.xml        # Permissions: INTERNET, AUDIO, NOTIFICATIONS, ALARMS
│   │       │
│   │       ├── java/com/example/myapplication/
│   │       │   ├── ArogyaApplication.kt           # 🏠 App-level dependency container
│   │       │   ├── MainActivity.kt                # 🚀 Entry point + locale setup
│   │       │   │
│   │       │   ├── data/
│   │       │   │   ├── local/
│   │       │   │   │   ├── AppDatabase.kt         # 💾 Room DB — 4 tables + seed data
│   │       │   │   │   ├── dao/                   # 📋 PatientDao, GameResultDao, ReminderDao, AlertDao
│   │       │   │   │   └── entity/                # 📦 PatientEntity, GameResultEntity, ReminderEntity, AlertEntity
│   │       │   │   ├── remote/
│   │       │   │   │   ├── ApiService.kt          # 🌐 Retrofit API interface
│   │       │   │   │   ├── RetrofitClient.kt      # ⚙️ API client configuration
│   │       │   │   │   ├── GameResultRequest.kt   # 📤 Request DTOs
│   │       │   │   │   └── GameResultResponse.kt  # 📥 Response DTOs
│   │       │   │   ├── preferences/
│   │       │   │   │   └── SettingsDataStore.kt   # ⚙️ Language & role preferences
│   │       │   │   └── repository/
│   │       │   │       ├── PatientRepository.kt   # 👤 Patient data operations
│   │       │   │       ├── GameRepository.kt      # 🎮 Game results + API sync
│   │       │   │       ├── ReminderRepository.kt  # 🔔 Reminder CRUD
│   │       │   │       ├── AlertRepository.kt     # ⚠️ Caregiver alerts
│   │       │   │       └── SettingsRepository.kt  # ⚙️ App settings
│   │       │   │
│   │       │   ├── ui/
│   │       │   │   ├── AppViewModelProvider.kt    # 🏭 ViewModel factory (12 ViewModels)
│   │       │   │   ├── navigation/
│   │       │   │   │   ├── Screen.kt              # 🗺️ 17 screen definitions (type-safe)
│   │       │   │   │   └── AppNavigation.kt       # 🧭 Full navigation graph
│   │       │   │   ├── splash/
│   │       │   │   │   └── SplashScreen.kt        # ✨ Animated splash
│   │       │   │   ├── role/
│   │       │   │   │   ├── RoleSelectionScreen.kt # 🔀 Patient / Caregiver role picker
│   │       │   │   │   └── RoleViewModel.kt
│   │       │   │   ├── patient/
│   │       │   │   │   ├── home/                  # 🏠 Patient dashboard
│   │       │   │   │   ├── games/                 # 🎮 Games list + 6 game screens
│   │       │   │   │   │   ├── GamesListScreen.kt
│   │       │   │   │   │   ├── GameResultScreen.kt
│   │       │   │   │   │   ├── GamePlayViewModel.kt
│   │       │   │   │   │   └── play/
│   │       │   │   │   │       ├── MemoryMatchScreen.kt
│   │       │   │   │   │       ├── PatternMemoryScreen.kt
│   │       │   │   │   │       ├── AttentionGameScreen.kt
│   │       │   │   │   │       ├── DailyRoutineGameScreen.kt
│   │       │   │   │   │       ├── ObjectRecognitionGameScreen.kt
│   │       │   │   │   │       └── EmotionRecognitionGameScreen.kt
│   │       │   │   │   ├── reminders/             # 🔔 Reminder management
│   │       │   │   │   ├── progress/              # 📊 Progress tracker + charts
│   │       │   │   │   ├── profile/               # 👤 Patient profile editor
│   │       │   │   │   └── language/              # 🌐 Language settings
│   │       │   │   ├── caregiver/
│   │       │   │   │   ├── dashboard/             # 📊 Caregiver overview
│   │       │   │   │   ├── patient/               # 👤 Patient detail management
│   │       │   │   │   ├── history/               # 📋 Game history with filters
│   │       │   │   │   └── alerts/                # ⚠️ Alert system
│   │       │   │   ├── voice/
│   │       │   │   │   ├── VoiceAssistantScreen.kt
│   │       │   │   │   └── VoiceAssistantViewModel.kt
│   │       │   │   ├── theme/                     # 🎨 Material 3 theme, colors, typography
│   │       │   │   └── common/                    # 🧱 Shared UI components
│   │       │   │
│   │       │   └── utils/
│   │       │       ├── VoiceAssistantManager.kt          # 🗣️ STT / TTS engine
│   │       │       ├── ReminderNotificationHelper.kt     # 🔔 Alarm scheduling
│   │       │       ├── ReminderReceiver.kt               # 📡 Broadcast receiver
│   │       │       └── LocaleUtils.kt                    # 🌐 Locale context switching
│   │       │
│   │       └── res/
│   │           ├── values/strings.xml             # 🇬🇧 English strings
│   │           ├── values-hi/strings.xml          # 🇮🇳 Hindi translations
│   │           ├── values-as/strings.xml          # 🇮🇳 Assamese translations
│   │           └── values-bn/strings.xml          # 🇮🇳 Bengali translations
│   │
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   ├── gradle/
│   ├── gradlew
│   └── gradlew.bat
│
├── ☁️ backend/                           # FastAPI Backend
│   ├── requirements.txt                  # fastapi, boto3, mangum, pydantic, python-jose, httpx
│   ├── app/
│   │   ├── main.py                       # 🚀 FastAPI app + Mangum Lambda handler
│   │   ├── core/
│   │   │   ├── config.py                 # ⚙️ Settings (AWS region, DynamoDB, Cognito)
│   │   │   └── security.py              # 🔐 Cognito JWT verification (JWKS + RS256)
│   │   ├── db/
│   │   │   ├── dynamodb.py              # 🗄️ DynamoDB table connection
│   │   │   └── repositories/            # 📦 Data access layer
│   │   ├── routes/
│   │   │   ├── auth.py                  # 🔐 GET /auth/me
│   │   │   ├── results.py               # 📊 POST /game-results
│   │   │   ├── history.py               # 📋 GET /game-history + adaptive difficulty
│   │   │   ├── games.py                 # 🎮 GET /games/{id}/config
│   │   │   ├── analytics.py             # 📈 GET /analytics
│   │   │   ├── dashboard.py             # 📊 GET /dashboard
│   │   │   ├── activity.py              # 🏃 GET /activity
│   │   │   ├── reminders.py             # 🔔 Full CRUD — reminders
│   │   │   ├── alerts.py                # ⚠️ Caregiver alert system
│   │   │   ├── sync.py                  # 🔄 POST /sync — offline batch sync
│   │   │   └── voice.py                 # 🗣️ GET /voice/languages + STT/TTS stubs
│   │   ├── schemas/
│   │   │   ├── common.py                # 📋 GameResultCreate
│   │   │   ├── reminders.py             # 📋 ReminderCreate, ReminderUpdate
│   │   │   └── sync.py                  # 📋 SyncOperation, SyncRequest
│   │   └── services/                    # 🧠 Business logic layer
│   │
│   └── tests/                           # 🧪 Test suite
│
└── 📄 docs/                             # 📚 Documentation
```

---

## 📱 Current Prototype

The current prototype demonstrates the complete application workflow from game play through performance tracking to caregiver monitoring.

### ✅ Implemented

| Component | Details |
|-----------|---------|
| 📱 **Android Application** | Kotlin + Jetpack Compose + Material 3 |
| 🧩 **6 Cognitive Games** | Full game logic, UI, scoring, and timer |
| 💾 **Room Database** | 4 tables (patients, game_results, reminders, alerts) with seed data |
| 📊 **Performance Tracking** | Score, accuracy, time, difficulty, domain breakdown, 7-day trends |
| 🧠 **Adaptive Difficulty** | Rule-based: score ≥80 → upgrade, <50 → downgrade (Easy/Medium/Hard) |
| 📴 **Offline Data Handling** | All operations save to Room first; API sync is secondary |
| 🔔 **Reminder System** | Full CRUD + AlarmManager + notifications via BroadcastReceiver |
| 👨‍⚕️ **Caregiver Dashboard** | Patient summary, game history, domain filters, sorting, alerts |
| 🗣️ **Voice Assistant** | On-device STT/TTS, voice commands, 4-language support |
| 🌐 **Multilingual UI** | Full string translations: English, Hindi, Assamese, Bengali |
| 👤 **Profile Management** | Edit patient name, age, caregiver name |
| ⚡ **FastAPI Backend** | 11 route modules, Pydantic validation, Mangum handler |
| ☁️ **AWS Lambda** | Deployed with API Gateway |
| 🗄️ **DynamoDB** | Single-table design — game results, reminders, alerts, sync operations |
| 🔐 **Cognito Auth** | JWT verification via JWKS (RS256) on backend |
| 🔄 **Sync API** | `POST /sync` for batch offline operation processing |

### 🚧 In Development

| Component | Details |
|-----------|---------|
| 🔗 **Full API Integration** | Expanding Retrofit endpoints beyond game results |
| 🔄 **Sync Queue** | Automatic retry for failed API sync with WorkManager |
| 🔐 **Android Auth Flow** | Cognito SDK integration on the mobile client |
| 📊 **Caregiver Analytics** | Advanced trend analysis and reporting |
| 🧠 **AI Personalization** | ML-based cognitive trend prediction and game recommendations |
| 🗣️ **Voice-First Interaction** | Enhanced STT/TTS with NER language support |
| 🗺️ **NER Cultural Content** | Region-specific imagery, proverbs, cultural game content |
| 🎮 **Expanded Game Library** | Additional cognitive domains and game types |
| 📈 **Long-Term Trends** | Multi-week cognitive trajectory analysis |
| 🔔 **Intelligent Alerts** | AI-triggered caregiver notifications |

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Version |
|-------------|---------|
| 📱 Android Studio | Latest stable |
| ☕ JDK | 11+ |
| 📦 Android SDK | API 37 (min API 24) |
| 🐍 Python | 3.10+ |
| ☁️ AWS CLI | Configured with credentials |
| 🔧 Git | Latest |

### 📥 Clone Repository

```bash
git clone https://github.com/Phoenix-Forces/phoenix-forces-sih26003.git
cd phoenix-forces-sih26003
```

### 📱 Run Android Application

```bash
cd frontend

# Build debug APK:
./gradlew assembleDebug       # Linux/Mac
gradlew.bat assembleDebug     # Windows

# Output:
# app/build/outputs/apk/debug/app-debug.apk
```

> 💡 **Tip**: Run on a **physical device** for full voice assistant (STT/TTS) functionality. Emulators may not support all speech recognition features.

### 🐍 Run Backend Locally

```bash
cd backend

# Create virtual environment:
python -m venv .venv

# Activate:
.venv\Scripts\activate        # Windows
source .venv/bin/activate     # Linux/Mac

# Install dependencies:
pip install -r requirements.txt

# Start server:
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

📖 API documentation: **http://localhost:8000/docs** (Swagger UI)

---

## ⚙️ Backend Configuration

Create a `.env` file inside the `backend/` directory:

```env
AWS_REGION=ap-south-1
DYNAMODB_TABLE_NAME=your_dynamodb_table
COGNITO_USER_POOL_ID=your_cognito_user_pool_id
COGNITO_CLIENT_ID=your_cognito_client_id
```

> ⚠️ **Security**: Never commit real AWS credentials, Cognito secrets, or production environment variables to version control.

### AWS Lambda Deployment

The backend is deployed as an AWS Lambda function behind API Gateway using **Mangum** as the ASGI adapter. The deployment package is pre-built in `lambda_deployment.zip`.

---

## 🔌 API Overview

| Method | Endpoint | Purpose |
|--------|----------|---------|
| `GET` | `/` | Root — API status |
| `GET` | `/health` | Health check |
| `GET` | `/auth/me` | Authenticated user info |
| `POST` | `/game-results` | Save game performance result |
| `GET` | `/patients/{id}/game-history` | Retrieve all game results |
| `GET` | `/patients/{id}/games/{gameId}/difficulty` | Adaptive difficulty recommendation |
| `GET` | `/patients/{id}/games/{gameId}/config` | Game configuration |
| `GET` | `/patients/{id}/analytics` | Performance analytics summary |
| `GET` | `/patients/{id}/dashboard` | Caregiver dashboard data |
| `GET` | `/patients/{id}/activity` | Patient activity feed |
| `GET` | `/patients/{id}/reminders` | Get all reminders |
| `POST` | `/reminders` | Create reminder |
| `PATCH` | `/reminders/{id}` | Update reminder |
| `DELETE` | `/reminders/{id}` | Delete reminder |
| `POST` | `/reminders/{id}/complete` | Mark reminder complete |
| `GET` | `/patients/{id}/alerts` | Get caregiver alerts |
| `POST` | `/patients/{id}/alerts` | Create alert |
| `PATCH` | `/alerts/{id}` | Update alert status |
| `POST` | `/sync` | Batch offline sync |
| `GET` | `/voice/languages` | Supported voice languages |

> 📄 See **[API_CONTRACT.md](./API_CONTRACT.md)** for full request/response schemas.

---

## 🔄 Offline Synchronization

Arogya Cognitive follows an **offline-first** architecture:

```
    📱 User Plays Game
           ↓
    💾 Result Stored Locally (Room)
           ↓
    🔄 Sync Attempt (Retrofit)
           │
     ┌─────┴──────┐
     │            │
  ✅ Online    📴 Offline
     │            │
     ▼            ▼
  ☁️ DynamoDB   💾 Stays in Room
                  │
           🌐 Internet Returns
                  │
                  ▼
           🔄 POST /sync
                  │
                  ▼
           ☁️ DynamoDB
```

This ensures the application continues collecting cognitive activity data even in areas with unreliable internet connectivity — a key requirement for NER deployment.

---

## 🧠 Adaptive Difficulty

The current prototype implements a **rule-based adaptive difficulty engine**:

```
              📊 Game Result
                   │
                   ▼
           🧠 Performance Analysis
                   │
         ┌─────────┼─────────┐
         │         │         │
      Score ≥ 80  50-79    Score < 50
         │         │         │
         ▼         ▼         ▼
      ⬆️ Increase  ➡️ Hold   ⬇️ Decrease
      Difficulty  Difficulty  Difficulty
```

| Difficulty Tiers | Frontend (Score-Based) | Backend (Accuracy-Based) |
|-----------------|----------------------|------------------------|
| **Level 1** | Easy | accuracy ≤ 0.50 → decrease |
| **Level 2** | Medium | 0.50 < accuracy < 0.80 → hold |
| **Level 3** | Hard | accuracy ≥ 0.80 → increase |


The planned intelligence layer will move beyond fixed rules toward **AI-assisted personalization**, using historical performance patterns to personalize:

- 🎮 Game selection and ordering
- 📈 Difficulty curves
- ⏱️ Session length
- 🧠 Cognitive domain focus
- 🔔 Reminder timing
- 💡 User-specific activity recommendations

---

## 🗄️ Database Schema

### ☁️ DynamoDB — Single-Table Design

```
PATIENT#<patient_id>
│
├── RESULT#<timestamp>#<uuid>     → Game Result
├── REMINDER#<uuid>               → Reminder
├── ALERT#<uuid>                  → Caregiver Alert
└── SYNC#<operation_id>           → Sync Operation
```

| Entity | Partition Key (PK) | Sort Key (SK) |
|--------|-------------------|---------------|
| 🎮 Game Result | `PATIENT#{id}` | `RESULT#{timestamp}#{uuid}` |
| 🔔 Reminder | `PATIENT#{id}` | `REMINDER#{uuid}` |
| ⚠️ Alert | `PATIENT#{id}` | `ALERT#{uuid}` |
| 🔄 Sync Operation | `PATIENT#{id}` | `SYNC#{operation_id}` |

### 💾 Room Database (Android — Local)

| Table | Key Columns |
|-------|------------|
| `patients` | id, name, age, caregiverName, primaryLanguage, currentDifficulty, streakDays, lastActiveDate |
| `game_results` | id, gameType, score, correctCount, incorrectCount, timeTakenSeconds, difficultyLevel, cognitiveDomain, timestamp |
| `reminders` | id, title, category, timeString, isEnabled, repeatInterval, notes |
| `alerts` | id, title, message, severity, timestamp, isRead |

---

## 🔐 Security

```
📱 Android App
     │
     ▼
🔐 Amazon Cognito
     │
     ▼
🎫 JWT Token (RS256)
     │
     ▼
🌐 API Gateway
     │
     ▼
⚡ FastAPI Backend
     │
     ▼
✅ Token Verification (JWKS)
```

| Security Layer | Implementation |
|---------------|----------------|
| 🔐 **Authentication** | Amazon Cognito User Pool |
| 🎫 **Token Format** | JWT (RS256) via JWKS endpoint |
| 🛡️ **API Protection** | `Authorization: Bearer <token>` |
| ✅ **Validation** | Pydantic schema validation on all endpoints |
| 🔒 **Credentials** | Environment-based configuration — never in source |
| 🏗️ **IAM** | AWS IAM roles for Lambda ↔ DynamoDB access |
| 🧱 **Separation** | Frontend and backend responsibility split |

---

## 🎨 Design & UX

The application is designed around **elderly accessibility**:

| Design Principle | Implementation |
|-----------------|----------------|
| 🧭 **Simple Navigation** | Minimal interaction paths, clear navigation hierarchy |
| 👆 **Large Touch Targets** | Oversized buttons and cards for easier interaction |
| 💬 **Clear Feedback** | Immediate visual + audio feedback on game actions |
| 🧠 **Low Cognitive Load** | Focused screens with single-purpose layouts |
| 🌐 **Multilingual** | 4 languages with dynamic switching |
| 🗣️ **Voice Support** | STT/TTS for hands-free interaction |
| 🎨 **Consistent Theme** | Material 3 design system with reusable Compose components |
| ♿ **Accessible Content** | High contrast, readable typography, elderly-friendly sizing |

---

## 🛣️ Future Roadmap

```
                     CURRENT PROTOTYPE
                           │
                           ▼
                 ┌─────────────────┐
                 │ ✅ 6 Cognitive  │
                 │    Games        │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ ✅ Performance  │
                 │    Tracking     │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ ✅ Adaptive     │
                 │    Baseline     │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ ✅ Offline      │
                 │    Sync         │
                 └────────┬────────┘
                          │
                          ▼
                      Upcoming
                          │
          ┌───────────────┼───────────────┐
          ▼               ▼               ▼
   🧠 AI-Assisted   🗣️ Voice &      📊 Caregiver
   Personalization   Languages      Analytics
          │               │               │
          └───────────────┼───────────────┘
                          ▼
                ┌──────────────────┐
                │  🏆 Complete     │
                │  Arogya          │
                │  Cognitive       │
                │  System          │
                └──────────────────┘
```

### Planned Enhancements

| Priority | Enhancement |
|----------|------------|
| 🧠 | AI-assisted cognitive personalization |
| 🎮 | Expanded cognitive game library |
| 🗣️ | Multilingual voice interaction (NER languages) |
| 🔊 | Enhanced speech-to-text / text-to-speech |
| 📊 | Advanced caregiver analytics dashboard |
| 📈 | Long-term cognitive activity trends |
| 🔔 | Intelligent AI-triggered caregiver alerts |
| 🔄 | Stronger offline sync with WorkManager |
| 🗺️ | Regional and culturally relevant NER content |
| ☁️ | Production-scale cloud deployment |
| 🔒 | Enhanced security and privacy controls |
| 🧪 | Larger-scale field validation |

---

## 📊 Project Status

| Component | Status |
|-----------|--------|
| 📱 Android Application | ✅ Implemented |
| 🧩 Cognitive Games (6) | ✅ Implemented |
| 💾 Local Room Database | ✅ Implemented |
| 📊 Performance Tracking | ✅ Implemented |
| 🧠 Rule-Based Adaptation | ✅ Implemented |
| 📴 Offline Data Handling | ✅ Implemented |
| 🔔 Reminder System | ✅ Implemented |
| 👨‍⚕️ Caregiver Dashboard | ✅ Implemented |
| 🗣️ Voice Assistant (STT/TTS) | ✅ Implemented |
| 🌐 Multilingual UI (4 langs) | ✅ Implemented |
| ☁️ FastAPI Backend | ✅ Implemented |
| ⚡ AWS Lambda | ✅ Deployed |
| 🌐 API Gateway | ✅ Deployed |
| 🗄️ DynamoDB | ✅ Configured |
| 🔐 Cognito Authentication | ✅ Configured |
| 🔄 Cloud Sync API | ✅ Backend Ready |
| 🔗 Full API Integration | 🚧 In Development |
| 📊 Advanced Caregiver Analytics | 🚧 In Development |
| 🗣️ Enhanced Voice Interface | 🚧 In Development |
| 🧠 AI Personalization | 🚧 In Development |
| 🗺️ NER Cultural Content | 🚧 In Development |

---

## 👥 Team

### 🏆 Phoenix Forces

| Member | Role |
|--------|------|
| **Gagandeep G N** | 👨‍💻 Team Lead & Backend Developer |
| **Kiran Kumar K S** | 📱 Frontend Developer |
| **Sachin Goudar** | 📱 Frontend Developer |
| **Jeevan CD** | ☁️ Backend Developer |
| **Mahi Jadhav** | 📝 Research & Presentation |
| **Dhruthi V S** | 📝 Research & Presentation |

---

## 🏆 Smart India Hackathon

<p align="center">
  <img src="https://img.shields.io/badge/Smart_India_Hackathon-2026-3b82f6?style=for-the-badge&logo=hackthebox&logoColor=white" alt="SIH 2026" />
</p>

**Problem Statement ID**: SIH26003

**Problem Statement**: *AI-Based Cognitive Gaming and Memory Assistance Platform for Elderly Dementia Patients in the North Eastern Region (NER)*

**Team**: Phoenix Forces

The project focuses on building an accessible and scalable digital platform for cognitive support, with particular attention to:

- 🧓 Elderly accessibility
- 📴 Offline functionality
- 🌐 Regional language support (NER)
- 🧠 Cognitive engagement
- 👨‍⚕️ Caregiver awareness
- ☁️ Cloud scalability
- 🎯 Personalized experiences

---



## 🧪 Testing

### Backend API Testing

```bash
# Health check:
curl http://localhost:8000/health
# Expected: {"status": "healthy"}

# Swagger UI:
# http://localhost:8000/docs
```

### Sync Testing

Synchronization testing validates:
- ✅ Operation queuing
- ✅ Successful synchronization
- ✅ Failed operation handling
- ✅ Cloud persistence
- ✅ Idempotent operations

---

## 🔭 Future Scope

Arogya Cognitive is designed as a **modular platform** — additional capabilities can be introduced without redesigning the complete system:

| Area | Future Enhancement |
|------|--------------------|
| 🧠 **AI** | ML-driven personalization, anomaly detection, trend prediction |
| 🎮 **Games** | Additional cognitive domains, culturally adapted content |
| 🗣️ **Voice** | Voice-first interaction with NER language models |
| 🌐 **Languages** | Manipuri, Bodo, Mizo, and additional NER languages |
| 📊 **Analytics** | Caregiver web dashboard, exportable reports |
| 🏥 **Healthcare** | Integration with healthcare worker workflows |
| ☁️ **Scale** | Production deployment, multi-region, field testing |
| 🔒 **Privacy** | Privacy-preserving analytics, data encryption at rest |

---

## ⚠️ Disclaimer

This project is developed as part of **Smart India Hackathon 2026** for educational and demonstration purposes. It is **not** intended for clinical diagnosis or medical treatment. Always consult qualified healthcare professionals for medical decisions.
---

<p align="center">
  <strong>Built with ❤️ by Team Phoenix Forces for SIH 2026</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Made_in-India-FF9933?style=flat-square&logo=india&logoColor=white" alt="Made in India" />
  <img src="https://img.shields.io/badge/For-NER_Elderly_Care-6C63FF?style=flat-square" alt="NER" />
</p>
