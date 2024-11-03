//package com.lucifer.finance.auth;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.lucifer.finance.MainActivity;
//import com.lucifer.finance.R;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private Button loginButton, registerButton;
//
//    private FirebaseAuth mAuth;
//
//    private View view;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
//
//        emailEditText = findViewById(R.id.email_edit_text);
//        passwordEditText = findViewById(R.id.password_edit_text);
//        loginButton = findViewById(R.id.login_button);
//        registerButton = findViewById(R.id.register_button);
//        ImageButton showPasswordButton = findViewById(R.id.show_password_button);
//        EditText passwordEditText = findViewById(R.id.password_edit_text);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        // Disable the button initially if no text is present
//        showPasswordButton.setEnabled(false);
//        showPasswordButton.setAlpha(0.0f); // Set a lower opacity to indicate it's disabled
//
//        passwordEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 0) {
//                    showPasswordButton.setEnabled(true);
//                    showPasswordButton.setAlpha(1.0f); // Full opacity when enabled
//                } else {
//                    showPasswordButton.setEnabled(false);
//                    showPasswordButton.setAlpha(0.0f); // Lower opacity when disabled
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        showPasswordButton.setOnClickListener(v -> {
//            if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
//                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                showPasswordButton.setImageResource(R.drawable.ic_visibility_off); // change icon to hide password
//            } else {
//                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                showPasswordButton.setImageResource(R.drawable.ic_visibility); // change icon to show password
//            }
//            // Move cursor to the end of the input
//            passwordEditText.setSelection(passwordEditText.length());
//        });
//
//        // Add a FocusChangeListener to reset the password visibility when focus is lost
//        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
//            if (!hasFocus) {
//                // Reset password field to hidden when focus is lost
//                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                showPasswordButton.setImageResource(R.drawable.ic_visibility_off); // Set icon to hide password
//                passwordEditText.setSelection(passwordEditText.length()); // Move cursor to the end of the input
//            }
//        });
//
//
//        registerButton.setOnClickListener(v -> gotoRegister());
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//
//                mAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
//                                    // Navigate to main activity
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                } else {
//                                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
//    }
//
//    private void gotoRegister() {
//        Intent main = new Intent(getApplicationContext(), RegistrationActivity.class);
//        startActivity(main);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
////            if (firsttime)
////            {
////                Intent intro = new Intent(getApplicationContext(), appIntro.class);
////                startActivity(intro);
////                finish();
////            }
////            else
////            {
//            Intent main = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(main);
//            finish();
////            }
//        }
//    }
//}





package com.lucifer.finance.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucifer.finance.MainActivity;
import com.lucifer.finance.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton, registerButton;
    private TextView forgotPassword;
    private FirebaseAuth mAuth;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        ImageButton showPasswordButton = findViewById(R.id.show_password_button);
        EditText passwordEditText = findViewById(R.id.password_edit_text);
        forgotPassword = findViewById(R.id.forgot_password);

        mAuth = FirebaseAuth.getInstance();
        rootView = findViewById(android.R.id.content);

        // Disable the button initially if no text is present
        showPasswordButton.setEnabled(false);
        showPasswordButton.setAlpha(0.0f); // Set a lower opacity to indicate it's disabled

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    showPasswordButton.setEnabled(true);
                    showPasswordButton.setAlpha(1.0f); // Full opacity when enabled
                } else {
                    showPasswordButton.setEnabled(false);
                    showPasswordButton.setAlpha(0.0f); // Lower opacity when disabled
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        showPasswordButton.setOnClickListener(v -> {
            if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPasswordButton.setImageResource(R.drawable.ic_visibility_off); // change icon to hide password
            } else {
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPasswordButton.setImageResource(R.drawable.ic_visibility); // change icon to show password
            }
            // Move cursor to the end of the input
            passwordEditText.setSelection(passwordEditText.length());
        });

        // Add a FocusChangeListener to reset the password visibility when focus is lost
        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // Reset password field to hidden when focus is lost
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPasswordButton.setImageResource(R.drawable.ic_visibility_off); // Set icon to hide password
                passwordEditText.setSelection(passwordEditText.length()); // Move cursor to the end of the input
            }
        });

        registerButton.setOnClickListener(v -> gotoRegister());

        // Forgot Password logic
        forgotPassword.setOnClickListener(v -> resetPassword());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() && password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(rootView, "Please fill the fields", Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                    return;
                }

                if (email.isEmpty() && !password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(rootView, "Please fill the email fields", Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                    return;
                }

                if (!email.isEmpty() && password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(rootView, "Please fill the password fields", Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                    return;
                }

                // Validate email and password
                if (!isValidEmail(email)) {
                    Snackbar snackbar = Snackbar.make(rootView, "Please enter a valid email address", Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
//                    Toast.makeText(LoginActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPassword(password)) {
                    Snackbar snackbar = Snackbar.make(rootView, "Password must be at least 8 characters and contain a mix of uppercase, lowercase, and numeric characters", Snackbar.LENGTH_INDEFINITE);
                    customizeSnackbar(snackbar);
                    snackbar.show();
//                    Toast.makeText(LoginActivity.this, "Password must be at least 6 characters and contain a mix of uppercase, lowercase, and numeric characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null && user.isEmailVerified()) {
                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        // Navigate to main activity
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    } else {
                                        // Notify user to verify email
                                        Snackbar snackbar = Snackbar.make(rootView, "Please verify your email before logging in", Snackbar.LENGTH_SHORT);
                                        customizeSnackbar(snackbar);
                                        snackbar.show();
//                                        Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                                        mAuth.signOut(); // Sign out to prevent unauthorized access
                                    }
                                } else {
                                    Snackbar snackbar = Snackbar.make(rootView, "Login failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT);
                                    customizeSnackbar(snackbar);
                                    snackbar.show();
//                                    Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    // Method to reset the password
    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Snackbar snackbar = Snackbar.make(rootView, "Please enter your mail to reset the password", Snackbar.LENGTH_SHORT);
            customizeSnackbar(snackbar);
            snackbar.show();
        } else if (!isValidEmail(email)) {
            Snackbar snackbar = Snackbar.make(rootView, "Please enter a valid email address", Snackbar.LENGTH_SHORT);
            customizeSnackbar(snackbar);
            snackbar.show();
        } else {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar snackbar = Snackbar.make(rootView, "Password reset mail sent", Snackbar.LENGTH_SHORT);
                                customizeSnackbar(snackbar);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(rootView, "Error: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT);
                                customizeSnackbar(snackbar);
                                snackbar.show();
                            }
                        }
                    });
        }
    }

    // Validate email format
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Validate password strength
    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasNumber = password.matches(".*\\d.*");

        return hasUppercase && hasLowercase && hasNumber;
    }

    private void gotoRegister() {
        Intent main = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isEmailVerified()) {
                // User is already signed in and email is verified
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                // Ask user to verify email if not verified
                Toast.makeText(LoginActivity.this, "Please verify your email before proceeding.", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            }
        }
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(LoginActivity.this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }
}
