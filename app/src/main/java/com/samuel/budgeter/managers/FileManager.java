package com.samuel.budgeter.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samuel.budgeter.core.Transaction;
import com.samuel.budgeter.core.YearlyBudget;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileManager {
    private static FileManager fileManager;
    private static final String yearlyBudgetDataFileName = "yearly-budget-data";
    private static final String budgetDataPrefix = "budget-data-";
    private static final String userDataFileName = "user-data";

    private Set<Long> budgetedYears;

    private FileManager() {
        budgetedYears = new HashSet<>();
        budgetedYears.add(BudgetManager.getInstance().getStartOfLoadedYearInMillis());
    }

    public static FileManager getInstance() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    public void saveBudgetData(Context context) {
        try {
            final Gson gson = new Gson();
            String jsonString = gson.toJson(budgetedYears) + "\n";
            Log.d("JSON", "budgeted years : " + jsonString);
            FileOutputStream outputStream = context.openFileOutput(yearlyBudgetDataFileName, Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            long startOfYearInMillis = BudgetManager.getInstance().getStartOfLoadedYearInMillis();
            List<Transaction> transactions = BudgetManager.getInstance().getAllTransactions();
            final Gson gson = new Gson();
            String jsonString = gson.toJson(transactions) + "\n";
            Log.d("Test", "budget string : " + jsonString);
            Log.d("Test", "writing to file : " + budgetDataPrefix + startOfYearInMillis);
            FileOutputStream outputStream = context.openFileOutput(budgetDataPrefix + startOfYearInMillis, Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserData(Context context) {
        try {
            UserDataManager userDataManager = UserDataManager.getInstance();
            final Gson gson = new Gson();
            String jsonString = gson.toJson(userDataManager, UserDataManager.class) + "\n";
            FileOutputStream outputStream = context.openFileOutput(userDataFileName, Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeUserData(Context context) {
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

    public void initializeBudgetData(Context context) {
        Gson gson = new Gson();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream inputStream = context.openFileInput(yearlyBudgetDataFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String thisLine = reader.readLine();
            while (thisLine != null) {
                stringBuilder.append(thisLine).append("\n");
                thisLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            Log.d("File", "No budget data found.");
            return;
        } catch (Exception e) {
            Log.d("File", "ERROR HERE.");
            e.printStackTrace();
        }
        Type setType = new TypeToken<Set<Long>>() {}.getType();
        Set<Long> savedYears = gson.fromJson(stringBuilder.toString(), setType);
        budgetedYears.addAll(savedYears);

        stringBuilder = new StringBuilder();
        long startOfYearInMillis = BudgetManager.getInstance().getStartOfLoadedYearInMillis();
        try {
            FileInputStream inputStream = context.openFileInput(budgetDataPrefix + startOfYearInMillis);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String thisLine = reader.readLine();
            while (thisLine != null) {
                stringBuilder.append(thisLine).append("\n");
                thisLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            Log.d("File", "No data found for current year");
            return;
        } catch (Exception e) {
            Log.d("File", "ERROR HERE.");
            e.printStackTrace();
        }
        Type listType = new TypeToken<List<Transaction>>() {}.getType();
        List<Transaction> transactions = gson.fromJson(stringBuilder.toString(), listType);
        for(Transaction transaction: transactions) {
            BudgetManager.getInstance().addTransaction(transaction, null);
        }
    }

    public void saveYear(DateTime newYear) {
        budgetedYears.add(newYear.getMillis());
    }

    public void loadYear(DateTime startOfYear, Context context) {
        BudgetManager.getInstance().setLoadedYear(new YearlyBudget(startOfYear));
        budgetedYears.add(startOfYear.getMillis());
        Gson gson = new Gson();
        StringBuilder stringBuilder = new StringBuilder();
        long startOfYearInMillis = startOfYear.getMillis();
        try {
            FileInputStream inputStream = context.openFileInput(budgetDataPrefix + startOfYearInMillis);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String thisLine = reader.readLine();
            while (thisLine != null) {
                stringBuilder.append(thisLine).append("\n");
                thisLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Type listType = new TypeToken<List<Transaction>>() {}.getType();
        List<Transaction> transactions = gson.fromJson(stringBuilder.toString(), listType);
        for(Transaction transaction: transactions) {
            BudgetManager.getInstance().addTransaction(transaction, null);
        }
    }
}
