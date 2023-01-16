package com.example.appbook;

import static com.example.appbook.Constanes.MAX_BYTS_PDF;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static final String formatTimestamp(long timestamp){
        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String data= DateFormat.format("dd/MM/yyyy",cal).toString();

        return data;
    }

    public static void deleteBook(Context context,String bookId,String bookUrl,String bookTitle) {

        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("please waite...");

        progressDialog.setMessage("Deleting....");
        progressDialog.show();

        StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        storageReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Books");
                        reference.child(bookId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "scssefuly deleted....", Toast.LENGTH_SHORT).show();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        });
    }

    public static void loadsize(Context context,String  pdfUrl, String pdfTitle, TextView sizeTv) {


        StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                double bytes=storageMetadata.getSizeBytes();
                double kb=bytes/1024;

                double mb=kb/1024;

                if(mb>=1){
                    sizeTv.setText(String.format("%.2f",mb)+"MB");

                }else if(kb>-1){
                    sizeTv.setText(String.format("%.2f",kb)+"KB");
                }else {
                    sizeTv.setText(String.format("%.2f",bytes)+"bytes");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  static void loadpdfromUrlsinglePage(Context context,String pdfUrl, PDFView pdfView, ProgressBar progressBar) {


        StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        storageReference.getBytes(MAX_BYTS_PDF).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                pdfView.fromBytes(bytes).pages(0).spacing(0).swipeHorizontal(false)
                        .enableSwipe(false).onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).onPageError(new OnPageErrorListener() {


                    @Override
                    public void onPageError(int page, Throwable t) {

                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                        progressBar.setVisibility(View.INVISIBLE);

                    }
                })
                        .load();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void loadCategory(String categoryId,TextView categoryTv) {




        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("category");
        databaseReference.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String category=""+snapshot.child("category").getValue();

                categoryTv.setText(category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
