package com.example.programandroid;
/*
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeScreen extends AppCompatActivity {

    private String currentActivities;
    private String futureActivities;
    private String userMessage;
    private TextView welcomeTextView;
    private TextView currentActivitiesTextView;
    private TextView futureActivitiesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        welcomeTextView = findViewById(R.id.WelcomeText); // Свързване с елемента от XML
        currentActivitiesTextView = findViewById(R.id.current_activities_text);
        futureActivitiesTextView = findViewById(R.id.future_activities_text);

        currentActivitiesTextView.setSelected(true);
        futureActivitiesTextView.setSelected(true);

        // Взимаме запазения токен
        TokenManager tokenManager = new TokenManager(this);
        String accessToken = tokenManager.getAccessToken();
        //Log.d("HomeScreen", "Access Token: " + accessToken);

        if (accessToken != null) {
            fetchUserInfo(accessToken);
            fetchUserActivities(accessToken);
        } else {
            Log.e("HomeScreen", "Access Token липсва!");
        }
    }

    private void fetchUserInfo(String accessToken) {
        String url = "http://10.0.2.2:8000/api/user-info/";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("fetchUserInfo", "Заявката не успя: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("fetchUserInfo", "Неуспешен отговор: " + response.code());
                    return;
                }

                String responseBody = response.body().string();
                Log.d("fetchUserInfo", "Отговор: " + responseBody);

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    //userFirstName = jsonResponse.getString("first_name");
                    userMessage = jsonResponse.getString("message");

                    // Обновяваме UI в главния (UI) поток
                    runOnUiThread(() -> {
                        welcomeTextView.setText(userMessage);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void fetchUserActivities(String accessToken) {
        String url = "http://10.0.2.2:8000/api/user-activities/";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("fetchUserActivities", "Заявката за извличане на текущи занятия не успя: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("fetchUserActivities", "Неуспешен отговор: " + response.code());
                    return;
                }

                String responseBody = response.body().string();
                Log.d("fetchUserActivities", "Отговор: " + responseBody);

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    currentActivities = jsonResponse.getString("active_activities");
                    futureActivities = jsonResponse.getString("future_activities");
//                    Log.d("fetchUserActivities", "Текущи занятия: " + currentActivities);
//                    Log.d("fetchUserActivities", "Предстоящи занятия: " + futureActivities);

                    // Обновяваме UI в главния (UI) поток
                    runOnUiThread(() -> {
                        currentActivitiesTextView.setText(currentActivities);
                        futureActivitiesTextView.setText(futureActivities);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

 */