package com.example.handyman.bottomsheet;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.handyman.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

// for rating pop-up windows
public class RatingBottomSheet extends BottomSheetDialog {

    private String type = "";

    public RatingBottomSheet(@NonNull Context context, String type) {
        super(context);
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_bottom_sheet);

        TextView txt = findViewById(R.id.txt);
        txt.setText(txt.getText().toString()+type);

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnRate = findViewById(R.id.btnRate);

        EditText edtRating = findViewById(R.id.edtRating);
        RatingBar ratingBar = findViewById(R.id.rating);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtRating.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Feedback can not be empty!", Toast.LENGTH_SHORT).show();
                } else {

                    if (type.equals("Owner")){

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ratings", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("ratedOwner"+getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("ownerEmail",""),true);
                        editor.apply();

                        HashMap<String, String> hashmap = new HashMap<>();
                        hashmap.put("type", type);
                        hashmap.put("rating", ratingBar.getRating()+"");
                        hashmap.put("feedback", edtRating.getText().toString());
                        hashmap.put("name", getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("ownerName",""));
                        hashmap.put("email", getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("ownerEmail",""));

                        String push = FirebaseDatabase.getInstance().getReference().child("Ratings").push().getKey();
                        FirebaseDatabase.getInstance().getReference().child("Ratings").child(push).setValue(hashmap);

                    }else {

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ratings", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("ratedWorker"+getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("workerEmail",""),true);
                        editor.apply();

                        HashMap<String, String> hashmap = new HashMap<>();
                        hashmap.put("type", type);
                        hashmap.put("rating", ratingBar.getRating()+"");
                        hashmap.put("feedback", edtRating.getText().toString());
                        hashmap.put("name", getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("workerName",""));
                        hashmap.put("email", getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("workerEmail",""));

                        String push = FirebaseDatabase.getInstance().getReference().child("Ratings").push().getKey();
                        FirebaseDatabase.getInstance().getReference().child("Ratings").child(push).setValue(hashmap); // save the rating inside the firebase

                    }

                    setContentView(R.layout.thank_you_bottom_sheet);
                    Button cancelBtn = findViewById(R.id.cancel);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                }
            }
        });

    }

}
