package com.example.firebaseassig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseassig.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        TextView textEmailAddress = findViewById(R.id.Email);
        TextView loginPassword = findViewById(R.id.Password);
        TextView loginBtn = findViewById(R.id.Login);
        TextView signupTxt = findViewById(R.id.signupText);

        loginBtn.setOnClickListener(view -> {
            String email = textEmailAddress.getText().toString().trim();
            String pass = loginPassword.getText().toString().trim();

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!pass.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseMessaging.getInstance().subscribeToTopic("Samar")
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.e("tag", "Done");
                                                        if (!task.isSuccessful()){
                                                            Log.e("tag", "Failed");
                                                        }
                                                    }
                                                });

                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity .class);
                                        intent.putExtra("email", user.getEmail());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }});
                }else {
                    loginPassword.setError("Fill the pass field");
                }
            } else if (email.isEmpty()) {
                textEmailAddress.setError("Fill the email field");
            }else {
                textEmailAddress.setError("Please enter valid email");
            }
        });

        signupTxt.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUp.class)));
    }
}
