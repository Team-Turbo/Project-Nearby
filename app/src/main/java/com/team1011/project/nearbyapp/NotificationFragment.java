package com.team1011.project.nearbyapp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.team1011.Database.Person;
import com.team1011.Database.PersonDataSource;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * interface.
 */
public class NotificationFragment extends ListFragment {
    private ArrayAdapter<Person> mAdapter;
    private List<Person> DiscoveredPeople;
    private PersonDataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new PersonDataSource(getActivity().getApplicationContext());
        dataSource.open();
        DiscoveredPeople = dataSource.getAllPeople();

        mAdapter = new ArrayAdapter<Person>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DiscoveredPeople);

        setListAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_activity_notifications);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        UI_Shell.activeChatFrag.setArgs(mAdapter.getItem(position).getRegID(),
                mAdapter.getItem(position).getPerson());

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, UI_Shell.activeChatFrag).commit();
    }
}
