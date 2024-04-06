package com.example.handyman.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.bottomsheet.RatingBottomSheet;
import com.example.handyman.job.JobsFragment;
import com.example.handyman.professions.CategoriesFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainPageActivityOwner extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_owner);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(new SectionsPagerAdapter(this));

        // -------------- for rating system pop-up ----------------------
        SharedPreferences sharedPreferences = getSharedPreferences("ratings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // it checks if it is the first time the app is open or not, then show the pop-up windows
        if (sharedPreferences.getBoolean("appOpenFirstTimeOwner"+getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("ownerEmail",""), true)){
            editor.putBoolean("appOpenFirstTimeOwner"+getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("ownerEmail",""),false);
            editor.apply();
        }else if (!sharedPreferences.getBoolean("appOpenFirstTimeOwner"+getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("ownerEmail",""), true) && !sharedPreferences.getBoolean("ratedOwner"+getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("ownerEmail",""), false)){
            RatingBottomSheet ratingBottomSheet = new RatingBottomSheet(this, "Owner");
            ratingBottomSheet.show();

        }
        // ----------------- link the TabLayout with the ViewPager2 ------------------
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(position == 0 ? "Categories" : "Jobs")).attach();
    }

    private static class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(AppCompatActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new CategoriesFragment();
            } else {
                return new JobsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2; // there are two tabs
        }
    }

    // ------------- for home button ----------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
