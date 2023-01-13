package com.example.appbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.appbook.databinding.ActivityDasboardAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DasboardAdminActivity extends AppCompatActivity {
ActivityDasboardAdminBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelCategory>modelCategoryArrayList;
    private  AdapterCategory adapterCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDasboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth= FirebaseAuth.getInstance();
        Cheakuser();

        binding.searshEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                adapterCategory.getFilter().filter(charSequence);

            }catch (Exception e){
                Toast.makeText(DasboardAdminActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Cheakuser();
                loadCategory();
            }
        });

        binding.addcategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DasboardAdminActivity.this,CategoryAddActivity.class);
                startActivity(intent);
            }
        });

        binding.addpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DasboardAdminActivity.this,PDF_Add_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void loadCategory() {

        modelCategoryArrayList=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("category");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelCategoryArrayList.clear();

                for (DataSnapshot ds:snapshot.getChildren()){
                    ModelCategory modelCategory=ds.getValue(ModelCategory.class);
                    modelCategoryArrayList.add(modelCategory);
                }
                AdapterCategory adapterCategory=new AdapterCategory(modelCategoryArrayList);
                binding.categoryList.setAdapter(adapterCategory);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Cheakuser() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            Intent intent=new Intent(DasboardAdminActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            String email=firebaseUser.getEmail();
            binding.subTitileTv.setText(email);
        }
    }
}