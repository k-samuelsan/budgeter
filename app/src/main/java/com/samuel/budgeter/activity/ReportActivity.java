package com.samuel.budgeter.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.samuel.budgeter.R;
import com.samuel.budgeter.core.Transaction;
import com.samuel.budgeter.core.Utils;
import com.samuel.budgeter.managers.BudgetManager;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    List<Transaction> transactions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        transactions.addAll(BudgetManager.getInstance().getAllTransactions());
        Utils.sortTransactions(transactions, 0, transactions.size()-1);
        updateReport();

    }

    private void updateReport() {

    }


}
