package com.nsu.csd.presentation.newMeet;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.nsu.csd.presentation.common.SingleFragmentActivity;
import com.nsu.csd.presentation.meetList.MeetListActivity;

public class NewMeetActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return NewMeetFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(this, MeetListActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
