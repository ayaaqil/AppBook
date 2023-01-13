package com.example.appbook;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterCategory extends Filter {
    private ArrayList<ModelCategory> filterArrayList;
    AdapterCategory adapterCategory;

    public FilterCategory(ArrayList<ModelCategory> filterArrayList, AdapterCategory adapterCategory) {
        this.filterArrayList = filterArrayList;
        this.adapterCategory = adapterCategory;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results=new FilterResults();
        if(charSequence!=null&&charSequence.length()>0){
            charSequence=charSequence.toString().toUpperCase();
            ArrayList<ModelCategory>filterdModels=new ArrayList<>();
            for (int i =0;i<filterArrayList.size();i++){
                if(filterArrayList.get(i).getCategory().toUpperCase().contains(charSequence)){
                    filterdModels.add(filterArrayList.get(i));
                }
            }
            results.count=filterdModels.size();
            results.values=filterdModels;

        }else {
            results.count=filterArrayList.size();
            results.values=filterArrayList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterCategory.modelCategoryArrayList=(ArrayList<ModelCategory>) filterResults.values;

        adapterCategory.notifyDataSetChanged();

    }
}
