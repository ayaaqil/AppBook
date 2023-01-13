package com.example.appbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appbook.databinding.ActivityPdfAddBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PDF_Add_Activity extends AppCompatActivity {
    ActivityPdfAddBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
         binding.attachBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 pdfpickIntent();
             }
         });


    }

    private void pdfpickIntent() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
    }
}