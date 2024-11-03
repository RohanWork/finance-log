package com.lucifer.finance.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser ;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucifer.finance.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Objects;

/*a
*//*
public class UserProfileActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private Button updateProfileButton, changePasswordButton, changeEmailButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private View rootView;
    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set the toolbar as the ActionBar
        toolbar.setTitle("User Profile");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser  currentUser  = mAuth.getCurrentUser ();

        if (currentUser  != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentUser .getUid());
            initializeViews();
            loadUserData();
            setButtonListeners();
        } else {
            // Handle case where user is not logged in
            Toast.makeText(this, "User  not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }
    }

    private void initializeViews() {
        rootView = findViewById(android.R.id.content);
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.edit_new_email);
        updateProfileButton = findViewById(R.id.button_update_profile);
        changePasswordButton = findViewById(R.id.button_change_password);
//        changeEmailButton = findViewById(R.id.button_change_email);
    }

    private void setButtonListeners() {
        updateProfileButton.setOnClickListener(v -> updateProfile());
        changePasswordButton.setOnClickListener(v -> resetPassword());
//        changeEmailButton.setOnClickListener(v -> updateEmail());
    }

    private void loadUserData() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    firstNameEditText.setText(userData.getFirstName());
                    lastNameEditText.setText(userData.getLastName());
                    emailEditText.setText(userData.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        userDatabase.child("firstName").setValue(firstName);
        userDatabase.child("lastName").setValue(lastName);

        showSnackbar("Profile updated successfully");
    }

    private void resetPassword() {
        FirebaseUser  user = mAuth.getCurrentUser ();
        if (user != null) {
            mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSnackbar("Password reset email sent");
                } else {
                    showSnackbar("Failed to send reset email");
                }
            });
        }
    }

    *//*
*//*private void updateEmail() {
            EditText emailEditText = findViewById(R.id.edit_new_email);  // Assuming you have an EditText for email input in your layout
            String newEmail = emailEditText.getText().toString().trim();

        if (newEmail.isEmpty()) {
            showSnackbar("Please, enter the email address");
        } else if (!isValidEmail(newEmail)) {
            showSnackbar("Please enter a valid email address");
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Send a verification email to the new address
                user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showSnackbar("Verification email sent. Please verify your email before it is updated.");
                    } else {
                        System.out.println(task.getException().getMessage().toString());
                        showSnackbar("Failed to send verification email");
                    }
                }).addOnFailureListener(e -> showSnackbar(Objects.requireNonNull(e.getMessage())));
            }
        }
    }*//**//*


    *//*
*//*private void updateEmail() {
        EditText emailEditText = findViewById(R.id.edit_new_email);
        String newEmail = emailEditText.getText().toString().trim();

        if (newEmail.isEmpty()) {
            showSnackbar("Please, enter the email address");
        } else if (!isValidEmail(newEmail)) {
            showSnackbar("Please enter a valid email address");
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Send verification email to the new address
                user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showSnackbar("Verification email sent. Please verify to update.");

                        // Start checking for email update
                        checkEmailUpdated(user, newEmail);
                    } else {
                        showSnackbar("Failed to send verification email");
                    }
                }).addOnFailureListener(e -> showSnackbar(Objects.requireNonNull(e.getMessage())));
            }
        }
    }*//**//*


    // Polling method to check if email is updated
    *//*
*//*private void checkEmailUpdated(FirebaseUser user, String newEmail) {
        Handler handler = new Handler();
        Runnable checkEmailTask = new Runnable() {
            @Override
            public void run() {
                user.reload().addOnCompleteListener(reloadTask -> {
                    if (reloadTask.isSuccessful()) {
                        if (user.getEmail() != null && user.getEmail().equals(newEmail)) {
                            // Email update confirmed, update in database
                            updateEmailInDatabase(user.getUid(), newEmail);
                        } else {
                            // Retry after delay if email not yet updated
                            handler.postDelayed(this, 2000);  // Retry every 2 seconds
                        }
                    } else {
                        showSnackbar("Failed to reload user");
                    }
                });
            }
        };
        handler.post(checkEmailTask);
    }*//**//*


    // Update the email in Firebase Realtime Database
   *//*
*//* private void updateEmailInDatabase(String uid, String newEmail) {
        DatabaseReference userDatabase = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid);

        userDatabase.child("email").setValue(newEmail).addOnCompleteListener(dbTask -> {
            if (dbTask.isSuccessful()) {
                showSnackbar("Email updated successfully in profile");
            } else {
                showSnackbar("Failed to update email in profile");
            }
        });
    }*//**//*


   *//*
*//* private boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }*//**//*


    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        customizeSnackbar(snackbar);
        snackbar.show();
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(UserProfileActivity.this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(UserProfileActivity.this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }
}*//*
a*/





