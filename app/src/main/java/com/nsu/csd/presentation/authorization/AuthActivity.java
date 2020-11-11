package com.nsu.csd.presentation.authorization;

import androidx.fragment.app.Fragment;

import com.nsu.csd.presentation.common.SingleFragmentActivity;

/**
 * Activity for auth and registration
 */

public class AuthActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return AuthFragment.newInstance();
    }
}
