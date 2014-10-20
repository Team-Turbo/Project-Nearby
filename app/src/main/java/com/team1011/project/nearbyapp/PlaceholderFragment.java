package com.team1011.project.nearbyapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
        this(0);
    }
    public PlaceholderFragment(int sectionNumber) {
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);

        ((TextView) rootView.findViewById(R.id.section_label)).setText("Section "
                + String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER)));

        return rootView;
    }
}