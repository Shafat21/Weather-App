package com.mms.weatherappfinal;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements LoginAuthHandler.AuthListener, LoginApiHandler.ApiListener {

    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogin);
        TextView buttonGoToRegister = findViewById(R.id.buttonGoToRegister);

        // Set a click listener for the registration button
        buttonGoToRegister.setOnClickListener(view -> {
            // Start RegisterActivity when the TextView is clicked
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(view -> {
            // Extract email and password from the input fields
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            Log.d("LoginActivity", "Login button clicked");

            // Validate email and password
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the login method from AuthHandler
            LoginAuthHandler.login(email, password, this);

        });
    }

    // AuthHandler.AuthListener methods
    @Override
    public void onAuthSuccess(String message) {
        Log.d("LoginActivity", "onAuthSuccess called");
        runOnUiThread(() -> {
            // Navigate to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close all activities in the task
        });
        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onAuthError(String error) {
        runOnUiThread(() -> {
            // There was an error during login
            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }


    // ApiHandler.ApiListener methods
    @Override
    public void onApiResponse(String response) {
        Log.d("LoginActivity", "API response: " + response);

    }



    @Override
    public void onApiError(String error) {
        Log.e("LoginActivity", "API error: " + error);
        // Call the listener's onAuthError method
        onAuthError(error);

    }
}
