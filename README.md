# 🚀 TodoHabitFocus

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-2024.11.00-green.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/Dagger_Hilt-2.57-orange.svg?style=flat&logo=dagger)](https://dagger.dev/hilt/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean_MVVM-red.svg?style=flat)](#architecture-overview)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**TodoHabitFocus** is a high-performance, modern productivity suite designed for Android. It seamlessly integrates task management, habit tracking, and deep-focus Pomodoro sessions into a single, cohesive experience. Built with a focus on scalability, offline-first reliability, and a premium user interface.

---

## ✨ Features

### 📝 Task Management
*   **Smart Tasks:** Create, edit, and organize tasks with high-priority labels.
*   **Deadlines:** Integrated due dates and timely reminder notifications.
*   **Productivity Flow:** Quickly mark tasks as complete and track your daily output.

### 🔥 Habit Tracking
*   **Streak System:** Build long-term consistency with visual streak counters.
*   **Flexible Scheduling:** Support for daily and weekly recurring habits.
*   **Visual Progress:** Calendar-based tracking to visualize your consistency over time.

### ⏱️ Focus & Pomodoro
*   **Deep Work Timer:** Configurable Pomodoro timer to eliminate distractions.
*   **Session Management:** Automated break timers (Short/Long) to maintain mental energy.
*   **Focus History:** Detailed logs of your concentration sessions.

### 📊 Advanced Analytics
*   **Insights:** Weekly productivity trends and task completion velocity.
*   **Habit Metrics:** Percentage-based consistency tracking for every habit.
*   **Focus Stats:** Visualizing total deep-work hours and session efficiency.

---

## 🛠 Tech Stack & Libraries

*   **Language:** [Kotlin](https://kotlinlang.org/) - 100% Type-safe and modern.
*   **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) - Declarative UI with Material 3.
*   **Asynchronous:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html) - Reactive data streams.
*   **Dependency Injection:** [Hilt](https://dagger.dev/hilt/) - Standardized DI for Android.
*   **Local Database:** [Room](https://developer.android.com/training/data-storage/room) - Robust offline-first data persistence.
*   **Cloud Backend:** [Supabase](https://supabase.com/) - Real-time sync and authentication.
*   **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) - Type-safe navigation between features.
*   **Background Tasks:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Reliable background processing for reminders.
*   **Image Loading:** [Coil](https://coil-kt.github.io/coil/) - Modern image loading for Compose.

---

## 🏗 Architecture Overview

The project follows **Clean Architecture** principles combined with **MVVM** (Model-View-ViewModel) and a **Multi-Module** strategy.

### Layer Separation
1.  **Domain Layer:** Contains pure Kotlin business logic (Use Cases) and Entity models. No dependencies on Android frameworks.
2.  **Data Layer:** Implementation of repositories, handling data sources (Room + Supabase).
3.  **Presentation Layer:** Compose-based UI and ViewModels that hold state and interact with Use Cases.

### Module Structure
```text
├── app                      # Application entry point & Navigation
├── core                     # Common utilities and base classes
├── core-ui                  # Reusable UI components & Theme
├── core-designsystem        # Design tokens, colors, and typography
├── core-database            # Room DB configuration and DAOs
├── core-network             # Supabase client and networking logic
├── core-notification       # Notification and WorkManager setup
├── feature-auth             # Login, Registration, and Password Recovery
├── feature-onboarding       # Welcome flow and setup
├── feature-home             # Premium Dashboard & Overview
├── feature-task             # Task CRUD and management
├── feature-habit            # Habit tracking and streaks
├── feature-focus            # Pomodoro timer logic
├── feature-analytics        # Charts and productivity metrics
└── feature-settings         # User preferences and account management
```

---

## 📸 Screenshots

| Onboarding | Dashboard | Task Management |
| :---: | :---: | :---: |
| ![Onboarding](https://via.placeholder.com/300x600?text=Onboarding+Screen) | ![Dashboard](https://via.placeholder.com/300x600?text=Dashboard+Screen) | ![Tasks](https://via.placeholder.com/300x600?text=Task+Screen) |

| Habit Tracker | Focus Timer | Performance Analytics |
| :---: | :---: | :---: |
| ![Habits](https://via.placeholder.com/300x600?text=Habit+Screen) | ![Focus](https://via.placeholder.com/300x600?text=Focus+Screen) | ![Analytics](https://via.placeholder.com/300x600?text=Analytics+Screen) |

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/TodoHabitFocus.git
```

### 2. Configure Supabase
1. Create a project on [Supabase.com](https://supabase.com/).
2. Obtain your `SUPABASE_URL` and `SUPABASE_ANON_KEY`.
3. Create a `local.properties` file in the root directory:
```properties
supabase.url=YOUR_SUPABASE_URL
supabase.key=YOUR_SUPABASE_ANON_KEY
```

### 3. Build & Run
1. Open the project in **Android Studio (Ladybug or newer)**.
2. Perform a **Gradle Sync**.
3. Select the `app` module and click **Run**.

---

## 📱 Build Instructions

### Generate APK
1. Navigate to `Build` > `Build Bundle(s) / APK(s)` > `Build APK(s)`.
2. The APK will be generated at `app/build/outputs/apk/debug/`.

### Run Unit Tests
```bash
./gradlew test
```

---

## 🛣 Future Roadmap
- [ ] AI-powered task suggestions.
- [ ] Shared habit challenges with friends.
- [ ] Desktop (Kotlin Multiplatform) support.
- [ ] Integration with Google Calendar.

---

## 🤝 Contribution Guide
Contributions are welcome! Please follow these steps:
1. Fork the project.
2. Create a Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.

---

## 👨‍💻 Developer
**Your Name**  
[GitHub](https://github.com/yourusername) | [LinkedIn](https://linkedin.com/in/yourusername) | [Portfolio](https://yourportfolio.com)
