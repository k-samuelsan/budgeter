package com.samuel.budgeter;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class MonthlyBudget {

    private static MonthlyBudget currentMonth;
    long startOfMonthInMillisec;
    List<WeeklyBudget> weeklyBudgetList;

    public MonthlyBudget(long startOfMonthInMillisec) {
        this.startOfMonthInMillisec = startOfMonthInMillisec;
        DateTime startOfMonth = new DateTime(startOfMonthInMillisec);
        if(currentMonth == null || startOfMonth.isAfter(new DateTime(currentMonth.startOfMonthInMillisec))) {
            Log.d("Test", "hi");
            currentMonth = this;
        }
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

    public void addExpense(Expense expense) {
        getWeeklyBudget(expense.getDate()).addExpense(expense);
    }

    public void addIncome(Income income) {
        getWeeklyBudget(income.getDateInMillisec()).addIncome(income);
    }

    private WeeklyBudget getWeeklyBudget(long dateInMillisec) {
        DateTime date = new DateTime(dateInMillisec);
        for(int i = 1; i < weeklyBudgetList.size(); i++) {
            if(date.isBefore(weeklyBudgetList.get(i).getStartOfWeek())) {
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

    public boolean containsDate(long dateInMillisec) {
        DateTime date = new DateTime(dateInMillisec);
        DateTime startOfMonth = new DateTime(startOfMonthInMillisec);
        return (date.isAfter(startOfMonth) && date.isBefore(startOfMonth.plusMonths(1)));
    }

    public static MonthlyBudget getCurrentMonth() {
        return currentMonth;
    }

    public List<Expense> getAllExpenses() {
        List<Expense> budgets = new ArrayList<>();
        for(WeeklyBudget weeklyBudget: weeklyBudgetList) {
            if(weeklyBudget.hasExpenses()) {
                budgets.addAll(weeklyBudget.getExpenses());
            }
        }
        return budgets;
    }

    public List<Income> getAllIncome() {
        List<Income> incomeList = new ArrayList<>();
        for(WeeklyBudget weeklyBudget: weeklyBudgetList) {
            if(weeklyBudget.hasIncome()) {
                incomeList.addAll(weeklyBudget.getIncomeList());
            }
        }
        return incomeList;
    }

}
