package com.example.health_centre;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class visulaize_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visulaize_fragment, container, false);

        // Get references to both speedometers
       Speedometer standard = view.findViewById(R.id.speedometer);
        ExtremeSpeedometer extreme = view.findViewById(R.id.extremeSpeedometer);

        // Default BMI value
        float bmiValue = 0f;

        // Check if a BMI value was passed via arguments
        if (getArguments() != null) {
            bmiValue = getArguments().getFloat("bmi_value", 0f);
        }

        // Show correct speedometer based on BMI range
        if (bmiValue <= 50) {
            standard.setVisibility(View.VISIBLE);
            extreme.setVisibility(View.GONE);
            standard.setBmiValue(bmiValue);
        } else {
            standard.setVisibility(View.GONE);
            extreme.setVisibility(View.VISIBLE);
            extreme.setBmiValue(bmiValue);
        }

        return view;
    }
}
