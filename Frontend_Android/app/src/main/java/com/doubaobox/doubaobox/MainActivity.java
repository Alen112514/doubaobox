package com.doubaobox.doubaobox;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_VOICE_INPUT = 1;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 100;
    private Retrofit retrofit;
    private AIService aiService;
    private Button buttonStart;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request microphone permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        }

        buttonStart = findViewById(R.id.StartButton);

        // Initialize Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")  // Replace with actual backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the AIService
        aiService = retrofit.create(AIService.class);

        // Initialize TextToSpeech for responding to user
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });

        // Start conversation when button is clicked
        buttonStart.setOnClickListener(v -> startVoiceInput());
    }

    // Capture voice input from the user
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        try {
            startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
        } catch (Exception e) {
            Log.e("Error", "Voice recognition not supported", e);
        }
    }

    // Handle the result of the voice input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_VOICE_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String userInput = result.get(0);  // Get the user's spoken text
                sendInputToBackend(userInput);     // Send input to backend for AI response
            }
        }
    }

    // Send user's spoken input to the Flask backend
    private void sendInputToBackend(String userInput) {
        AIRequest request = new AIRequest(userInput);

        // Make the network call to send input and receive AI response
        aiService.getAIResponse(request).enqueue(new Callback<AIResponse>() {
            @Override
            public void onResponse(Call<AIResponse> call, Response<AIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String aiResponse = response.body().getResponse();
                    speakResponse(aiResponse);  // Convert AI response to speech
                }
            }

            @Override
            public void onFailure(Call<AIResponse> call, Throwable t) {
                Log.e("Error", "Failed to communicate with server", t);
                Toast.makeText(MainActivity.this, "Communication error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Use Text-to-Speech to speak the AI response to the user
    private void speakResponse(String response) {
        textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        // Shutdown TextToSpeech when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "RECORD_AUDIO permission granted");
            } else {
                Log.d("MainActivity", "RECORD_AUDIO permission denied");
            }
        }
    }
}
