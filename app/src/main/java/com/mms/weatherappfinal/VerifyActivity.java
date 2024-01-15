package com.mms.weatherappfinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {

    private EditText editTextOtp;
    private Button verifyButton;
    private String referenceNo; // This should be passed from the RegisterActivity or where you get the reference number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify); // Ensure you have the corresponding layout

        editTextOtp = findViewById(R.id.verifycode); // Replace with your actual ID
        verifyButton = findViewById(R.id.verify); // Replace with your actual ID

        // TODO: You need to somehow get the reference number here, maybe passed from the previous Activity

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = editTextOtp.getText().toString();
                if (!otp.isEmpty()) {
                    verifyOtp(otp, referenceNo);
                } else {
                    Toast.makeText(VerifyActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyOtp(String otp, String referenceNo) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<VerifyResponse> call = apiService.verifyOTP(otp, referenceNo);

        call.enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(@NonNull Call<VerifyResponse> call, @NonNull Response<VerifyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle the successful response here
                    String subscriptionStatus = response.body().getSubscriptionStatus();
                    Toast.makeText(VerifyActivity.this, "Subscription Status: " + subscriptionStatus, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VerifyActivity.this, "Failed to verify OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyResponse> call, Throwable t) {
                Toast.makeText(VerifyActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
