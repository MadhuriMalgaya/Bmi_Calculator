package com.example.health_centre;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends Fragment {

    private CardView cardMale, cardFemale;
    private Map<String, LmsData> lmsMap = new HashMap<>();
    private AtomicInteger weight = new AtomicInteger();
    private AtomicInteger age = new AtomicInteger();
    private int selectedSex = 0; // 1 for male, 2 for female

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardMale = view.findViewById(R.id.girlCard);
        cardFemale = view.findViewById(R.id.boyCard);

        TextView heightValue = view.findViewById(R.id.height_value);
        SeekBar heightSeekBar = view.findViewById(R.id.heightSeekBar);
        EditText weightValue = view.findViewById(R.id.weight_value);
        EditText ageValue = view.findViewById(R.id.Agevalue);
        ImageButton plus = view.findViewById(R.id.btn_plus);
        ImageButton minus = view.findViewById(R.id.btn_minus);
        ImageButton ageplus = view.findViewById(R.id.btn_plus1);
        ImageButton ageminus = view.findViewById(R.id.btn_minus1);

        Animation blinkAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink);

        TextView genderAlert = view.findViewById(R.id.gender_alert);
        TextView heightAlert = view.findViewById(R.id.height_alert);
        TextView weightAlert = view.findViewById(R.id.weight_alert);
        TextView ageAlert = view.findViewById(R.id.age_alert);
        Button btnCalculate = view.findViewById(R.id.btn_calculate);

        try {
            loadLmsDataFromCsv();
        } catch (IOException e) {
            Log.e("HomeFragment", "Error loading LMS data", e);
        }

        btnCalculate.setOnClickListener(v -> {
            int currentWeight = getIntFromEditText(weightValue);
            int currentAge = getIntFromEditText(ageValue);
            float currentHeightFeet = Float.parseFloat(heightValue.getText().toString());

            weight.set(currentWeight);
            age.set(currentAge);

            boolean isValid = true;

            if (!cardMale.isSelected() && !cardFemale.isSelected()) {
                genderAlert.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                genderAlert.setVisibility(View.GONE);
            }

            if (currentAge <= 0) {
                ageAlert.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                ageAlert.setVisibility(View.GONE);
            }

            if (currentWeight <= 0) {
                weightAlert.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                weightAlert.setVisibility(View.GONE);
            }

            if (heightSeekBar.getProgress() == 0) {
                heightAlert.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                heightAlert.setVisibility(View.GONE);
            }

            if (isValid) {
                selectedSex = cardMale.isSelected() ? 1 : 2;

                float bmi;
                if (currentAge <= 20) {
                    bmi = calculateBmiZScore(currentAge, selectedSex, currentWeight, currentHeightFeet);
                } else {
                    bmi = calculateStandardBmi(currentWeight, currentHeightFeet);
                }

                openResultActivity(bmi, currentWeight, currentHeightFeet, currentAge);
            }
        });

        plus.setOnClickListener(v -> {
            v.startAnimation(blinkAnimation);
            int current = getIntFromEditText(weightValue) + 1;
            weight.set(current);
            weightValue.setText(String.valueOf(current));
        });

        minus.setOnClickListener(v -> {
            v.startAnimation(blinkAnimation);
            int current = getIntFromEditText(weightValue);
            if (current > 0) current--;
            weight.set(current);
            weightValue.setText(String.valueOf(current));
        });

        ageplus.setOnClickListener(v -> {
            v.startAnimation(blinkAnimation);
            int current = getIntFromEditText(ageValue) + 1;
            age.set(current);
            ageValue.setText(String.valueOf(current));
        });

        ageminus.setOnClickListener(v -> {
            v.startAnimation(blinkAnimation);
            int current = getIntFromEditText(ageValue);
            if (current > 0) current--;
            age.set(current);
            ageValue.setText(String.valueOf(current));
        });

        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float feetValue = 1.0f + (progress / 10.0f);
                String formatted = String.format("%.1f", feetValue);
                heightValue.setText(formatted);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        cardMale.setOnClickListener(v -> selectGenderCard(true));
        cardFemale.setOnClickListener(v -> selectGenderCard(false));

        return view;
    }

    private void selectGenderCard(boolean isMale) {
        if (isMale) {
            cardMale.setSelected(true);
            cardFemale.setSelected(false);
            cardMale.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_accent));
            cardFemale.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cardBg));
        } else {
            cardMale.setSelected(false);
            cardFemale.setSelected(true);
            cardFemale.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_accent));
            cardMale.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cardBg));
        }
    }

    private void openResultActivity(float bmi, int weight, float height, int age) {
        Intent intent = new Intent(requireContext(), BmiCalculateActivity.class);
        intent.putExtra("bmi_value", bmi);
        intent.putExtra("weight", weight);
        intent.putExtra("height", height);
        intent.putExtra("age", age);
        startActivity(intent);
    }

    private static class LmsData {
        double L, M, S;
        public LmsData(double L, double M, double S) {
            this.L = L; this.M = M; this.S = S;
        }
    }

    private void loadLmsDataFromCsv() throws IOException {
        AssetManager assetManager = requireContext().getAssets();
        InputStream inputStream = assetManager.open("cds_lms.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        reader.readLine(); // skip header
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length < 5) continue;
            int sex = Integer.parseInt(parts[0].trim());
            int ageMonths = (int) Float.parseFloat(parts[1].trim());
            double L = Double.parseDouble(parts[2].trim());
            double M = Double.parseDouble(parts[3].trim());
            double S = Double.parseDouble(parts[4].trim());
            String key = sex + "_" + ageMonths;
            lmsMap.put(key, new LmsData(L, M, S));
        }
        reader.close();
    }

    private float calculateStandardBmi(int weightKg, float heightFeet) {
        double heightMeters = heightFeet * 0.3048;
        if (heightMeters <= 0) return 0;
        return (float) (weightKg / (heightMeters * heightMeters));
    }

    private float calculateBmiZScore(int ageYears, int sex, int weightKg, float heightFeet) {
        double heightMeters = heightFeet * 0.3048;
        if (heightMeters <= 0) return 0;
        double bmi = weightKg / (heightMeters * heightMeters);
        int ageMonths = ageYears * 12;
        String key = sex + "_" + ageMonths;
        if (!lmsMap.containsKey(key)) {
            Log.w("BMI", "LMS data not found for: " + key);
            return (float) bmi;
        }
        LmsData lms = lmsMap.get(key);
        double L = lms.L, M = lms.M, S = lms.S;
        double z = (Math.pow((bmi / M), L) - 1) / (L * S);
        return (float) z;
    }

    private int getIntFromEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) return 0;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