/*
public class UserProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private ImageView profileImageView;
    private Button updateProfileButton, changePasswordButton, uploadImageButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private StorageReference storageReference;
    private Uri imageUri;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Set up Toolbar and other UI elements
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("User Profile");

        // Initialize Firebase Auth and Database references
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
            initializeViews();
            loadUserData();
            setButtonListeners();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        rootView = findViewById(android.R.id.content);
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.edit_new_email);
        profileImageView = findViewById(R.id.profile_image);
        updateProfileButton = findViewById(R.id.button_update_profile);
        changePasswordButton = findViewById(R.id.button_change_password);
        uploadImageButton = findViewById(R.id.button_upload_image);
    }

    private void setButtonListeners() {
        updateProfileButton.setOnClickListener(v -> updateProfile());
        changePasswordButton.setOnClickListener(v -> resetPassword());
        uploadImageButton.setOnClickListener(v -> openImagePicker());
    }

    private void loadUserData() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    firstNameEditText.setText(userData.getFirstName());
                    lastNameEditText.setText(userData.getLastName());
                    emailEditText.setText(userData.getEmail());

                    // Load profile picture if URL exists
                    if (userData.getProfilePictureUrl() != null) {
                        Glide.with(UserProfileActivity.this)
                                .load(userData.getProfilePictureUrl())
                                .placeholder(R.drawable.ic_profile)
                                .into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
            uploadImageToFirebase();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            int i=0;
            StorageReference fileRef = storageReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail() +".jpg");
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveImageUrlToDatabase(imageUrl);
                        showSnackbar("Profile picture updated successfully.");
                    }))
                    .addOnFailureListener(e -> showSnackbar("Failed to upload profile picture."));
        }
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        userDatabase.child("profilePictureUrl").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showSnackbar("Profile picture saved in database.");
                    } else {
                        showSnackbar("Failed to save profile picture in database.");
                    }
                });
    }

    private void updateProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        userDatabase.child("firstName").setValue(firstName);
        userDatabase.child("lastName").setValue(lastName);

        showSnackbar("Profile updated successfully");
    }

    private void resetPassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.sendPasswordResetEmail(Objects.requireNonNull(user.getEmail())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSnackbar("Password reset email sent");
                } else {
                    showSnackbar("Failed to send reset email");
                }
            });
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        customizeSnackbar(snackbar);
        snackbar.show();
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }

    // Method to handle profile picture click
    public void onProfilePictureClick(View view) {
        openImagePicker();
    }
}
*/





