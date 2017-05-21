package com.samuel.budgeter.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.budgeter.managers.BudgetManager;
import com.samuel.budgeter.managers.FileManager;
import com.samuel.budgeter.core.Income;
import com.samuel.budgeter.R;
import com.samuel.budgeter.managers.UserDataManager;
import com.samuel.budgeter.core.Utils;

import org.joda.time.DateTime;

import java.util.Calendar;

public class AddIncomeActivity extends AppCompatActivity {
    private final static String HOURLY = "Hourly";
    private final static String FLAT = "Flat";
    private final static String NONE = "None";
    String currentIncomeType = NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        final EditText dateInput = (EditText) findViewById(R.id.dateInput);
        dateInput.setOnClickListener(Utils.getOnClickDatePicker(this, dateInput, Calendar.getInstance()));
        final EditText hoursWorkedInput = (EditText) findViewById(R.id.hoursWorkedInput);
        final EditText minutesWorkedInput = (EditText) findViewById(R.id.minutesWorkedInput);
        final EditText amountInput = (EditText) findViewById(R.id.flatAmountInput);
        final ConstraintLayout hourlyIncomeInput = (ConstraintLayout) findViewById(R.id.hourlyIncomeInput);
        final ConstraintLayout flatIncomeInput = (ConstraintLayout) findViewById(R.id.flatIncomeInput);

        final Button hourlyIncomeBtn = (Button) findViewById(R.id.hourlyIncomeBtn);
        updateHourlyRate(UserDataManager.getInstance().getHourlyRate());
        final Button flatIncomeBtn = (Button) findViewById(R.id.flatIncomeBtn);
        hourlyIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flatIncomeInput.setVisibility(View.GONE);
                hourlyIncomeInput.setVisibility(View.VISIBLE);
                hourlyIncomeBtn.setBackgroundColor(Color.GREEN);
                flatIncomeBtn.setBackgroundColor(Color.LTGRAY);
                currentIncomeType = HOURLY;
            }
        });
        flatIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourlyIncomeInput.setVisibility(View.GONE);
                flatIncomeInput.setVisibility(View.VISIBLE);
                hourlyIncomeBtn.setBackgroundColor(Color.LTGRAY);
                flatIncomeBtn.setBackgroundColor(Color.GREEN);
                currentIncomeType = FLAT;
            }
        });

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final Button changeHourlyRateBtn = (Button) findViewById(R.id.changeRateBtn);
        changeHourlyRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText hourlyRateInput = new EditText(context);
                hourlyRateInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                alert.setTitle("Change hourly rate");
                alert.setView(hourlyRateInput);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String hourlyRateString = hourlyRateInput.getText().toString();
                        if(hourlyRateString.equals("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Hourly rate unchanged.", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        Double hourlyRate = Double.parseDouble(hourlyRateString);
                        UserDataManager.getInstance().setHourlyRate(hourlyRate);
                        updateHourlyRate(hourlyRate);
                        FileManager.getInstance().saveUserData(context);
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });
        Button addIncomeBtn = (Button) findViewById(R.id.addIncomeBtn);
        addIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateString = dateInput.getText().toString();
                if(dateString.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "The date field must be filled in.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                DateTime date = DateTime.parse(dateString, Utils.DATE_FORMAT);
                double amount = 0;
                if(currentIncomeType.equals(HOURLY)) {
                    String hoursWorkedString = hoursWorkedInput.getText().toString();
                    if(hoursWorkedString.equals("")) {
                        hoursWorkedString = "0";
                    }
                    String minutesWorkedString = minutesWorkedInput.getText().toString();
                    if(minutesWorkedString.equals("")) {
                        minutesWorkedString = "0";
                    }
                    int hoursWorked = Integer.parseInt(hoursWorkedString);
                    int minutesWorked = Integer.parseInt(minutesWorkedString);
                    double timeWorked = hoursWorked + ((double)minutesWorked / 60);
                    Log.d("worked", "hours worked " + hoursWorked + " minutes " + minutesWorked + " total " + timeWorked);
                    if(timeWorked <= 0) {
                        Toast toast = Toast.makeText(getApplicationContext(), "You must have at least 1m to log hourly income.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    amount = UserDataManager.getInstance().getHourlyRate()*timeWorked;

                } else if (currentIncomeType.equals(FLAT)) {
                    String amountString = amountInput.getText().toString();
                    if(amountString.equals("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "The amount field must be filled in.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    amount = Double.parseDouble(amountString);
                }
                BudgetManager.getInstance().addIncome(new Income(amount, date), context);
                Toast toast = Toast.makeText(getApplicationContext(), "Added income with amount $" + String.format("%.2f",amount) + ".", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });
    }

    private void updateHourlyRate(double hourlyRate) {
        final TextView hourlyRateVal = (TextView) findViewById(R.id.hourlyRateVal);
        hourlyRateVal.setText("$" +  String.format("%.2f", hourlyRate));
    }
}
