package com.example.appbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbook.databinding.RowCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory> implements Filterable {

    private Context context;
    public ArrayList<ModelCategory>modelCategoryArrayList,filterList;
    private RowCategoryBinding binding;
    private FilterCategory filter;


    public AdapterCategory(Context context, ArrayList<ModelCategory> modelCategoryArrayList) {
       this.context=context;
        this.modelCategoryArrayList = modelCategoryArrayList;
        this.filterList=modelCategoryArrayList;
    }

    public AdapterCategory(ArrayList<ModelCategory> modelCategoryArrayList) {

    }


    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding=RowCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new HolderCategory(binding);
    }

    @Override
    public int getItemCount() {
        return modelCategoryArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        ModelCategory modelCategory=modelCategoryArrayList.get(position);

        String id=modelCategory.getId();
        String category=modelCategory.getCategory();
        String uid=modelCategory.getUid();
        long timestamp=modelCategory.getTimestamp();

        holder.Category_title.setText(category);
        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete").setMessage("are you sure you want to delete this category")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteCategory(modelCategory,holder);

                            }
                        }).setNegativeButton("Cansel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

            }
        });

    }

    private void deleteCategory(ModelCategory modelCategory, HolderCategory holder) {
        String id=modelCategory.getId();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("category");
        databaseReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter=new FilterCategory(filterList,this);
        }
        return filter;
    }

    class HolderCategory extends RecyclerView.ViewHolder{

        TextView Category_title;
        ImageButton delete_icon;

        public HolderCategory( RowCategoryBinding binding) {


            super(binding.getRoot());

            Category_title= binding.categoryTitle;;
            delete_icon= binding.deleteIcon;
        }
    }
}
