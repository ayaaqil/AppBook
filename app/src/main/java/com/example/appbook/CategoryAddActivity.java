package com.example.appbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.appbook.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {
    ActivityCategoryAddBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.Submeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valideData();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    String Category_name="";
    private void valideData() {
    Category_name=binding.nameCategoryAdd.getEditText().getText().toString();

    if(TextUtils.isEmpty(Category_name)){
        Toast.makeText(this, "please enter category", Toast.LENGTH_SHORT).show();
    }else {
        addCategoryFirebase();
    }
    }

    private void addCategoryFirebase() {
        progressDialog.setMessage("added category");
        progressDialog.show();

        long timestamp= System.currentTimeMillis();



        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("category",Category_name);
        hashMap.put("timestamp",timestamp);
        hashMap.put("uid",firebaseAuth.getUid());


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("category");
        databaseReference.child(""+timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(CategoryAddActivity.this, "category added sucssful....", Toast.LENGTH_SHORT).show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CategoryAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}