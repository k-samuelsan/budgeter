package com.samuel.budgeter.core;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Utils {
    public final static DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("MM/dd/yy");

    public static View.OnClickListener getOnClickDatePicker (final Context context, final EditText dateInput, final Calendar calendar) {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dateInput, calendar);
            }

        };

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        };
    }

    public static View.OnTouchListener getFocusClearer(final List<EditText> editTexts) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    for(EditText editText: editTexts) {
                        if(editText.isFocused()) {
                            Rect outRect = new Rect();
                            editText.getGlobalVisibleRect(outRect);
                            if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                                editText.clearFocus();
                                v.requestFocus();
                                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }

                        }
                    }
                }
                return false;
            }
        };
    }

    private static void updateLabel(EditText dateInput, final Calendar calendar) {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateInput.setText(sdf.format(calendar.getTime()));
    }

    public static void sortTransactions(List<Transaction> transactions, int low, int high) {
        //quicksort
        int i = low;
        int j = high;
        DateTime pivot = getDate(transactions.get(low + (high - low)/2));
        while(i <= j) {
            while(getDate(transactions.get(i)).isBefore(pivot)) {
                i++;
            }
            while(getDate(transactions.get(j)).isAfter(pivot)) {
                j--;
            }
            if(i <= j) {
                Collections.swap(transactions, i, j);
                i++;
                j--;
            }
        }
        if(low < j) {
            sortTransactions(transactions, low, j);
        }
        if(i < high) {
            sortTransactions(transactions, i, high);
        }
    }

    private static DateTime getDate(Transaction transaction) {
        return new DateTime(transaction.getDateInMillis());
    }
}
