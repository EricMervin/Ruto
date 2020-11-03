package com.quarantino.ruto.LoginActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.quarantino.ruto.Activities.MainDashboard;
import com.quarantino.ruto.Activities.PermissionsActivity;
import com.quarantino.ruto.HelperClasses.Preferences.sharedPrefs;
import com.quarantino.ruto.HelperClasses.UserHelperClass;
import com.quarantino.ruto.HelperClasses.UserHelperClassFirebase;
import com.quarantino.ruto.R;

public class LoginScreen extends AppCompatActivity {

    private static final int RC_SIGN_IN = 710;
    Animation imageFadeUp, textField1FadeUp, textField2FadeUp, headingFadeUp, subHeadingFadeUp, button1FadeUp, button2FadeUp;

    ImageView logInIllustration;
    TextView logInHeading, logInSubHeading;
    TextInputLayout logInUsernameInput, logInPasswordInput;
    Button logInBtn, googleLogInButton;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private GoogleSignInClient firebaseGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        //Hooks
        logInHeading = findViewById(R.id.headingLogIn);
        logInUsernameInput = findViewById(R.id.logInUsernameInput);
        logInPasswordInput = findViewById(R.id.logInPasswordInput);
        logInBtn = findViewById(R.id.logInBtn);
        googleLogInButton = findViewById(R.id.googleButton);

        googleRequest();

        googleLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        //Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        UserHelperClass userData = new UserHelperClass(getApplicationContext());
        logInUsernameInput.getEditText().setText(userData.getUsername());
    }

    private void googleRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        firebaseGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void googleSignIn() {
        Intent signInIntent = firebaseGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("users");

                    String userEmail = user.getEmail();
                    int index = userEmail.indexOf('@');
                    String userUsername = userEmail.substring(0, index);
                    userUsername = userUsername.replace('.', '_');

                    //Auto generated name and password
                    final String userName = user.getDisplayName();
                    final String userPassword = "test@123";

                    Query checkUser = reference.orderByChild("username").equalTo(userUsername);

                    final String finalUserUsername = userUsername;
                    final String finalUserEmail = userEmail;

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String nameFromDB = dataSnapshot.child(finalUserUsername).child("name").getValue(String.class);
                                String usernameFromDB = dataSnapshot.child(finalUserUsername).child("username").getValue(String.class);
                                String emailFromDB = dataSnapshot.child(finalUserUsername).child("email").getValue(String.class);
                                String passwordFromDB = dataSnapshot.child(finalUserUsername).child("password").getValue(String.class);

                                //SharedPreferences : Storing user Info
                                UserHelperClass helperClass = new UserHelperClass(getApplicationContext());
                                helperClass.setName(nameFromDB);
                                helperClass.setUsername(usernameFromDB);
                                helperClass.setPassword(passwordFromDB);
                                helperClass.setEmail(emailFromDB);

                                Log.d("Google Auth", "User exists");
                            } else {
                                // SharedPreferences : Storing user Info in Firebase
                                UserHelperClassFirebase helperClass = new UserHelperClassFirebase(userName, finalUserUsername, finalUserEmail, userPassword);
                                reference.child(finalUserUsername).setValue(helperClass);

                                //SharedPreferences : Storing user Info Locally
                                UserHelperClass helperClassLocal = new UserHelperClass(getApplicationContext());
                                helperClassLocal.setName(userName);
                                helperClassLocal.setUsername(finalUserUsername);
                                helperClassLocal.setPassword(userPassword);
                                helperClassLocal.setEmail(finalUserEmail);

                                Log.d("Google Auth", "New user created");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //SharedPreferences : Login Token
                    sharedPrefs preference = new sharedPrefs(getApplicationContext());
                    preference.setIsLoggedIn(true);
                    preference.setPermission(true);

                    //Start next activity
                    Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
//                    Log.d("User Name", userName);
//                    Log.d("User Username", userUsername);
//                    Log.d("User Email", userEmail);
//                    Log.d("User Password", userPassword);
                } else {
                    Log.d("Google Sign In Error", task.getException().toString());
                }
            }
        });
    }

    private Boolean validateUsername() {
        String value = logInUsernameInput.getEditText().getText().toString();

        if (value.isEmpty()) {
            logInUsernameInput.setError("Field cannot be empty.");
            logInUsernameInput.requestFocus();
            return false;
        } else {
            logInUsernameInput.setError(null);
            logInUsernameInput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = logInPasswordInput.getEditText().getText().toString();

        if (value.isEmpty()) {
            logInPasswordInput.setError("Field cannot be empty.");
            logInPasswordInput.requestFocus();
            return false;
        } else {
            logInPasswordInput.setError(null);
            logInPasswordInput.setErrorEnabled(false);
            return true;
        }
    }

    public void authenticateUser(View view) {
        if (!validateUsername() || !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        final String userEnteredUsername = logInUsernameInput.getEditText().getText().toString().trim();
        final String userEnteredPassword = logInPasswordInput.getEditText().getText().toString().trim();

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordFromDatabase = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    logInUsernameInput.setError(null);
                    logInUsernameInput.setErrorEnabled(false);

                    if (passwordFromDatabase.equals(userEnteredPassword)) {
                        logInUsernameInput.setError(null);
                        logInUsernameInput.setErrorEnabled(false);

                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        //SharedPreferences : Storing user Info
                        UserHelperClass helperClass = new UserHelperClass(getApplicationContext());
                        helperClass.setName(nameFromDB);
                        helperClass.setUsername(usernameFromDB);
                        helperClass.setPassword(userEnteredPassword);
                        helperClass.setEmail(emailFromDB);

                        //SharedPreferences : Login Token
                        sharedPrefs preference = new sharedPrefs(getApplicationContext());
                        preference.setIsLoggedIn(true);

                        //Start Next Activity : Main Dashboard
                        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                        finish();
                    } else {
                        logInPasswordInput.setError("Wrong password entered.");
                        logInPasswordInput.requestFocus();
                    }
                } else {
                    logInUsernameInput.setError("No user found. Check username or create an account.");
                    logInUsernameInput.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openSignUp(View view) {
        startActivity(new Intent(this, SignUpScreen.class));
        finish();
    }
}
