package com.example.health_centre;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.health_centre.databinding.ActivityBmiCalculateBinding;
public class BmiCalculateActivity extends AppCompatActivity {


    ActivityBmiCalculateBinding binding;
    bmi_result_fragment bmiResultFragment;
    visulaize_fragment visulaizeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBmiCalculateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Pass BMI to fragment using bundle
        Bundle bundle = new Bundle();
        bundle.putFloat("bmi_value", getIntent().getFloatExtra("bmi_value", 0f));
        bundle.putInt("weight", getIntent().getIntExtra("weight", 0));
        bundle.putFloat("height", getIntent().getFloatExtra("height", 0f));
        bundle.putInt("age", getIntent().getIntExtra("age", 0));


        bmiResultFragment = new bmi_result_fragment();
        bmiResultFragment.setArguments(bundle);  // Pass bundle here

        visulaizeFragment = new visulaize_fragment();
        visulaizeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainers, bmiResultFragment)
                .commit();

        binding.bottomNavigationBar2.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bmi) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainers, bmiResultFragment)
                        .commit();
                return true;
            } else if (id == R.id.visualize) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainers, visulaizeFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
}