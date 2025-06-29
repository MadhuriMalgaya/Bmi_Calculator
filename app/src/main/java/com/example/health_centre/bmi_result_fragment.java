package com.example.health_centre;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class bmi_result_fragment extends Fragment {

    private float bmiValue,height;
    private int weight, age;



    private CardView topCard, outerCard;
    private TextView bmiCategory, bmiValueText;
    private LinearLayout normalCategory, underweightCategory, overWeight, obeseClass1, obeseClass2, obeseClass3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi_result_fragment, container, false);

        // Initialize views
        topCard = view.findViewById(R.id.ciclecard);
        outerCard = view.findViewById(R.id.outsidecard);
        bmiCategory = view.findViewById(R.id.bmiCategories);
        bmiValueText = view.findViewById(R.id.bmiValueText);

        normalCategory = view.findViewById(R.id.normalCategory);
        underweightCategory = view.findViewById(R.id.underweight);
        overWeight = view.findViewById(R.id.overweight);
        obeseClass1 = view.findViewById(R.id.obeseClass1);
        obeseClass2 = view.findViewById(R.id.obeseClass2);
        obeseClass3 = view.findViewById(R.id.obeseClass3);



        // Get BMI value from arguments
        if (getArguments() != null) {
            bmiValue = getArguments().getFloat("bmi_value", 0f);
            weight = getArguments().getInt("weight", 0);
            height = getArguments().getFloat("height", 0f);
            age = getArguments().getInt("age", 0);
        }

        // Display BMI value
        bmiValueText.setText(String.format("%.1f", bmiValue));

        // Determine BMI category and update UI accordingly
        updateBmiCategoryUI(bmiValue);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView underweight_text= view.findViewById(R.id.underweight_text);
        underweight_text.setText("<18.5");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView obese_class3= view.findViewById(R.id.obese_3_text);
        obese_class3.setText("40<");

        TextView weightValueText = view.findViewById(R.id.weight_value);
        TextView heightValueText = view.findViewById(R.id.height_value);
        TextView ageValueText = view.findViewById(R.id.Agevalue);

        weightValueText.setText(String.valueOf(weight));
        heightValueText.setText(String.valueOf(height));
        ageValueText.setText(String.valueOf(age));

        Button btn_update= view.findViewById(R.id.btn_calculate);
        btn_update.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.putExtra("from_result", true);
            intent.putExtra("bmi_value", bmiValue);
            intent.putExtra("weight", weight);
            intent.putExtra("height", height);
            intent.putExtra("age", age);
            intent.putExtra("gender", getArguments().getInt("gender", 1)); // default: male
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });





        return view;
    }

    private void updateBmiCategoryUI(float bmi) {
        // Reset all backgrounds and bubble effects
        resetCategoryHighlights();

        if (bmi < 18.5) {
            // Underweight
            topCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_underweight_dark));
            outerCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_underweight_light));
            bmiCategory.setText("Underweight");
            highlightBubble(underweightCategory);
        } else if (bmi < 25) {
            // Normal
            topCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_normal_dark));
            outerCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_normal));
            bmiCategory.setText("Normal");
            highlightBubble(normalCategory);
        } else if (bmi < 30) {
            // Overweight
            topCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_overweight_dark));
            outerCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_overweight_light));
            bmiCategory.setText("Overweight");
            highlightBubble(overWeight);
        } else if (bmi < 35) {
            // Obese Class I
            topCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese1));
            outerCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese1_light));
            bmiCategory.setText("Obese Class I");
            highlightBubble(obeseClass1);
        } else if (bmi < 40) {
            // Obese Class II
            topCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese2_dark));
            outerCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese2));
            bmiCategory.setText("Obese Class II");
            highlightBubble(obeseClass2);
        } else {
            // Obese Class III
            topCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese3_dark));
            outerCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese3));
            bmiCategory.setText("Obese Class III");
            highlightBubble(obeseClass3);
        }
    }

    private void resetCategoryHighlights() {
        normalCategory.setBackground(null);
        underweightCategory.setBackground(null);
        overWeight.setBackground(null);
        obeseClass1.setBackground(null);
        obeseClass2.setBackground(null);
        obeseClass3.setBackground(null);
    }

    private void highlightBubble(LinearLayout categoryLayout) {
        categoryLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bubble_background));
    }
}
