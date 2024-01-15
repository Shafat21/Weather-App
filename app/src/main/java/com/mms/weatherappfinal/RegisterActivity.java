package com.mms.weatherappfinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements LoginApiHandler.ApiListener {

    private EditText editTextName, editTextMobile, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView buttonGoToLogin = findViewById(R.id.buttonGoToLogin); // Assuming you have this button

        buttonRegister.setOnClickListener(v -> register());

        buttonGoToLogin.setOnClickListener(v -> goToLogin());

//        buttonRegister.setOnClickListener(view -> {
//            String mobileNumber = editTextMobile.getText().toString();
//            if (!mobileNumber.isEmpty()) {
//                requestOTP(mobileNumber);
//            } else {
//                Toast.makeText(RegisterActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void register() {
        String name = editTextName.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate mobile number
        if (mobile.length() != 11 || !TextUtils.isDigitsOnly(mobile)) {
            Toast.makeText(RegisterActivity.this, "Mobile number should be 11 digits and contain only numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email
        if (!email.toLowerCase().endsWith(".com")) {
            Toast.makeText(RegisterActivity.this, "Email should end with .com", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password
        if (password.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the API to register
        new LoginApiHandler(this).execute("register", name, mobile, email, password);
        System.out.println("Register button clicked");
    }

//    private void requestOTP(String mobileNumber) {
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<OTPResponse> call = apiService.requestOTP(mobileNumber);
//
//        call.enqueue(new Callback<OTPResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<OTPResponse> call, @NonNull Response<OTPResponse> response) {
//                Log.d("RawResponse", "Raw JSON: " + response.raw().toString());
//                if (response.isSuccessful() && response.body() != null) {
//                    // Handle the successful response here
//                    String referenceNo = response.body().getReferenceNo();
//                    Toast.makeText(RegisterActivity.this, "OTP sent. Reference No: " + referenceNo, Toast.LENGTH_LONG).show();
//                } else {
//
//                    Toast.makeText(RegisterActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<OTPResponse> call, @NonNull Throwable t) {
//                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    private void goToLogin() {
        // Launch LoginActivity when the button is clicked
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onApiResponse(final String response) {
        runOnUiThread(() -> {
            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
            System.out.println("API Response: " + response);

            // Check if the response contains "successful" (you may adjust this condition based on your actual response)
            if (response.toLowerCase().contains("successful")) {
                // Registration was successful, navigate to MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onApiError(final String error) {
        runOnUiThread(() -> {
            // Handle API error on the main thread
            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }

}
