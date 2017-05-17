package com.samuel.budgeter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class newExpenseActivity extends AppCompatActivity {
    final static DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("MM/dd/yy");
    EditText amountInput;
    EditText dateInput;
    EditText detailsInput;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        final Button addExpenseBtn = (Button) findViewById(R.id.addExpenseBtn);
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = amountInput.getText().toString();
                if(amountString.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You must fill in the flatIncome field.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                double amount = Double.parseDouble(amountString);
                String details = detailsInput.getText().toString();
                DateTime date = DateTime.parse(dateInput.getText().toString(), DATE_FORMAT);
                DateTimeZone timeZone = DateTimeZone.forOffsetMillis(myCalendar.getTimeZone().getOffset(myCalendar.getTimeInMillis()));
                if(date.isAfter(new DateTime().withZone(timeZone))) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Cannot register an expense in the future.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Expense expense = new Expense(amount, date, details);
                    BudgetManager.getInstance().addExpense(expense);
                    startActivity(new Intent(newExpenseActivity.this, MainActivity.class));
                }
            }
        });

        amountInput = (EditText) findViewById(R.id.costInput);
        dateInput = (EditText) findViewById(R.id.dateInput);
        detailsInput = (EditText) findViewById(R.id.detailsInput);
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(newExpenseActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateInput.setText(sdf.format(myCalendar.getTime()));
    }
}
