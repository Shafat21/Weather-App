package com.mms.weatherappfinal;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginAuthHandler {

    // Replace with your server URL
    private static final String SERVER_URL = "https://shafat.dragondesignstudio.com/Weather/login.php";

    public interface AuthListener {
        void onAuthSuccess(String message);
        void onAuthError(String error);
    }

    public static void login(final String email, final String password, final AuthListener listener) {
        new ApiHandler(new ApiHandler.ApiListener() {
            @Override
            public void onApiResponse(String response) {
                Log.d("AuthHandler", "Login response: " + response);
                if (response.equals("success")) {
                    // Move onAuthSuccess inside onApiResponse
                    listener.onAuthSuccess("Login Successful");
                } else {
                    listener.onAuthError(response);
                }
            }

            @Override
            public void onApiError(String error) {
                Log.e("AuthHandler", "Login error: " + error);
                listener.onAuthError(error);
            }
        }).execute("login", email, password);
    }

    private static class ApiHandler extends AsyncTask<String, Void, String> {

        private final ApiListener mListener;

        public interface ApiListener {
            void onApiResponse(String response);
            void onApiError(String error);
        }

        public ApiHandler(ApiListener listener) {
            this.mListener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            if (params.length < 3) {
                return "Error: Insufficient parameters";
            }
            Map<String, String> postData = new HashMap<>();
            postData.put("email", params[1]);
            postData.put("password", params[2]);

            try {
                URL url = new URL(SERVER_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(getPostDataString(postData).getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    mListener.onApiResponse(response.toString());
                    in.close();
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                mListener.onApiError("Error: " + e.getMessage());
                return "Error: " + e.getMessage();
            }
        }

        private String getPostDataString(Map<String, String> params) {
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey())).append("=").append(URLEncoder.encode(entry.getValue())).append("&");
            }

            return result.toString();
        }
    }
}
