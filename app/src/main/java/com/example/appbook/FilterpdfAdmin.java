package com.example.appbook;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterpdfAdmin extends Filter {
    private ArrayList<Modelpdf> filterlist;
    AdapterpdfAdmin adapterpdfAdmin;

    public FilterpdfAdmin(ArrayList<Modelpdf> filterlist, AdapterpdfAdmin adapterpdfAdmin) {
        this.filterlist = filterlist;
        this.adapterpdfAdmin = adapterpdfAdmin;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results=new FilterResults();
        if(charSequence!=null&&charSequence.length()>0){
            charSequence=charSequence.toString().toUpperCase();
            ArrayList<Modelpdf>modelpdfArrayList=new ArrayList<>();
            for (int i =0;i<modelpdfArrayList.size();i++){
                if(modelpdfArrayList.get(i).getTitel().toUpperCase().contains(charSequence)){
                    filterlist.add(modelpdfArrayList.get(i));
                }
            }
            results.count=filterlist.size();
            results.values=filterlist;

        }else {
            results.count=filterlist.size();
            results.values=filterlist;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterpdfAdmin.modelpdfArrayList=(ArrayList<Modelpdf>) filterResults.values;

        adapterpdfAdmin.notifyDataSetChanged();

    }
}
