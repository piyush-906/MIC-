# MIC-BETA ANDROID (starter project)

Native Android Studio starter based on the uploaded MIC-BETA V20 audio-library UI and controls.

## Current implementation
- Dark MIC-BETA interface
- Import audio through Android Storage Access Framework
- Local audio playback
- Royal Clear and Loud Mic playback presets
- Device-supported Android Equalizer, BassBoost and LoudnessEnhancer effects on this app's playback session

## Important limitation
This project does **not** inject audio into WhatsApp or DualSpace's microphone. Standard third-party Android apps cannot generally replace another app's microphone input. The original V20 injector depends on Chrome/WebRTC APIs (`getUserMedia`, `RTCPeerConnection`) and cannot be directly carried over to native Android. Do not interpret the local playback effects as WhatsApp outgoing-mic processing.

## Build
Open this folder in Android Studio (Giraffe or newer), allow Gradle sync, then Build > Build Bundle(s) / APK(s) > Build APK(s). The project uses Android Gradle Plugin 8.7.3, compile SDK 35, and min SDK 26. A compatible Android SDK and JDK 17 are required.

## Build APK using only an Android phone
See `BUILD-ON-PHONE-BN.txt`. A GitHub Actions workflow is included at `.github/workflows/build-apk.yml`; it builds the debug APK on GitHub's cloud runner, so no PC is needed.
