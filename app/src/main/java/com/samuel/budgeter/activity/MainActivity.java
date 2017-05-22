package com.samuel.budgeter.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.samuel.budgeter.managers.BudgetManager;
import com.samuel.budgeter.managers.FileManager;
import com.samuel.budgeter.R;

import java.util.Observable;
import java.util.Observer;


public class MainActivity extends AppCompatActivity implements Observer {
    static boolean initialized = false;
    BudgetManager budgetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Debug", "new main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!initialized) {
            FileManager.getInstance().initializeBudgetData(this);
            FileManager.getInstance().initializeUserData(this);
            initialized = true;
        }
        budgetManager = BudgetManager.getInstance();
        budgetManager.addObserver(this);
        updateNetIncome();
        FileManager.getInstance().saveBudgetData(this);
        final Button newExpenseBtn = (Button) findViewById(R.id.addExpenseBtn);
        newExpenseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddExpenseActivity.class));
            }
        });
        final Button addIncomeBtn = (Button) findViewById(R.id.addIncomeBtn);
        addIncomeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddIncomeActivity.class));
            }
        });
    }

    private void updateNetIncome() {
        TextView monthlyIncomeValueTxt = (TextView) findViewById(R.id.monthlyIncomeValueTxt);
        TextView weeklyIncomeValueTxt = (TextView) findViewById(R.id.weeklyIncomeValueTxt);
        double netMonthly = budgetManager.getCurrentMonthNetIncome();
        double netWeekly = budgetManager.getCurrentWeekNetIncome();
        final int POSITIVE_BUDGET_GREEN = Color.rgb(0, 0x8d, 0);
        monthlyIncomeValueTxt.setText("$" + String.format("%.2f", netMonthly));
        if(netMonthly >= 0) {
            monthlyIncomeValueTxt.setTextColor(POSITIVE_BUDGET_GREEN);
        } else {
            monthlyIncomeValueTxt.setTextColor(Color.RED);
        }
        weeklyIncomeValueTxt.setText("$" + String.format("%.2f", netWeekly));
        if(netWeekly >= 0) {
            weeklyIncomeValueTxt.setTextColor(POSITIVE_BUDGET_GREEN);
        } else {
            weeklyIncomeValueTxt.setTextColor(Color.RED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d("updated", "updated");
        updateNetIncome();
    }
}
