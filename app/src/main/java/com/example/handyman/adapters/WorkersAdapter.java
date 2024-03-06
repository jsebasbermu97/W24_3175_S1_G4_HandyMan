package com.example.handyman.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.handyman.R;
import com.example.handyman.worker.Worker;

import java.util.List;

public class WorkersAdapter extends ArrayAdapter<Worker> {
    public WorkersAdapter(Context context, List<Worker> workers) {
        super(context, 0, workers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Worker worker = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_workers, parent, false);
        }

        TextView workerName = convertView.findViewById(R.id.textViewWorker);
        workerName.setGravity(Gravity.CENTER_VERTICAL);
        TextView workerProfession = convertView.findViewById(R.id.textViewProfessions);
        workerProfession.setGravity(Gravity.CENTER_VERTICAL);

        workerName.setText(worker.name);
        workerProfession.setText(worker.profession);

        return convertView;
    }
}
