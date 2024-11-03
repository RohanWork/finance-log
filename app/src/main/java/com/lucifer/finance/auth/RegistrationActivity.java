//package com.lucifer.finance.auth;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.material.snackbar.Snackbar;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.lucifer.finance.MainActivity;
//import com.lucifer.finance.R;
//
//import java.util.Objects;
//import java.util.Random;
//
//public class RegistrationActivity extends AppCompatActivity {
//
//    private EditText nameEditText;
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private ImageView profileImageView;
//    private Button uploadButton;
//    private Button registerButton;
//
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    private StorageReference mStorage;
//
//    private Uri profileImageUri;
//    private View rootView;  // Use this as the root view for Snackbar
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
//
//        // Initialize views
//        nameEditText = findViewById(R.id.name_edit_text);
//        emailEditText = findViewById(R.id.email_edit_text);
//        passwordEditText = findViewById(R.id.password_edit_text);
//        profileImageView = findViewById(R.id.profile_image_view);
//        uploadButton = findViewById(R.id.upload_button);
//        registerButton = findViewById(R.id.register_button);
//
//        // Initialize Firebase components
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mStorage = FirebaseStorage.getInstance().getReference();
//
//        // Get the root view for Snackbar
//        rootView = findViewById(android.R.id.content);
//
//        profileImageView.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            startActivityForResult(intent, 1);
//        });
//
//        uploadButton.setOnClickListener(v -> {
//            try {
//                if (profileImageUri != null) {
//                    StorageReference profilePictureRef = mStorage.child("profile_pictures/" + System.currentTimeMillis() + ".jpg");
//                    profilePictureRef.putFile(profileImageUri)
//                            .addOnCompleteListener(task -> {
//                                if (task.isSuccessful()) {
//                                    // Get the download URL of the uploaded profile picture
//                                    Task<Uri> downloadUrlTask = task.getResult().getStorage().getDownloadUrl();
//                                    downloadUrlTask.addOnCompleteListener(task1 -> {
//                                        if (task1.isSuccessful()) {
//                                            Uri downloadUri = task1.getResult();
//                                            String profilePictureUrl = downloadUri.toString();
//                                            // Handle the next steps, such as user creation
//                                        } else {
//                                            // Handle URL retrieval failure
//                                            Snackbar snackbar = Snackbar.make(rootView, "Failed to get profile picture URL: " + task1.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                            customizeSnackbar(snackbar);
//                                            snackbar.show();
//                                        }
//                                    });
//                                } else {
//                                    // Handle file upload failure
//                                    Snackbar snackbar = Snackbar.make(rootView, "Profile picture upload failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                    customizeSnackbar(snackbar);
//                                    snackbar.show();
//                                }
//                            });
//                } else {
//                    Snackbar snackbar = Snackbar.make(rootView, "Please select a profile picture", Snackbar.LENGTH_SHORT);
//                    customizeSnackbar(snackbar);
//                    snackbar.show();
//                }
//            } catch (Exception e) {
//                Snackbar snackbar = Snackbar.make(rootView, "Error uploading profile picture: \n" + e.getMessage(), Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        });
//
//
//        registerButton.setOnClickListener(v -> {
//            try {
//                if (profileImageUri != null) {
//                    uploadButton.performClick();
//                } else {
//                    Snackbar snackbar = Snackbar.make(rootView, "Please select a profile picture", Snackbar.LENGTH_SHORT);
//                    customizeSnackbar(snackbar);
//                    snackbar.show();
//                }
//            } catch (Exception e) {
//                Snackbar snackbar = Snackbar.make(rootView, "Error registering: \n" + e.getMessage(), Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        });
//    }
//
//    private void customizeSnackbar(Snackbar snackbar) {
//        try {
//            snackbar.setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black));
//            snackbar.setBackgroundTint(ContextCompat.getColor(RegistrationActivity.this, R.color.yellow));
//            View snackbarView = snackbar.getView();
//            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView.setTextSize(15);
//            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
//        } catch (Exception e) {
//            // Handle exception
//            Snackbar snackbarE = Snackbar.make(rootView, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbarE);
//            snackbarE.show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
//                profileImageUri = data.getData();
//                profileImageView.setImageURI(profileImageUri);
//            }
//        } catch (Exception e) {
//            // Handle exception
//            Snackbar snackbar = Snackbar.make(rootView, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//        }
//    }
//
//    public static String generateUniqueId() {
//        try {
//            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//            StringBuilder uniqueId = new StringBuilder();
//            Random random = new Random();
//
//            for (int i = 0; i < 6; i++) {
//                uniqueId.append(chars.charAt(random.nextInt(chars.length())));
//            }
//
//            return uniqueId.toString();
//        } catch (Exception e) {
//            return "";
//        }
//    }
//}















