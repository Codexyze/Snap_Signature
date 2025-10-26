# 🎨 Canvas Studio - Signature & Drawing App

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.04.01-green.svg)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-27-orange.svg)](https://developer.android.com/about/versions/oreo)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-35-brightgreen.svg)](https://developer.android.com/about/versions)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A modern, feature-rich **Android drawing and signature application** built with **Jetpack Compose** and **MVVM architecture**. Create beautiful drawings, signatures, and sketches with an intuitive interface, extensive color palettes, and seamless gallery integration.

---

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Installation](#-installation)
- [Usage](#-usage)
- [Permissions](#-permissions)
- [Build Configuration](#-build-configuration)
- [Key Components](#-key-components)
- [API Reference](#-api-reference)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

### 🖌️ Drawing Capabilities
- **Freehand Drawing**: Smooth, responsive drawing with real-time stroke rendering
- **Adjustable Brush Size**: Dynamic brush size from 1dp to 100dp with live preview
- **Eraser Tool**: Precision eraser with adjustable size
- **Undo Function**: Step-by-step undo for mistake correction
- **Clear Canvas**: Quick canvas reset functionality

### 🎨 Color System
- **160+ Colors**: Extensive color palette organized by categories
- **Quick Access Colors**: 20 frequently used colors for instant selection
- **Expandable Palette**: Categorized color sections:
  - Basic (6 colors)
  - Reds (14 colors)
  - Pinks (14 colors)
  - Purples (14 colors)
  - Blues (14 colors)
  - Greens (14 colors)
  - Oranges (14 colors)
  - Yellows (14 colors)
- **Real-time Color Preview**: Visual feedback on selected color
- **Smart Color Indicators**: Adaptive selection indicators based on color luminance

### 💾 Storage & Export
- **Gallery Integration**: Save drawings directly to device gallery
- **MediaStore API**: Android 10+ scoped storage support
- **Legacy Storage**: Android 9 and below compatibility
- **High-Quality Export**: PNG format with full resolution
- **Automatic Organization**: Saves to dedicated "CanvasStudio" folder
- **Permission Management**: Smart permission handling across Android versions

### 🎯 UI/UX Features
- **Material Design 3**: Modern, beautiful interface
- **Dynamic Theming**: Supports Android 12+ Material You
- **Animated Interactions**: Smooth transitions and visual feedback
- **Splash Screen**: Custom splash screen with branding
- **Responsive Layout**: Adapts to different screen sizes
- **Gradient Backgrounds**: Eye-catching visual design
- **Elevation & Shadows**: Depth-aware interface elements

---

## 🏗️ Architecture

This project follows **Clean Architecture** principles with **MVVM (Model-View-ViewModel)** pattern:

```
┌─────────────────────────────────────────┐
│           UI Layer (Compose)            │
│  MainActivity, Composables, Theme       │
└───────────────┬─────────────────────────┘
                │
┌───────────────▼─────────────────────────┐
│         ViewModel Layer                 │
│  DrawingViewModel, State Management     │
└───────────────┬─────────────────────────┘
                │
┌───────────────▼─────────────────────────┐
│         Repository Layer                │
│  DrawingRepository, Data Operations     │
└───────────────┬─────────────────────────┘
                │
┌───────────────▼─────────────────────────┐
│          Model Layer                    │
│  DrawStroke, SaveState, SaveResult      │
└─────────────────────────────────────────┘
```

### Architecture Benefits
- ✅ **Separation of Concerns**: Clear responsibility boundaries
- ✅ **Testability**: Easy unit and integration testing
- ✅ **Maintainability**: Modular, easy to update and extend
- ✅ **Scalability**: Simple to add new features
- ✅ **Reactive**: State-driven UI updates with Compose

---

## 🛠️ Tech Stack

### Core Technologies
- **Language**: [Kotlin 2.0.0](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Build System**: Gradle with Kotlin DSL
- **Min SDK**: 27 (Android 8.1 Oreo)
- **Target SDK**: 35 (Android 15)

### Jetpack Components
| Component | Version | Purpose |
|-----------|---------|---------|
| **Compose BOM** | 2024.04.01 | Compose library versions |
| **Material3** | Latest | Material Design 3 components |
| **Lifecycle** | 2.8.7 | Lifecycle-aware components |
| **ViewModel** | 2.8.7 | UI state management |
| **LiveData** | 2.8.7 | Observable data holder |
| **Activity Compose** | 1.10.0 | Activity integration |
| **Core KTX** | 1.15.0 | Kotlin extensions |
| **Core Splashscreen** | 1.0.0 | Splash screen API |

### Libraries
```kotlin
// Compose
androidx.compose.ui:ui
androidx.compose.material3:material3
androidx.compose.ui:ui-graphics
androidx.compose.material:material-icons-extended:1.7.8

// Lifecycle
androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7
androidx.lifecycle:lifecycle-runtime-compose:2.8.7
androidx.lifecycle:lifecycle-livedata-ktx:2.8.7

// Core
androidx.core:core-ktx:1.15.0
androidx.core:core-splashscreen:1.0.0
```

---

## 📁 Project Structure

```
CanvasJetpackcompose/
├── 📱 app/
│   ├── 📄 build.gradle.kts          # App-level build configuration
│   ├── 📄 proguard-rules.pro        # ProGuard rules
│   └── 📂 src/
│       ├── 📂 main/
│       │   ├── 📄 AndroidManifest.xml
│       │   ├── 📂 java/com/example/snapsignature/
│       │   │   ├── 📄 MainActivity.kt                    # Main activity & UI
│       │   │   ├── 📂 model/
│       │   │   │   └── 📄 DrawStroke.kt                 # Drawing data model
│       │   │   ├── 📂 viewmodel/
│       │   │   │   └── 📄 DrawingViewModel.kt           # State & business logic
│       │   │   ├── 📂 repository/
│       │   │   │   └── 📄 DrawingRepository.kt          # Data operations
│       │   │   ├── 📂 ui/theme/
│       │   │   │   ├── 📄 Color.kt                      # Color definitions
│       │   │   │   ├── 📄 Theme.kt                      # App theme
│       │   │   │   └── 📄 Type.kt                       # Typography
│       │   │   └── 📂 Screens/
│       │   │       ├── 📄 Sample.kt                     # Demo: Paint screen
│       │   │       ├── 📄 DrawLinee.kt                  # Demo: Line drawing
│       │   │       ├── 📄 Circle.kt                     # Demo: Circle drawing
│       │   │       └── 📄 AndroidBot.kt                 # Demo: Android bot
│       │   └── 📂 res/
│       │       ├── 📂 drawable/                         # App icons & images
│       │       ├── 📂 mipmap-*/                         # Launcher icons
│       │       ├── 📂 values/
│       │       │   ├── 📄 strings.xml                   # String resources
│       │       │   ├── 📄 colors.xml                    # Color resources
│       │       │   └── 📄 themes.xml                    # Theme resources
│       │       └── 📂 xml/
│       │           ├── 📄 backup_rules.xml              # Backup configuration
│       │           └── 📄 data_extraction_rules.xml     # Data extraction rules
│       ├── 📂 androidTest/                              # Instrumented tests
│       └── 📂 test/                                     # Unit tests
├── 📂 gradle/
│   ├── 📄 libs.versions.toml         # Dependency versions (Version Catalog)
│   └── 📂 wrapper/                   # Gradle wrapper files
├── 📄 build.gradle.kts               # Project-level build configuration
├── 📄 settings.gradle.kts            # Project settings
├── 📄 gradle.properties              # Gradle properties
├── 📄 gradlew                        # Gradle wrapper script (Unix)
├── 📄 gradlew.bat                    # Gradle wrapper script (Windows)
└── 📄 README.md                      # This file
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 11 or higher
- **Gradle**: 8.8.0 (included via wrapper)
- **Android SDK**: API 27 to 35

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/canvas-jetpack-compose.git
   cd canvas-jetpack-compose
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory
   - Click "OK"

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button (▶️) or press `Shift + F10`
   - Select your target device

### Building from Command Line

**Debug Build:**
```bash
./gradlew assembleDebug
```

**Release Build:**
```bash
./gradlew assembleRelease
```

**Install on Device:**
```bash
./gradlew installDebug
```

**Run Tests:**
```bash
./gradlew test
./gradlew connectedAndroidTest
```

---

## 💻 Usage

### Basic Drawing

1. **Start Drawing**
   - Open the app
   - Touch and drag on the white canvas to draw
   - The app responds in real-time to your touches

2. **Change Brush Size**
   - Use the slider under "Brush Size"
   - Range: 1dp to 100dp
   - Preview shows current size

3. **Select Colors**
   - Tap any color in the quick palette (20 colors)
   - Tap the expand button (▼) for more colors
   - Browse through categorized palettes

4. **Use Eraser**
   - Tap the "Eraser" button
   - Adjust eraser size with the slider
   - Draw to erase areas

5. **Undo Mistakes**
   - Tap the "Undo" button
   - Removes the last stroke
   - Can undo multiple times

6. **Clear Canvas**
   - Tap the trash icon (🗑️) in the top bar
   - Clears entire canvas instantly

7. **Save Drawing**
   - Tap the "Save" button
   - Grant storage permission if requested
   - Drawing saves to Gallery/CanvasStudio/

### Advanced Features

**Color Categories:**
- Basic: Grayscale colors
- Reds: 14 shades of red
- Pinks: 14 shades of pink
- Purples: 14 shades of purple
- Blues: 14 shades of blue
- Greens: 14 shades of green
- Oranges: 14 shades of orange
- Yellows: 14 shades of yellow

**Brush Modes:**
- **Drawing Mode**: Normal colored strokes
- **Eraser Mode**: White strokes to erase

---

## 🔐 Permissions

The app requires storage permissions to save drawings:

### Android 13+ (API 33+)
```xml
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```

### Android 10-12 (API 29-32)
- No permission needed (Scoped Storage)

### Android 9 and below (API 28-)
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### Permission Handling
The app automatically:
- ✅ Requests appropriate permissions based on Android version
- ✅ Explains why permissions are needed
- ✅ Provides fallback if permissions denied
- ✅ Shows user-friendly error messages

---

## ⚙️ Build Configuration

### App Configuration
```kotlin
android {
    namespace = "com.example.snapsignature"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.canvasjetpackcompose"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}
```

### Version Catalog (libs.versions.toml)
```toml
[versions]
agp = "8.8.0"
kotlin = "2.0.0"
coreKtx = "1.15.0"
lifecycleRuntimeKtx = "2.8.7"
activityCompose = "1.10.0"
composeBom = "2024.04.01"
```

---

## 🔑 Key Components

### 1. DrawingViewModel
**Location**: `app/src/main/java/.../viewmodel/DrawingViewModel.kt`

**Responsibilities:**
- Manages drawing state (strokes, colors, brush size)
- Handles user interactions (draw, undo, clear, save)
- Provides reactive state to UI
- Coordinates with repository for data operations

**Key Methods:**
```kotlin
fun startStroke(offset: Offset)           // Begin new stroke
fun addPointToStroke(offset: Offset)      // Add point during drawing
fun endStroke()                           // Complete stroke
fun undoLastStroke()                      // Remove last stroke
fun clearCanvas()                         // Clear all strokes
fun selectColor(color: Color)             // Change brush color
fun setStrokeWidth(width: Float)          // Change brush size
fun setEraserMode(enabled: Boolean)       // Toggle eraser
fun saveDrawing(densityScale: Float)      // Save to gallery
```

### 2. DrawingRepository
**Location**: `app/src/main/java/.../repository/DrawingRepository.kt`

**Responsibilities:**
- Converts strokes to bitmap
- Saves images to device storage
- Handles MediaStore integration
- Manages different Android versions

**Key Methods:**
```kotlin
suspend fun saveDrawingToGallery(
    strokes: List<DrawStroke>,
    width: Int,
    height: Int,
    backgroundColor: Color,
    densityScale: Float
): SaveResult
```

### 3. DrawStroke (Model)
**Location**: `app/src/main/java/.../model/DrawStroke.kt`

**Data Class:**
```kotlin
data class DrawStroke(
    val points: MutableList<Offset>,      // Stroke coordinates
    val color: Color,                      // Stroke color
    val strokeWidthDp: Float,              // Brush size
    val isEraser: Boolean                  // Eraser mode flag
)
```

### 4. MainActivity
**Location**: `app/src/main/java/.../MainActivity.kt`

**Responsibilities:**
- Main entry point
- Hosts Compose UI
- Manages splash screen
- Handles permissions

**Key Composables:**
```kotlin
@Composable
fun DrawingScreen()                       // Main screen

@Composable
fun EnhancedBrushSizeControl()           // Brush size slider

@Composable
fun EnhancedColorPaletteSection()        // Color picker

@Composable
fun EnhancedActionButtonsRow()           // Undo/Save buttons
```

---

## 📚 API Reference

### DrawingViewModel State

| Property | Type | Description |
|----------|------|-------------|
| `strokes` | `SnapshotStateList<DrawStroke>` | All completed strokes |
| `currentStroke` | `State<DrawStroke?>` | Currently drawing stroke |
| `selectedColor` | `State<Color>` | Current brush color |
| `strokeWidthDp` | `MutableFloatState` | Current brush size |
| `isEraser` | `State<Boolean>` | Eraser mode flag |
| `showExpandedColors` | `State<Boolean>` | Color palette expansion |
| `saveState` | `SharedFlow<SaveState>` | Save operation status |

### SaveState Sealed Class
```kotlin
sealed class SaveState {
    object Idle : SaveState()                    // No operation
    object Loading : SaveState()                 // Saving in progress
    data class Success(val message: String)      // Save successful
    data class Error(val message: String)        // Save failed
}
```

### SaveResult Sealed Class
```kotlin
sealed class SaveResult {
    data class Success(val message: String)      // Repository success
    data class Error(val message: String)        // Repository error
}
```

---

## 🎨 Customization

### Adding Custom Colors
Edit `MainActivity.kt` and modify the `colorCategories`:

```kotlin
ColorCategory(
    name = "My Colors",
    icon = Icons.Default.Palette,
    colors = listOf(
        Color(0xFFFF0000),
        Color(0xFF00FF00),
        // Add more colors...
    )
)
```

### Changing Brush Size Range
Modify the slider in `EnhancedBrushSizeControl`:

```kotlin
Slider(
    value = strokeWidth,
    onValueChange = onStrokeWidthChange,
    valueRange = 1f..200f,  // Change max value
    modifier = Modifier.fillMaxWidth()
)
```

### Custom Save Location
Edit `DrawingRepository.kt`:

```kotlin
put(MediaStore.MediaColumns.RELATIVE_PATH, 
    Environment.DIRECTORY_PICTURES + "/MyCustomFolder")
```

---

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Test Coverage
- ViewModel business logic
- Repository data operations
- Permission handling
- State management

---

## 🐛 Troubleshooting

### Common Issues

**Issue**: App crashes when saving
- **Solution**: Ensure storage permissions are granted
- Check Android version and permission requirements

**Issue**: Drawing is laggy
- **Solution**: Reduce canvas resolution or optimize stroke rendering
- Test on different devices

**Issue**: Colors not appearing correctly
- **Solution**: Check Material Theme configuration
- Verify Dynamic Color settings

**Issue**: Gradle sync fails
- **Solution**: Check internet connection
- Invalidate caches: `File > Invalidate Caches / Restart`
- Update Gradle wrapper: `./gradlew wrapper --gradle-version=8.8.0`

---

## 🔄 Version History

### Version 1.0.0 (Current)
- ✅ Initial release
- ✅ Basic drawing functionality
- ✅ Color palette with 160+ colors
- ✅ Brush size adjustment (1-100dp)
- ✅ Eraser tool
- ✅ Undo functionality
- ✅ Save to gallery
- ✅ Material Design 3 UI
- ✅ Android 8.1+ support

### Upcoming Features (Roadmap)
- 🔜 Redo functionality
- 🔜 Custom color picker
- 🔜 Drawing tools (shapes, text)
- 🔜 Layer support
- 🔜 Export formats (JPEG, SVG)
- 🔜 Drawing templates
- 🔜 Share functionality
- 🔜 Cloud sync

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

### Code Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Write unit tests for new features

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


---

## 🙏 Acknowledgments

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI toolkit
- [Material Design 3](https://m3.material.io/) - Design system
- [Android Developers](https://developer.android.com/) - Documentation and guides
- [Kotlin](https://kotlinlang.org/) - Programming language


