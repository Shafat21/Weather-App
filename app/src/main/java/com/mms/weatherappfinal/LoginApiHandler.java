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

public class LoginApiHandler extends AsyncTask<String, Void, String> {

    // Define an interface for handling API responses
    public interface ApiListener {
        void onApiResponse(String response);
        void onApiError(String error);
    }

    private final ApiListener mListener;

    // Constructor to set the listener
    public LoginApiHandler(ApiListener listener) {
        this.mListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {


        if (params.length < 4) {
            return "Error: Insufficient parameters";
        }
        String endpoint = params[0];
        Map<String, String> postData = new HashMap<>();

        // Add parameters based on the endpoint (login or register)
        if (endpoint.equals("register")) {
            postData.put("name", params[1]);
            postData.put("mobileNumber", params[2]);
        }

        postData.put("email", params[3]);
        postData.put("password", params[4]);

        try {
            String serverUrl = "https://shafat.dragondesignstudio.com/Weather/";
            URL url = new URL(serverUrl + endpoint.toLowerCase() + ".php");

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
                // Add logging statements
                Log.d("ApiHandler", "URL: " + url);
                Log.d("ApiHandler", "Parameters: " + getPostDataString(postData));
                Log.d("ApiHandler", "Response: " + response);


                // Call the listener's onApiResponse method
                mListener.onApiResponse(response.toString());
                in.close();
                return response.toString();
            } else {
                System.out.println("Error: " + responseCode);
                return "Error: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Call the listener's onApiError method
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
