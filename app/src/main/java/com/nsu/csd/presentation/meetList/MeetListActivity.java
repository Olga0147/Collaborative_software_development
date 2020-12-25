package com.nsu.csd.presentation.meetList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nsu.csd.presentation.common.SingleFragmentActivity;

public class MeetListActivity extends SingleFragmentActivity {

    private RecyclerView recyclerView;

    @Override
    protected Fragment getFragment() {
        return MeetListFragment.newInstance();
    }

}
