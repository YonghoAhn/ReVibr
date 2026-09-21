# Re:Vibr

Historical Android accessibility project focused on **Braille-oriented text input/output and messaging through touch, vibration, speech, and gestures**.

Re:Vibr was developed in 2017–2018 as an experimental mobile interface for users who could benefit from non-visual interaction with SMS/MMS and contacts.

## Features

- six-dot Braille input through gesture sequences
- Hangul, English, and numeric Braille conversion
- Braille-to-vibration output
- text-to-speech feedback
- speech-recognition experiments
- SMS sending and SMS/MMS reading
- contact browsing and search
- gesture-driven navigation
- background message monitoring and service restart logic

## Project structure

The repository contains the full Android application source, including:

- Activities for send / receive / contacts / options
- SMS and MMS parsing utilities
- Braille input, output, and conversion logic
- Hangul composition utilities
- TTS and vibration helpers
- broadcast receivers and background service code
- Android resources and Gradle project files

## Historical status

This repository is preserved as a **historical project** and is not actively maintained.

The project targets an old Android toolchain (compile SDK 26 / target SDK 23 era) and uses APIs and support libraries that predate current Android requirements. Building it today will require modernization.

## Security note

No reusable private key or signing keystore is intentionally included in the current tree. Local Android/Firebase configuration and keystore files are ignored.

The repository has a long public history from 2017–2018. Any credentials ever used with historical external services should be treated as expired/revoked rather than reused.
