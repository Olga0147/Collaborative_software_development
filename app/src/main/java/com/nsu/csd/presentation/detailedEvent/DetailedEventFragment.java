package com.nsu.csd.presentation.detailedEvent;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.nsu.csd.model.EventDTO;
import com.nsu.csd.model.ServerError;
import com.nsu.csd.presentation.eventList.EventListActivity;
import com.nsu.csd.presentation.update.UpdateEventFragment;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailedEventFragment extends Fragment {

    Gson gson = new GsonBuilder().create();

    String token;
    String event_id;

    EditText eventTitle ;
    EventDTO eventDTO;

    EditText eventDate;
    EditText eventStart;
    EditText eventEnd;
    RadioButton low;
    RadioButton mi;
    RadioButton hi;
    CheckBox pn;
    CheckBox vt;
    CheckBox sr;
    CheckBox cht;
    CheckBox pt;
    CheckBox sb;
    CheckBox vs;
    EditText eventOp;
    Button update;
    Button delete;

    View.OnClickListener onClickListener_update_btn = v -> {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.fragment_container_id, UpdateEventFragment.newInstance()).
                addToBackStack(UpdateEventFragment.class.getName()).
                commit();
    };

    View.OnClickListener onClickListener_delete = v -> deleteEvent();


    public static DetailedEventFragment newInstance() {
        Bundle args = new Bundle();
        DetailedEventFragment fragment = new DetailedEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("com.example.myapp.PREFERENCE", Context.MODE_PRIVATE);
        token =  prefs.getString("token","");

        Bundle arguments = getActivity().getIntent().getExtras();
        if(arguments!=null){
            event_id  = arguments.get("event_id").toString();
            prefs.edit().putString("event_id",event_id).apply();
        }

        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().
                addToBackStack(DetailedEventFragment.class.getName()).
                commit();

        View view = inflater.inflate(R.layout.detailed_event,container,false);

        //errorText = view.findViewById(R.id.new_event_error1);

        eventTitle = view.findViewById(R.id.event_title1);
        eventTitle.setEnabled(false);

        eventDate = view.findViewById(R.id.event_date1);
        eventDate.setEnabled(false);
        eventStart = view.findViewById(R.id.start_time1);
        eventStart.setEnabled(false);
        eventEnd = view.findViewById(R.id.end_time1);
        eventEnd.setEnabled(false);

        low = view.findViewById(R.id.low_rb1);
        low.setEnabled(false);
        mi = view.findViewById(R.id.middle_rb1);
        mi.setEnabled(false);
        hi = view.findViewById(R.id.hight_rb1);
        hi.setEnabled(false);

        pn = view.findViewById(R.id.pn1);
        pn.setEnabled(false);
        vt = view.findViewById(R.id.vt1);
        vt.setEnabled(false);
        sr = view.findViewById(R.id.sr1);
        sr.setEnabled(false);
        cht = view.findViewById(R.id.cht1);
        cht.setEnabled(false);
        pt = view.findViewById(R.id.pt1);
        pt.setEnabled(false);
        sb = view.findViewById(R.id.sb1);
        sb.setEnabled(false);
        vs = view.findViewById(R.id.vs1);
        vs.setEnabled(false);

        eventOp = view.findViewById(R.id.opisanie1);
        eventOp.setEnabled(false);

        update = view.findViewById(R.id.update_btn);
        update.setOnClickListener(onClickListener_update_btn);
        delete = view.findViewById(R.id.delete_btn);
        delete.setOnClickListener(onClickListener_delete);

        loadFullEvent();

        return view;
    }

    private void loadFullEvent(){

        ApiUtilsToken.getApiService(token).get_event_full(event_id).enqueue(
                new retrofit2.Callback<EventDTO>() {

                    final Handler mainHandler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<EventDTO> call, final retrofit2.Response<EventDTO> response) {
                        //TODO
                        mainHandler.post(() -> {
                            if (!response.isSuccessful()) {
                                showMessage(R.string.event_error);
                            } else {
                                eventDTO = response.body();
                               fill();
                            }
                        });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<EventDTO> call, Throwable t) {
                        showMessage(R.string.connection_error);
                    }
                }
        );//enqueue() Для асинхронного получения
    }

    private void deleteEvent(){
        ApiUtilsToken.getApiService(token).delete_event(event_id).enqueue(
                new retrofit2.Callback<Void>() {

                    final Handler mainHandler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<Void> call, final retrofit2.Response<Void> response) {
                        //TODO
                        mainHandler.post(() -> {
                            if (!response.isSuccessful()) {
                                showMessage(R.string.event_error);
                            } else {
                                Intent mainIntent = new Intent(getActivity(), EventListActivity.class);
                                startActivity(mainIntent);
                                getActivity().finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                        showMessage(R.string.connection_error);
                    }
                }
        );//enqueue() Для асинхронного получения
    }

    @SuppressLint("SetTextI18n")
    private void fill(){
        eventTitle.setText(eventDTO.getTitle() != null ? eventDTO.getTitle() : "");

        //date
        String Start=eventDTO.getStart().getDateTime();
        String DateStart =Start.split("T")[0];
        String TimeStart =Start.split("T")[1];

        String YearStart = DateStart.split("-")[0];
        String MonthStart = DateStart.split("-")[1];
        String DayStart = DateStart.split("-")[2];

        String HourStart = TimeStart.split(":")[0];
        String MinuteStart = TimeStart.split(":")[1];
        eventStart.setText(HourStart+":"+MinuteStart);
        eventDate.setText(DayStart+"."+MonthStart+"."+YearStart);

        String End=eventDTO.getEnd().getDateTime();
        String TimeEnd =End.split("T")[1];

        String HourEnd = TimeEnd.split(":")[0];
        String MinuteEnd = TimeEnd.split(":")[1];
        eventEnd.setText(HourEnd+":"+MinuteEnd);

        String im = eventDTO.getImportance();
        if(im!=null){
            switch (im){
                case "LOW":low.setChecked(true);break;
                case "MEDIUM":mi.setChecked(true);break;
                case "HIGH":hi.setChecked(true);break;
            }
        }

        String rr = eventDTO.getRecurrenceRule();
        rr = rr.split(";")[1];
        if(rr!=null){
            if(rr.contains("MO")){pn.setChecked(true);}
            if(rr.contains("TU")){vt.setChecked(true);}
            if(rr.contains("WE")){
                sr.setChecked(true);
            }
            if(rr.contains("TH")){cht.setChecked(true);}
            if(rr.contains("FR")){pt.setChecked(true);}
            if(rr.contains("SA")){sb.setChecked(true);}
            if(rr.contains("SU")){vs.setChecked(true);}
        }

        eventOp.setText(eventDTO.getDescription() != null ? eventDTO.getDescription() : "");
    }

    private Date stringToDate(String aDate, String aFormat) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }


}
