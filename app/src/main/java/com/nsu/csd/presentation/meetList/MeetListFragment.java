package com.nsu.csd.presentation.meetList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nsu.csd.R;
import com.nsu.csd.data.remote.ApiUtils;
import com.nsu.csd.data.remote.ApiUtilsToken;
import com.nsu.csd.model.MeetingSummaryDto;
import com.nsu.csd.presentation.authorization.AuthActivity;
import com.nsu.csd.presentation.eventList.EventListActivity;
import com.nsu.csd.presentation.newMeet.NewMeetActivity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MeetListFragment  extends Fragment {

    private RecyclerView recyclerView;
    private MeetListAdapter listAdapter;
    private String token;

    private Button newMeetButton;

    private ImageButton menu_calend_btn;
    private ImageButton outBtn;


    private List<MeetingSummaryDto> meetingSummaryDtos;

    public static MeetListFragment newInstance() {
        Bundle args = new Bundle();
        MeetListFragment fragment = new MeetListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private final View.OnClickListener onClickListener_new_meet = v -> {
    Intent mainIntent = new Intent(getActivity(), NewMeetActivity.class);
    startActivity(mainIntent);
    getActivity().finish();
    };

    private final View.OnClickListener onClickListener_calend = v -> {
        Intent mainIntent = new Intent(getActivity(), EventListActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    };

    private final View.OnClickListener onClickListener_out = v -> {
        ApiUtils.deleteApiService();
        ApiUtilsToken.deleteApiService();
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("com.example.myapp.PREFERENCE", Context.MODE_PRIVATE);
        prefs.edit().remove("token").apply();
        Intent mainIntent = new Intent(getActivity(), AuthActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    };

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("com.example.myapp.PREFERENCE", Context.MODE_PRIVATE);
        token =  prefs.getString("token","");

        View view = inflater.inflate(R.layout.start_meet_layout,container,false);
        newMeetButton = view.findViewById(R.id.new_meet_btn);
        newMeetButton.setOnClickListener(onClickListener_new_meet);

        menu_calend_btn = view.findViewById(R.id.main_menu_events_btn);
        menu_calend_btn.setOnClickListener(onClickListener_calend);

        outBtn = view.findViewById(R.id.main_menu_settings_btn);
        outBtn.setOnClickListener(onClickListener_out);

        recyclerView = view.findViewById(R.id.recycler_view_meet);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        loadNotes();
        return view;
    }

    public void loadNotes() {

        ApiUtilsToken.getApiService(token).get_meet_list().enqueue(
                new retrofit2.Callback<List<MeetingSummaryDto>>() {

                    final Handler mainHandler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<List<MeetingSummaryDto>> call, final retrofit2.Response<List<MeetingSummaryDto>> response) {
                        mainHandler.post(() -> {
                            if (!response.isSuccessful()) {
                                showMessage(R.string.data_error);
                            } else {
                                meetingSummaryDtos = response.body();
                                listAdapter = new MeetListAdapter(meetingSummaryDtos, getActivity(),token);
                                recyclerView.setAdapter(listAdapter);
                            }
                        });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<MeetingSummaryDto>> call, Throwable t) {
                        showMessage(R.string.connection_error);
                    }
                }
        );//enqueue() Для асинхронного получения
    }


    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }


}
