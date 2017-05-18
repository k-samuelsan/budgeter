package com.samuel.budgeter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    static boolean initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Debug", "new main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!initialized) {
            FileManager.getInstance(this).initializeBudgetData();
            FileManager.getInstance(this).initializeUserData();
            initialized = true;
        }

        TextView weeklyIncomeText = (TextView) findViewById(R.id.weeklyIncomeText);
        double netIncome = BudgetManager.getInstance(this).getCurrentMonthNetIncome();
        Resources res = getResources();
        weeklyIncomeText.setText(res.getString(R.string.monthly_income_string, netIncome));
        final Button newExpenseBtn = (Button) findViewById(R.id.addExpenseBtn);
        newExpenseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, newExpenseActivity.class));
            }
        });
        final Button addIncomeBtn = (Button) findViewById(R.id.addIncomeBtn);
        addIncomeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddIncomeActivity.class));
            }
        });
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
}
