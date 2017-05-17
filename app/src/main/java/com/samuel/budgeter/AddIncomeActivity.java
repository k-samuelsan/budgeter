package com.samuel.budgeter;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddIncomeActivity extends AppCompatActivity {
    private final static String HOURLY = "Hourly";
    private final static String FLAT = "Flat";
    private final static String NONE = "None";

    EditText date;
    EditText hoursWorked;
    EditText minutesWorked;
    EditText flatIncome;
    ConstraintLayout hourlyIncomeInput;
    ConstraintLayout flatIncomeInput;
    String currentIncomeType = NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        date = (EditText) findViewById(R.id.dateInput);
        hoursWorked = (EditText) findViewById(R.id.hoursWorkedInput);
        minutesWorked = (EditText) findViewById(R.id.minutesWorkedInput);
        flatIncome = (EditText) findViewById(R.id.flatAmountInput);
        hourlyIncomeInput = (ConstraintLayout) findViewById(R.id.hourlyIncomeInput);
        flatIncomeInput = (ConstraintLayout) findViewById(R.id.flatIncomeInput);

        final Button hourlyIncomeBtn = (Button) findViewById(R.id.hourlyIncomeBtn);
        final Button flatIncomeBtn = (Button) findViewById(R.id.flatIncomeBtn);
        hourlyIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flatIncomeInput.setVisibility(View.GONE);
                hourlyIncomeInput.setVisibility(View.VISIBLE);
                hourlyIncomeBtn.setBackgroundColor(Color.GREEN);
                flatIncomeBtn.setBackgroundColor(Color.LTGRAY);
            }
        });
        flatIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourlyIncomeInput.setVisibility(View.GONE);
                flatIncomeInput.setVisibility(View.VISIBLE);
                hourlyIncomeBtn.setBackgroundColor(Color.LTGRAY);
                flatIncomeBtn.setBackgroundColor(Color.GREEN);
            }
        });
    }
}
