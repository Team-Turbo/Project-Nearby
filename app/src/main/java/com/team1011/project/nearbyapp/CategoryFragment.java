package com.team1011.project.nearbyapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

    private RelativeLayout bgLayout;
    private TableLayout tableLayout;

    private int[] id = new int[2];

    private Category mCategory;

    public void setArgs(int categoryNum, int sectionNumber) {
        id[0] = categoryNum;
        id[1] = sectionNumber;
    }

    public int[] getID() {
        return id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        bgLayout = (RelativeLayout) rootView.findViewById(R.id.fragment_category_bg);
        tableLayout  = (TableLayout) rootView.findViewById(R.id.fragment_category_table);

        bgLayout.setBackgroundColor(
                Color.rgb(
                        (int) (Math.random() * 225),
                        (int) (Math.random() * 225),
                        (int) (Math.random() * 225)
                )
        );

        ((TextView) rootView.findViewById(R.id.section_label)).setText(
                "Category: " + id[0]
                        + "\n" + "Section " + id[1]
        );

        setCategory(id[0]);

        // for the below sections
        switch (id[1])
        {
            // "match creation" section
            case 0:
                Iterator it = mCategory.getElements().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, LinkedList<String>> pairs = (Map.Entry<String, LinkedList<String>>) it.next();

                    String typeOfElement = pairs.getValue().getFirst();

                    if (typeOfElement.equals(Category.type.EDITTEXT)) {
                        processEditText(pairs.getKey());
                    } else if (typeOfElement.equals(Category.type.CHOICES)) {
                        processChoice(pairs.getKey(), pairs.getValue());
                    } else if (typeOfElement.equals(Category.type.RADIO)) {
                        processRadio(pairs.getKey(), pairs.getValue());
                    }
                }
                break;

            // "current matches / chats" section
            case 1:
                break;
        }

        return rootView;
    }

    public void setCategory(int categoryPosition) {
        switch (categoryPosition) {
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

    /* UI elements with no choices */
    private void processEditText(String key) {
        TableRow tr = new TableRow(getActivity());
        TextView tv = new TextView(getActivity());
        EditText et = new EditText(getActivity());

        tv.setTextAppearance(getActivity(), R.style.catui_identifier);
        tv.setGravity(Gravity.END);
        et.setTextAppearance(getActivity(), R.style.catui_identifier);

        tv.setText(key + ": ");

        tr.addView(tv);
        tr.addView(et);

        tableLayout.addView(tr);
    }

    /* UI elements with choices, ex. for lists */
    private void processChoice(String key, LinkedList<String> choices) {
        TableRow tr = new TableRow(getActivity());
        TextView tv = new TextView(getActivity());
        Spinner spinner = new Spinner(getActivity());

        tv.setTextAppearance(getActivity(), R.style.catui_identifier);
        tv.setGravity(Gravity.END);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getActivity(),
                android.R.layout.simple_spinner_item);
        adapter.addAll(choices.subList(1,choices.size()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        tv.setText(key + ": ");

        tr.addView(tv);
        tr.addView(spinner);

        tableLayout.addView(tr);
    }

    /* UI elements which choices, ex. for radio buttons */
    private void processRadio(String key, LinkedList<String> choices) {
        TableRow tr = new TableRow(getActivity());
        TextView tv = new TextView(getActivity());
        RadioGroup rg = new RadioGroup(getActivity());

        tv.setTextAppearance(getActivity(), R.style.catui_identifier);
        tv.setGravity(Gravity.END);

        rg.setOrientation(LinearLayout.HORIZONTAL);
        for (int s = 1; s < choices.size(); ++s)
        {
            RadioButton rb = new RadioButton(getActivity());
            rb.setText(choices.get(s));
            rg.addView(rb);
        }

        tv.setText(key + ": ");

        tr.addView(tv);
        tr.addView(rg);

        tableLayout.addView(tr);
    }

}