/*
public class UserProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private ImageView profileImageView;
    private Button updateProfileButton, changePasswordButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private StorageReference storageReference;
    private Uri imageUri;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Set up Toolbar and other UI elements
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("User Profile");

        // Initialize Firebase Auth and Database references
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
            initializeViews();
            loadUserData();
            setButtonListeners();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        rootView = findViewById(android.R.id.content);
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.edit_new_email);
        profileImageView = findViewById(R.id.profile_image);
        updateProfileButton = findViewById(R.id.button_update_profile);
        changePasswordButton = findViewById(R.id.button_change_password);
    }

    private void setButtonListeners() {
        updateProfileButton.setOnClickListener(v -> updateProfile());
        changePasswordButton.setOnClickListener(v -> resetPassword());
        profileImageView.setOnClickListener(v -> openImagePicker());
    }

    private void loadUserData() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    firstNameEditText.setText(userData.getFirstName());
                    lastNameEditText.setText(userData.getLastName());
                    emailEditText.setText(userData.getEmail());

                    // Load profile picture if URL exists
                    if (userData.getProfilePictureUrl() != null) {
                        Glide.with(UserProfileActivity.this)
                                .load(userData.getProfilePictureUrl())
                                .placeholder(R.drawable.ic_profile)
                                .override(1024, 1024) // Adjust the size as needed
                                .circleCrop() // Makes the image circular
                                .into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            String baseFileName = email.split("@")[0]; // Get username part
            getImageCount(baseFileName);
        }
    }

    private void getImageCount(String baseFileName) {
        storageReference.listAll().addOnSuccessListener(listResult -> {
            int count = 0;
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith(baseFileName)) {
                    count++;
                }
            }
            String fileName = baseFileName + "_" + (count + 1) + ".jpg"; // Increment count for new filename
            uploadFile(fileName);
        }).addOnFailureListener(e -> showSnackbar("Failed to access storage."));
    }

    private void uploadFile(String fileName) {
        StorageReference fileRef = storageReference.child(fileName);
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveImageUrlToDatabase(imageUrl);
                    showSnackbar("Profile picture updated successfully.");
                }))
                .addOnFailureListener(e -> showSnackbar("Failed to upload profile picture."));
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        userDatabase.child("profilePictureUrl").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        showSnackbar("Profile picture saved in database.");
                    } else {
                        showSnackbar("Failed to save profile picture in database.");
                    }
                });
    }

    private void updateProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        userDatabase.child("firstName").setValue(firstName);
        userDatabase.child("lastName").setValue(lastName);

        // Upload image only when user clicks the update button
        uploadImageToFirebase();

        showSnackbar("Profile updated successfully");
    }

    private void resetPassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.sendPasswordResetEmail(Objects.requireNonNull(user.getEmail())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSnackbar("Password reset email sent");
                } else {
                    showSnackbar("Failed to send reset email");
                }
            });
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        customizeSnackbar(snackbar);
        snackbar.show();
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }
}

*/





