# raviserene_fyp

Android ride-sharing (FYP) app prototype — connect drivers and passengers for shared rides.

Project objectives
- Allow users to offer rides (drivers) or request rides (passengers).
- Post and browse travel plans with filters and preferences (gender, smoking, pets, music).
- Firebase-backed real-time storage; map integration for routing; ratings & reviews.

Tech stack
- Android (Kotlin)
- Firebase: Authentication, Cloud Firestore, Cloud Messaging
- Google Maps / Location Services
- Jetpack components: Navigation, ViewModel, LiveData
- Material Design Components

Minimum Viable Product (MVP) — first slice
- Authentication (Email + Google)
- Profile & role selection (Driver / Rider)
- Post Trip (Driver)
- Browse Trips (Rider) with basic filters
- Data stored in Firestore

Getting started (local)
1. Clone repository.
2. Open in Android Studio (recommended).
3. Create a Firebase project and add an Android app. Download google-services.json and place it in app/.
4. Enable Email & Google sign-in in Firebase Console.
5. Build & run on device or emulator.

Project structure (high-level)
- app/src/main/java/com/raviserene/fyp/... : activities, fragments, viewmodels
- app/src/main/res/layout : layouts
- firebase/ : sample Firestore rules (firestore.rules)
- docs/ : design docs, reports, demo instructions

Roadmap (high level)
- Sprint 1: Auth + profile + role selection, post & browse trips
- Sprint 2: Matching algorithm + filters
- Sprint 3: Maps & pickup routing
- Sprint 4: Chat/notifications + ratings & reviews
- Final: Polish, documentation, demo APK & report

Branching & workflow
- main — stable releases / deliverable builds
- dev — integration branch for in-progress work
- feat/auth, feat/post-trip, feat/browse-trips, feat/maps, feat/chat — feature branches

Contact
- Owner: serenentu