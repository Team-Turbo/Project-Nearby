package com.team1011.project.nearbyapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Melvin on 10/22/2014.
 *
 * The category fragment.
 */
public class CategoryFragment extends Fragment {

    enum textType {
        RECEIVED, SENT
    }

    public static final String ARG_SECTION_NUMBER = "section_number";

    private View mRootView;

    private Category mCategory;

    public CategoryFragment(int sectionNumber) {
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        mRootView = rootView;

        rootView.findViewById(R.id.fragment_category_bg).setBackgroundColor(
                Color.rgb(
                        (int) (Math.random() * 225),
                        (int) (Math.random() * 225),
                        (int) (Math.random() * 225)
                )
        );

        ((TextView) rootView.findViewById(R.id.section_label)).setText(
                "Category: " + UI_Shell.getCurrentCategory()
                        + "\n" + "Section " + String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER))
        );

        setCategory(UI_Shell.getCurrentCategoryInt());

        Iterator it = mCategory.getElements().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, LinkedList<String>> pairs = (Map.Entry<String, LinkedList<String>>)it.next();

            String typeOfElement = pairs.getValue().getFirst();

            if (typeOfElement.equals("EditText")) {
                processEditText(pairs.getKey());
            } else {
                processList(pairs.getKey(), pairs.getValue());
            }
        }

        return rootView;
    }

    public void setCategory(int position) {
        switch (position) {
            case 0:
                mCategory = new Jobs();
                break;
            case 1:
                mCategory = new Dating();
                break;
            case 2:
                mCategory = new BuySell();
                break;
        }
    }

    /*
    public abstract String getNotificationText(textType tt);

    public abstract String getTabText(textType tt);

    public abstract Map<String, String> getElements();

    public abstract void setValue(String key, String value);
    */

    /* UI elements with no choices */
    private void processEditText(String key) {

    }

    /* UI elements with choices, ex. for lists */
    private void processList(String key, LinkedList<String> choices) {

    }

}