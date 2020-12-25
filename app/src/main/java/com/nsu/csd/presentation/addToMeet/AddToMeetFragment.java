package com.nsu.csd.presentation.addToMeet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsu.csd.R;
import com.nsu.csd.data.remote.ApiUtilsToken;
import com.nsu.csd.presentation.meetList.MeetListActivity;
import com.nsu.csd.presentation.newMeet.NewMeetFragment;

import java.util.Objects;

public class AddToMeetFragment extends Fragment {

    String token;

    Gson gson = new GsonBuilder().create();
    EditText meetCode;

    Button add_btn;
    Button create_btn;

    TextView errorText;

    public static AddToMeetFragment newInstance() {
        Bundle args = new Bundle();
        AddToMeetFragment fragment = new AddToMeetFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private View.OnClickListener onClickListener_add = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String code  = meetCode.getText().toString();
            if ((code.equals(""))) {
                changeError_msg("Код не должен быть пустым");
            } else {
                addToMeet(code);
            }
        }
    };

    private View.OnClickListener onClickListener_create = v -> {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.fragment_container_id, NewMeetFragment.newInstance()).
                commit();
    };


    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    private void addToMeet(String code) {

        ApiUtilsToken.getApiService(token).add_to_meet(code).enqueue(
                new retrofit2.Callback<Void>() {

                    final Handler mainHandler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<Void> call, final retrofit2.Response<Void> response) {
                        //TODO
                        mainHandler.post(() -> {
                            if (!response.isSuccessful()) {
                                //ServerError serverError = gson.fromJson(response.errorBody().string(), ServerError.class);
                                changeError_msg("Упс, данные не верены");
                            } else {
                                showMessage(R.string.registration_success);
                                Intent mainIntent = new Intent(getActivity(), MeetListActivity.class);
                                startActivity(mainIntent);
                                getActivity().finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                        mainHandler.post(() -> changeError_msg("Ошибка подключения"));
                    }
                }
        );//enqueue() Для асинхронного получения
    }

    private void changeError_msg(String connection_error) {
        errorText.setText(connection_error);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("com.example.myapp.PREFERENCE", Context.MODE_PRIVATE);
        token =  prefs.getString("token","");

        View view = inflater.inflate(R.layout.add_to_meet,container,false);

        errorText = view.findViewById(R.id.new_meet_error);

        meetCode = view.findViewById(R.id.meet_code);

        add_btn = view.findViewById(R.id.add_meet_btn);
        add_btn.setOnClickListener(onClickListener_add);

        create_btn = view.findViewById(R.id.create_btn);
        create_btn.setOnClickListener(onClickListener_create);
        return view;
    }
}
