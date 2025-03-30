# 🎙️ Android Voice AI Assistant App

This project is a mobile application built with **Android (Java)** and powered by a **Flask backend** that connects to the **Volcengine Ark API** for AI-based voice interaction.

---

## 🧠 Overview

The app captures a user's voice input, converts it into text, sends it to an AI model via a Flask API, and then receives and plays the AI’s response.

---

## 📱 Android Frontend

### Features

- 🎤 Voice input via `RecognizerIntent`
- 🔁 Communication with Flask API using Retrofit
- 🔊 Text-to-speech output for AI response
- ☁️ Internet + Audio permission handling

### Key Tech

- Java
- Retrofit2
- Android Speech API
- Text-to-Speech
- Min SDK: `?`, Target SDK: 31

### Main Files

- `MainActivity.java` — Main screen logic
- `AIService.java` — Retrofit API interface
- `AIRequest.java` / `AIResponse.java` — Request/response model
- `AndroidManifest.xml` — Includes `INTERNET` and `RECORD_AUDIO` permissions

---

## 🔙 Flask Backend

### Overview

A lightweight server using Flask that acts as a middleware between the mobile app and the Volcengine AI API.

### Dependencies

Make sure to install:

```bash
pip install flask flask-cors volcengine-sdk-python
```

### Environment Variables

Create a `.env` file with:

```
ARK_API_KEY=your_volcengine_api_key
```

### Running the Server

```bash
python app.py
```

- `POST /ai/conversation` — Receives JSON input from mobile, forwards to Ark, returns result.

---

## 🔗 API Contract

### Request from Android

```json
{
  "input": "What's the weather today?"
}
```

### Response from Flask

```json
{
  "output": "Today's weather is sunny with a high of 25°C."
}
```

---

## 🚀 Getting Started

### Backend

```bash
cd backend-folder/
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python app.py
```

### Android Studio Setup

1. Open `Frontend_Android` in Android Studio.
2. Ensure your device/emulator has internet access.
3. Update the Retrofit base URL in `AIService.java` to point to your backend.
4. Run the app.

---

## 📌 Future Improvements

- [ ] Add error handling in both frontend/backend
- [ ] Deploy backend using Docker or a cloud platform
- [ ] Add conversation history
- [ ] Use streaming instead of single-response requests

---

## 🧑‍💻 Author
https://github.com/Alen112514


---

