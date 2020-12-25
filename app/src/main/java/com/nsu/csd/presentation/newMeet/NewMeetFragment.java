package com.nsu.csd.presentation.newMeet;

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
import com.nsu.csd.model.NewMeetingDto;
import com.nsu.csd.presentation.addToMeet.AddToMeetFragment;
import com.nsu.csd.presentation.authorization.AuthFragment;
import com.nsu.csd.presentation.meetList.MeetListActivity;
import com.nsu.csd.presentation.registration.RegFragment;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NewMeetFragment extends Fragment {

    String token;

    Gson gson = new GsonBuilder().create();
    EditText meetTitle;

    EditText meetStartDate;
    EditText meetEndDate;

    Button create;
    Button find_btn;

    TextView errorText;

    public static NewMeetFragment newInstance() {
        Bundle args = new Bundle();
        NewMeetFragment fragment = new NewMeetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Date stringToDate(String aDate) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd.MM.yyyy");

        return simpledateformat.parse(aDate, pos);
    }

    View.OnClickListener onClickListener_create = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NewMeetingDto newMeetingDto = new NewMeetingDto();
            newMeetingDto.setTitle(meetTitle.getText().toString());

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String timezone = "Africa/Abidjan";
            newMeetingDto.setTimeZone(timezone);

            Date date1;
            Date date2;
            String text;
            try{
                date1 = stringToDate(meetStartDate.getText().toString());// Conversion
                text = sdf.format(date1);

            }catch (Exception e){
                errorText.setText(R.string.date1_error);
                return;
            }

            newMeetingDto.setStart(text);

            try{
                date2 = stringToDate(meetEndDate.getText().toString());// Conversion
                text = sdf.format(date2);

            }catch (Exception e){
                errorText.setText(R.string.date1_error);
                return;
            }

        if(!isOlder(date1,date2)){
            errorText.setText(R.string.date2_error);
            return;
        }

            newMeetingDto.setEnd(text);


            saveEvent(newMeetingDto);

        }
    };

    private boolean isOlder(Date first,Date second){

        if(first.getYear()<second.getYear()){
            return true;
        }
        if(first.getYear()>second.getYear()){
            return false;
        }

        if(first.getYear()==second.getYear()){
            if(first.getMonth()<second.getMonth()){
                return true;
            }
            if(first.getMonth()>second.getMonth()){
                return false;
            }
            if(first.getMonth()==second.getMonth()){
                if(first.getDay()<=second.getDay()){
                    return true;
                }
                if(first.getDay()>second.getDay()){
                    return false;
                }
            }
        }

        return false;
    }

    View.OnClickListener onClickListener_find = v -> {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.fragment_container_id, AddToMeetFragment.newInstance()).
                commit();
    };

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    private void saveEvent(NewMeetingDto newMeetingDto) {

        ApiUtilsToken.getApiService(token).save_meet(newMeetingDto).enqueue(
                new retrofit2.Callback<Void>() {

                    final Handler mainHandler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<Void> call, final retrofit2.Response<Void> response) {
                        //TODO
                        mainHandler.post(() -> {
                            if (!response.isSuccessful()) {
                                //ServerError serverError = gson.fromJson(response.errorBody().string(), ServerError.class);
                                changeError_msg("Упс, формат данных не верен");
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

        View view = inflater.inflate(R.layout.new_meet,container,false);

        errorText = view.findViewById(R.id.new_meet_error);

        meetTitle = view.findViewById(R.id.meet_title1);

        meetStartDate = view.findViewById(R.id.meet_date1);
        meetEndDate = view.findViewById(R.id.meet_date2);

        create = view.findViewById(R.id.create_meet);
        create.setOnClickListener(onClickListener_create);

        find_btn = view.findViewById(R.id.find_btn);
        find_btn.setOnClickListener(onClickListener_find);

        return view;
    }
}
