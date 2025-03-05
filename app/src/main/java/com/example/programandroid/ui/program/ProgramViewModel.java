package com.example.programandroid.ui.program;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgramViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> scheduleLiveData = new MutableLiveData<>();
    private JSONObject schedule;

    public LiveData<ArrayList<String>> getScheduleLiveData() {
        return scheduleLiveData;
    }

    public JSONObject getSchedule() {
        return schedule;
    }

    public void fetchSchedule(String accessToken) {
        String url = "http://10.0.2.2:8000/api/user-program/";

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
                Log.e("ProgramViewModel", "Заявката не успя: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("ProgramViewModel", "Неуспешен отговор: " + response.code());
                    return;
                }

                String responseBody = response.body().string();
                Log.d("ProgramViewModel", "Отговор: " + responseBody);

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    schedule = jsonResponse;
                    updateSchedule("Monday"); // Извикваме с началния ден
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateSchedule(String day) {
        try {
            if (schedule != null && schedule.has(day)) {
                String currentDayActivities = schedule.getString(day);
                String[] activities = currentDayActivities.split(", ");
                ArrayList<String> activityList = new ArrayList<>();
                for (String activity : activities) {
                    activityList.add(activity);
                }
                scheduleLiveData.postValue(activityList);
            } else {
                Log.e("ProgramViewModel", "Не е намерен ден: " + day);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
