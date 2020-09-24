package com.quarantino.ruto.LoginActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.quarantino.ruto.HelperClasses.userHelperClass;
import com.quarantino.ruto.HelperClasses.userHelperClassFirebase;
import com.quarantino.ruto.R;

public class SignUpScreen extends AppCompatActivity {

    Animation imageFadeUp, textField1FadeUp, textField2FadeUp, textField3FadeUp, textField4FadeUp, headingFadeUp, subHeadingFadeUp, button1FadeUp, button2FadeUp;

    ImageView signUpIllustration;
    TextView signUpHeading, signUpSubHeading;
    private TextInputLayout signUpNameInput, signUpUsernameInput, signUpEmailInput, signUpPasswordInput;
    Button signUpBtn, alreadyMemberBtn;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        //Animations
//        headingFadeUp = AnimationUtils.loadAnimation(this, R.anim.su_li_headingup);
//        subHeadingFadeUp = AnimationUtils.loadAnimation(this, R.anim.su_li_subheadingup);
//        imageFadeUp = AnimationUtils.loadAnimation(this, R.anim.su_li_imageup);
//        textField1FadeUp = AnimationUtils.loadAnimation(this, R.anim.su_li_textfield1up);
//        textField2FadeUp = AnimationUtils.loadAnimation(this, R.anim.su_li_textfield2up);
//        textField3FadeUp = AnimationUtils.loadAnimation(this, R.anim.signup_textfield3up);
//        textField4FadeUp = AnimationUtils.loadAnimation(this, R.anim.signup_textfield4up);
//        button1FadeUp = AnimationUtils.loadAnimation(this, R.anim.signup_button1up);
//        button2FadeUp = AnimationUtils.loadAnimation(this, R.anim.signup_button2up);

        //Hooks
//        signUpIllustration = findViewById(R.id.illustrationSignUp);
        signUpHeading = findViewById(R.id.headingSignUp);
//        signUpSubHeading = findViewById(R.id.subHeadingSignUp);
        signUpNameInput = findViewById(R.id.signUpNameInput);
        signUpUsernameInput = findViewById(R.id.signUpUsernameInput);
//        signUpPhoneNumInput = findViewById(R.id.signUpPhoneNumInput);
        signUpPasswordInput = findViewById(R.id.signUpPasswordInput);
        signUpEmailInput = findViewById(R.id.signUpEmailInput);
        signUpBtn = findViewById(R.id.signUpBtn);
//        alreadyMemberBtn = findViewById(R.id.alreadyMemberBtn);

        //Animation Assignment
//        signUpHeading.setAnimation(headingFadeUp);
//        signUpSubHeading.setAnimation(subHeadingFadeUp);
//        signUpIllustration.setAnimation(imageFadeUp);
//        signUpNameInput.setAnimation(textField1FadeUp);
//        signUpUsernameInput.setAnimation(textField2FadeUp);
//        signUpPhoneNumInput.setAnimation(textField3FadeUp);
//        signUpPasswordInput.setAnimation(textField4FadeUp);
//        signUpBtn.setAnimation(button1FadeUp);
//        alreadyMemberBtn.setAnimation(button2FadeUp);

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
//        registerUser();
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
                    // userHelperClass helperClass = new userHelperClass(userName, userUsername ,userPhoneNumber, userPassword);
                    // reference.child(userUsername).setValue(helperClass);
                    authStateListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            if(firebaseAuth.getCurrentUser() != null){
                                signUpEmailInput.setError("You already have an account");
                                signUpEmailInput.requestFocus();
                                return;
                            }
                        }
                    };

                    firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("users");

                                //SharedPreferences : Storing user Info in Firebase
                                userHelperClassFirebase helperClass = new userHelperClassFirebase(userName, userUsername, userEmail,  userPassword);
                                reference.child(userUsername).setValue(helperClass);

                                //SharedPreferences : Storing user Info Locally
                                userHelperClass helperClassLocal = new userHelperClass(getApplicationContext());
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
                            }
                        }
                    });

//                    rootNode = FirebaseDatabase.getInstance();
//                    reference = rootNode.getReference("users");
//
//                    //SharedPreferences : Storing user Info in Firebase
//                    userHelperClassFirebase helperClass = new userHelperClassFirebase(userName, userUsername, userEmail,  userPassword);
//                    reference.child(userUsername).setValue(helperClass);
//
//                    //SharedPreferences : Storing user Info Locally
//                    userHelperClass helperClassLocal = new userHelperClass(getApplicationContext());
//                    helperClassLocal.setName(userName);
//                    helperClassLocal.setUsername(userUsername);
//                    helperClassLocal.setPassword(userPassword);
//                    helperClassLocal.setEmail(userEmail);
//
//                    //SharedPreferences : Login Token
//                    sharedPrefs preference = new sharedPrefs(getApplicationContext());
//                    preference.setIsLoggedIn(true);
//
//                    //Start next activity
//                    Intent intent = new Intent(getApplicationContext(), MainDashboard.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void registerUser() {
        String userName = signUpNameInput.getEditText().getText().toString();
        String userUsername = signUpUsernameInput.getEditText().getText().toString();;
        String userPassword = signUpPasswordInput.getEditText().getText().toString();
        String userEmail = signUpEmailInput.getEditText().getText().toString();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    signUpEmailInput.setError("Hello");
                }
            }
        };

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
