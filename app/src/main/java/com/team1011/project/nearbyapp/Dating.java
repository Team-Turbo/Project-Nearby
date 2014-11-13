package com.team1011.project.nearbyapp;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Thomas on 2014-11-12.
 */
public class Dating extends Category {
    private String[] fields = {"Gender", "Age"};
    private String[] gender = {"EditText"};
    private String[] age = {"EditText"};

    Dating() {
        Map<String, LinkedList<String>> m = this.getElements();
        m.put("Gender", toLinkedList(gender));
        m.put("Age", toLinkedList(age));
    }

    @Override
    public boolean compare(Map<String, LinkedList<String>> m1, Map<String, LinkedList<String>> m2) {
        int age1, age2;
        if (m1.get(fields[0]).equals(m2.get(fields[0]))) {
            return false;
        }
        age1 = Integer.getInteger(m1.get(fields[1]).element());
        age2 = Integer.getInteger(m2.get(fields[1]).element());
        if (age1+5 >= age2 && age1-5 <= age2) {
            return false;
        }
        return true;
    }
}
