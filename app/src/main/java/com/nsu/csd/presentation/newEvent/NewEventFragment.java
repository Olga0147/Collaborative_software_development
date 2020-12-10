package com.nsu.csd.presentation.newEvent;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsu.csd.R;
import com.nsu.csd.data.remote.ApiUtilsToken;
import com.nsu.csd.model.NewEventDTO;
import com.nsu.csd.model.ServerError;
import com.nsu.csd.presentation.eventList.EventListActivity;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class NewEventFragment extends Fragment {

    String token;

    Gson gson = new GsonBuilder().create();
    EditText eventTitle ;

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
    Button create;

    TextView errorText;


    public static NewEventFragment newInstance() {
        Bundle args = new Bundle();
        NewEventFragment fragment = new NewEventFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private Date stringToDate(String aDate) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        return simpledateformat.parse(aDate, pos);
    }

    View.OnClickListener onClickListener_create = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NewEventDTO newEventDTO = new NewEventDTO();
            newEventDTO.setTitle(eventTitle.getText().toString());
            newEventDTO.setDescription(eventOp.getText().toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String timezone = "Africa/Abidjan";

            //TODO:date2
            NewEventDTO.EndDTO endDTO = new NewEventDTO.EndDTO();
            Date date2;
            String text;
            try{
                date2 = stringToDate(eventDate.getText().toString()+" "+eventEnd.getText().toString()+":00");// Conversion
                text = sdf.format(date2);

            }catch (Exception e){
                errorText.setText(R.string.date_error);
                return;
            }

            endDTO.setDateTime(text);
            endDTO.setTimeZone(timezone);
            newEventDTO.setEnd(endDTO);

            //TODO:date1
            NewEventDTO.StartDTO startDTO = new NewEventDTO.StartDTO();
            Date date1;
            String text1;
            try{
                date1 = stringToDate(eventDate.getText().toString()+" "+eventStart.getText().toString()+":00");// Conversion
                text1 = sdf.format(date1);

            }catch (Exception e){
                errorText.setText(R.string.date_error);
                return;
            }

            startDTO.setDateTime(text1);
            startDTO.setTimeZone(timezone);
            newEventDTO.setStart(startDTO);

            if(low.isChecked()){
                newEventDTO.setImportance("LOW");
            }else if(mi.isChecked()){
                newEventDTO.setImportance("MEDIUM");
            }else if(hi.isChecked()){
                newEventDTO.setImportance("HIGH");
            }
            else {
                errorText.setText(R.string.import_error);
                return;
            }

            ArrayList<String> byday = new ArrayList<>();
            if(pn.isChecked()){byday.add("MO");}
            if(vt.isChecked()){byday.add("TU");}
            if(sr.isChecked()){byday.add("WE");}
            if(cht.isChecked()){byday.add("TH");}
            if(pt.isChecked()){byday.add("FR");}
            if(sb.isChecked()){byday.add("SA");}
            if(vs.isChecked()){byday.add("SU");}

            if(!byday.isEmpty()){
                StringBuilder rrule = new StringBuilder("FREQ=WEEKLY;BYDAY=");
                for (int i = 0; i < byday.size(); i++) {
                    rrule.append(byday.get(i));
                    if(i!=byday.size()-1){
                        rrule.append(",");
                    }
                }
                newEventDTO.setRecurrenceRule(rrule.toString());
            }

            saveEvent(newEventDTO);

        }
    };

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    private void saveEvent(NewEventDTO newEventDTO) {

        ApiUtilsToken.getApiService(token).events(newEventDTO).enqueue(
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
                                Intent mainIntent = new Intent(getActivity(), EventListActivity.class);
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

        View view = inflater.inflate(R.layout.new_event,container,false);

        errorText = view.findViewById(R.id.new_event_error);

        eventTitle = view.findViewById(R.id.event_title);

        eventDate = view.findViewById(R.id.event_date);
        eventStart = view.findViewById(R.id.start_time);
        eventEnd = view.findViewById(R.id.end_time);

        low = view.findViewById(R.id.low_rb);
        mi = view.findViewById(R.id.middle_rb);
        hi = view.findViewById(R.id.hight_rb);

        pn = view.findViewById(R.id.pn);
        vt = view.findViewById(R.id.vt);
        sr = view.findViewById(R.id.sr);
        cht = view.findViewById(R.id.cht);
        pt = view.findViewById(R.id.pt);
        sb = view.findViewById(R.id.sb);
        vs = view.findViewById(R.id.vs);

        eventOp = view.findViewById(R.id.opisanie);

        create = view.findViewById(R.id.new_event_create_btn);
        create.setOnClickListener(onClickListener_create);
        return view;
    }


}
