To run the app:
- Prerequisites
  - Android Studio: Ladybug (2024.2.1) or newer is required to support SDK 36.
  - Android SDK: * Compile SDK: 36 (Android 16)
  - Minimum SDK: 24 (Android 7.0 Nougat)
  - Java Development Kit (JDK): JDK 17 or higher (required for modern Gradle builds and Java 11 compatibility).
  - Kotlin: Version 2.0.0 or higher (standard for Jetpack Compose projects).
  - Jetpack Compose: Enabled (Ensure your Compose Compiler version matches your Kotlin version).
- Device Requirements
  - Physical Device/Emulator: Needs to run Android 7.0 (API 24) or higher.
  - For Best Experience: Use an emulator with API 36 to test the latest features and behaviors intended by the targetSdk.
- git clone https://github.com/aiva89/ubiquiti-test-repo.git
- Open Android Studio
  - Run gradle sync
  - Select your device and press green play button (Run)
    
Assumptions/tradeoffs
- "Cameras" line.id == "unifi-protect"
- "IoT" line.id == "unifi-led", "unifi-connect", "unifi-access", "smart-power"
- The UI was implemented using the free version of Figma. As Developer Mode was unavailable, dimensions and styling were interpreted using standard inspection and translated into Jetpack Compose values manually.

Libraries used:
- Retrofit for networking: It turns your REST API into a type-safe Kotlin interface, automating JSON parsing and eliminating boilerplate networking code.
- Coil for image loading: It is a lightweight, Kotlin-first image loader that uses Coroutines and OkHttp to handle caching and memory management with minimal setup.
- Jetpack Compose for UI: It uses a modern declarative approach to build native UIs with less code, making the interface reactive to state changes automatically.
