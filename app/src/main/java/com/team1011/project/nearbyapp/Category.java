package com.team1011.project.nearbyapp;

import org.apache.http.message.BasicNameValuePair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Thomas on 2014-11-12.
 */
public abstract class Category {

    public static class type {
        public static final String EDITTEXT = "EditText";
        public static final String CHOICES = "Choices";
        public static final String RADIO = "Radio";
    }

    private Map<String, LinkedList<String>> elements = new HashMap<String, LinkedList<String>>();
    private LinkedList<String> fields = new LinkedList<String>();

    public LinkedList<String> getFields() {
        return fields;
    }

    protected void setFields(String[] newFields) {
        fields = toLinkedList(newFields);
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

    public Map<String, LinkedList<String>> stringToMap(String[] data) {
        Map<String, LinkedList<String>> map = new HashMap<String, LinkedList<String>>();
        BasicNameValuePair pair;

        for (String s : data) {
            pair = retrieve(s, '=');
            map.put(pair.getName(), toLinkedList(pair.getValue()));
        }

        return map;
    }

    public String[] mapToString(Map<String, LinkedList<String>> map) {
        String[] strings = new String[fields.size()];

        for (int i = 0; i < fields.size(); i++) {
            strings[i] = fields.get(i);
            strings[i] += "=";
            strings[i] += elements.get(fields.get(i)).get(1); // second element contains the value
        }

        return strings;
    }

    public BasicNameValuePair retrieve(String s, char delimit1) {
        String name = "";
        String value = "";
        int i = 0;
        while (s.charAt(i) != (delimit1)) {
            name += s.charAt(i);
            i++;
        }
        i++;
        while (i < s.length()) {
            value += s.charAt(i);
            i++;
        }
        return (new BasicNameValuePair(name, value));
    }

    public LinkedList<String> toLinkedList(String s) {
        LinkedList<String> list = new LinkedList<String>();
        list.add(s);
        return list;
    }

    public abstract boolean compare(Map<String, LinkedList<String>> m1, Map<String, LinkedList<String>> m2);
}
