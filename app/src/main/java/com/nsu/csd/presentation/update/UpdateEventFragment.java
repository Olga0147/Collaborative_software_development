package com.nsu.csd.presentation.update;

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
import com.nsu.csd.model.EventDTO;
import com.nsu.csd.model.ServerError;
import com.nsu.csd.presentation.eventList.EventListActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class UpdateEventFragment extends Fragment {

    Gson gson = new GsonBuilder().create();
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
    Button save;

    TextView errorText;

    View.OnClickListener onClickListener_save_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO
            EventDTO newEventDTO = new EventDTO();
            newEventDTO.setTitle(eventTitle.getText().toString());
            newEventDTO.setDescription(eventOp.getText().toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String timezone = "Africa/Abidjan";

            //TODO:date2
            EventDTO.EndDTO endDTO = new EventDTO.EndDTO();
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
            EventDTO.StartDTO startDTO = new EventDTO.StartDTO();
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
            } else {
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

            SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("com.example.myapp.PREFERENCE", Context.MODE_PRIVATE);
            String id = prefs.getString("event_id","");
            newEventDTO.setId(id);

            updateEvent(newEventDTO);
        }
    };

    public static UpdateEventFragment newInstance() {
        Bundle args = new Bundle();
        UpdateEventFragment fragment = new UpdateEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.update_event,container,false);

        errorText = view.findViewById(R.id.new_event_error2);

        eventTitle = view.findViewById(R.id.event_title2);

        eventDate = view.findViewById(R.id.event_date2);
        eventStart = view.findViewById(R.id.start_time2);
        eventEnd = view.findViewById(R.id.end_time2);

        low = view.findViewById(R.id.low_rb2);
        mi = view.findViewById(R.id.middle_rb2);
        hi = view.findViewById(R.id.hight_rb2);

        pn = view.findViewById(R.id.pn2);
        vt = view.findViewById(R.id.vt2);
        sr = view.findViewById(R.id.sr2);
        cht = view.findViewById(R.id.cht2);
        pt = view.findViewById(R.id.pt2);
        sb = view.findViewById(R.id.sb2);
        vs = view.findViewById(R.id.vs2);

        eventOp = view.findViewById(R.id.opisanie2);

        save = view.findViewById(R.id.new_event_save_btn);
        save.setOnClickListener(onClickListener_save_btn);

        loadFullEvent();

        return view;
    }

    private void loadFullEvent(){
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("com.example.myapp.PREFERENCE", Context.MODE_PRIVATE);
        String token = prefs.getString("token","");
        String id = prefs.getString("event_id","");


        ApiUtilsToken.getApiService(token).get_event_full(id).enqueue(
                new retrofit2.Callback<EventDTO>() {

                    final Handler mainHandler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<EventDTO> call, final retrofit2.Response<EventDTO> response) {
                        //TODO
                        mainHandler.post(() -> {
                            if (!response.isSuccessful()) {
                                try {
                                    ServerError serverError = gson.fromJson(response.errorBody().string(), ServerError.class);
                                    //TODO: server error
                                } catch (IOException e) {
                                    //TODO: log
                                }
                            } else {
                                eventDTO = response.body();
                                fill();
                            }
                        });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<EventDTO> call, Throwable t) {
                        // mainHandler.post(() -> changeError_msg("Connection error"));
                        //TODO
                    }
                }
        );//enqueue() Для асинхронного получения
    }

    private void fill(){
        eventTitle.setText(eventDTO.getTitle() != null ? eventDTO.getTitle() : "");

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
            if(rr.contains("WE")){sr.setChecked(true);}
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

    private Date stringToDate(String aDate) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    private void updateEvent(EventDTO newEventDTO) {
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("com.example.myapp.PREFERENCE", Context.MODE_PRIVATE);
        String token = prefs.getString("token","");

        ApiUtilsToken.getApiService(token).update_event(newEventDTO).enqueue(
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
                                getFragmentManager().popBackStack();
                                String newDate = eventDate.getText().toString();

                                Intent mainIntent = new Intent(getActivity(), EventListActivity.class);
                                mainIntent.putExtra("date",newDate);
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

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }
}
