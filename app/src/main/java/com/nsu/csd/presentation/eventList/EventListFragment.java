package com.nsu.csd.presentation.eventList;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsu.csd.R;
import com.nsu.csd.data.remote.ApiUtilsToken;
import com.nsu.csd.model.EventSummaryWithIdDTO;
import com.nsu.csd.model.ServerError;
import com.nsu.csd.presentation.newEvent.NewEventActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EventListFragment extends Fragment {

    private String token;

    private Date currentDate;

    private RecyclerView recyclerView;
    private EventListAdapter listAdapter;
    private Button newEventButton;

    private Button left;
    private Button right;

    private List<EventSummaryWithIdDTO> eventSummaryWithIdDTOS;
    Gson gson = new GsonBuilder().create();

    public static EventListFragment newInstance() {
        Bundle args = new Bundle();
        EventListFragment fragment = new EventListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Date stringToDate(String aDate) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd.MM.yyyy");
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }


    private final View.OnClickListener onClickListener_new_event = v -> {
        Intent mainIntent = new Intent(getActivity(), NewEventActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    };

    private final View.OnClickListener onClickListener_left = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, -1);

            Date nextDay = calendar.getTime();

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");;
            String newDate = dateFormat.format(nextDay);

            Intent mainIntent = new Intent(getActivity(), EventListActivity.class);
            mainIntent.putExtra("date",newDate);
            startActivity(mainIntent);
            getActivity().finish();
        }
    };

    private final View.OnClickListener onClickListener_right = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            Date nextDay = calendar.getTime();

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");;
            String newDate = dateFormat.format(nextDay);

            Intent mainIntent = new Intent(getActivity(), EventListActivity.class);
            mainIntent.putExtra("date",newDate);
            startActivity(mainIntent);
            getActivity().finish();

        }
    };

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("com.example.myapp.PREFERENCE", Context.MODE_PRIVATE);
        token =  prefs.getString("token","");

        View view = inflater.inflate(R.layout.main_layout,container,false);
        newEventButton = view.findViewById(R.id.new_event_btn);
        newEventButton.setOnClickListener(onClickListener_new_event);

        Bundle arguments = getActivity().getIntent().getExtras();
        if(arguments!=null && arguments.containsKey("date")){
            currentDate = stringToDate((String) arguments.get("date"));
        }else{
            currentDate = new Date();
        }

        //узнаем текущюю дату
        String y = String.valueOf(currentDate.getYear()+1900);
        String m = String.valueOf(currentDate.getMonth()+1);
        String d = String.valueOf(currentDate.getDate());
        TextView date = view.findViewById(R.id.event_data);
        date.setText(d+"."+m+"."+y);

        TextView day_of_week = view.findViewById(R.id.day_of_week);
        LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        Locale localeRu = new Locale("ru", "RU");
        String dw = dayOfWeek.getDisplayName(TextStyle.FULL,localeRu);
        day_of_week.setText(dw);


        recyclerView = view.findViewById(R.id.recycler_view_notes);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        left = view.findViewById(R.id.left_btn);
        left.setOnClickListener(onClickListener_left);
        right = view.findViewById(R.id.right_btn);
        right.setOnClickListener(onClickListener_right);

        loadNotes(y,m,d);
        return view;
    }

    public void loadNotes(String  y,String  m,String  d) {

            ApiUtilsToken.getApiService(token).get_ev_day(y,m,d).enqueue(
                    new retrofit2.Callback<List<EventSummaryWithIdDTO>>() {

                        final Handler mainHandler = new Handler(getActivity().getMainLooper());

                        @Override
                        public void onResponse(retrofit2.Call<List<EventSummaryWithIdDTO>> call, final retrofit2.Response<List<EventSummaryWithIdDTO>> response) {
                            mainHandler.post(() -> {
                                if (!response.isSuccessful()) {
                                    showMessage(R.string.data_error);
                                } else {
                                   eventSummaryWithIdDTOS = response.body();
                                    listAdapter = new EventListAdapter(eventSummaryWithIdDTOS, getActivity(),token);
                                    recyclerView.setAdapter(listAdapter);
                                }
                            });
                        }

                        @Override
                        public void onFailure(retrofit2.Call<List<EventSummaryWithIdDTO>> call, Throwable t) {
                            showMessage(R.string.connection_error);
                        }
                    }
            );//enqueue() Для асинхронного получения
    }


    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }


}
