package com.samuel.budgeter.core;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class MonthlyBudget {

    private static MonthlyBudget currentMonth;
    private long startOfMonthInMillis;
    private List<WeeklyBudget> weeklyBudgetList;

    public MonthlyBudget(long startOfMonthInMillis) {
        this.startOfMonthInMillis = startOfMonthInMillis;
        DateTime startOfMonth = new DateTime(startOfMonthInMillis);
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
        getWeekForDate(expense.getDate()).addExpense(expense);
    }

    public void addIncome(Income income) {
        getWeekForDate(income.getDateInMillis()).addIncome(income);
    }

    public WeeklyBudget getWeekForDate(long dateInMillis) {
        DateTime date = new DateTime(dateInMillis);
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
        DateTime startOfMonth = new DateTime(startOfMonthInMillis);
        return (date.isAfter(startOfMonth) && date.isBefore(startOfMonth.plusMonths(1)));
    }

    public static void setCurrentMonth(MonthlyBudget month) {
        currentMonth = month;
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
