package com.nsu.csd.presentation.newEvent;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.nsu.csd.presentation.common.SingleFragmentActivity;
import com.nsu.csd.presentation.eventList.EventListActivity;

public class NewEventActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return NewEventFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(this, EventListActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
