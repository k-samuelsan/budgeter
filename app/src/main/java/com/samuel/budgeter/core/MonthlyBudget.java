package com.samuel.budgeter.core;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class MonthlyBudget {

    private static MonthlyBudget currentMonth;
    private DateTime startOfMonth;
    private List<WeeklyBudget> weeklyBudgetList;

    public MonthlyBudget(DateTime startOfMonth) {
        this.startOfMonth = startOfMonth;
        weeklyBudgetList = new ArrayList<>();
        DateTime dateIterator = startOfMonth;
        DateTime endOfMonth = startOfMonth.dayOfMonth().withMaximumValue();
        WeeklyBudget thisWeekBudget = new WeeklyBudget(dateIterator);
        while(dateIterator.isBefore(endOfMonth) || dateIterator.isEqual(endOfMonth)) {
            if(dateIterator.getDayOfWeek() == DateTimeConstants.MONDAY) {
                weeklyBudgetList.add(thisWeekBudget);
                thisWeekBudget = new WeeklyBudget(dateIterator);
            }
            dateIterator = dateIterator.plusDays(1);
        }
        weeklyBudgetList.add(thisWeekBudget);
    }

    public void addTransaction(Transaction transaction) {
        DateTime date = new DateTime(transaction.getDateInMillis());
        getWeekForDate(date).addTransaction(transaction);
    }

    public WeeklyBudget getWeekForDate(DateTime date) {
        for(int i = 1; i < weeklyBudgetList.size(); i++) {
            if(date.isBefore(weeklyBudgetList.get(i).getStartOfWeek())) {
                Log.d("Before", date + " is before " + weeklyBudgetList.get(i).getStartOfWeek());
                return weeklyBudgetList.get(i-1);
            }
        }
        return weeklyBudgetList.get(weeklyBudgetList.size());
    }

    public double getNetIncome() {
        double totalIncome = 0;
        List<Double> weeklyNetIncomes = new ArrayList<>();
        for(WeeklyBudget weeklyBudget: weeklyBudgetList) {
            weeklyNetIncomes.add(weeklyBudget.getNetIncome());
        }
        for(Double weeklyNetIncome: weeklyNetIncomes) {
            totalIncome += weeklyNetIncome;
        }
        return totalIncome;
    }

    public boolean containsDate(long dateInMillis) {
        DateTime date = new DateTime(dateInMillis);
        DateTime startOfMonth = new DateTime(this.startOfMonth);
        return (date.isAfter(startOfMonth) && date.isBefore(startOfMonth.plusMonths(1)));
    }

    public static void setCurrentMonth(MonthlyBudget month) {
        currentMonth = month;
    }

    public static MonthlyBudget getCurrentMonth() {
        return currentMonth;
    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for(WeeklyBudget weeklyBudget: weeklyBudgetList) {
            if(weeklyBudget.hasTransactions()) {
                transactions.addAll(weeklyBudget.getTransactions());
            }
        }
        return transactions;
    }

    DateTime getStartOfMonth() {
        return startOfMonth;
    }

    boolean hasTransactions() {
        for(WeeklyBudget weeklyBudget: weeklyBudgetList) {
            if(weeklyBudget.hasTransactions()) {
                return true;
            }
        }
        return false;
    }

}
