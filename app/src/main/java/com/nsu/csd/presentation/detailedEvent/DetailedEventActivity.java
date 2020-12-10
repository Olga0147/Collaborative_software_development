package com.nsu.csd.presentation.detailedEvent;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.nsu.csd.presentation.common.SingleFragmentActivity;
import com.nsu.csd.presentation.eventList.EventListActivity;

public class DetailedEventActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return DetailedEventFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Integer i  = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Intent mainIntent = new Intent(this, EventListActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
