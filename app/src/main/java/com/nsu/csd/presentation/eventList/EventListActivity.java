package com.nsu.csd.presentation.eventList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nsu.csd.presentation.common.SingleFragmentActivity;

public class EventListActivity extends SingleFragmentActivity {

    private RecyclerView recyclerView;

    @Override
    protected Fragment getFragment() {
        return EventListFragment.newInstance();
    }



}
