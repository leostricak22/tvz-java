package org.example.sort;

import java.util.Comparator;

public class HighestIntegerComparator implements Comparator<Integer> {
    public int compare(Integer int1, Integer int2) {
        return int2.compareTo(int1);
    }
}
