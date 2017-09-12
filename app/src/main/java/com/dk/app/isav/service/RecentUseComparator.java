package com.dk.app.isav.service;

import android.app.usage.UsageStats;

import java.util.Comparator;

/**
 * Created by anuraag on 9/10/17.
 */

public class RecentUseComparator implements Comparator<UsageStats> {

    public int compare(UsageStats lhs, UsageStats rhs) {
        return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
    }


}
