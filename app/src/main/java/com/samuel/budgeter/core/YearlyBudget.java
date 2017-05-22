package com.samuel.budgeter.core;

import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class YearlyBudget {

    private static YearlyBudget currentYear;
    private DateTime startOfYear;
    private List<MonthlyBudget> monthlyBudgetList;

    public YearlyBudget(DateTime startOfYear) {
        this.startOfYear = startOfYear;
        monthlyBudgetList = new ArrayList<>();
        for(int i = 0; i < 12; i++) {
            DateTime startOfThisMonth = new DateTime(startOfYear).plusMonths(i);
            monthlyBudgetList.add(new MonthlyBudget(startOfThisMonth));
        }
    }

    public void addTransaction(Transaction transaction) {
        DateTime date = new DateTime(transaction.getDateInMillis());
        getMonthForDate(date).addTransaction(transaction);
    }

    public MonthlyBudget getMonthForDate(DateTime date) {
        for(int i = 1; i < monthlyBudgetList.size(); i++) {
            if(date.isBefore(monthlyBudgetList.get(i).getStartOfMonth())) {
                Log.d("Before", date + " is before " + monthlyBudgetList.get(i).getStartOfMonth());
                return monthlyBudgetList.get(i-1);
            }
        }
        return monthlyBudgetList.get(monthlyBudgetList.size()-1);
    }

    public double getNetIncome() {
        double totalIncome = 0;
        List<Double> monthlyNetIncomes = new ArrayList<>();
        for(MonthlyBudget monthlyBudget: monthlyBudgetList) {
            monthlyNetIncomes.add(monthlyBudget.getNetIncome());
        }
        for(Double weeklyNetIncome: monthlyNetIncomes) {
            totalIncome += weeklyNetIncome;
        }
        return totalIncome;
    }

    public boolean containsDate(long dateInMillis) {
        DateTime date = new DateTime(dateInMillis);
        Log.d("COMPARISON", "checking " + date + " against " + startOfYear);
        return (date.isAfter(startOfYear) && date.isBefore(startOfYear.plusYears(1)));
    }

    public static void setCurrentYear(YearlyBudget month) {
        currentYear = month;
    }

    public static YearlyBudget getCurrentYear() {
        return currentYear;
    }

    public List<Transaction> getTransactions() {
        List<Transaction> budgets = new ArrayList<>();
        for(MonthlyBudget monthlyBudget: monthlyBudgetList) {
            if(monthlyBudget.hasTransactions()) {
                budgets.addAll(monthlyBudget.getTransactions());
            }
        }
        return budgets;
    }

    public DateTime getStartOfYear() {
        return startOfYear;
    }
}
