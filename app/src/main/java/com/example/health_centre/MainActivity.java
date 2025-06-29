package com.example.health_centre;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.health_centre.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    HomeFragment homeFragment;
    Healthtips healthtips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize fragments
        homeFragment = new HomeFragment();
        healthtips = new Healthtips();


        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, homeFragment)
                .commit();

        // Bottom navigation listener with if-else
        binding.bottomNavigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameContainer, homeFragment)
                            .commit();
                    return true;

                } else if (id == R.id.tips) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameContainer, healthtips)
                            .commit();
                    return true;

                } else {
                    return false;
                }
            }
        });
    }
}
