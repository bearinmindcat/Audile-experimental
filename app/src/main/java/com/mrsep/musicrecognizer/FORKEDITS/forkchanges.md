# FORK EDITS - Complete Change Log
This document tracks all modifications made to the Audile app in this fork.

## Files in FORKEDITS Folder
`ForkEditsScreen.kt` - Compose UI screen for the "FORK EDITS" settings tab with toggle switches
`ForkEditsViewModel.kt` - ViewModel that provides access to fork-specific preferences
`AutoRecognitionService.kt` - Foreground service for continuous song recognition (Auto Shazam-style)
`AutoRecognitionTileService.kt` - Quick Settings tile service for toggling auto-recognition on/off
`forkchanges.md` - This documentation file

## App Icon Changes
### 1. Launcher Background - Blue to Black
File: `core/ui/src/main/res/drawable/ic_launcher_background.xml`
```xml
<!-- Before -->
android:startColor="#002A6B"
android:endColor="#416E91"

<!-- After -->
android:startColor="#000000"
android:endColor="#1A1A1A"
```

### 2. Launcher Foreground (Microphone) - Yellow to White
Files:
- `core/ui/src/main/res/color/gradient_ic_launcher_foreground.xml`
- `core/ui/src/debug/res/color/gradient_ic_launcher_foreground.xml`
```xml
<!-- Before (main had white, debug had yellow) -->
android:startColor="#FFF3CD"
android:endColor="#FFE94B"

<!-- After (both are now white) -->
android:startColor="#FFFFFF"
android:endColor="#FFFFFF"
```

## Navigation Changes
### 3. Added "FORK EDITS" Navigation Tab
File: `app/src/main/java/com/mrsep/musicrecognizer/presentation/TopLevelDestination.kt`
```kotlin
ForkEdits(
    route = "nav_graph_fork_edits",
    titleResId = StringsR.string.fork_edits,
    selectedIconResId = UiR.drawable.outline_local_dining_24,
    unselectedIconResId = UiR.drawable.outline_local_dining_24
)
```

### 4. Added Fork Edits Navigation Route
File:`app/src/main/java/com/mrsep/musicrecognizer/presentation/AppNavigation.kt`
```kotlin
navigation(
    startDestination = ForkEditsScreen.ROUTE,
    route = TopLevelDestination.ForkEdits.route
) {
    forkEditsScreen()
}
```

### 5. Added Fork & Knife Icon
File: `core/ui/src/main/res/drawable/outline_local_dining_24.xml`
Material Design "local_dining" icon for the navigation tab.
## String Resources Added
File: `core/strings/src/main/res/values/strings.xml`
```xml
<!-- Navigation -->
<string name="fork_edits">FORK EDITS</string>

<!-- Audio capture modes -->
<string name="audio_capture_mode_auto_device_mic">Automatic (Device -> Mic)</string>
<string name="audio_capture_mode_visualizer">Visualizer API</string>
<string name="audio_capture_mode_auto_visualizer_mic">Automatic (Visualizer -> Mic)</string>

<!-- Auto-recognize feature -->
<string name="auto_recognize_notification_channel_name">Auto-recognition service</string>
<string name="auto_recognize_notification_channel_desc">Continuous background music recognition</string>
<string name="auto_recognize_notification_title">Auto-recognition active</string>
<string name="auto_recognize_notification_text">Listening for songs in the background</string>
<string name="auto_recognize_tile_active">Auto recognize ON</string>
<string name="auto_recognize_tile_inactive">Auto recognize</string>
<string name="stop">Stop</string>
```

## Preferences/DataStore Changes
### 6. Added New Preference Fields
File: `core/datastore/src/main/proto/user_preferences_proto.proto`
```proto
bool show_timestamp_format_in_library = 48;
bool show_recognition_popup = 49;
bool auto_recognize_enabled = 50;
```

### 7. Added Domain Model Fields
File: `core/domain/src/main/java/.../UserPreferences.kt`
```kotlin
val showTimestampFormatInLibrary: Boolean
val showRecognitionPopup: Boolean
val autoRecognizeEnabled: Boolean
```

### 8. Added Repository Methods
File: `core/domain/src/main/java/.../PreferencesRepository.kt`
```kotlin
suspend fun setShowTimestampFormatInLibrary(value: Boolean)
suspend fun setShowRecognitionPopup(value: Boolean)
suspend fun setAutoRecognizeEnabled(value: Boolean)
```

## Audio Capture Mode Changes
### 9. Added Visualizer and AutoVisualizerMic Modes
File: `core/domain/src/main/java/.../AudioCaptureMode.kt`
```kotlin
enum class AudioCaptureMode {
    Microphone,
    Device,
    Auto,
    Visualizer,        // NEW - Uses Visualizer API
    AutoVisualizerMic  // NEW - Tries Visualizer first, falls back to Mic
}
```

