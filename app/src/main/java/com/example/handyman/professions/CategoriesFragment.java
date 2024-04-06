package com.example.handyman.professions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.handyman.R;
import com.example.handyman.adapters.CategoryAdapter;

import java.util.List;

// to show the fragment related to categories
public class CategoriesFragment extends Fragment {

    public CategoriesFragment() {} // it must be an empty constructor

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // pass the worker information to the next activity
        List<String> professions = Professions.getProfessions();
        ListView listViewCategories = view.findViewById(R.id.listViewCategories);
        CategoryAdapter categoryAdapter = new CategoryAdapter(professions);
        listViewCategories.setAdapter(categoryAdapter);

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedProfession = professions.get(position); // Directly use the professions list

                Intent intent;
                switch (selectedProfession) {
                    case "Carpenter":
                        intent = new Intent(getActivity(), CarpenterActivity.class);
                        break;
                    case "Electrician":
                        intent = new Intent(getActivity(), ElectricianActivity.class);
                        break;
                    case "Plumber":
                        intent = new Intent(getActivity(), PlumberActivity.class);
                        break;
                    case "Painter":
                        intent = new Intent(getActivity(), PainterActivity.class);
                        break;
                    default:
                        return;
                }
                startActivity(intent);
            }
        });
    }
}
