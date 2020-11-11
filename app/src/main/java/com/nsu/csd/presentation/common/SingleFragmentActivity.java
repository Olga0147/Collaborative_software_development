package com.nsu.csd.presentation.common;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nsu.csd.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        if(savedInstanceState == null){
            fragmentManager.beginTransaction().
                    replace(R.id.fragment_container_id,getFragment()).
                    commit();
        }
    }

    protected abstract Fragment getFragment();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            fragmentManager.popBackStack();
        }
    }

}
