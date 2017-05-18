package com.samuel.budgeter;


import android.content.Context;

public class UserDataManager {
    private static UserDataManager userDataManager;
    private double hourlyRate;
    private Context context;

    private UserDataManager(Context context) {
        hourlyRate = 0;
        this.context = context;
    }

    static UserDataManager getInstance(Context context) {
        if(userDataManager == null) {
            userDataManager = new UserDataManager(context);
        }
        return userDataManager;
    }

    public void setHourlyRate (double newRate) {
        hourlyRate = newRate;
        FileManager.getInstance(context).saveUserData();
    }

    public double getHourlyRate () {
        return hourlyRate;
    }

    public static void setInstance(UserDataManager source) {
        userDataManager = source;
    }
}
