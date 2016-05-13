/*
 * RecentItem.java
 * 
 * Created on 5.6.2007, 11:40:19
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.vajasoft.classfilescanner;

import java.util.Comparator;

/**
 *
 * @author z705692
 */
public class RecentItem implements Comparable<RecentItem> {
    private static String SEPARATOR = "@";
    private static Comparator<RecentItem> TIME_COMPARATOR = new Comparator<RecentItem>(){
            public int compare(RecentItem o1, RecentItem o2) {
                return o1.time < o2.time ? -1 : (o1.time > o2.time ? 1 : 0);
            }
    };
    private static Comparator<RecentItem> TIME_COMPARATOR_REVERSE = new Comparator<RecentItem>(){
            public int compare(RecentItem o1, RecentItem o2) {
                return o1.time < o2.time ? 1 : (o1.time > o2.time ? -1 : 0);
            }
    };
        
    private String value;
    private long time;

    public static Comparator<RecentItem> getTimeComparator(boolean reverse) {
        return reverse ? TIME_COMPARATOR_REVERSE : TIME_COMPARATOR;
    }
    
    public RecentItem(String val) {
        String[] parts = val.split(SEPARATOR);
        value = parts[0];
        time = parts.length > 1 ?  Long.valueOf(parts[1]): System.currentTimeMillis();
    }

    public String getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }

    public String asPersistentData() {
        return value + SEPARATOR + String.valueOf(time);
    }

    public String toString() {
        return value;
    }

    public int compareTo(RecentItem o) {
        return value.compareTo(o.value);
    }
}
