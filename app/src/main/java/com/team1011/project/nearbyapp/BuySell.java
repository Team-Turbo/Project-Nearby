package com.team1011.project.nearbyapp;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Thomas on 2014-11-12.
 */
public class BuySell extends Category {
    private String[] fields = {"Type", "Item Category", "Price", "Offset"};
    private String[] type = {"Radio", "Buying", "Selling"};
    private String[] itemCat = {"Choices", "Phone", "Tool", "Appliance", "Furniture", "Clothing",
            "Accessory", "Book", "Vehicle", "Electronics", "Sporting", "Toy/Game"};
    private String[] price = {"EditText"};
    private String[] offset = {"EditText"};

    public BuySell() {
        Map<String, LinkedList<String>> m = this.getElements();
        m.put(fields[0], toLinkedList(type));
        m.put(fields[1], toLinkedList(itemCat));
        m.put(fields[2], toLinkedList(price));
        m.put(fields[3], toLinkedList(offset));

        setFields(fields);
    }

    @Override
    public boolean compare(Map<String, LinkedList<String>> m1, Map<String, LinkedList<String>> m2) {
        double price1, price2, offset1, offset2;
        if (!(m1.get(fields[0]).get(0).equals(m2.get(fields[0]).get(0)))) {
            return false;
        }
        if (m1.get(fields[1]).get(0).equals(m2.get(fields[1]).get(0))) {
            return false;
        }
        try {
            price1 = Double.parseDouble(m1.get(fields[2]).get(0));
            price2 = Double.parseDouble(m2.get(fields[2]).get(0));
            offset1 = Double.parseDouble(m1.get(fields[3]).get(0));
            offset2 = Double.parseDouble(m2.get(fields[3]).get(0));
        } catch (NumberFormatException e) {
            return false;
        }
        if (price2 >= price1) {
            if (((price1 + offset1) < (price2 - offset2))) {
                return false;
            }
        } else if (price1 >= price2) {
            if (((price2 + offset2) < (price1 - offset1))) {
                return false;
            }
        }
        return true;
    }
}