//package com.lucifer.finance.auth;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.lucifer.finance.MainActivity;
//import com.lucifer.finance.R;
//
//import java.util.Objects;
//import java.util.Random;
//
//public class RegistrationActivity extends AppCompatActivity {
//
//    private EditText nameEditText;
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private ImageView profileImageView;
//    private Button registerButton;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    private StorageReference mStorage;
//    private Uri profileImageUri;
//    private View rootView;  // Use this as the root view for Snackbar
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
//
//        // Initialize views
//        nameEditText = findViewById(R.id.name_edit_text);
//        emailEditText = findViewById(R.id.email_edit_text);
//        passwordEditText = findViewById(R.id.password_edit_text);
//        profileImageView = findViewById(R.id.profile_image_view);
//        registerButton = findViewById(R.id.register_button);
//
//        // Initialize Firebase components
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mStorage = FirebaseStorage.getInstance().getReference();
//
//        // Get the root view for Snackbar
//        rootView = findViewById(android.R.id.content);
//
//        profileImageView.setOnClickListener(v -> {
//            try {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, 1);
//            } catch (Exception e) {
//                Snackbar snackbar = Snackbar.make(rootView, "Error selecting image: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//                Log.e("RegistrationActivity", "Error selecting image", e);
//            }
//        });
//
//        registerButton.setOnClickListener(v -> {
//            try {
//                if (profileImageUri != null) {
//                    uploadProfilePicture();
//                } else {
//                    Snackbar snackbar = Snackbar.make(rootView, "Please select a profile picture", Snackbar.LENGTH_SHORT);
//                    customizeSnackbar(snackbar);
//                    snackbar.show();
//                }
//            } catch (Exception e) {
//                Snackbar snackbar = Snackbar.make(rootView, "Error during registration: \n" + e.getMessage(), Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//                Log.e("RegistrationActivity", "Error during registration", e);
//            }
//        });
//    }
//
//    private void uploadProfilePicture() {
//        try {
//            if (profileImageUri != null) {
//                StorageReference profilePictureRef = mStorage.child("profile_pictures/" + System.currentTimeMillis() + ".jpg");
//
//                profilePictureRef.putFile(profileImageUri)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                profilePictureRef.getDownloadUrl().addOnCompleteListener(task1 -> {
//                                    if (task1.isSuccessful()) {
//                                        Uri downloadUri = task1.getResult();
//                                        String profilePictureUrl = downloadUri.toString();
//                                        registerUser(profilePictureUrl);
//                                    } else {
//                                        Snackbar snackbar = Snackbar.make(rootView, "Failed to get profile picture URL: " + task1.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                        customizeSnackbar(snackbar);
//                                        snackbar.show();
//                                        Log.e("RegistrationActivity", "Error retrieving download URL", task1.getException());
//                                    }
//                                });
//                            } else {
//                                Snackbar snackbar = Snackbar.make(rootView, "Profile picture upload failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                customizeSnackbar(snackbar);
//                                snackbar.show();
//                                Log.e("RegistrationActivity", "Error uploading file", task.getException());
//                            }
//                        });
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "No profile image selected", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        } catch (Exception e) {
//            Snackbar snackbar = Snackbar.make(rootView, "Error setting up upload: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//            Log.e("RegistrationActivity", "Error setting up upload", e);
//        }
//    }
//
//    private void registerUser(String profilePictureUrl) {
//        try {
//            String email = emailEditText.getText().toString().trim();
//            String password = passwordEditText.getText().toString().trim();
//            String name = nameEditText.getText().toString().trim();
//            String uniqueId = generateUniqueId();
//
//            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
//                Snackbar snackbar = Snackbar.make(rootView, "Please fill all fields.", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//                return;
//            }
//
//            mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, task -> {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            if (user != null) {
//                                UserData userData = new UserData(name, email, profilePictureUrl, uniqueId);
//                                mDatabase.child("users").child(user.getUid()).setValue(userData)
//                                        .addOnCompleteListener(task1 -> {
//                                            if (task1.isSuccessful()) {
//                                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
//                                                finish();  // Close the registration activity
//                                            } else {
//                                                Snackbar snackbar = Snackbar.make(rootView, "Failed to save user data: " + task1.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                                customizeSnackbar(snackbar);
//                                                snackbar.show();
//                                                Log.e("RegistrationActivity", "Error saving user data", task1.getException());
//                                            }
//                                        });
//                            } else {
//                                Snackbar snackbar = Snackbar.make(rootView, "User registration failed.", Snackbar.LENGTH_SHORT);
//                                customizeSnackbar(snackbar);
//                                snackbar.show();
//                                Log.e("RegistrationActivity", "FirebaseUser is null");
//                            }
//                        } else {
//                            Snackbar snackbar = Snackbar.make(rootView, "Registration failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                            customizeSnackbar(snackbar);
//                            snackbar.show();
//                            Log.e("RegistrationActivity", "Error during user registration", task.getException());
//                        }
//                    });
//        } catch (Exception e) {
//            Snackbar snackbar = Snackbar.make(rootView, "Error during user registration setup: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//            Log.e("RegistrationActivity", "Error during user registration setup", e);
//        }
//    }
//
//
//    private void customizeSnackbar(Snackbar snackbar) {
//        try {
//            snackbar.setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black));
//            snackbar.setBackgroundTint(ContextCompat.getColor(RegistrationActivity.this, R.color.yellow));
//            View snackbarView = snackbar.getView();
//            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView.setTextSize(15);
//            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
//        } catch (Exception e) {
//            Snackbar snackbarE = Snackbar.make(rootView, "Error customizing Snackbar: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbarE);
//            snackbarE.show();
//            Log.e("RegistrationActivity", "Error customizing Snackbar", e);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
//                profileImageUri = data.getData();
//                profileImageView.setImageURI(profileImageUri);
//            }
//        } catch (Exception e) {
//            Snackbar snackbar = Snackbar.make(rootView, "Error handling image selection: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//            Log.e("RegistrationActivity", "Error handling image selection", e);
//        }
//    }
//
//    public static String generateUniqueId() {
//        try {
//            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//            StringBuilder uniqueId = new StringBuilder();
//            Random random = new Random();
//
//            for (int i = 0; i < 6; i++) {
//                uniqueId.append(chars.charAt(random.nextInt(chars.length())));
//            }
//
//            return uniqueId.toString();
//        } catch (Exception e) {
//            Log.e("RegistrationActivity", "Error generating unique ID", e);
//            return "";
//        }
//    }
//}







//package com.lucifer.finance.auth;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.*;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.lucifer.finance.MainActivity;
//import com.lucifer.finance.R;
//
//import java.util.Objects;
//import java.util.Random;
//
//public class RegistrationActivity extends AppCompatActivity {
//
//    private static final int REQUEST_CODE_PERMISSIONS = 1001;
//    private static final int REQUEST_CODE_IMAGE_PICK = 1;
//    private static final String[] REQUIRED_PERMISSIONS = new String[] {
//            Manifest.permission.READ_EXTERNAL_STORAGE
//    };
//
//    private EditText nameEditText;
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private ImageView profileImageView;
//    private Button registerButton;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    private StorageReference mStorage;
//    private Uri profileImageUri;
//    private View rootView;  // Use this as the root view for Snackbar
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
//
//        // Initialize views
//        nameEditText = findViewById(R.id.name_edit_text);
//        emailEditText = findViewById(R.id.email_edit_text);
//        passwordEditText = findViewById(R.id.password_edit_text);
//        profileImageView = findViewById(R.id.profile_image_view);
//        registerButton = findViewById(R.id.register_button);
//
//        // Initialize Firebase components
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference("users");
//        mStorage = FirebaseStorage.getInstance().getReference();
//
//        // Get the root view for Snackbar
//        rootView = findViewById(android.R.id.content);
//
//        // Check for required permissions
//        if (!hasPermissions()) {
//            requestPermissions();
//        }
//
//        profileImageView.setOnClickListener(v -> {
//            try {
//                if (hasPermissions()) {
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
//                } else {
//                    Snackbar snackbar = Snackbar.make(rootView, "Permission required to select image", Snackbar.LENGTH_SHORT);
//                    customizeSnackbar(snackbar);
//                    snackbar.show();
//                }
//            } catch (Exception e) {
//                Snackbar snackbar = Snackbar.make(rootView, "Error selecting image: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//                Log.e("RegistrationActivity", "Error selecting image", e);
//            }
//        });
//
//        registerButton.setOnClickListener(v -> {
//            try {
//                if (profileImageUri != null) {
//                    uploadProfilePicture();
//                } else {
//                    Snackbar snackbar = Snackbar.make(rootView, "Please select a profile picture", Snackbar.LENGTH_SHORT);
//                    customizeSnackbar(snackbar);
//                    snackbar.show();
//                }
//            } catch (Exception e) {
//                Snackbar snackbar = Snackbar.make(rootView, "Error during registration: \n" + e.getMessage(), Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//                Log.e("RegistrationActivity", "Error during registration", e);
//            }
//        });
//    }
//
//    private boolean hasPermissions() {
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, proceed with accessing media
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "Permission required to access media", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        }
//    }
//
//    private void uploadProfilePicture() {
//        try {
//            if (profileImageUri != null) {
//                String name = nameEditText.getText().toString().trim();
//                if (name.isEmpty()){
//                    Snackbar snackbar = Snackbar.make(rootView, "Please enter the name first", Snackbar.LENGTH_SHORT);
//                    customizeSnackbar(snackbar);
//                    snackbar.show();
//                }
//                StorageReference profilePictureRef = mStorage.child("profile_pictures/" + name+"_"+System.currentTimeMillis() + ".jpg");
//
//                profilePictureRef.putFile(profileImageUri)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                profilePictureRef.getDownloadUrl().addOnCompleteListener(task1 -> {
//                                    if (task1.isSuccessful()) {
//                                        Uri downloadUri = task1.getResult();
//                                        String profilePictureUrl = downloadUri.toString();
//                                        registerUser(profilePictureUrl);
//                                    } else {
//                                        Snackbar snackbar = Snackbar.make(rootView, "Failed to get profile picture URL: " + task1.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                        customizeSnackbar(snackbar);
//                                        snackbar.show();
//                                        Log.e("RegistrationActivity", "Error retrieving download URL", task1.getException());
//                                    }
//                                });
//                            } else {
//                                Snackbar snackbar = Snackbar.make(rootView, "Profile picture upload failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                customizeSnackbar(snackbar);
//                                snackbar.show();
//                                Log.e("RegistrationActivity", "Error uploading file", task.getException());
//                            }
//                        });
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "No profile image selected", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        } catch (Exception e) {
//            Snackbar snackbar = Snackbar.make(rootView, "Error setting up upload: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//            Log.e("RegistrationActivity", "Error setting up upload", e);
//        }
//    }
//
//    private void registerUser(String profilePictureUrl) {
//        try {
//            String email = emailEditText.getText().toString().trim();
//            String password = passwordEditText.getText().toString().trim();
//            String name = nameEditText.getText().toString().trim();
//            String uniqueId = generateUniqueId();
//
//            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
//                Snackbar snackbar = Snackbar.make(rootView, "Please fill all fields.", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//                return;
//            }
//
//            mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnSuccessListener(authResult -> {
//                        Toast.makeText(RegistrationActivity.this, "Data validated", Toast.LENGTH_SHORT).show();
//                        Intent main = new Intent(RegistrationActivity.this, MainActivity.class);
//                        startActivity(main);
//
//                        try {
//                            String userKey = mDatabase.child("users").push().getKey();  // Generate a unique key for the user
//                            if (userKey != null) {
//                                UserData userData = new UserData(name, email, profilePictureUrl, uniqueId);
//                                mDatabase.child("users").child(userKey).setValue(userData)
//                                        .addOnSuccessListener(aVoid -> {
//                                            // Successfully saved user data
//                                            finish();  // Close the registration activity
//                                        })
//                                        .addOnFailureListener(exception -> {
//                                            Snackbar snackbar = Snackbar.make(rootView, "Failed to save user data: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
//                                            customizeSnackbar(snackbar);
//                                            snackbar.show();
//                                            Log.e("RegistrationActivity", "Error saving user data", exception);
//                                        });
//                            } else {
//                                Snackbar snackbar = Snackbar.make(rootView, "Failed to generate unique key for user data.", Snackbar.LENGTH_SHORT);
//                                customizeSnackbar(snackbar);
//                                snackbar.show();
//                                Log.e("RegistrationActivity", "Failed to generate unique key for user data.");
//                            }
//                        } catch (Exception exception) {
//                            Snackbar snackbar = Snackbar.make(rootView, "Error during user data upload: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
//                            customizeSnackbar(snackbar);
//                            snackbar.show();
//                            Log.e("RegistrationActivity", "Error during user data upload", exception);
//                        }
//                    })
//                    .addOnFailureListener(exception -> {
//                        Snackbar snackbar = Snackbar.make(rootView, "Registration failed: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
//                        customizeSnackbar(snackbar);
//                        snackbar.show();
//                        Log.e("RegistrationActivity", "Error during user registration", exception);
//                    });
//        } catch (Exception e) {
//            Snackbar snackbar = Snackbar.make(rootView, "Error during user registration setup: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//            Log.e("RegistrationActivity", "Error during user registration setup", e);
//        }
//    }
//
//
//    private void customizeSnackbar(Snackbar snackbar) {
//        try {
//            snackbar.setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black));
//            snackbar.setBackgroundTint(ContextCompat.getColor(RegistrationActivity.this, R.color.yellow));
//            View snackbarView = snackbar.getView();
//            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView.setTextSize(15);
//            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
//        } catch (Exception e) {
//            Snackbar snackbarE = Snackbar.make(rootView, "Error customizing Snackbar: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbarE);
//            snackbarE.show();
//            Log.e("RegistrationActivity", "Error customizing Snackbar", e);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
//                profileImageUri = data.getData();
//                profileImageView.setImageURI(profileImageUri);
//            }
//        } catch (Exception e) {
//            Snackbar snackbar = Snackbar.make(rootView, "Error handling image selection: " + e.getMessage(), Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//            Log.e("RegistrationActivity", "Error handling image selection", e);
//        }
//    }
//
//    public static String generateUniqueId() {
//        try {
//            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//            StringBuilder uniqueId = new StringBuilder();
//            Random random = new Random();
//
//            for (int i = 0; i < 6; i++) {
//                uniqueId.append(chars.charAt(random.nextInt(chars.length())));
//            }
//
//            return uniqueId.toString();
//        } catch (Exception e) {
//            Log.e("RegistrationActivity", "Error generating unique ID", e);
//            return "";
//        }
//    }
//}
















































//package com.lucifer.finance.auth;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.method.HideReturnsTransformationMethod;
//import android.text.method.PasswordTransformationMethod;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.*;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.lucifer.finance.MainActivity;
//import com.lucifer.finance.R;
//
//import java.util.Objects;
//import java.util.Random;
//
//public class RegistrationActivity extends AppCompatActivity {
//
//    private static final int REQUEST_CODE_PERMISSIONS = 1001;
//    private static final int REQUEST_CODE_IMAGE_PICK = 1;
//    private static final String[] REQUIRED_PERMISSIONS = new String[] {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_SMS,
//            Manifest.permission.RECEIVE_SMS
//    };
//
//    private EditText firstNameEditText, lastNameEditText;
//    private EditText emailEditText;
//    private EditText passwordEditText, confirmPasswordEditText;
//    private ImageView profileImageView;
//    private Button registerButton;
//    private ImageButton showPasswordButton, showConfirmPasswordButton;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    private StorageReference mStorage;
//    private Uri profileImageUri;
//    private View rootView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
//
//        // Initialize views
//        firstNameEditText = findViewById(R.id.first_name_edit_text);
//        lastNameEditText = findViewById(R.id.last_name_edit_text);
//        emailEditText = findViewById(R.id.email_edit_text);
//        passwordEditText = findViewById(R.id.password_edit_text);
//        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
//        showPasswordButton = findViewById(R.id.show_password_button);
//        showConfirmPasswordButton = findViewById(R.id.show_confirm_password_button);
//        profileImageView = findViewById(R.id.profile_image_view);
//        registerButton = findViewById(R.id.register_button);
//
//        // Initialize Firebase components
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference("users");
//        mStorage = FirebaseStorage.getInstance().getReference();
//
//        // Get the root view for Snackbar
//        rootView = findViewById(android.R.id.content);
//
//        // Check for required permissions
//        if (!hasPermissions()) {
//            requestPermissions();
//        }
//
//        profileImageView.setOnClickListener(v -> {
//            if (hasPermissions()) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "Permission required to select image", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        });
//
//        // Toggle password visibility
//        showPasswordButton.setOnClickListener(v -> togglePasswordVisibility(passwordEditText, showPasswordButton));
//        showConfirmPasswordButton.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordEditText, showConfirmPasswordButton));
//
//        registerButton.setOnClickListener(v -> {
//            if (profileImageUri != null) {
//                uploadProfilePicture();
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "Please select a profile picture", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        });
//    }
//
//    private void togglePasswordVisibility(EditText passwordEditText, ImageButton toggleButton) {
//        if (passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
//            // Show Password
//            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            toggleButton.setImageResource(R.drawable.ic_visibility); // Change to the visibility icon
//        } else {
//            // Hide Password
//            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            toggleButton.setImageResource(R.drawable.ic_visibility_off); // Change back to the visibility off icon
//        }
//        passwordEditText.setSelection(passwordEditText.getText().length()); // Move cursor to the end
//    }
//
//    private boolean hasPermissions() {
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, proceed with accessing media
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "Permission required to access media", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        }
//    }
//
//    private void uploadProfilePicture() {
//        if (profileImageUri != null) {
//            String uniqueId = generateUniqueId(); // Generate unique ID
//
//            StorageReference profilePictureRef = mStorage.child("profile_pictures/" + uniqueId + "_" + System.currentTimeMillis() + ".jpg");
//
//            profilePictureRef.putFile(profileImageUri)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            profilePictureRef.getDownloadUrl().addOnCompleteListener(task1 -> {
//                                if (task1.isSuccessful()) {
//                                    Uri downloadUri = task1.getResult();
//                                    String profilePictureUrl = downloadUri.toString();
//                                    registerUser(profilePictureUrl, uniqueId); // Pass uniqueId to registerUser
//                                } else {
//                                    Snackbar snackbar = Snackbar.make(rootView, "Failed to get profile picture URL: " + task1.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                    customizeSnackbar(snackbar);
//                                    snackbar.show();
//                                    Log.e("RegistrationActivity", "Error retrieving download URL", task1.getException());
//                                }
//                            });
//                        } else {
//                            Snackbar snackbar = Snackbar.make(rootView, "Profile picture upload failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                            customizeSnackbar(snackbar);
//                            snackbar.show();
//                            Log.e("RegistrationActivity", "Error uploading file", task.getException());
//                        }
//                    });
//        } else {
//            Snackbar snackbar = Snackbar.make(rootView, "No profile image selected", Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//        }
//    }
//
//    private void registerUser(String profilePictureUrl, String uniqueId) {
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String firstName = firstNameEditText.getText().toString().trim();
//        String lastName = lastNameEditText.getText().toString().trim();
//
//        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
//            Snackbar snackbar = Snackbar.make(rootView, "Please fill all fields.", Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//            return;
//        }
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnSuccessListener(authResult -> {
//                    Toast.makeText(RegistrationActivity.this, "Data validated", Toast.LENGTH_SHORT).show();
//                    Intent main = new Intent(RegistrationActivity.this, MainActivity.class);
//                    startActivity(main);
//
//                    String userKey = mDatabase.push().getKey();  // Generate a unique key for the user
//                    if (userKey != null) {
//                        UserData userData = new UserData(firstName.toLowerCase(), lastName.toLowerCase(), email.toLowerCase(), profilePictureUrl, uniqueId.toLowerCase());
//                        mDatabase.child(userKey).setValue(userData)
//                                .addOnSuccessListener(aVoid -> finish())
//                                .addOnFailureListener(exception -> {
//                                    Snackbar snackbar = Snackbar.make(rootView, "Failed to save user data: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
//                                    customizeSnackbar(snackbar);
//                                    snackbar.show();
//                                    Log.e("RegistrationActivity", "Error saving user data", exception);
//                                });
//                    } else {
//                        Snackbar snackbar = Snackbar.make(rootView, "Failed to generate unique key for user data.", Snackbar.LENGTH_SHORT);
//                        customizeSnackbar(snackbar);
//                        snackbar.show();
//                        Log.e("RegistrationActivity", "Failed to generate unique key for user data.");
//                    }
//                })
//                .addOnFailureListener(exception -> {
//                    Snackbar snackbar = Snackbar.make(rootView, "Registration failed: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
//                    customizeSnackbar(snackbar);
//                    snackbar.show();
//                    Log.e("RegistrationActivity", "Error during user registration", exception);
//                });
//    }
//
//    private void customizeSnackbar(Snackbar snackbar) {
//        snackbar.setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black));
//        snackbar.setBackgroundTint(ContextCompat.getColor(RegistrationActivity.this, R.color.yellow));
//        View snackbarView = snackbar.getView();
//        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        textView.setTextSize(15);
//        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
//            profileImageUri = data.getData();
//            profileImageView.setImageURI(profileImageUri);
//        }
//    }
//
//    public static String generateUniqueId() {
//        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder uniqueId = new StringBuilder();
//        Random random = new Random();
//
//        for (int i = 0; i < 6; i++) {
//            uniqueId.append(chars.charAt(random.nextInt(chars.length())));
//        }
//
//        return uniqueId.toString();
//    }
//}




package com.lucifer.finance.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lucifer.finance.MainActivity;
import com.lucifer.finance.R;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

//public class RegistrationActivity extends AppCompatActivity {
//
//    private static final int REQUEST_CODE_PERMISSIONS = 1001;
//    private static final int REQUEST_CODE_IMAGE_PICK = 1;
//    private static final String[] REQUIRED_PERMISSIONS = new String[] {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_SMS,
//            Manifest.permission.RECEIVE_SMS
//    };
//
//    private EditText firstNameEditText, lastNameEditText;
//    private EditText emailEditText;
//    private EditText passwordEditText, confirmPasswordEditText;
//    private ImageView profileImageView;
//    private Button registerButton;
//    private ImageButton showPasswordButton, showConfirmPasswordButton;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    private StorageReference mStorage;
//    private Uri profileImageUri;
//    private View rootView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
//
//        // Initialize views
//        firstNameEditText = findViewById(R.id.first_name_edit_text);
//        lastNameEditText = findViewById(R.id.last_name_edit_text);
//        emailEditText = findViewById(R.id.email_edit_text);
//        passwordEditText = findViewById(R.id.password_edit_text);
//        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
//        showPasswordButton = findViewById(R.id.show_password_button);
//        showConfirmPasswordButton = findViewById(R.id.show_confirm_password_button);
//        profileImageView = findViewById(R.id.profile_image_view);
//        registerButton = findViewById(R.id.register_button);
//
//        // Initialize Firebase components
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference("users");
//        mStorage = FirebaseStorage.getInstance().getReference();
//
//        // Get the root view for Snackbar
//        rootView = findViewById(android.R.id.content);
//
//        // Check for required permissions
//        if (!hasPermissions()) {
//            requestPermissions();
//        }
//
//        profileImageView.setOnClickListener(v -> {
//            if (hasPermissions()) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "Permission required to select image", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        });
//
//        // Toggle password visibility
//        setupPasswordVisibilityToggle(passwordEditText, showPasswordButton);
//        setupPasswordVisibilityToggle(confirmPasswordEditText, showConfirmPasswordButton);
//
//        // Register button click listener
//        registerButton.setOnClickListener(v -> {
//            if (profileImageUri != null) {
//                uploadProfilePicture();
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "Please select a profile picture", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        });
//    }
//
//    private void setupPasswordVisibilityToggle(EditText passwordEditText, ImageButton toggleButton) {
//        toggleButton.setEnabled(false);
//        toggleButton.setAlpha(0.0f); // Set a lower opacity to indicate it's disabled
//
//        passwordEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 0) {
//                    toggleButton.setEnabled(true);
//                    toggleButton.setAlpha(1.0f); // Full opacity when enabled
//                } else {
//                    toggleButton.setEnabled(false);
//                    toggleButton.setAlpha(0.0f); // Lower opacity when disabled
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        toggleButton.setOnClickListener(v -> {
//            if (passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
//                // Show Password
//                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                toggleButton.setImageResource(R.drawable.ic_visibility); // Change to the visibility icon
//            } else {
//                // Hide Password
//                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                toggleButton.setImageResource(R.drawable.ic_visibility_off); // Change back to the visibility off icon
//            }
//            passwordEditText.setSelection(passwordEditText.getText().length()); // Move cursor to the end
//        });
//
//        // Hide password by default
//        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
//    }
//
//    private boolean hasPermissions() {
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, proceed with accessing media
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "Permission required to access media", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        }
//    }
//
//    private void uploadProfilePicture() {
//        if (profileImageUri != null) {
//            String uniqueId = generateUniqueId(); // Generate unique ID
//
//            StorageReference profilePictureRef = mStorage.child("profile_pictures/" + uniqueId.toLowerCase() + "_" + System.currentTimeMillis() + ".jpg");
//
//            profilePictureRef.putFile(profileImageUri)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            profilePictureRef.getDownloadUrl().addOnCompleteListener(task1 -> {
//                                if (task1.isSuccessful()) {
//                                    Uri downloadUri = task1.getResult();
//                                    String profilePictureUrl = downloadUri.toString();
//                                    registerUser(profilePictureUrl, uniqueId); // Pass uniqueId to registerUser
//                                } else {
//                                    Snackbar snackbar = Snackbar.make(rootView, "Failed to get profile picture URL: " + task1.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                                    customizeSnackbar(snackbar);
//                                    snackbar.show();
//                                    Log.e("RegistrationActivity", "Error retrieving download URL", task1.getException());
//                                }
//                            });
//                        } else {
//                            Snackbar snackbar = Snackbar.make(rootView, "Profile picture upload failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT);
//                            customizeSnackbar(snackbar);
//                            snackbar.show();
//                            Log.e("RegistrationActivity", "Error uploading file", task.getException());
//                        }
//                    });
//        } else {
//            Snackbar snackbar = Snackbar.make(rootView, "No profile image selected", Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//        }
//    }
//
//    private void registerUser(String profilePictureUrl, String uniqueId) {
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String firstName = firstNameEditText.getText().toString().trim();
//        String lastName = lastNameEditText.getText().toString().trim();
//
//        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
//            Snackbar snackbar = Snackbar.make(rootView, "Please fill all fields.", Snackbar.LENGTH_SHORT);
//            customizeSnackbar(snackbar);
//            snackbar.show();
//            return;
//        }
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnSuccessListener(authResult -> {
//                    Toast.makeText(RegistrationActivity.this, "Data validated", Toast.LENGTH_SHORT).show();
//                    Intent main = new Intent(RegistrationActivity.this, MainActivity.class);
//                    startActivity(main);
//
//                    String userKey = mDatabase.push().getKey();  // Generate a unique key for the user
//                    if (userKey != null) {
//                        UserData userData = new UserData(firstName.toLowerCase(), lastName.toLowerCase(), email.toLowerCase(), profilePictureUrl, uniqueId.toLowerCase());
//                        mDatabase.child(userKey).setValue(userData)
//                                .addOnSuccessListener(aVoid -> finish())
//                                .addOnFailureListener(exception -> {
//                                    Snackbar snackbar = Snackbar.make(rootView, "Failed to save user data: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
//                                    customizeSnackbar(snackbar);
//                                    snackbar.show();
//                                    Log.e("RegistrationActivity", "Error saving user data", exception);
//                                });
//                    } else {
//                        Snackbar snackbar = Snackbar.make(rootView, "Failed to generate unique key for user data.", Snackbar.LENGTH_SHORT);
//                        customizeSnackbar(snackbar);
//                        snackbar.show();
//                        Log.e("RegistrationActivity", "Failed to generate unique key for user data.");
//                    }
//                })
//                .addOnFailureListener(exception -> {
//                    Snackbar snackbar = Snackbar.make(rootView, "Registration failed: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
//                    customizeSnackbar(snackbar);
//                    snackbar.show();
//                    Log.e("RegistrationActivity", "Error during user registration", exception);
//                });
//    }
//
//    private void customizeSnackbar(Snackbar snackbar) {
//        snackbar.setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black));
//        snackbar.setBackgroundTint(ContextCompat.getColor(RegistrationActivity.this, R.color.yellow));
//        View snackbarView = snackbar.getView();
//        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        textView.setTextSize(15);
//        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
//            profileImageUri = data.getData();
//            profileImageView.setImageURI(profileImageUri);
//        }
//    }
//
//    public static String generateUniqueId() {
//        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder uniqueId = new StringBuilder();
//        Random random = new Random();
//
//        for (int i = 0; i < 6; i++) {
//            uniqueId.append(chars.charAt(random.nextInt(chars.length())));
//        }
//
//        return uniqueId.toString();
//    }
//}
//

//code without photo url element

public class RegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private static final String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
    };

    private EditText firstNameEditText, lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private ImageButton showPasswordButton, showConfirmPasswordButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));

        // Initialize views
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        showPasswordButton = findViewById(R.id.show_password_button);
        showConfirmPasswordButton = findViewById(R.id.show_confirm_password_button);
        registerButton = findViewById(R.id.register_button);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Get the root view for Snackbar
        rootView = findViewById(android.R.id.content);

        // Check for required permissions
