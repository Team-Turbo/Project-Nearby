package com.team1011.project.nearbyapp;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Thomas on 2014-11-12.
 */
public class Jobs extends Category {
    private String[] fields = {"Area"};
    private String[] area = {"Choices", "Technology", "Service", "Business", "Agriculture", "Communication", "Security"};

    Jobs() {
        Map<String, LinkedList<String>> m = this.getElements();
        m.put("Area", toLinkedList(area));
    }

    @Override
    public boolean compare(Map<String, LinkedList<String>> m1, Map<String, LinkedList<String>> m2) {
        if (!(m1.get(fields[0]).get(0).equalsIgnoreCase(m2.get(fields[0]).get(0)))) {
            return false;
        }
        return true;
    }
}