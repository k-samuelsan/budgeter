package com.samuel.budgeter.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.samuel.budgeter.core.Transaction;
import com.samuel.budgeter.managers.BudgetManager;
import com.samuel.budgeter.R;
import com.samuel.budgeter.core.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    EditText amountInput;
    EditText dateInput;
    EditText detailsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        final Context context = this;
        final Button addExpenseBtn = (Button) findViewById(R.id.addExpenseBtn);
        final Calendar calendar = Calendar.getInstance();
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = amountInput.getText().toString();
                String dateString = dateInput.getText().toString();
                if(amountString.equals("") || dateString.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "The amount and date fields must be filled in.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                double amount = Double.parseDouble(amountString);
                String details = detailsInput.getText().toString();
                DateTime date = DateTime.parse(dateString, Utils.DATE_FORMAT);
                DateTimeZone timeZone = BudgetManager.getInstance().getTimeZone();
                if(date.isAfter(new DateTime().withZone(timeZone))) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Cannot register an expense in the future.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Transaction expense = new Transaction((amount*-1), date, details);
                    BudgetManager.getInstance().addTransaction(expense, context);
                    Toast toast = Toast.makeText(getApplicationContext(), "Added expense with amount $" + String.format("%.2f",amount) + ".", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });

        amountInput = (EditText) findViewById(R.id.amountInput);
        dateInput = (EditText) findViewById(R.id.dateInput);
        detailsInput = (EditText) findViewById(R.id.detailsInput);

        dateInput.setOnClickListener(Utils.getOnClickDatePicker(this, dateInput, calendar));

        FrameLayout touchInterceptor = (FrameLayout)findViewById(R.id.touchInterceptor);
        List<EditText> editTexts = new ArrayList<>();
        editTexts.addAll(Arrays.asList(amountInput, detailsInput));
        touchInterceptor.setOnTouchListener(Utils.getFocusClearer(editTexts));
    }

}
