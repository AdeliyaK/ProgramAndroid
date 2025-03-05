package com.example.programandroid.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<String> userMessage = new MutableLiveData<>();
    private final MutableLiveData<String> currentActivities = new MutableLiveData<>();
    private final MutableLiveData<String> futureActivities = new MutableLiveData<>();

    public HomeViewModel(Application application) {
        super(application);
    }

    public LiveData<String> getUserMessage() {
        return userMessage;
    }

    public LiveData<String> getCurrentActivities() {
        return currentActivities;
    }

    public LiveData<String> getFutureActivities() {
        return futureActivities;
    }

    public void fetchUserInfo(String accessToken) {
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }

                String responseBody = response.body().string();

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String message = jsonResponse.getString("message");

                    userMessage.postValue(message); // Обновяваме UI с резултата
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchUserActivities(String accessToken) {
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }

                String responseBody = response.body().string();

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String currentActivitiesData = jsonResponse.getString("active_activities");
                    String futureActivitiesData = jsonResponse.getString("future_activities");

                    currentActivities.postValue(currentActivitiesData);
                    futureActivities.postValue(futureActivitiesData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
