package com.quarantino.ruto.LoginActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quarantino.ruto.Activities.MainDashboard;
import com.quarantino.ruto.HelperClasses.Preferences.sharedPrefs;
import com.quarantino.ruto.HelperClasses.UserHelperClass;
import com.quarantino.ruto.HelperClasses.UserHelperClassFirebase;
import com.quarantino.ruto.R;

public class SignUpScreen extends AppCompatActivity {

    TextView signUpHeading;
    private TextInputLayout signUpNameInput, signUpUsernameInput, signUpEmailInput, signUpPasswordInput;
    Button signUpBtn;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        //Hooks
        signUpHeading = findViewById(R.id.headingSignUp);
        signUpNameInput = findViewById(R.id.signUpNameInput);
        signUpUsernameInput = findViewById(R.id.signUpUsernameInput);
        signUpPasswordInput = findViewById(R.id.signUpPasswordInput);
        signUpEmailInput = findViewById(R.id.signUpEmailInput);
        signUpBtn = findViewById(R.id.signUpBtn);

        //User logged in or not instance
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void openLogIn(View view) {
        startActivity(new Intent(this, LoginScreen.class));
        finish();
    }

    private Boolean validateName() {
        String value = signUpNameInput.getEditText().getText().toString();

        if (value.isEmpty()) {
            signUpNameInput.setError("Field cannot be empty.");
            signUpNameInput.requestFocus();
            return false;
        } else {
            signUpNameInput.setError(null);
            signUpNameInput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String value = signUpEmailInput.getEditText().getText().toString();

        if (value.isEmpty()) {
            signUpEmailInput.setError("Field cannot be empty.");
            signUpEmailInput.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            signUpEmailInput.setError("Enter a valid email address.");
            signUpEmailInput.requestFocus();
            return false;
        } else {
            signUpEmailInput.setError(null);
            signUpEmailInput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String value = signUpUsernameInput.getEditText().getText().toString();
        boolean hasWhiteSpace = value.contains(" ");
        boolean hasUpperCase = value.equals(value.toLowerCase());

        String pattern = "^[a-zA-Z0-9_]*$";

        if (value.isEmpty()) {
            signUpUsernameInput.setError("Field cannot be empty.");
            signUpUsernameInput.requestFocus();
            return false;
        } else if (value.length() >= 15) {
            signUpUsernameInput.setError("Username too long.");
            signUpUsernameInput.requestFocus();
            return false;
        } else if (hasWhiteSpace) {
            signUpUsernameInput.setError("Username cannot contain white spaces.");
            signUpUsernameInput.requestFocus();
            return false;
        } else if (hasUpperCase == false) {
            signUpUsernameInput.setError("Username can only have lowercase characters.");
            signUpUsernameInput.requestFocus();
            return false;
        } else if (!value.matches(pattern)) {
            signUpUsernameInput.setError("Username can only have underscores.");
            signUpUsernameInput.requestFocus();
            return false;
        } else {
            signUpUsernameInput.setError(null);
            signUpUsernameInput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = signUpPasswordInput.getEditText().getText().toString();
        String passwordReq = "^" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{4,}" + "$";
        // String passwordNoSpace = "(?=\\S+$)";

        if (value.isEmpty()) {
            signUpPasswordInput.setError("Field cannot be empty.");
            signUpPasswordInput.requestFocus();
            return false;
        } else if (value.length() < 8) {
            signUpPasswordInput.setError("Password is too short. It should be at least 8 characters long.");
            signUpPasswordInput.requestFocus();
            return false;
        } else if (!value.matches(passwordReq)) {
            signUpPasswordInput.setError("Password is too weak. Try mixing lowercase, uppercase and special characters for your password.");
            signUpPasswordInput.requestFocus();
            return false;
        }
        // else if(!value.matches(passwordNoSpace)){
        //    signUpPasswordInput.setError("Password cannot have spaces.");
        //    return false;
        // }
        else {
            signUpPasswordInput.setError(null);
            signUpPasswordInput.setErrorEnabled(false);
            return true;
        }
    }

    public void createNewUser(View view) {
        //Validation
        if (!validateName() || !validateUsername() || !validateEmail() || !validatePassword()) {
            return;
        }

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String userName = signUpNameInput.getEditText().getText().toString();
                final String userUsername = signUpUsernameInput.getEditText().getText().toString();
                final String userPassword = signUpPasswordInput.getEditText().getText().toString();
                final String userEmail = signUpEmailInput.getEditText().getText().toString();

                if (dataSnapshot.hasChild(userUsername)) {
                    signUpUsernameInput.setError("Username already taken.");
                } else {
                        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("users");

                                //SharedPreferences : Storing user Info in Firebase
                                UserHelperClassFirebase helperClass = new UserHelperClassFirebase(userName, userUsername, userEmail, userPassword);
                                reference.child(userUsername).setValue(helperClass);

                                //SharedPreferences : Storing user Info Locally
                                UserHelperClass helperClassLocal = new UserHelperClass(getApplicationContext());
                                helperClassLocal.setName(userName);
                                helperClassLocal.setUsername(userUsername);
                                helperClassLocal.setPassword(userPassword);
                                helperClassLocal.setEmail(userEmail);

                                //SharedPreferences : Login Token
                                sharedPrefs preference = new sharedPrefs(getApplicationContext());
                                preference.setIsLoggedIn(true);

                                //Start next activity
                                Intent intent = new Intent(getApplicationContext(), MainDashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                signUpEmailInput.setError("You already have an account");
                                signUpEmailInput.requestFocus();
                                return;
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
