package com.samuel.budgeter.managers;
import java.util.Calendar;
import java.util.Observable;

import android.content.Context;
import android.util.Log;

import com.samuel.budgeter.core.MonthlyBudget;
import com.samuel.budgeter.core.Transaction;
import com.samuel.budgeter.core.WeeklyBudget;
import com.samuel.budgeter.core.YearlyBudget;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager extends Observable {
    private static DateTime startOfCurrentYear;
    private static DateTime startOfCurrentMonth;
    private static DateTime startOfCurrentWeek;
    private static BudgetManager budgetManager;
    private YearlyBudget loadedYear;
    private DateTimeZone timeZone;

    private BudgetManager() {
        Calendar calendar = Calendar.getInstance();
        timeZone = DateTimeZone.forOffsetMillis(calendar.getTimeZone().getOffset(calendar.getTimeInMillis()));
        DateTime now = new DateTime().withTime(0,0,0,0).withZone(timeZone);
        startOfCurrentYear = now.withDayOfYear(1);
        startOfCurrentMonth = now.withDayOfMonth(1);
        startOfCurrentWeek = now.withDayOfWeek(DateTimeConstants.MONDAY);
        YearlyBudget thisYear = new YearlyBudget(startOfCurrentYear);
        MonthlyBudget thisMonth = thisYear.getMonthForDate(startOfCurrentMonth);
        WeeklyBudget thisWeek = thisMonth.getWeekForDate(startOfCurrentWeek);
        YearlyBudget.setCurrentYear(thisYear);
        MonthlyBudget.setCurrentMonth(thisMonth);
        WeeklyBudget.setCurrentWeek(thisWeek);
        loadedYear = thisYear;
    }

    public static BudgetManager getInstance() {
        if(budgetManager == null) {
            budgetManager = new BudgetManager();
        }
        return budgetManager;
    }

    public void addTransaction(Transaction transaction, Context context) {
        if(!loadedYear.containsDate(transaction.getDateInMillis())) {
            Log.d("SWITCH", "switching years: " + new DateTime(transaction.getDateInMillis()));
            DateTime startOfNewYear = new DateTime(transaction.getDateInMillis()).withDayOfYear(1).withTime(0,0,0,0);
            FileManager.getInstance().loadYear(startOfNewYear, context);
        }
        loadedYear.addTransaction(transaction);
        setChanged();
        notifyObservers();
        if(context != null) {
            FileManager.getInstance().saveBudgetData(context);
        }
    }

    public double getCurrentMonthNetIncome() {
        MonthlyBudget currentMonth = MonthlyBudget.getCurrentMonth();
        if(currentMonth == null) {
            return 0;
        }
        return currentMonth.getNetIncome();
    }

    public double getCurrentWeekNetIncome() {
        WeeklyBudget currentWeek = WeeklyBudget.getCurrentWeek();
        Log.d("test" , "getting net for week " + currentWeek.getStartOfWeek());
        if(currentWeek == null) {
            return 0;
        }
        return currentWeek.getNetIncome();
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(loadedYear.getTransactions());
        return transactions;
    }

    long getStartOfLoadedYearInMillis() {
        return loadedYear.getStartOfYear().getMillis();
    }

    void setLoadedYear(YearlyBudget yearlyBudget) {
        this.loadedYear = yearlyBudget;
        if(loadedYear.getStartOfYear().isEqual(startOfCurrentYear)) {
            MonthlyBudget thisMonth = loadedYear.getMonthForDate(startOfCurrentMonth);
            WeeklyBudget thisWeek = thisMonth.getWeekForDate(startOfCurrentWeek);
            YearlyBudget.setCurrentYear(loadedYear);
            MonthlyBudget.setCurrentMonth(thisMonth);
            WeeklyBudget.setCurrentWeek(thisWeek);
        }
    }

    public DateTimeZone getTimeZone() {
        return timeZone;
    }
}
