package com.example.appbook;



import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appbook.databinding.RowPdfAdminBinding;
import com.github.barteksc.pdfviewer.PDFView;



import java.util.ArrayList;

public class AdapterpdfAdmin extends RecyclerView.Adapter<AdapterpdfAdmin.HolderPDF> implements Filterable {

    Context context;
     ArrayList<Modelpdf>modelpdfArrayList,filterlist;
     RowPdfAdminBinding binding;

     FilterpdfAdmin filter;
     ProgressDialog progressDialog;

    public AdapterpdfAdmin(Context context, ArrayList<Modelpdf> modelpdfArrayList) {
        this.context = context;
        this.modelpdfArrayList = modelpdfArrayList;
        this.filterlist=modelpdfArrayList;

        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("please wait ...");
        progressDialog.setCanceledOnTouchOutside(false);


    }



    @NonNull
    @Override
    public HolderPDF onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= RowPdfAdminBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new HolderPDF(binding);

    }

    @Override
    public int getItemCount() {
        return modelpdfArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPDF holder, int position) {

        Modelpdf modelpdf=modelpdfArrayList.get(position);
        String pdfId=modelpdf.getId();
        String pdfCategoryId=modelpdf.getCategoryId();
        String title=modelpdf.getTitel();
        String des=modelpdf.getDescription();
        String pdfUrl=modelpdf.getUrl();
        long timestamp=modelpdf.getTimetamp();
        String formatedata= MyApplication.formatTimestamp(timestamp);



        binding.titleTv.setText(title);
        binding.descriptionTv.setText(des);
        binding.dateTv.setText(formatedata);


        MyApplication.loadCategory(pdfCategoryId, holder.categoryTv);
      MyApplication.loadpdfromUrlsinglePage(context,""+pdfUrl,holder.pdfView,holder.progressBar);
        MyApplication.loadsize(context,""+pdfUrl,""+title,holder.sizeTv);

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreoptionsDialog(modelpdf,holder);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PdfDetilesActivity.class);
                intent.putExtra("bookId",pdfId);
                context.startActivity(intent);
            }
        });

    }

    private void moreoptionsDialog(Modelpdf modelpdf, HolderPDF holder) {

        String bookId=modelpdf.getId();
        String bookUrl=modelpdf.getUrl();
        String bookTitle=modelpdf.getTitel();

        String []option={"Edit","Delete"};
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Choose option").setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    Intent intent=new Intent(context,PdfEditActivity.class);
                    intent.putExtra("bookId",bookId);
                    context.startActivity(intent);
                }else if(i==1){

                    MyApplication.deleteBook(context,""+bookId,""+bookUrl,""+bookTitle);

                }

            }
        }).show();
    }





    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new FilterpdfAdmin(filterlist,this);
        }
        return filter;
    }

    class  HolderPDF extends RecyclerView.ViewHolder{

        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv,descrptionTv,categoryTv,sizeTv,dateTv;
        ImageButton moreBtn;

        public HolderPDF(RowPdfAdminBinding binding) {
            super(binding.getRoot());

            pdfView= binding.pdfView;
            progressBar= binding.progressbar;
            titleTv= binding.titleTv;
            descrptionTv= binding.descriptionTv;
            categoryTv= binding.categoryTv;
            sizeTv= binding.sizeTv;
            dateTv= binding.dateTv;
            moreBtn= binding.moeBtn;
        }
    }
}
