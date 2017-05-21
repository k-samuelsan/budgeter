package com.samuel.budgeter.managers;


public class UserDataManager {
    private static UserDataManager userDataManager;
    private double hourlyRate;

    private UserDataManager() {
        hourlyRate = 0;
    }

    public static UserDataManager getInstance() {
        if(userDataManager == null) {
            userDataManager = new UserDataManager();
        }
        return userDataManager;
    }

    public void setHourlyRate (double newRate) {
        hourlyRate = newRate;
    }

    public double getHourlyRate () {
        return hourlyRate;
    }

    static void setInstance(UserDataManager source) {
        userDataManager = source;
    }
}
