package com.nsu.csd.presentation.work;

import androidx.fragment.app.Fragment;

import com.nsu.csd.presentation.common.SingleFragmentActivity;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return MainFragment.newInstance();
    }

}
