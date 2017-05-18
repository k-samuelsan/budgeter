package com.samuel.budgeter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class FileManager {
    private static FileManager fileManager;
    private static final String budgetDataFileName = "budget-data";
    private static final String userDataFileName = "user-data";
    private Context context;

    private FileManager(Context context) {
        this.context = context;
    }

    public static FileManager getInstance(Context context) {
        if (fileManager == null) {
            fileManager = new FileManager(context);
        }
        return fileManager;
    }

    public void saveBudgetData() {
        try {
            List<Expense> expenses = BudgetManager.getInstance(context).getAllExpenses();
            List<Income> incomeList = BudgetManager.getInstance(context).getAllIncome();
            BudgetObject budgetObject = new BudgetObject(expenses, incomeList);
            final Gson gson = new Gson();
            String jsonString = gson.toJson(budgetObject) + "\n";
            Log.d("Test", "budget string : " + jsonString);
            Log.d("Test", "expesnse : " + expenses);
            Log.d("Test", "income : " + incomeList);
            FileOutputStream outputStream = context.openFileOutput(budgetDataFileName, Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserData() {
        try {
            UserDataManager userDataManager = UserDataManager.getInstance(context);
            final Gson gson = new Gson();
            String jsonString = gson.toJson(userDataManager, UserDataManager.class) + "\n";
            FileOutputStream outputStream = context.openFileOutput(userDataFileName, Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeUserData() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream inputStream = context.openFileInput(userDataFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String thisLine = reader.readLine();
            while (thisLine != null) {
                stringBuilder.append(thisLine).append("\n");
                thisLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            Log.d("File", "No user data found.");
        } catch (Exception e) {
            Log.d("File", "ERROR HERE.");
            e.printStackTrace();
        }
        Gson gson = new Gson();
        UserDataManager.setInstance(gson.fromJson(stringBuilder.toString(), UserDataManager.class));
    }

    public void initializeBudgetData() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream inputStream = context.openFileInput(budgetDataFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String thisLine = reader.readLine();
            Log.d("Test1", ""+ thisLine.length());
            while (thisLine != null) {
                stringBuilder.append(thisLine).append("\n");
                thisLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            Log.d("File", "No budget data found.");
        } catch (Exception e) {
            Log.d("File", "ERROR HERE.");
            e.printStackTrace();
        }
        Gson gson = new Gson();
        BudgetObject budgetObject = gson.fromJson(stringBuilder.toString(), BudgetObject.class);
        if(budgetObject == null) {
            return;
        }
        for(Expense expense: budgetObject.expenses) {
            BudgetManager.getInstance(context).addExpense(expense);
        }
        for(Income income: budgetObject.incomeList) {
            BudgetManager.getInstance(context).addIncome(income);
        }
        Log.d("NET" , "" + budgetObject.toString());
    }

    private class BudgetObject {
        List<Expense> expenses;
        List<Income> incomeList;

        BudgetObject(List<Expense> expenses, List<Income> incomeList) {
            this.expenses = expenses;
            this.incomeList = incomeList;
        }
    }
}
