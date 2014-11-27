package com.team1011.project.nearbyapp;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Thomas on 2014-11-12.
 */
public class Dating extends Category {
    private String[] fields = {"Your Gender", "Your Age", "Their Gender", "Their Age Low", "Their Age High"};
    private String[] yourGender = {type.RADIO, "Male", "Female"};
    private String[] yourAge = {"EditText"};
    private String[] theirGender = {type.RADIO, "Male", "Female"};
    private String[] theirAgeLow = {"EditText"};
    private String[] theirAgeHigh = {"EditText"};

    public Dating() {
        Map<String, LinkedList<String>> m = this.getElements();
        m.put(fields[0], toLinkedList(yourGender));
        m.put(fields[1], toLinkedList(yourAge));
        m.put(fields[2], toLinkedList(theirGender));
        m.put(fields[3], toLinkedList(theirAgeLow));
        m.put(fields[4], toLinkedList(theirAgeHigh));

        setFields(fields);
    }

    @Override
    public boolean compare(Map<String, LinkedList<String>> m1, Map<String, LinkedList<String>> m2) {
        int myAge1, theirAgeLow1, theirAgeHi1, myAge2, theirAgeLow2, theirAgeHi2;
        if (!(m1.get(fields[0]).equals(m2.get(fields[3])) &&
                m1.get(fields[3]).equals(m2.get(fields[0])))) {
            return false;
        }
        try {
            myAge1 = Integer.parseInt(m1.get(fields[1]).element());
            myAge2 = Integer.parseInt(m2.get(fields[1]).element());
            theirAgeLow1 = Integer.parseInt(m1.get(fields[3]).element());
            theirAgeLow2 = Integer.parseInt(m2.get(fields[3]).element());
            theirAgeHi1 = Integer.parseInt(m1.get(fields[4]).element());
            theirAgeHi2 = Integer.parseInt(m2.get(fields[4]).element());
        } catch (NumberFormatException e) {
            return false;
        }

        theirAgeLow1 = (theirAgeLow1 < 18) ? 18 : theirAgeLow1;
        theirAgeHi1 = (theirAgeHi1 < 18) ? 18 : theirAgeHi1;
        theirAgeLow2 = (theirAgeLow2 < 18) ? 18 : theirAgeLow2;
        theirAgeHi2 = (theirAgeHi2 < 18) ? 18 : theirAgeHi2;

        if ((theirAgeLow2 >= myAge1 && myAge1 <= theirAgeHi2) &&
                theirAgeLow1 >= myAge2 && myAge2 <= theirAgeHi1)
            return true;
        else
            return false;
    }
}
