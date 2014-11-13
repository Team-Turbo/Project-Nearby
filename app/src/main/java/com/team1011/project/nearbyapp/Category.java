package com.team1011.project.nearbyapp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Thomas on 2014-11-12.
 */
public abstract class Category {
    private Map<String, LinkedList<String>> elements = new HashMap<String, LinkedList<String>>();
    private String[] fields;

    public String[] getFields() {
        return fields;
    }

    protected static LinkedList<String> toLinkedList(String[] strings) {
        LinkedList<String> list = new LinkedList<String>();
        for (String s : strings) {
            list.add(s);
        }
        return list;
    }
    public Map<String, LinkedList<String>> getElements() {
        return elements;
    }

    public abstract boolean compare(Map<String, LinkedList<String>> m1, Map<String, LinkedList<String>> m2);
}
