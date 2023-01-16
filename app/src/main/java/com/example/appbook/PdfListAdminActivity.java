package com.example.appbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.appbook.databinding.ActivityPdfListAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PdfListAdminActivity extends AppCompatActivity {
    ActivityPdfListAdminBinding binding;
    ArrayList<Modelpdf>pdfArrayList;
    AdapterpdfAdmin adapterpdfAdmin;

    String categoryId,categoryTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
       categoryId= intent.getStringExtra("categoryId");
      categoryTitle  =intent.getStringExtra("categoryTitle");

      binding.subTitileTv.setText(categoryTitle);
        
        loadpdflist();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.searshEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    adapterpdfAdmin.getFilter().filter(charSequence);
                }catch (Exception e){
                    Toast.makeText(PdfListAdminActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadpdflist() {
        pdfArrayList=new ArrayList<>();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfArrayList.clear();
                        for (DataSnapshot ds :snapshot.getChildren()){
                            Modelpdf modelpdf=ds.getValue(Modelpdf.class);
                            pdfArrayList.add(modelpdf);
                        }
                        adapterpdfAdmin=new AdapterpdfAdmin(PdfListAdminActivity.this,pdfArrayList);
                        binding.bookRV.setAdapter(adapterpdfAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}