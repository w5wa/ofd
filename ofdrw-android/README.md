# OFDRW Android Viewer

An Android 15+ OFD document viewer app with bilingual (English / Arabic) support.

## Features

| Feature | Details |
|---------|---------|
| **OFD Rendering** | WebView-based rendering via [ofd.js](https://github.com/DLTech21/ofd.js) |
| **Languages** | English 🇬🇧 and Arabic 🇸🇦 (full RTL layout) |
| **File picker** | Picks `.ofd` files from any file manager or email attachment |
| **Dark mode** | Material 3 DayNight theme |
| **Min API** | 35 (Android 15) |
| **Architecture** | Works on ARM64 (arm64-v8a) — compatible with any modern Android device |

---

## Prerequisites

| Tool | Version |
|------|---------|
| Android Studio | Hedgehog (2023.1.1) or later |
| JDK | 17+ |
| Android SDK | API 35 (Android 15) |
| Gradle | 8.9 (bundled via wrapper) |

---

## Getting ofd.js

The viewer uses **ofd.js** to render OFD documents. You must download the bundle
separately and place it in the assets folder before building.

```bash
# Using npm (recommended)
cd ofdrw-android/app/src/main/assets/viewer/
npm pack ofd.js
# then extract ofd.umd.js and rename it to ofd.js
# OR use jsDelivr CDN (requires internet on first load):
curl -fsSL "https://cdn.jsdelivr.net/npm/ofd.js/dist/ofd.umd.js" -o ofd.js
```

Place the resulting `ofd.js` file at:

```
ofdrw-android/app/src/main/assets/viewer/ofd.js
```

> If `ofd.js` is absent, the app still launches but shows setup instructions instead of rendered pages.

---

## Build

### From the command line

```bash
cd ofdrw-android

# Debug APK (signed with debug key — ready to sideload)
./gradlew assembleDebug

# Release APK (unsigned — sign with your keystore before distributing)
./gradlew assembleRelease
```

Output APKs are placed in:

```
app/build/outputs/apk/debug/app-debug.apk
app/build/outputs/apk/release/app-release-unsigned.apk
```

### From Android Studio

1. Open **Android Studio** → **Open** → select the `ofdrw-android/` folder.
2. Let Gradle sync complete.
3. **Run ▶** to install on a connected device or emulator.

---

## Language / Locale

The app ships two locale configurations:

| Locale | File |
|--------|------|
| English (default) | `app/src/main/res/values/strings.xml` |
| Arabic | `app/src/main/res/values-ar/strings.xml` |

Android automatically switches to Arabic when the **system locale** (or the
**per-app language** setting on Android 13+) is set to Arabic. All layouts are
automatically mirrored for RTL because `android:supportsRtl="true"` is set in
the manifest.

---

## CI / CD

The workflow `.github/workflows/build-android.yml` builds and uploads APK
artifacts on every version tag push (`v*`).

---

## Project Structure

```
ofdrw-android/
├── app/
│   ├── build.gradle                # App module build file
│   ├── proguard-rules.pro          # R8/ProGuard rules
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   └── viewer/
│       │       ├── index.html      # WebView viewer page
│       │       └── ofd.js          # ← place ofd.js here (not bundled)
│       ├── java/org/ofdrw/android/
│       │   ├── MainActivity.kt     # File picker / home screen
│       │   ├── OfdViewerActivity.kt # WebView OFD viewer
│       │   ├── OfdBridge.kt        # JS bridge (passes OFD bytes to WebView)
│       │   └── AboutDialogFragment.kt
│       └── res/
│           ├── layout/             # UI layouts
│           ├── values/             # English strings, colours, themes
│           ├── values-ar/          # Arabic strings
│           ├── values-night/       # Dark-theme colours
│           ├── menu/               # Action bar menu
│           ├── xml/                # locales_config.xml
│           └── drawable/           # Vector icons
├── build.gradle                    # Root build file
├── settings.gradle
├── gradle.properties
└── gradle/
    ├── libs.versions.toml          # Version catalog
    └── wrapper/
        └── gradle-wrapper.properties
```