### 10. Updated All When Expressions
Files updated to handle new enum values:
- `feature/recognition/src/main/java/.../RecognitionScreen.kt`
- `feature/recognition/src/main/java/.../service/RecognitionControlActivity.kt`
- `feature/recognition/src/main/java/.../service/RecognitionControlService.kt`
- `feature/recognition/src/main/java/.../widget/RecognitionWidget.kt`

### 11. Conditional Dropdown Display
File: `feature/preferences/src/main/java/.../AudioSourceDialog.kt`
- When Visualizer API is OFF: Shows Microphone, Device, Automatic
- When Visualizer API is ON: Shows all 5 options including Visualizer modes
- "Automatic" renamed to "Automatic (Device -> Mic)" when Visualizer is enabled
### 12. Auto-Reset on Visualizer Disable
File: `app/src/main/java/.../FORKEDITS/ForkEditsViewModel.kt`
When Visualizer API toggle is turned OFF:
- Default mode resets to Microphone (if was Visualizer or AutoVisualizerMic)
- Button long press mode resets to Auto (if was Visualizer or AutoVisualizerMic)

## Notification Changes
### 13. Shazam-Style Result Notifications
File: `feature/recognition/src/main/java/.../service/ResultNotificationHelper.kt`
Changed from BigPictureStyle to compact format:
- Title: Song name
- Text: Artist name
- Large icon: Album artwork thumbnail
- Expanded: Artist + Album (Year)

## Auto-Recognize Feature
### 14. Auto-Recognition Service
File: `app/src/main/java/.../FORKEDITS/AutoRecognitionService.kt`
- Foreground service for continuous recognition
- Recognizes songs every 10 seconds
- Duplicate detection (same song won't re-notify)
- Uses Visualizer API if enabled, otherwise Microphone
- Swipe-to-dismiss stops the service
- Simple notification: Title, Artist, Artwork (no expanded text)

### 15. Quick Settings Tile
File: `app/src/main/java/.../FORKEDITS/AutoRecognitionTileService.kt`
- Tile label: "Auto recognize" / "Auto recognize ON"
- Toggles AutoRecognitionService on/off
- Updates preference state

### 16. Auto-Recognize Icon
File: `core/ui/src/main/res/drawable/ic_auto_recognize.xml`
Custom icon with:
- Outer circle: thin stroke (0.5)
- Inner circle: medium stroke (0.85)
- Microphone: scaled 0.7x in center

### 17. AndroidManifest Services
File: `app/src/main/AndroidManifest.xml`
```xml
<service
    android:name=".forkedits.AutoRecognitionService"
    android:exported="false"
    android:foregroundServiceType="microphone" />

<service
    android:name=".forkedits.AutoRecognitionTileService"
    android:exported="true"
    android:icon="@drawable/ic_auto_recognize"
    android:label="@string/auto_recognize_tile_inactive"
    android:permission="android.permission.BIND_QUICK_SETTINGS_TILE">
    ...
</service>
```

## FORK EDITS Screen Features
### Visualizer API Toggle
- Title: "Visualizer API"
- Subtitle: "Uses waveform to determine song"
- Effect: Enables Visualizer-based audio capture modes
### Timestamp Toggle
- Title: "Timestamp"
- Subtitle: "Displays timestamp in library"
- Format: `YYYY-MM-DD-HH-MM-SS`
### Recognition Pop-up Toggle
- Title: "Permanent recognition pop-up"
- Subtitle: "Recognized songs appear in notification bar"
- Shows: Song title, artist, album artwork
### Auto Recognize Song
- Title: "Auto recognize song"
- Subtitle: "Icon in quick settings, high usage"
- Status: "ALWAYS ENABLED" (controlled via Quick Settings tile)

## Build Configuration Changes
### 18. Downgraded Android Gradle Plugin
File: `gradle/libs.versions.toml`
```toml
# Before
androidGradlePlugin = "8.13.2"
androidTools = "31.13.2"

# After
androidGradlePlugin = "8.12.2"
androidTools = "31.12.2"
```

### 19. Increased Gradle Heap Size
File: `gradle.properties`
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

## How It All Works
### Audio Capture Flow
1. User selects audio capture mode (Mic, Device, Visualizer, Auto variants)
2. AudioRecordingControllerFactory creates appropriate controller
3. Audio samples sent to RecognitionInteractor
4. Fingerprint generated and sent to recognition provider (Shazam/AudD/ACRCloud)
5. Result stored in database and shown via notification
### Auto-Recognize Flow
1. User enables via Quick Settings tile
2. AutoRecognitionService starts as foreground service
3. Every 10 seconds: capture audio → recognize → notify (if new song)
4. Duplicate detection prevents re-notifying same song
5. Swipe notification or tap Stop to disable
## Icon Sources
- Material Design Icons: https://fonts.google.com/icons
- Custom ic_auto_recognize.xml: Hand-crafted vector with circles + microphone
- 