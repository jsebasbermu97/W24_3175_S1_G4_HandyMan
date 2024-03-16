package com.example.handyman.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.handyman.R;
import com.example.handyman.professions.CarpenterActivity;
import com.example.handyman.professions.ElectricianActivity;
import com.example.handyman.professions.PainterActivity;
import com.example.handyman.professions.PlumberActivity;
import com.example.handyman.worker.Worker;

import java.util.List;

// to show the list of workers in the user main page
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
        workerName.setGravity(Gravity.CENTER);
        TextView workerProfession = convertView.findViewById(R.id.textViewProfessions);
        workerProfession.setGravity(Gravity.CENTER);
        ImageView workerImage = convertView.findViewById(R.id.imageViewProfession);

        workerName.setText(worker.name);
        workerProfession.setText(worker.profession);
        switch (worker.profession) {
            case "Carpenter":
                workerImage.setImageResource(R.drawable.carpenter);
                break;
            case "Electrician":
                workerImage.setImageResource(R.drawable.electrician);
                break;
            case "Plumber":
                workerImage.setImageResource(R.drawable.plumber);
                break;
            case "Painter":
                workerImage.setImageResource(R.drawable.painter);
                break;
        }

        return convertView;
    }
}
