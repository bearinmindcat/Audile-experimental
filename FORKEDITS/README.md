# Fork Edits Documentation

## Main Source Location

All Fork Edits source code and detailed documentation is now located at:

```
app/src/main/java/com/mrsep/musicrecognizer/forkedits/
```

See the README.md in that folder for complete documentation of all changes.

## Quick Reference - Modified Files

| File | Change |
|------|--------|
| `gradle/libs.versions.toml` | Downgraded AGP 8.13.2 → 8.12.2 |
| `core/ui/src/main/res/drawable/ic_launcher_background.xml` | Blue → Black icon |
| `core/ui/src/main/res/drawable/outline_local_dining_24.xml` | New fork icon |
| `core/strings/src/main/res/values/strings.xml` | Added "Fork Edits" string |
| `app/.../presentation/TopLevelDestination.kt` | Added ForkEdits tab |
| `app/.../presentation/AppNavigation.kt` | Added navigation route |
