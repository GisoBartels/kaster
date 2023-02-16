Password Kaster is a stateless password manager based on the concept of [Spectre App](https://spectre.app).


This project serves as practical real-world example to try out the latest technologies and development techniques and
to create an opinionated approach how modern UI development could look like.

### Featured techniques and technologies
* [TDD / BDD](https://dannorth.net/introducing-bdd/) on mobile & multiplatform with super fast [subcutaneous tests](https://www.ministryoftesting.com/articles/8745e4ec)
* Non-flaky, isolated and fast UI integration tests to complement the subcutaneous tests
* Kotlin Multiplatform with [Compose Multiplatform](https://github.com/JetBrains/compose-jb)
  * Android
  * Desktop
  * _coming soon™_ iOS
  * _coming later_ Web
* Clean [UDF](https://developer.android.com/jetpack/compose/architecture#udf) with Jetpack Compose
  * View model output is pure state, no one-shot events
  * Single input for all view events
* [Showcasing](https://github.com/airbnb/Showkase) UI elements with different device configurations
* [Snapshot testing](https://github.com/cashapp/paparazzi) without Android emulator