/*
public class UserProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private ImageView profileImageView;
    private Button updateProfileButton, changePasswordButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private StorageReference storageReference;
    private Uri imageUri;
    private View rootView;
    private ProgressBar progressBar; // Declare the ProgressBar
    private TextView progressNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Set up Toolbar and other UI elements
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("User Profile");

        // Initialize Firebase Auth and Database references
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
            initializeViews();
            loadUserData();
            setButtonListeners();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        rootView = findViewById(android.R.id.content);
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.edit_new_email);
        profileImageView = findViewById(R.id.profile_image);
        updateProfileButton = findViewById(R.id.button_update_profile);
        changePasswordButton = findViewById(R.id.button_change_password);
        progressBar = findViewById(R.id.progress_bar); // Initialize ProgressBar
        progressNote = findViewById(R.id.progress_note);

    }

    private void setButtonListeners() {
        updateProfileButton.setOnClickListener(v -> updateProfile());
        changePasswordButton.setOnClickListener(v -> resetPassword());
        profileImageView.setOnClickListener(v -> openImagePicker());
    }

    private void loadUserData() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    firstNameEditText.setText(userData.getFirstName());
                    lastNameEditText.setText(userData.getLastName());
                    emailEditText.setText(userData.getEmail());

                    // Load profile picture if URL exists
                    if (userData.getProfilePictureUrl() != null) {
                        Glide.with(UserProfileActivity.this)
                                .load(userData.getProfilePictureUrl())
                                .placeholder(R.drawable.ic_profile)
                                .override(1024, 1024) // Adjust the size as needed
                                .circleCrop() // Makes the image circular
                                .into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            String baseFileName = email.split("@")[0]; // Get username part
            getImageCount(baseFileName);
        }
    }

    private void getImageCount(String baseFileName) {
        storageReference.listAll().addOnSuccessListener(listResult -> {
            int count = 0;
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith(baseFileName)) {
                    count++;
                }
            }
            String fileName = baseFileName + "_" + (count + 1) + ".jpg"; // Increment count for new filename
            uploadFile(fileName);
        }).addOnFailureListener(e -> showSnackbar("Failed to access storage."));
    }

    private void uploadFile(String fileName) {
        StorageReference fileRef = storageReference.child(fileName);
        showProgressBar(); // Show ProgressBar before upload
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveImageUrlToDatabase(imageUrl);
                    hideProgressBar(); // Hide ProgressBar after upload completes
                    showSnackbar("Profile picture updated successfully.");
                }))
                .addOnFailureListener(e -> {
                    hideProgressBar(); // Hide ProgressBar if upload fails
                    showSnackbar("Failed to upload profile picture.");
                });
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        userDatabase.child("profilePictureUrl").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // showSnackbar("Profile picture saved in database.");
                    } else {
                        showSnackbar("Failed to save profile picture in database.");
                    }
                });
    }

    private void updateProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        userDatabase.child("firstName").setValue(firstName);
        userDatabase.child("lastName").setValue(lastName);

        // Upload image only when user clicks the update button
        uploadImageToFirebase();

        showSnackbar("Profile updated successfully");
    }

    private void resetPassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.sendPasswordResetEmail(Objects.requireNonNull(user.getEmail())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSnackbar("Password reset email sent");
                } else {
                    showSnackbar("Failed to send reset email");
                }
            });
        }
    }

    private void showProgressBar() {
        progressNote.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressNote.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        customizeSnackbar(snackbar);
        snackbar.show();
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }
}
*/





