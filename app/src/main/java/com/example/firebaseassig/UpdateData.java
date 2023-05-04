package com.example.firebaseassig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateData extends AppCompatActivity {

    private EditText editTextUpdateEmail, editTextUpdateMobile;
    private String txtEmail, txtFullName, txtMobile;
    private FirebaseAuth authProfile;
    private ProgressDialog progressDialog;
    private Object User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        editTextUpdateEmail = findViewById(R.id.Email);
        editTextUpdateMobile = findViewById(R.id.Mobile);


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showProfile(firebaseUser);

        Button  upload= findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateData.this, uploaddata.class));
                finish();
            }
        });



        Button btnUpdateProfile = findViewById(R.id.update);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);
            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser) {


         if (TextUtils.isEmpty(txtMobile)) {
            editTextUpdateMobile.setError("Fill in the mobile field");
        }else {
            txtEmail = editTextUpdateEmail.getText().toString();
            txtMobile = editTextUpdateMobile.getText().toString();


            User writeUserDetails = new User(txtEmail,txtFullName,txtMobile);
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
            String userID = firebaseUser.getUid();
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            referenceProfile.child(userID).setValue(User).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(txtFullName).build();
                        firebaseUser.updateProfile(profileUpdate);

                        Toast.makeText(UpdateData.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateData.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(UpdateData.this, "errorrrrrrrrrrrrrr", Toast.LENGTH_SHORT).show();
                    }
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File");
        progressDialog.show();

        String userIDRegister = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userIDRegister).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User readUserDetails = snapshot.getValue(User.class);
                if (readUserDetails != null){
                    txtEmail = firebaseUser.getEmail();
                    txtFullName = firebaseUser.getDisplayName();
                    txtMobile= readUserDetails.mobile;

                    editTextUpdateEmail.setText(txtEmail);
                    editTextUpdateMobile.setText(txtMobile);
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateData.this, "Something error", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }
}