package com.example.appbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.appbook.databinding.ActivityRegestirBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegestirActivity extends AppCompatActivity {
    ActivityRegestirBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegestirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.regestir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valideData();

            }


        });


    }
    private String name="" ,email = "",password="",Cpassword="";

    private void valideData() {
        name=binding.name.getText().toString();
        email=binding.emailEt.getText().toString();
        password=binding.passwordEt.getText().toString();
        Cpassword=binding.confirmPasword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "enter your name", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "enter your email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Cpassword)){
            Toast.makeText(this, "enter cpassword", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(Cpassword)){
            Toast.makeText(this, "password does not matches", Toast.LENGTH_SHORT).show();
        }else {
            createUserAccount();
        }




    }

    private void createUserAccount() {
        progressDialog.setMessage("creating Account..");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                updateUserinfo();

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegestirActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void updateUserinfo() {
        progressDialog.setTitle("Saving User info...");
        long timestamp= System.currentTimeMillis();

        String uid=firebaseAuth.getUid();

        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("email",email);
        hashMap.put("name",name);
        hashMap.put("profileImge","");
        hashMap.put("usertype","user");
        hashMap.put("timestamp",timestamp);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(RegestirActivity.this, "account created...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RegestirActivity.this,User_dashboardActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             progressDialog.dismiss();
                Toast.makeText(RegestirActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}