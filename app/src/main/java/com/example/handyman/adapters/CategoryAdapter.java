package com.example.handyman.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.handyman.R;

import java.util.List;

// to show the list of categories inside the user main page
public class CategoryAdapter extends BaseAdapter {
    List<String> adapterProfessionList;

    public CategoryAdapter(List<String> adapterProfessionList) {
        this.adapterProfessionList = adapterProfessionList;
    }

    @Override
    public int getCount() {
        return adapterProfessionList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterProfessionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories, parent, false);
        }

        TextView txtViewCategory = convertView.findViewById(R.id.textViewWorkerJob);
        txtViewCategory.setText(adapterProfessionList.get(position));
        txtViewCategory.setGravity(Gravity.CENTER_VERTICAL);
        return convertView;
    }
}
