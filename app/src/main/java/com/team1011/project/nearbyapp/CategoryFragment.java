package com.team1011.project.nearbyapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
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

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Melvin on 10/22/2014.
 *
 * The category fragment.
 */
public class CategoryFragment extends Fragment {

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
                        255, 105, 180
                )
        );

        setCategory(id[0]);

        // for the below sections
        switch (id[1])
        {
            // "match creation" section
            case 0:
                Map<String,LinkedList<String>> elements = mCategory.getElements();

                for (String key : mCategory.getFields())
                {
                    LinkedList<String> values = elements.get(key);
                    String typeOfElement = values.element();

                    if (typeOfElement.equals(Category.type.EDITTEXT)) {
                        processEditText(key);
                    } else if (typeOfElement.equals(Category.type.CHOICES)) {
                        processChoice(key, values);
                    } else if (typeOfElement.equals(Category.type.RADIO)) {
                        processRadio(key, values);
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
        tv.setTextAppearance(getActivity(), R.style.catui_identifier);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        tv.setText(key + ": ");

        EditText et = new EditText(getActivity());
        et.setTextAppearance(getActivity(), R.style.catui_edittext_text);
        et.setBackgroundResource(android.R.drawable.editbox_background);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        tr.addView(tv);
        tr.addView(et);

        tableLayout.addView(tr);
    }

    /* UI elements with choices, ex. for lists */
    private void processChoice(String key, LinkedList<String> choices) {
        TableRow tr = new TableRow(getActivity());

        TextView tv = new TextView(getActivity());
        tv.setTextAppearance(getActivity(), R.style.catui_identifier);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        tv.setText(key + ": ");

        Spinner spinner = new Spinner(getActivity());
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getActivity(),
                android.R.layout.simple_spinner_item);
        adapter.addAll(choices.subList(1,choices.size()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        tr.addView(tv);
        tr.addView(spinner);

        tableLayout.addView(tr);
    }

    /* UI elements which choices, ex. for radio buttons */
    private void processRadio(String key, LinkedList<String> choices) {
        TableRow tr = new TableRow(getActivity());
        RadioGroup rg = new RadioGroup(getActivity());

        TextView tv = new TextView(getActivity());
        tv.setTextAppearance(getActivity(), R.style.catui_identifier);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        tv.setText(key + ": ");

        rg.setOrientation(LinearLayout.HORIZONTAL);
        for (int s = 1; s < choices.size(); ++s)
        {
            RadioButton rb = new RadioButton(getActivity());
            rb.setText(choices.get(s));
            rb.setTextAppearance(getActivity(), R.style.catui_identifier);
            rg.addView(rb);
        }

        tr.addView(tv);
        tr.addView(rg);

        tableLayout.addView(tr);
    }
}