//        if (!hasPermissions()) {
//            requestPermissions();
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            handlePermissionAndService();
        }

        // Toggle password visibility
        setupPasswordVisibilityToggle(passwordEditText, showPasswordButton);
        setupPasswordVisibilityToggle(confirmPasswordEditText, showConfirmPasswordButton);

        // Register button click listener
        registerButton.setOnClickListener(v -> {
            registerUser();
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void handlePermissionAndService() {
        Dexter.withContext(RegistrationActivity.this)
                .withPermissions(Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_MEDIA_IMAGES)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // Check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Snackbar snackbar = Snackbar.make(rootView, "All required permissions granted by user", Snackbar.LENGTH_SHORT);
                            customizeSnackbar(snackbar);
                            snackbar.show();
                        }

                        // Check for any permission permanently denied
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionExplanation();
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar snackbar = Snackbar.make(rootView, "Permissions required for SMS and media access.\nClick on it to allow grant permission.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Allow", v -> ActivityCompat.requestPermissions(RegistrationActivity.this,
                                        new String[]{Manifest.permission.RECEIVE_SMS,
                                                Manifest.permission.READ_MEDIA_IMAGES
                                                },
                                        1));
                        customizeSnackbar(snackbar);
                        snackbar.show();
                    }
                }).check();
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    private void showPermissionExplanation() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);

        TextView titleView = dialogView.findViewById(R.id.alert_dialog_title);
        titleView.setText("Permission Required");

        TextView messageView = dialogView.findViewById(R.id.alert_dialog_message);
        messageView.setText("\nThis app needs SMS and media file access permissions to function properly. Please enable them in the popup.\n");

        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme))
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(RegistrationActivity.this,
                        new String[]{
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_MEDIA_IMAGES
                        }, 1))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    private void setupPasswordVisibilityToggle(EditText passwordEditText, ImageButton toggleButton) {
        toggleButton.setEnabled(false);
        toggleButton.setAlpha(0.0f); // Set a lower opacity to indicate it's disabled

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    toggleButton.setEnabled(true);
                    toggleButton.setAlpha(1.0f); // Full opacity when enabled
                } else {
                    toggleButton.setEnabled(false);
                    toggleButton.setAlpha(0.0f); // Lower opacity when disabled
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        toggleButton.setOnClickListener(v -> {
            if (passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                // Show Password
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                toggleButton.setImageResource(R.drawable.ic_visibility); // Change to the visibility icon
            } else {
                // Hide Password
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                toggleButton.setImageResource(R.drawable.ic_visibility_off); // Change back to the visibility off icon
            }
            passwordEditText.setSelection(passwordEditText.getText().length()); // Move cursor to the end
        });

        // Hide password by default
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private boolean hasPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, proceed with accessing media
//            } else {
//                Snackbar snackbar = Snackbar.make(rootView, "Permission required to access media", Snackbar.LENGTH_SHORT);
//                customizeSnackbar(snackbar);
//                snackbar.show();
//            }
//        }
//    }

    /*
    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar snackbar = Snackbar.make(rootView, "Please fill all fields.", Snackbar.LENGTH_SHORT);
            customizeSnackbar(snackbar);
            snackbar.show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(RegistrationActivity.this, "Data validated", Toast.LENGTH_SHORT).show();
                    Intent main = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(main);

                    String userKey = mDatabase.push().getKey();  // Generate a unique key for the user
                    if (userKey != null) {
                        UserData userData = new UserData(firstName.toLowerCase(), lastName.toLowerCase(), email.toLowerCase(), "test_url", "");
                        mDatabase.child(userKey).setValue(userData)
                                .addOnSuccessListener(aVoid -> finish())
                                .addOnFailureListener(exception -> {
                                    Snackbar snackbar = Snackbar.make(rootView, "Failed to save user data: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
                                    customizeSnackbar(snackbar);
                                    snackbar.show();
                                    Log.e("RegistrationActivity", "Error saving user data", exception);
                                });
                    } else {
                        Snackbar snackbar = Snackbar.make(rootView, "Failed to generate unique key for user data.", Snackbar.LENGTH_SHORT);
                        customizeSnackbar(snackbar);
                        snackbar.show();
                        Log.e("RegistrationActivity", "Failed to generate unique key for user data.");
                    }
                })
                .addOnFailureListener(exception -> {
                    Snackbar snackbar = Snackbar.make(rootView, "Registration failed: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                    Log.e("RegistrationActivity", "Error during user registration", exception);
                });
    }*/


    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        // Check if any field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Snackbar snackbar = Snackbar.make(rootView, "Please fill the fields", Snackbar.LENGTH_SHORT);
            customizeSnackbar(snackbar);
            snackbar.show();
            return;
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar snackbar = Snackbar.make(rootView, "Invalid email address", Snackbar.LENGTH_SHORT);
            customizeSnackbar(snackbar);
            snackbar.show();
            return;
        }

        // Validate password
        if (password.length() < 8) {
            Snackbar snackbar = Snackbar.make(rootView, "Password must be at least 8 characters long", Snackbar.LENGTH_SHORT);
            customizeSnackbar(snackbar);
            snackbar.show();
            return;
        }

        if (!isValidPassword(password)) {
            Snackbar snackbar = Snackbar.make(rootView, "Password must be at least 8 characters and contain a mix of uppercase, lowercase, and numeric characters", Snackbar.LENGTH_INDEFINITE);
            customizeSnackbar(snackbar);
            snackbar.show();
            return;
        }

        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            Snackbar snackbar = Snackbar.make(rootView, "Passwords do not match", Snackbar.LENGTH_SHORT);
            customizeSnackbar(snackbar);
            snackbar.show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Send email verification
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        user.sendEmailVerification()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Email verification sent
                                        Snackbar.make(rootView, "Verification email sent to " + email, Snackbar.LENGTH_LONG)
                                                .setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black))
                                                .setBackgroundTint(getColor(R.color.yellow))
                                                .show();

                                        // Use Firebase Authentication UID as the key to store user data
                                        String userId = user.getUid();
                                        UserData userData = new UserData(firstName.toLowerCase(), lastName.toLowerCase(), email.toLowerCase(), "", generateUniqueId());

                                        // Store user data in the database using Firebase UID as key
                                        mDatabase.child(userId).setValue(userData)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Prompt user to check their email for verification
                                                    Snackbar.make(rootView, "Please verify your email before logging in.", Snackbar.LENGTH_LONG)
                                                            .setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black))
                                                            .setBackgroundTint(getColor(R.color.yellow))
                                                            .show();

                                                    // Redirect user to login page
                                                    Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                                    startActivity(loginIntent);
                                                    finish();
                                                })
                                                .addOnFailureListener(exception -> {
                                                    Snackbar snackbar = Snackbar.make(rootView, "Failed to save user data: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
                                                    customizeSnackbar(snackbar);
                                                    snackbar.show();
                                                    Log.e("RegistrationActivity", "Error saving user data", exception);
                                                });
                                    } else {
                                        // Failed to send verification email
                                        Snackbar.make(rootView, "Failed to send verification email. Try again.", Snackbar.LENGTH_LONG)
                                                .setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black))
                                                .setBackgroundTint(getColor(R.color.yellow))
                                                .show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(exception -> {
                    Snackbar snackbar = Snackbar.make(rootView, "Registration failed: " + exception.getMessage(), Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasNumber = password.matches(".*\\d.*");

        return hasUppercase && hasLowercase && hasNumber;
    }


    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(RegistrationActivity.this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(RegistrationActivity.this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }

    /*public static String generateUniqueId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder uniqueId = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            uniqueId.append(chars.charAt(random.nextInt(chars.length())));
        }

        return uniqueId.toString();
    }*/

    public static String generateUniqueId() {
        return UUID.randomUUID().toString(); // Generates a unique ID in the format of xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    }
}
