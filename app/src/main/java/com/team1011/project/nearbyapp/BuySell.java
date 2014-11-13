package com.team1011.project.nearbyapp;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Thomas on 2014-11-12.
 */
public class BuySell extends Category {
    private String[] fields = {"Gender", "Age"};

    @Override
    public boolean compare(Map<String, LinkedList<String>> m1, Map<String, LinkedList<String>> m2) {
        return false;
    }
}
