# phoenix-forces-sih26003
SIH 2026 — AI-Based Cognitive Gaming and Memory Assistance Platform for Elderly Dementia Patients in NER
# 🧠 Arogya Cognitive

### AI-Assisted Cognitive Support Platform for Elderly Dementia Patients

> **An elderly-friendly cognitive support platform combining adaptive cognitive games, offline-first assistance, performance tracking and caregiver monitoring.**

**Smart India Hackathon 2026 — SIH26003**  
**Team: Phoenix Forces**  
**Institution: GM University**

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Problem Statement](#-problem-statement)
- [Our Solution](#-our-solution)
- [How It Works](#-how-it-works)
- [Key Features](#-key-features)
- [Cognitive Modules](#-cognitive-modules)
- [Technology Stack](#-technology-stack)
- [System Architecture](#-system-architecture)
- [Project Structure](#-project-structure)
- [Current Prototype](#-current-prototype)
- [Grand Finale Development Plan](#-grand-finale-development-plan)
- [Getting Started](#-getting-started)
- [Backend Configuration](#-backend-configuration)
- [API Overview](#-api-overview)
- [Offline Synchronization](#-offline-synchronization)
- [Adaptive Difficulty](#-adaptive-difficulty)
- [Security](#-security)
- [Team](#-team)
- [Hackathon](#-hackathon)
- [Project Status](#-project-status)
- [Disclaimer](#-disclaimer)
- [License](#-license)

---

# 🌟 Overview

**Arogya Cognitive** is an Android-based cognitive support platform developed for **Smart India Hackathon 2026 Problem Statement SIH26003**.

The platform is designed for elderly users experiencing memory and cognitive difficulties, particularly in regions where reliable internet connectivity and accessible digital support may be limited.

Arogya Cognitive combines:

- 🧠 Cognitive games
- 📊 Performance tracking
- 🎯 Adaptive difficulty
- 📴 Offline-first functionality
- 🔄 Cloud synchronization
- 👨‍👩‍👧 Caregiver monitoring
- 🔔 Reminders and alerts
- 🌐 Regional accessibility
- 🤖 AI-assisted personalization roadmap

The platform focuses on **cognitive engagement and support** and is not intended to diagnose or treat dementia.

---

# 📌 Problem Statement

## SIH26003

**AI-Based Cognitive Gaming and Memory Assistance Platform for Elderly Dementia Patients in the North Eastern Region (NER)**

Dementia and cognitive decline can affect memory, attention, reasoning and the ability to perform everyday activities.

The challenge becomes more significant when elderly users face:

- Limited access to continuous digital support
- Poor or unreliable internet connectivity
- Difficulty using complex digital interfaces
- Limited caregiver visibility into daily cognitive activities
- Language and regional accessibility barriers

Arogya Cognitive addresses these challenges through an **elderly-centric, adaptive and offline-first cognitive support platform**.

---

# 💡 Our Solution

Arogya Cognitive provides a simple Android interface where elderly users can participate in cognitive activities while the system records performance and adapts the experience.

Caregivers can monitor activity and progress through a dedicated caregiver interface.

### Core Approach

```text
PLAY
  ↓
CAPTURE PERFORMANCE
  ↓
ANALYZE
  ↓
ADAPT
  ↓
SYNC
  ↓
MONITOR
✨ Key Features
Feature	Description
🧠 Cognitive Games	Memory, pattern, attention and daily routine activities
🎯 Adaptive Difficulty	Difficulty adjusts according to user performance
📴 Offline-First	Core activities and results can continue without continuous internet
🔄 Data Synchronization	Local operations synchronize with the cloud when connectivity returns
📊 Progress Analytics	Tracks scores, accuracy, activity and cognitive domains
👨‍👩‍👧 Caregiver Monitoring	Caregivers can view patient activity and progress
🔔 Reminders & Alerts	Supports daily reminders and caregiver alerts
🌐 Regional Accessibility	Designed for multilingual and culturally relevant experiences
🔐 Secure Authentication	AWS Cognito-based identity and authentication
☁️ Serverless Backend	AWS Lambda, API Gateway and DynamoDB architecture
🤖 AI Personalization	Planned AI-assisted personalization based on performance patterns
🧠 Cognitive Modules
Memory Card Match

A visual memory activity where users remember and match cards.

Pattern Memory

Users observe patterns and interact with activities designed around pattern recognition and memory.

Attention & Concentration

Activities designed to engage attention, concentration and response accuracy.

Daily Routine Recall

Routine-based cognitive activities designed around familiar everyday tasks and memory.

🛠️ Technology Stack
📱 Android Frontend
Technology	Purpose
Kotlin	Primary programming language
Android	Mobile application platform
Jetpack Compose	Modern UI development
Room Database	Local/offline data persistence
Kotlin Coroutines / Flow	Asynchronous and reactive data handling
Retrofit	Backend API communication
⚙️ Backend
Technology	Purpose
Python	Backend programming language
FastAPI	REST API framework
Uvicorn	Application server
Pydantic	Request and data validation
Boto3	AWS service integration
☁️ AWS Cloud
AWS Service	Purpose
Amazon API Gateway	HTTPS API entry point
AWS Lambda	Serverless backend execution
Amazon DynamoDB	Scalable NoSQL data storage
Amazon Cognito	Authentication and identity
Amazon CloudWatch	Monitoring and logging
🔧 Development Tools
Git
GitHub
VS Code
Android SDK
Gradle
Python Virtual Environment
pytest
HTTPX
🏗️ System Architecture
                    ┌─────────────────────┐
                    │     Android App     │
                    │   Kotlin / Compose  │
                    └──────────┬──────────┘
                               │
                         HTTPS / JSON
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Amazon API        │
                    │      Gateway        │
                    └──────────┬──────────┘
                               │
                    ┌──────────┴──────────┐
                    │                     │
                    ▼                     ▼
          ┌─────────────────┐    ┌─────────────────┐
          │ Amazon Cognito  │    │   AWS Lambda    │
          │ Authentication  │    │ FastAPI Backend │
          └─────────────────┘    └────────┬────────┘
                                          │
                         ┌────────────────┼────────────────┐
                         │                │                │
                         ▼                ▼                ▼
                    Game Results    Performance     Data Sync
                         │                │                │
                         └────────────────┼────────────────┘
                                          │
                                          ▼
                              ┌─────────────────────┐
                              │      DynamoDB       │
                              │    Cloud Database   │
                              └──────────┬──────────┘
                                         │
                                         ▼
                              ┌─────────────────────┐
                              │ Caregiver Dashboard │
                              └─────────────────────┘
📁 Project Structure
phoenix-forces-sih26003/
│
├── frontend/
│   │
│   ├── app/
│   │   ├── src/
│   │   │   └── main/
│   │   │       ├── java/
│   │   │       │   └── com/example/myapplication/
│   │   │       │
│   │   │       │   ├── data/
│   │   │       │   │   ├── local/
│   │   │       │   │   ├── preferences/
│   │   │       │   │   ├── remote/
│   │   │       │   │   └── repository/
│   │   │       │   │
│   │   │       │   ├── ui/
│   │   │       │   ├── utils/
│   │   │       │   ├── ArogyaApplication.kt
│   │   │       │   └── MainActivity.kt
│   │   │       │
│   │   │       └── AndroidManifest.xml
│   │   │
│   │   ├── build.gradle.kts
│   │   ├── settings.gradle.kts
│   │   ├── gradle.properties
│   │   └── gradlew
│   │
│   └── .gitignore
│
├── backend/
│   │
│   ├── app/
│   │   ├── core/
│   │   ├── db/
│   │   ├── routes/
│   │   └── schemas/
│   │
│   ├── requirements.txt
│   ├── .env.example
│   └── .gitignore
│
├── docs/
│
├── API_CONTRACT.md
├── README.md
└── .gitignore
🧪 Current Prototype

The current repository contains the working prototype and cloud backend integration.

Implemented
✅ Android application
✅ Elderly-focused user interface
✅ Patient / Caregiver role selection
✅ Cognitive Games Hub
✅ Memory Card Match
✅ Pattern Memory
✅ Attention & Concentration
✅ Daily Routine Recall
✅ Local Room database
✅ Game result storage
✅ Performance tracking
✅ Rule-based adaptive difficulty
✅ Offline-first data handling
✅ Cloud synchronization API
✅ FastAPI backend
✅ AWS Lambda deployment
✅ Amazon API Gateway
✅ DynamoDB integration
✅ Amazon Cognito authentication
✅ Caregiver monitoring interface
✅ Alerts and reminders architecture
🚀 Grand Finale Development Plan

The current prototype establishes the core application, offline architecture and cloud backend.

The Grand Finale development plan focuses on extending the platform with:

🤖 AI-Assisted Personalization

Develop a more advanced personalization layer using accumulated performance patterns to adapt:

Game selection
Difficulty
Session frequency
Cognitive domains
User-specific recommendations
🗣️ Multilingual Voice Interaction

Extend the interface toward:

Speech-to-text
Text-to-speech
Regional language interaction
Voice-assisted navigation
📊 Advanced Caregiver Analytics

Enhance the caregiver dashboard with:

Long-term activity trends
Domain-wise performance
Personalized progress insights
Alert prioritization
Activity consistency
🌏 Regional Content

Expand cognitive activities using:

Regional languages
Familiar objects
Local cultural references
Region-specific daily-life scenarios
🚀 Getting Started
Prerequisites
Android
Android SDK
JDK
Gradle
Android device or emulator
Backend
Python 3.x
AWS account
Configured AWS credentials
📱 Run the Android Application

Clone the repository:

git clone https://github.com/Phoenix-Forces/phoenix-forces-sih26003.git

Enter the frontend directory:

cd phoenix-forces-sih26003/frontend

Build the application:

./gradlew assembleDebug
Windows
gradlew.bat assembleDebug

The generated APK can be found under:

app/build/outputs/apk/debug/
⚙️ Run the Backend

Enter the backend directory:

cd phoenix-forces-sih26003/backend

Create a virtual environment:

python -m venv .venv
Windows
.venv\Scripts\activate
macOS / Linux
source .venv/bin/activate

Install dependencies:

pip install -r requirements.txt

Create your environment configuration from:

.env.example

Start the FastAPI server:

uvicorn app.main:app --reload

FastAPI automatically provides interactive API documentation through its Swagger interface.

🔐 Backend Configuration

The backend uses environment variables for configuration.

Example:

COGNITO_CLIENT_ID=your-cognito-client-id
COGNITO_USER_POOL_ID=your-cognito-user-pool-id
DYNAMODB_TABLE_NAME=your-dynamodb-table-name

Never commit AWS credentials, private keys or API secrets to GitHub.

🔌 API Overview

The backend provides APIs for major platform operations.

Module	Purpose
/auth	Authentication operations
/games	Cognitive game information
/game-results	Store cognitive game results
/analytics	Performance and analytics
/dashboard	Caregiver dashboard data
/alerts	Caregiver alerts
/reminders	Reminder management
/history	Patient activity history
/sync	Offline operation synchronization
/activity	Activity tracking
/voice	Voice-related backend functionality

Detailed API definitions are maintained in:

API_CONTRACT.md
📴 Offline Synchronization

Arogya Cognitive follows an offline-first architecture.

When the device is offline:

User Action
     ↓
Room Database
     ↓
Pending Sync Operation

When connectivity becomes available:

Pending Operations
       ↓
Synchronization API
       ↓
FastAPI Backend
       ↓
DynamoDB
       ↓
Sync Confirmation

This allows the application to continue core activities even when reliable internet connectivity is unavailable.

🎯 Adaptive Difficulty

The current prototype uses a performance-based adaptive baseline.

The system evaluates game performance and adjusts the difficulty level accordingly.

Game Result
    ↓
Score / Accuracy
    ↓
Performance Evaluation
    ↓
Difficulty Adjustment
    ↓
Next Game

The current implementation uses predefined performance rules.

The Grand Finale roadmap can extend this baseline into AI-assisted personalization using broader performance patterns.

🗄️ Data Model

The backend follows a scalable DynamoDB single-table design.

Core data categories include:

PATIENT
GAME
RESULT
SESSION
REMINDER
ALERT
SYNC
DIFFICULTY

Game results can contain:

Patient ID
Game type
Score
Correct answers
Incorrect answers
Time taken
Difficulty level
Timestamp
Cognitive domain
Accuracy
🔒 Security

Security is considered across both mobile and cloud layers.

Authentication
Amazon Cognito
Token-based authentication
Patient / caregiver role separation
Data Protection
DynamoDB cloud storage
Environment-based configuration
Secrets excluded from Git
Local database for offline operation
Cloud Architecture

The backend uses AWS serverless services to reduce infrastructure management requirements and support scalable deployment.

📊 Platform Flow
┌────────────┐
│    PLAY    │
└─────┬──────┘
      ↓
┌────────────┐
│  ANALYZE   │
└─────┬──────┘
      ↓
┌────────────┐
│   ADAPT    │
└─────┬──────┘
      ↓
┌────────────┐
│    SYNC    │
└─────┬──────┘
      ↓
┌────────────┐
│  MONITOR   │
└────────────┘
👥 Team — Phoenix Forces
Name	Role
Gagandeep G N	Team Lead & Backend Developer
Kiran Kumar K S	Frontend Developer
Sachin Goudar	Frontend Developer
Jeevan CD	Backend Developer
Mahi Jadhav	Research & Presentation
Dhruthi	Research & Presentation
🏆 Hackathon
Smart India Hackathon 2026

Problem Statement ID: SIH26003

Problem Statement:

AI-Based Cognitive Gaming and Memory Assistance Platform for Elderly Dementia Patients in the North Eastern Region (NER)

Team: Phoenix Forces

Institution: GM University

📌 Project Status

Status: Prototype / SIH Development

The repository contains the current Android frontend, backend services, offline data layer and AWS cloud integration developed for the SIH project.

Further development is planned for:

AI-assisted personalization
Multilingual voice interaction
Advanced caregiver analytics
Expanded regional content
Additional testing and refinement
⚠️ Disclaimer

Arogya Cognitive is designed as a cognitive engagement and support platform.

It is not intended to:

Diagnose dementia
Replace professional medical care
Provide clinical diagnosis
Replace prescribed treatment

The platform is intended to support cognitive activities, daily assistance and caregiver visibility.

📄 License

This project is developed as part of Smart India Hackathon 2026 by Phoenix Forces, GM University.

<p align="center">
🧠 Arogya Cognitive
PLAY • ANALYZE • ADAPT • SYNC • MONITOR

Phoenix Forces — Smart India Hackathon 2026

</p> ```
