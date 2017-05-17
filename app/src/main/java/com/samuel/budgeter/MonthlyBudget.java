package com.samuel.budgeter;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class MonthlyBudget {

    private static MonthlyBudget currentMonth;
    DateTime startOfMonth;
    List<WeeklyBudget> weeklyBudgetList;

    public MonthlyBudget(DateTime startOfMonth) {
        this.startOfMonth = startOfMonth;
        if(currentMonth == null || startOfMonth.isAfter(currentMonth.startOfMonth)) {
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
        getWeeklyBudget(income.getDate()).addIncome(income);
    }

    private WeeklyBudget getWeeklyBudget(DateTime date) {
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

    public boolean containsDate(DateTime date) {
        return (date.isAfter(startOfMonth) && date.isBefore(startOfMonth.plusMonths(1)));
    }

    public static MonthlyBudget getCurrentMonth() {
        return currentMonth;
    }

}
