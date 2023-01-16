package com.example.appbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.appbook.databinding.ActivityPdfAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class PDF_Add_Activity extends AppCompatActivity {
    private static final int PDF_PICK_CODE = 100;
    private Uri pdfuri=null;
    ActivityPdfAddBinding binding;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private ArrayList<String>CategoryTitleArrayList,CategoryIdArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();
        loadPdfcategory();


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);

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

         binding.categoryTv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 categoryPickDialog();
             }
         });

         binding.submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 validateData();
             }
         });


    }
     private  String titel="",description="";
    private void validateData() {
        titel=binding.bookEt.getText().toString();
        description=binding.descriptionEt.getText().toString();


        if(TextUtils.isEmpty(titel)){
            Toast.makeText(this, "enter your titel", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "enter description", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(selectedCategoryTitle)){
            Toast.makeText(this, "enter category", Toast.LENGTH_SHORT).show();


        }else if (pdfuri==null) {
            Toast.makeText(this, "Pick pdf", Toast.LENGTH_SHORT).show();


        }else {
            uploadPdfStorage();
        }


    }

    private void uploadPdfStorage() {
        progressDialog.setTitle("Uploading pdg...");
        progressDialog.show();

        long timetamp= System.currentTimeMillis();

        String filepathAndName="Books/"+timetamp;

        StorageReference storageReference= FirebaseStorage.getInstance().getReference(filepathAndName);
        storageReference.putFile(pdfuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri>uriTask= taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                    String UploadedpdfUrl=""+uriTask.getResult();

                    uploadpdfinfoTodb(UploadedpdfUrl,timetamp);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PDF_Add_Activity.this, "pdf upload failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void uploadpdfinfoTodb(String uploadedpdfUrl, long timetamp) {
        progressDialog.setMessage("uploding pdf info...");
        String uid= firebaseAuth.getUid();
        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("uid",""+uid);
        hashMap.put("id",""+timetamp);
        hashMap.put("titel",""+titel);
        hashMap.put("description",""+description);
        hashMap.put("categoryId",""+selectedCategoryId);
        hashMap.put("url",""+uploadedpdfUrl);
        hashMap.put("timetamp",""+timetamp);
        hashMap.put("viewsCount",""+timetamp);
        hashMap.put("downloadsCount",""+timetamp);


        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.child(""+timetamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(PDF_Add_Activity.this, "sccesfulley", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PDF_Add_Activity.this, "faile to upload to db due"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });






    }

    private void loadPdfcategory() {
        CategoryTitleArrayList=new ArrayList<>();
        CategoryIdArrayList=new ArrayList<>();
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
    private String selectedCategoryId,selectedCategoryTitle;

    private void categoryPickDialog() {
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

    private void pdfpickIntent() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select pdf"),PDF_PICK_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_OK){
            if(requestCode==PDF_PICK_CODE){
                pdfuri=data.getData();

            }else {
                Toast.makeText(this, "cancelled parking pdf", Toast.LENGTH_SHORT).show();

            }
        }
    }
}