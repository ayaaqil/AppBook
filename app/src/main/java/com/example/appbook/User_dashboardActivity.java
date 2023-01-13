package com.example.appbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appbook.databinding.ActivityUserDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_dashboardActivity extends AppCompatActivity {
    ActivityUserDashboardBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth= FirebaseAuth.getInstance();

        Cheakuser();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Cheakuser();
            }
        });
    }

    private void Cheakuser() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            String email=firebaseUser.getEmail();
            binding.subTitileTv.setText(email);
        }
    }
}