/*
public class UserProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private ImageView profileImageView;
    private Button updateProfileButton, changePasswordButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private StorageReference storageReference;
    private Uri imageUri;
    private View rootView;
    private ProgressBar progressBar;
    private TextView progressNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("User Profile");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
            initializeViews();
            loadUserData();
            setButtonListeners();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        rootView = findViewById(android.R.id.content);
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.edit_new_email);
        profileImageView = findViewById(R.id.profile_image);
        updateProfileButton = findViewById(R.id.button_update_profile);
        changePasswordButton = findViewById(R.id.button_change_password);
        progressBar = findViewById(R.id.progress_bar);
        progressNote = findViewById(R.id.progress_note);
    }

    private void setButtonListeners() {
        updateProfileButton.setOnClickListener(v -> updateProfile());
        changePasswordButton.setOnClickListener(v -> resetPassword());
        profileImageView.setOnClickListener(v -> openImagePicker());
    }

    private void loadUserData() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    firstNameEditText.setText(userData.getFirstName());
                    lastNameEditText.setText(userData.getLastName());
                    emailEditText.setText(userData.getEmail());

                    if (userData.getProfilePictureUrl() != null) {
                        Glide.with(UserProfileActivity.this)
                                .load(userData.getProfilePictureUrl())
                                .placeholder(R.drawable.ic_profile)
                                .override(1024, 1024)
//                                .circleCrop()
                                .into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            startCrop(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri croppedUri = UCrop.getOutput(data);
            if (croppedUri != null) {
                profileImageView.setImageURI(croppedUri);
                imageUri = croppedUri; // Update the imageUri to the cropped image
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "Cropping failed: " + cropError, Toast.LENGTH_SHORT).show();
        }
    }

    private void startCrop(Uri uri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_profile_image.jpg"));
        UCrop uCrop = UCrop.of(uri, destinationUri);

        uCrop.withAspectRatio(1, 1) // Set a 1:1 aspect ratio for profile images
                .withMaxResultSize(512, 512) // Set the maximum resolution
                .start(this);
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            assert email != null;
            String baseFileName = email.split("@")[0];
            getImageCount(baseFileName);
        }
    }

    private void getImageCount(String baseFileName) {
        storageReference.listAll().addOnSuccessListener(listResult -> {
            int count = 0;
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith(baseFileName)) {
                    count++;
                }
            }
            String fileName = baseFileName + "_" + (count + 1) + ".jpg";
            uploadFile(fileName);
        }).addOnFailureListener(e -> showSnackbar("Failed to access storage."));
    }

    private void uploadFile(String fileName) {
        StorageReference fileRef = storageReference.child(fileName);
        showProgressBar();
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveImageUrlToDatabase(imageUrl);
                    hideProgressBar();
                    showSnackbar("Profile picture updated successfully.");
                }))
                .addOnFailureListener(e -> {
                    hideProgressBar();
                    showSnackbar("Failed to upload profile picture.");
                });
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        userDatabase.child("profilePictureUrl").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // showSnackbar("Profile picture saved in database.");
                    } else {
                        showSnackbar("Failed to save profile picture in database.");
                    }
                });
    }

    private void updateProfile() {
        String newFirstName = firstNameEditText.getText().toString().trim();
        String newLastName = lastNameEditText.getText().toString().trim();

        // Validate fields
        if (newFirstName.isEmpty()) {
            showSnackbar("First name is required");
            firstNameEditText.requestFocus();
            return;
        }

        if (newLastName.isEmpty()) {
            showSnackbar("Last name is required");
            lastNameEditText.requestFocus();
            return;
        }

        // Retrieve current values from Firebase
        userDatabase.child("firstName").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String currentFirstName = task.getResult().getValue(String.class);

                userDatabase.child("lastName").get().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful() && task2.getResult() != null) {
                        String currentLastName = task2.getResult().getValue(String.class);

                        // Check if new values are different from current values
                        if (newFirstName.equals(currentFirstName) && newLastName.equals(currentLastName)) {
                            showSnackbar("No changes detected");
                        } else {
                            // Update Firebase database with new values if changes are detected
                            userDatabase.child("firstName").setValue(newFirstName);
                            userDatabase.child("lastName").setValue(newLastName);
                            uploadImageToFirebase();
                            showSnackbar("Profile updated successfully");
                        }
                    }
                });
            }
        });
    }



    private void resetPassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.sendPasswordResetEmail(Objects.requireNonNull(user.getEmail())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSnackbar("Password reset email sent");
                } else {
                    showSnackbar("Failed to send reset email");
                }
            });
        }
    }

    private void showProgressBar() {
        progressNote.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressNote.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        customizeSnackbar(snackbar);
        snackbar.show();
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }
}
*/





