package com.example.appbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.appbook.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Animation top;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        top= AnimationUtils.loadAnimation(this,R.anim.top_splash);


        binding.bookIcon.setAnimation(top);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              Cheakuser();
            }
        }, 5000);
    }

    private void Cheakuser() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            startActivity(new Intent(splashActivity.this,MainActivity.class));
            finish();

        }else {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
            databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String usertype= ""+dataSnapshot.child("usertype").getValue();

                    if(usertype.equals("user")){
                        Intent intent=new Intent(splashActivity.this,User_dashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }else  if(usertype.equals("admin")){
                        Intent intent=new Intent(splashActivity.this,DasboardAdminActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}