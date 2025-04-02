package com.example.programandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String FCMtoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                getFCMToken(); // Само ако разрешението е дадено
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        } else {
            getFCMToken(); // За Android < 13 не се изисква разрешение
        }
    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        //Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Вземи токена
                    String token = task.getResult();
                    Log.d("Mainnn", "FCM Token: " + token);
                    FCMtoken = token;

                    // Изпрати токена към сървъра
                    //sendTokenToServer(token);
                });
    }
    public class SecureRequest {
        private static final String TAG = "SecureRequest";
        private final OkHttpClient client;

        public SecureRequest() {
            client = getSecureOkHttpClient();
        }

        private OkHttpClient getSecureOkHttpClient() {
            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((java.security.KeyStore) null);
                X509TrustManager trustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new javax.net.ssl.TrustManager[]{trustManager}, new java.security.SecureRandom());

                return new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create secure OkHttpClient", e);
            }
        }

        public void sendSecureRequest(String username, String password) {
            String url = "http://10.0.2.2:8000/api/token/";
//            String url = "http://192.168.1.7:8000/api/token/";

            JSONObject json = new JSONObject();
            try {
                json.put("username", username);
                json.put("password", password);
                json.put("FCMtoken", FCMtoken);

            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Connection failed: " + e.getMessage());
                    String errorMessage = "Connection failed: " + e.getMessage();

                    runOnUiThread(() -> {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Грешка")
                                .setMessage(errorMessage)
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Response: " + responseBody);

                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);

                            if (response.isSuccessful()) {
                                if (jsonResponse.has("access")) {
                                    // Запазване на токените
                                    TokenManager tokenManager = new TokenManager(MainActivity.this);
                                    tokenManager.saveAccessToken(jsonResponse.getString("access"));
                                    tokenManager.saveRefreshToken(jsonResponse.getString("refresh"));


//                                    new AlertDialog.Builder(MainActivity.this)
//                                            .setTitle("Успех")
//                                            .setMessage("Успешен вход!")
//                                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                                            .show();


                                    startActivity(new Intent(MainActivity.this, Navigation.class).putExtra("name", username));
                                }
                            } else {
                                if (jsonResponse.has("detail")) {
                                    String errorMessage = jsonResponse.getString("detail");
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Грешка")
                                            .setMessage("Грешно потребителско име или парола!")
                                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                            .show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
    }

    public void onLoginButtonClick(View view) {
        String username = ((EditText) findViewById(R.id.etUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        SecureRequest secureRequest = new SecureRequest();
        secureRequest.sendSecureRequest(username, password);
    }
}