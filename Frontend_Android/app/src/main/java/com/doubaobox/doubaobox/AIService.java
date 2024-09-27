package com.doubaobox.doubaobox;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AIService {

    @POST("/ai/conversation")  // Endpoint of your Flask backend
    Call<AIResponse> getAIResponse(@Body AIRequest request);
}
