# AudileModded

A modded fork of the open source Audile music recognition app with additional features like auto-recognize, Visualizer API support, and Shazam-style notifications. All data is saved locally.

[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80">](https://play.google.com/store/apps/details?id=com.mrsep.musicrecognizer)
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/com.mrsep.musicrecognizer)

**Download APK here:** [audilemodded.apk](releases/audilemodded.apk)

## Screenshots

<!-- Add screenshots here -->

## Get from source

```bash
git clone https://gitlab.com/bearincrypto1/audilemodded.git
```

## Fork Features

- **Auto-Recognize** - Quick Settings tile for continuous background recognition
- **Visualizer API** - Alternative audio capture using waveform analysis
- **Permanent Recognition Pop-up** - Shazam-style stacked notifications
- **Timestamp Display** - Shows recognition time in library
- **Custom Icon** - Black/white themed app icon

## Project Structure

```
com.mrsep.musicrecognizer/
├── FORKEDITS/             # All fork-specific code
│   ├── AutoRecognitionService.kt      # Background recognition service
│   ├── AutoRecognitionTileService.kt  # Quick Settings tile
│   ├── ForkEditsScreen.kt             # Fork settings UI
│   ├── ForkEditsViewModel.kt          # Fork settings logic
│   └── forkchanges.md                 # Complete changelog
├── presentation/          # Main app navigation & UI
├── feature/               # Feature modules (recognition, library, etc)
├── core/                  # Core modules (audio, data, domain, etc)
└── build-logic/           # Gradle convention plugins
```

## License

This project is licensed under the GPL-3.0 License - see the [LICENSE](LICENSE.md) file for details.

## Credits

Based on [Audile](https://github.com/aleksey-saenko/MusicRecognizer) by aleksey-saenko.
