package com.example.appbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.appbook.databinding.ActivityPdfEditBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfEditActivity extends AppCompatActivity {
    ActivityPdfEditBinding binding;
    String bookId;
    ProgressDialog progressDialog;

    private ArrayList<String> CategoryTitleArrayList,CategoryIdArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookId= getIntent().getStringExtra("bookId");

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);
       
        loadCategory();
        loadBookinfo();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog();
            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();

            }
        });
    }
 String title="",description="";
    private void Validate() {
        title=binding.bookEt.getText().toString();
        description=binding.descriptionEt.getText().toString();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "enter your titel", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "enter description", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(selectedCategoryId)) {
            Toast.makeText(this, "Pick category", Toast.LENGTH_SHORT).show();

        }else {
            updatepdf();
        }
    }

    private void updatepdf() {
        progressDialog.setMessage("update pdf");
        progressDialog.show();

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("titel",""+title);
        hashMap.put("description",""+description);
        hashMap.put("categoryId",""+selectedCategoryId);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Books");
       databaseReference.child(bookId)
               .updateChildren(new HashMap<>()).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused) {
               progressDialog.dismiss();
               Toast.makeText(PdfEditActivity.this, "update book", Toast.LENGTH_SHORT).show();

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
          progressDialog.dismiss();
               Toast.makeText(PdfEditActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });

    }

    private void loadBookinfo() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        selectedCategoryId=""+snapshot.child("categoryId").getValue();
                        String des=""+snapshot.child("categoryId").getValue();
                        String Title=""+snapshot.child("categoryId").getValue();

                        binding.bookEt.setText(Title);
                        binding.descriptionEt.setText(des);

                        DatabaseReference refbook= FirebaseDatabase.getInstance().getReference("category");
                        refbook.child(selectedCategoryId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String category=""+snapshot.child("category").getValue();
                                        binding.categoryTv.setText(category);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    String selectedCategoryId="",selectedCategoryTitle="";


    private void categoryDialog() {
        String[] categoriesArray=new String[CategoryTitleArrayList.size()];
        for (int i =0;i<CategoryTitleArrayList.size();i++){
            categoriesArray[i]=CategoryTitleArrayList.get(i);
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick category").setItems(categoriesArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                selectedCategoryTitle=CategoryTitleArrayList.get(i);
                selectedCategoryId=CategoryIdArrayList.get(i);

                binding.categoryTv.setText(selectedCategoryTitle);

            }
        }).show();

    }
    private void loadCategory() {
        CategoryIdArrayList=new ArrayList<>();
        CategoryTitleArrayList=new ArrayList<>();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("category");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CategoryTitleArrayList.clear();
                CategoryIdArrayList.clear();
                for(DataSnapshot ds :snapshot.getChildren()){

                    String categoryId=""+ds.child("id").getValue();
                    String categoryTitle=""+ds.child("category").getValue();

                    CategoryTitleArrayList.add(categoryTitle);
                    CategoryIdArrayList.add(categoryId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}