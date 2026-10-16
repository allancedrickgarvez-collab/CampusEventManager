# CampusEventManager

An Android app for managing campus events, backed by Firebase (Auth, Firestore,
Storage, Cloud Messaging) with a small set of Node.js Cloud Functions.

---

## Requirements

| Tool | Version |
| --- | --- |
| Android Studio | latest stable |
| JDK | 11 or newer (Android Studio's bundled JDK works) |
| Android SDK Platform | **36** (`compileSdk = 36`) |
| Minimum device | Android 7.0 / API 24 |
| Node.js | **18** (only needed for `functions/`) |

---

## First-time setup

1. **Clone the repo**

   ```bash
   git clone <repo-url>
   cd CampusEventManager
   ```

2. **Open the project in Android Studio**
   `File > Open` and select the project root, then let Gradle sync.

3. **Let Android Studio generate `local.properties`**
   This file holds the path to *your* Android SDK and is intentionally not
   committed. Android Studio creates it automatically on the first sync.
   If you need to make it by hand, use forward slashes and escape the colon:

   ```properties
   sdk.dir=C\:\\Users\\<you>\\AppData\\Local\\Android\\Sdk
   ```

4. **Install the Cloud Functions dependencies** (only if you work on `functions/`)

   ```bash
   cd functions
   npm install
   ```

5. **Build and run** the `app` configuration on an emulator or device.

---

## Deliberately not in this repo

These are machine-specific or generated, and are listed in `.gitignore`.
**Do not commit them** — if one shows up in `git status`, it is a mistake.

| Path | Why |
| --- | --- |
| `local.properties` | Contains your personal Android SDK path |
| `functions/node_modules/` | Recreated with `npm install` |
| `build/`, `.gradle/`, `.kotlin/` | Gradle caches and build output |
| `.idea/` | Editor state, differs per machine |
| `*.apk`, `*.aab`, `app/release/` | Built artifacts |
| `*.jks`, `*.keystore`, `keystore.properties` | **Signing keys — never commit** |
| `serviceAccountKey.json`, `.env` | **Firebase / server secrets — never commit** |

---

## Firebase notes

* `app/google-services.json` **is** committed on purpose, so every member can
  build against the shared Firebase project straight after cloning.
* The API key inside it is not a confidential secret: it is embedded in every
  built APK and is protected by the package name + signing SHA-1 restrictions
  configured in the Google Cloud console.
* Never commit a **service account private key** (`serviceAccountKey.json`) or a
  release **keystore**. Those are real secrets.

---

## Project layout

```
CampusEventManager/
├── app/                          Android application module
│   ├── src/main/java/
│   │   ├── com/example/campuseventmanager/   Activities & adapters
│   │   ├── services/FirebaseService.kt       Firebase data access layer
│   │   └── ui/                               Login, Register, dashboards
│   ├── src/main/res/             Layouts, drawables, animations
│   └── build.gradle.kts
├── functions/                    Firebase Cloud Functions (Node 18)
│   ├── index.js
│   └── package.json
├── gradle/libs.versions.toml     Version catalog (dependency versions)
├── build.gradle.kts              Root build script
├── settings.gradle.kts           Module list
└── firebase.json                 Firebase project config
```

Dependency versions are centralised in `gradle/libs.versions.toml` — change
versions there rather than in the module build files.

---

## Working as a team

```bash
git pull                     # get the latest before you start
git checkout -b my-feature   # branch for your change
# ... make your changes ...
git add .
git commit -m "Describe what you changed"
git push -u origin my-feature
```

Then open a Pull Request on GitHub so someone can review before it lands on
`main`. Please avoid pushing straight to `main`.
