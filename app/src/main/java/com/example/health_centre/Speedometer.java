package com.example.health_centre;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class Speedometer extends View {

    private Paint arcPaint, needlePaint, textPaint, tickPaint, centerTextPaint;
    private RectF oval;
    private double bmi = 0;

    private final String[] categories = {
            "Underweight", "Normal", "Overweight", "Obese I", "Obese II", "Obese III"
    };

    private final float[] bmiThresholds = {0.1f, 18.5f, 25f, 30f, 35f, 40f, 50f};
    private final int[] segmentColors = {
            Color.parseColor("#F44336"), // Underweight - red
            Color.parseColor("#4CAF50"), // Normal - green
            Color.parseColor("#FFEB3B"), // Overweight - yellow
            Color.parseColor("#EF9A9A"), // Obese I - light red
            Color.parseColor("#E57373"), // Obese II - red
            Color.parseColor("#C62828")   // Obese III - dark red
    };

    public Speedometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(120); // thickness
        arcPaint.setStrokeCap(Paint.Cap.BUTT);

        needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        needlePaint.setColor(Color.BLACK);
        needlePaint.setStrokeWidth(10);

        tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tickPaint.setColor(Color.BLACK);
        tickPaint.setStrokeWidth(2);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(28);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextAlign(Paint.Align.CENTER);

        centerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerTextPaint.setColor(Color.BLACK);
        centerTextPaint.setTextSize(40);
        centerTextPaint.setTextAlign(Paint.Align.CENTER);
        centerTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setBmiValue(double value) {
        this.bmi = Math.max(0.1, Math.min(50, value));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 60;

        int centerX = width / 2;
        int centerY = height / 2;

        oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float startAngle = -90f; // Start from top
        float totalAngle = 360f;

        // Draw arc segments and labels
        for (int i = 0; i < categories.length; i++) {
            float startBmi = bmiThresholds[i];
            float endBmi = bmiThresholds[i + 1];
            float sweep = (endBmi - startBmi) / (bmiThresholds[bmiThresholds.length - 1] - bmiThresholds[0]) * totalAngle;
            arcPaint.setColor(segmentColors[i]);
            canvas.drawArc(oval, startAngle, sweep, false, arcPaint);

            // Draw category text
            float midAngle = startAngle + sweep / 2;
            double rad = Math.toRadians(midAngle);
            float textX = (float) (centerX + (radius + 60) * Math.cos(rad));
            float textY = (float) (centerY + (radius + 60) * Math.sin(rad));
            canvas.drawText(categories[i], textX, textY, textPaint);

            startAngle += sweep;
        }

        // Draw needle
        // Compute needle angle in radians
        double needleAngle = -90 + ((bmi - bmiThresholds[0]) / (bmiThresholds[bmiThresholds.length - 1] - bmiThresholds[0])) * totalAngle;
        double radian = Math.toRadians(needleAngle);

// Define lengths and dimensions
        float needleLength = radius - 50;
        float arrowLength = 50;
        float shaftWidth = 4;

// Needle tip (outer point)
        float tipX = (float) (centerX + needleLength * Math.cos(radian));
        float tipY = (float) (centerY + needleLength * Math.sin(radian));

// Arrow base (where shaft starts)
        float baseX = (float) (centerX + (needleLength - arrowLength) * Math.cos(radian));
        float baseY = (float) (centerY + (needleLength - arrowLength) * Math.sin(radian));

// Draw arrowhead (black triangle)
        double leftAngle = radian + Math.PI / 2;
        double rightAngle = radian - Math.PI / 2;

        float arrowHalfWidth = 18;

        float leftX = (float) (baseX + arrowHalfWidth * Math.cos(leftAngle));
        float leftY = (float) (baseY + arrowHalfWidth * Math.sin(leftAngle));

        float rightX = (float) (baseX + arrowHalfWidth * Math.cos(rightAngle));
        float rightY = (float) (baseY + arrowHalfWidth * Math.sin(rightAngle));

        Path arrowPath = new Path();
        arrowPath.moveTo(tipX, tipY);       // tip of arrow
        arrowPath.lineTo(leftX, leftY);     // left base
        arrowPath.lineTo(rightX, rightY);   // right base
        arrowPath.close();

        needlePaint.setStyle(Paint.Style.FILL);
        needlePaint.setColor(Color.BLACK);
        canvas.drawPath(arrowPath, needlePaint);

// Draw shaft (thin gray line from center to arrow base)
        needlePaint.setColor(Color.DKGRAY);
        needlePaint.setStrokeWidth(shaftWidth);
        canvas.drawLine(centerX, centerY, baseX, baseY, needlePaint);

// Draw circular base at center
        needlePaint.setColor(Color.DKGRAY);
        canvas.drawCircle(centerX, centerY, 4, needlePaint);



        String category = getBmiCategory(bmi);
        centerTextPaint.setColor(getCategoryColor(bmi));
        canvas.drawText("BMI  " + String.format("%.1f", bmi), centerX, centerY + 10, centerTextPaint);

        canvas.drawText(category, centerX, centerY + 60, centerTextPaint);
    }

    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal";
        else if (bmi < 30) return "Overweight";
        else if (bmi < 35) return "Obese I";
        else if (bmi < 40) return "Obese II";
        else return "Obese III";
    }
    private int getCategoryColor(double bmi) {
        if (bmi < 18.5) return Color.parseColor("#F44336");      // Underweight - Red
        else if (bmi < 25) return Color.parseColor("#4CAF50");   // Normal - Green
        else if (bmi < 30) return Color.parseColor("#FFEB3B");   // Overweight - Yellow
        else if (bmi < 35) return Color.parseColor("#EF9A9A");   // Obese I - Light Red
        else if (bmi < 40) return Color.parseColor("#E57373");   // Obese II - Red
        else return Color.parseColor("#C62828");                 // Obese III - Dark Red
    }


}
