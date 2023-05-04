package com.example.firebaseassig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    String email, mobile, name;

    TextView emailTv,phoneTv, nameTv, updateDataTv;
    ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailTv = findViewById(R.id.Email);
        nameTv = findViewById(R.id.FullName);
        phoneTv = findViewById(R.id.Mobile);


        updateDataTv = findViewById(R.id.update);

        imageView = findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, uploaddata.class);
                startActivity(intent);
            }
        });

        updateDataTv.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateData.class);
                startActivity(intent);
            }
        });




        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(MainActivity.this, "something error", Toast.LENGTH_SHORT).show();
        }else {
            showUserProfile(firebaseUser);
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User readUserDetails = snapshot.getValue(User.class);
                if (readUserDetails != null){
                    email = firebaseUser.getEmail();
                    name = firebaseUser.getDisplayName();
                    mobile = readUserDetails.mobile;


                    emailTv.setText(email);
                    nameTv.setText(name);
                    phoneTv.setText(mobile);


//                    Uri uri = firebaseUser.getPhotoUrl();
//
//                    Picasso.with(MainActivity.this).load(uri).into(imageView);
//                }else {
//                    Toast.makeText(MainActivity.this, "Something error", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this, "something error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}