package com.example.firebaseassig;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignUp extends AppCompatActivity {



protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText Email = findViewById(R.id.Email);
        EditText editTextSignupPassword = findViewById(R.id.Password);
        EditText FullName = findViewById(R.id.FullName);
        EditText editTextMobile = findViewById(R.id.Mobile);
        Button SignUp = findViewById(R.id.signup);
        TextView signupText = findViewById(R.id.signupText);

        signupText.setOnClickListener(view1 -> startActivity(new Intent(SignUp.this, LoginActivity.class)));


        SignUp.setOnClickListener(view -> {
        String email = Email.getText().toString().trim();
        String pass = editTextSignupPassword.getText().toString().trim();
        String name = FullName.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();


        if (TextUtils.isEmpty(email)){
                Email.setError("Fill in the email field");
        } else if (TextUtils.isEmpty(name)){
                FullName.setError("Fill in the fullName field");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Email.setError("Valid email is required");
                FullName.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
        editTextSignupPassword.setError("Password is required");

        }else if (TextUtils.isEmpty(mobile)) {
        editTextMobile.setError("Fill in the mobile field");
        }else {



        FirebaseMessaging.getInstance().subscribeToTopic("naada")
        .addOnCompleteListener(new OnCompleteListener<Void>() {
@Override
public void onComplete(@NonNull Task<Void> task) {
        Log.e("tag", "Done");
        if (!task.isSuccessful()){
        Log.e("tag", "Failed");
        }
        }
        });
        }
        });
        }

private void registerUser(String name, String email, String pass, String mobile, String gender) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(SignUp.this,
        new OnCompleteListener<AuthResult>() {
@Override
public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
        Toast.makeText(SignUp.this, "User Register Successfully", Toast.LENGTH_SHORT).show();

        FirebaseUser firebaseUser = auth.getCurrentUser();

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        firebaseUser.updateProfile(profileChangeRequest);

        User writeUserDetails = new User(email,name,mobile,gender);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

        reference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(SignUp.this, "User registered successfully, PLease verify your email", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                        } else {
                                Toast.makeText(SignUp.this, "User registered failed", Toast.LENGTH_SHORT).show();

                        };
                };
        });
        };
};

        });
};
}



