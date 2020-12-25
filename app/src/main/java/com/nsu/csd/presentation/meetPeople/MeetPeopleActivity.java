package com.nsu.csd.presentation.meetPeople;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.nsu.csd.presentation.common.SingleFragmentActivity;
import com.nsu.csd.presentation.monthMeet.MonthMeetActivity;

public class MeetPeopleActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return MeetPeopleFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle arguments = getIntent().getExtras();
         String  meet_id  = arguments.get("meet_id").toString();
        Intent mainIntent = new Intent(this, MonthMeetActivity.class);
        mainIntent.putExtra("meeting_id",meet_id);
        startActivity(mainIntent);
        finish();
    }
}