public class UserProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private ImageView profileImageView;
    private Button updateProfileButton, changePasswordButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private StorageReference storageReference;
    private Uri imageUri;
    private View rootView;
    private ProgressBar progressBar;
    private TextView progressNote;
    private boolean isImageChanged = false; // Flag to track image change

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("User Profile");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
            initializeViews();
            loadUserData();
            setButtonListeners();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        rootView = findViewById(android.R.id.content);
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.edit_new_email);
        profileImageView = findViewById(R.id.profile_image);
        updateProfileButton = findViewById(R.id.button_update_profile);
        changePasswordButton = findViewById(R.id.button_change_password);
        progressBar = findViewById(R.id.progress_bar);
        progressNote = findViewById(R.id.progress_note);
    }

    private void setButtonListeners() {
        updateProfileButton.setOnClickListener(v -> updateProfile());
        changePasswordButton.setOnClickListener(v -> resetPassword());
        profileImageView.setOnClickListener(v -> openImagePicker());
    }

    private void loadUserData() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    firstNameEditText.setText(userData.getFirstName());
                    lastNameEditText.setText(userData.getLastName());
                    emailEditText.setText(userData.getEmail());

                    if (userData.getProfilePictureUrl() != null) {
                        Glide.with(UserProfileActivity.this)
                                .load(userData.getProfilePictureUrl())
                                .placeholder(R.drawable.ic_profile)
                                .override(1024, 1024)
//                                .circleCrop()
                                .into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            isImageChanged = true; // Set the flag to true when a new image is selected
            startCrop(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri croppedUri = UCrop.getOutput(data);
            if (croppedUri != null) {
                profileImageView.setImageURI(croppedUri);
                imageUri = croppedUri; // Update the imageUri to the cropped image
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "Cropping failed: " + cropError, Toast.LENGTH_SHORT).show();
        }
    }

    private void startCrop(Uri uri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_profile_image.jpg"));
        UCrop uCrop = UCrop.of(uri, destinationUri);

        uCrop.withAspectRatio(1, 1) // Set a 1:1 aspect ratio for profile images
                .withMaxResultSize(512, 512) // Set the maximum resolution
                .start(this);
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            assert email != null;
            String baseFileName = email.split("@")[0];
            getImageCount(baseFileName);
        }
    }

    private void getImageCount(String baseFileName) {
        storageReference.listAll().addOnSuccessListener(listResult -> {
            int count = 0;
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith(baseFileName)) {
                    count++;
                }
            }
            String fileName = baseFileName + "_" + (count + 1) + ".jpg";
            uploadFile(fileName);
        }).addOnFailureListener(e -> showSnackbar("Failed to access storage."));
    }

    private void uploadFile(String fileName) {
        StorageReference fileRef = storageReference.child(fileName);
        showProgressBar();
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveImageUrlToDatabase(imageUrl);
                    hideProgressBar();
                    showSnackbar("Profile picture updated successfully.");
                }))
                .addOnFailureListener(e -> {
                    hideProgressBar();
                    showSnackbar("Failed to upload profile picture.");
                });
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        userDatabase.child("profilePictureUrl").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // showSnackbar("Profile picture saved in database.");
                    } else {
                        showSnackbar("Failed to save profile picture in database.");
                    }
                });
    }

    private void updateProfile() {
        String newFirstName = firstNameEditText.getText().toString().trim();
        String newLastName = lastNameEditText.getText().toString().trim();

        // Validate fields
        if (newFirstName.isEmpty()) {
            showSnackbar("First name is required");
            firstNameEditText.requestFocus();
            return;
        }

        if (newLastName.isEmpty()) {
            showSnackbar("Last name is required");
            lastNameEditText.requestFocus();
            return;
        }

        // Retrieve current values from Firebase
        userDatabase.child("firstName").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String currentFirstName = task.getResult().getValue(String.class);

                userDatabase.child("lastName").get().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful() && task2.getResult() != null) {
                        String currentLastName = task2.getResult().getValue(String.class);

                        // Check if new values are different from current values
                        boolean isNameChanged = !newFirstName.equals(currentFirstName) || !newLastName.equals(currentLastName);
                        boolean isImageChanged = this.isImageChanged; // Check if the image has been changed

                        // Check if no changes are detected
                        if (!isNameChanged && !isImageChanged) {
                            showSnackbar("No changes detected");
                        } else {
                            // Update Firebase database with new values if changes are detected
                            if (isNameChanged) {
                                userDatabase.child("firstName").setValue(newFirstName);
                                userDatabase.child("lastName").setValue(newLastName);
                            }
                            // Upload the image only if it has changed
                            if (isImageChanged) {
                                uploadImageToFirebase();
                            }
                            showSnackbar("Profile updated successfully");
                        }
                    }
                });
            }
        });
    }

    private void resetPassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.sendPasswordResetEmail(Objects.requireNonNull(user.getEmail())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSnackbar("Password reset email sent");
                } else {
                    showSnackbar("Failed to send reset email");
                }
            });
        }
    }

    private void showProgressBar() {
        progressNote.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressNote.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        customizeSnackbar(snackbar);
        snackbar.show();
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }
}




