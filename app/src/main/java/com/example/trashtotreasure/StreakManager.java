package com.example.trashtotreasure;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.Calendar;
public class StreakManager {
    private static final String PREFS_NAME = "TrashToTreasurePrefs";
    private static final String KEY_STREAK = "streak_count";
    private static final String KEY_LAST_DATE = "last_date_millis";
    private static final String KEY_XP = "player_xp";

    //used for storing streak values
    private final SharedPreferences prefs;
    //constructor for StreakManager
    public StreakManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    //returns streak count from storage
    public int getStreakCount() {
        return prefs.getInt(KEY_STREAK, 0);
    }

    //returns total xp from storage
    public int getTotalXp() {
        return prefs.getInt(KEY_XP, 0);
    }

    //calculates level
    public int getLevel() {
        return (getTotalXp() / 100) + 1;
    }

    //returns xp progress within the current level (0-99)
    public int getXpInLevel() {
        return getTotalXp() % 100;
    }

    //calculates rank based on level
    public String getRank() {
        int level = getLevel();
        if (level >= 10) return "Diamond";
        if (level >= 7) return "Gold";
        if (level >= 4) return "Silver";
        return "Bronze";
    }

    //determines whether the streak increments, stays the same or resets
    public void recordScan() {
        //checks for the last time an item has been scanned
        long lastScanTime = prefs.getLong(KEY_LAST_DATE, 0);
        long currentTime = System.currentTimeMillis();
        //compares last scan time and current scan time
        int currentStreak = getStreakCount();
        int xpGain = 0;

        if (isSameDay(lastScanTime, currentTime)) {
            //same day
            xpGain = 10;
        } else if (isYesterday(lastScanTime, currentTime)) {
            //success
            currentStreak++;
            xpGain = 20;
        } else {
            //messed day
            currentStreak = 1;
            xpGain = 20;
        }

        //save values
        prefs.edit()
                .putInt(KEY_STREAK, currentStreak)
                .putLong(KEY_LAST_DATE, currentTime)
                .putInt(KEY_XP, getTotalXp() + xpGain)
                .apply();
    }
    //helper to check if two timestamps are on the same calendar day
    private boolean isSameDay(long time1, long time2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        cal2.setTimeInMillis(time2);
        //true if times are same, false if times are different
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    //helper to check if time1 was exactly the day before time2
    private boolean isYesterday(long time1, long time2) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.setTimeInMillis(time2);
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        //gets two values to compare
        Calendar lastScan = Calendar.getInstance();
        lastScan.setTimeInMillis(time1);
        //true if time was a day after yesterday, false otherwise
        return yesterday.get(Calendar.YEAR) == lastScan.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == lastScan.get(Calendar.DAY_OF_YEAR);
    }
}
