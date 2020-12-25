package com.nsu.csd.presentation.monthMeet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.nsu.csd.R;
import com.nsu.csd.data.remote.ApiUtilsToken;
import com.nsu.csd.model.MeetingInfoDto;
import com.nsu.csd.presentation.eventList.EventListActivity;
import com.nsu.csd.presentation.meetList.MeetListActivity;
import com.nsu.csd.presentation.meetPeople.MeetPeopleActivity;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MonthMeetFragment extends Fragment {

    String token;
    String meeting_id;
    MeetingInfoDto meetingInfoDto;

    Button people;

    TextView meetTitle;
    TextView meetDate;

    TextView month;
    Button left;
    Button right;

    Button meet_smb;
    Button leave;

    TableRow tableRow1;
    TableRow tableRow2;
    TableRow tableRow3;
    TableRow tableRow4;
    TableRow tableRow5;
    TableRow tableRow6;

    private Date currentStartMonthDate;

    private View.OnClickListener onClickListener_meet_smb = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager manager = getFragmentManager();
            MyDialogFragment myDialogFragment = new MyDialogFragment(meetingInfoDto.getInviteKey());
            myDialogFragment.show(manager, "myDialog");
        }
    };

    private View.OnClickListener onClickListener_people = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Gson gson = new Gson();
            String str = gson.toJson(meetingInfoDto.getParticipants());

            Intent mainIntent = new Intent( getActivity(), MeetPeopleActivity.class);
            mainIntent.putExtra("people", str);
            mainIntent.putExtra("meet_id",meetingInfoDto.getId());
            getActivity().startActivity(mainIntent);
            getActivity().finish();
        }
    };

    private View.OnClickListener onClickListener_leave = v -> leaveMeet();

    private View.OnClickListener onClickListener_left = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String Start = meetingInfoDto.getStart();
            meetTitle.setText(meetingInfoDto.getTitle());

            String YStart = Start.split("-")[0];
            String MStart = Start.split("-")[1];
            Date date = stringToDate(YStart+"-"+MStart+"-"+"01");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentStartMonthDate);
            calendar.add(Calendar.MONTH, -1);
            Date nextDay = calendar.getTime();

           if(!isOlder(date,nextDay)){
               left.setEnabled(false);
               return;
           }

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;
            String newDate = dateFormat.format(nextDay);

            Intent mainIntent = new Intent(getActivity(), MonthMeetActivity.class);
            mainIntent.putExtra("date",newDate);
            mainIntent.putExtra("meeting_id",meeting_id);
            startActivity(mainIntent);
            getActivity().finish();
        }
    };

    private View.OnClickListener onClickListener_right = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Start = meetingInfoDto.getEnd();
            meetTitle.setText(meetingInfoDto.getTitle());

            String YStart = Start.split("-")[0];
            String MStart = Start.split("-")[1];
            Date date = stringToDate(YStart+"-"+MStart+"-"+"01");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentStartMonthDate);
            calendar.add(Calendar.MONTH, +1);
            Date nextDay = calendar.getTime();

            if(!isOlder(nextDay,date)){
                right.setEnabled(false);
                return;
            }

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;
            String newDate = dateFormat.format(nextDay);

            Intent mainIntent = new Intent(getActivity(), MonthMeetActivity.class);
            mainIntent.putExtra("date",newDate);
            mainIntent.putExtra("meeting_id",meeting_id);
            startActivity(mainIntent);
            getActivity().finish();
        }
    };

    private boolean checkRight(){
        String Start = meetingInfoDto.getEnd();

        String YStart = Start.split("-")[0];
        String MStart = Start.split("-")[1];
        Date date = stringToDate(YStart+"-"+MStart+"-"+"01");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentStartMonthDate);
        calendar.add(Calendar.MONTH, +1);
        Date nextDay = calendar.getTime();

        return isOlder(nextDay,date);
    }

    private boolean checkLeft(){
        String Start = meetingInfoDto.getStart();

        String YStart = Start.split("-")[0];
        String MStart = Start.split("-")[1];
        Date date = stringToDate(YStart+"-"+MStart+"-"+"01");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentStartMonthDate);
        calendar.add(Calendar.MONTH, -1);
        Date nextDay = calendar.getTime();

        return isOlder(date,nextDay);
    }

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

    private Date addMonth(Date d){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MONTH, +1);

        Date nextDay = calendar.getTime();
        return nextDay;
    }

    public static MonthMeetFragment newInstance() {
        Bundle args = new Bundle();
        MonthMeetFragment fragment = new MonthMeetFragment();
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
            meeting_id  = arguments.get("meeting_id").toString();
        }

        View view = inflater.inflate(R.layout.month_meet,container,false);

        people = view.findViewById(R.id.people);
        people.setOnClickListener(onClickListener_people);

        meetTitle = view.findViewById(R.id.meet_title);
        meetDate = view.findViewById(R.id.date_meet);

        month = view.findViewById(R.id.month);

        left = view.findViewById(R.id.left_btn);
        left.setOnClickListener(onClickListener_left);
        //left.setEnabled(false);
        right = view.findViewById(R.id.right_btn);
        right.setOnClickListener(onClickListener_right);
        //right.setEnabled(false);

        tableRow1 = view.findViewById(R.id._1_week);
        tableRow2 = view.findViewById(R.id._2_week);
        tableRow3 = view.findViewById(R.id._3_week);
        tableRow4 = view.findViewById(R.id._4_week);
        tableRow5 = view.findViewById(R.id._5_week);
        tableRow6 = view.findViewById(R.id._6_week);

        meet_smb = view.findViewById(R.id.meet_smb_btn);
        meet_smb.setOnClickListener(onClickListener_meet_smb);
        leave = view.findViewById(R.id.leave_btn);
        leave.setOnClickListener(onClickListener_leave);

        loadFullMonth();

        return view;
    }

    private void loadFullMonth(){
        ApiUtilsToken.getApiService(token).get_month_full(meeting_id).enqueue(
                new retrofit2.Callback<MeetingInfoDto>() {

                    final Handler mainHandler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<MeetingInfoDto> call, final retrofit2.Response<MeetingInfoDto> response) {
                        //TODO
                        mainHandler.post(() -> {
                            if (!response.isSuccessful()) {
                                showMessage(R.string.event_error);
                            } else {
                                meetingInfoDto = response.body();
                                fill();
                            }
                        });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<MeetingInfoDto> call, Throwable t) {
                        showMessage(R.string.connection_error);
                    }
                }
        );//enqueue() Для асинхронного получения
    }

    private void leaveMeet(){
        ApiUtilsToken.getApiService(token).leave_meet(meeting_id).enqueue(
                new retrofit2.Callback<Void>() {

                    final Handler mainHandler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<Void> call, final retrofit2.Response<Void> response) {
                        //TODO
                        mainHandler.post(() -> {
                            if (!response.isSuccessful()) {
                                showMessage(R.string.event_error);
                            } else {
                                Intent mainIntent = new Intent(getActivity(), MeetListActivity.class);
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

        String Start = meetingInfoDto.getStart();
        meetTitle.setText(meetingInfoDto.getTitle());

        String YStart;
        String MStart;
        String DStart;
        YStart = Start.split("-")[0];
        MStart = Start.split("-")[1];
        DStart = Start.split("-")[2];

        String End=meetingInfoDto.getEnd();
        String YEnd = End.split("-")[0];
        String MEnd = End.split("-")[1];
        String DEnd = End.split("-")[2];

        meetDate.setText(DStart+"."+MStart + "."+YStart +" - " + DEnd+"."+MEnd + "."+YEnd);
        month.setText(month(Integer.parseInt(MStart)));

        Date date = stringToDate(YStart+"-"+MStart+"-"+"01");
        Date dateE = stringToDate(YEnd+"-"+MEnd+"-"+"01");
        int sD = (date.getDay()==0)?7:date.getDay();

        YearMonth ym = YearMonth.of(Integer.parseInt(YStart),Integer.parseInt(MStart));
        int daysInMonth = ym.lengthOfMonth() ;
        List<MeetingInfoDto.DaysDTO> daysDTOList =  meetingInfoDto.getDays();
        int daysDTOListI = 0;


        Bundle arguments = getActivity().getIntent().getExtras();
        if(arguments!=null && arguments.containsKey("date")){

            currentStartMonthDate = stringToDate((String) arguments.get("date"));
            sD = (currentStartMonthDate.getDay()==0)?7:currentStartMonthDate.getDay();

            int mmm = currentStartMonthDate.getMonth()+1;
            int yyy = currentStartMonthDate.getYear()+1900;

            if (Integer.parseInt(MStart) != mmm ||
                Integer.parseInt(YStart) != yyy) {
                int sum = daysInMonth-Integer.parseInt(DStart);
                Date d = stringToDate(YStart+"-"+MStart+"-"+"01");
                d = addMonth(d);

                for (int i = d.getMonth()+1 ;  i>currentStartMonthDate.getMonth()+1 &&
                                               d.getYear()<=currentStartMonthDate.getYear()
                                                ||
                                                i<currentStartMonthDate.getMonth()+1 &&
                                                d.getYear()<=currentStartMonthDate.getYear()
                ;){

                    YearMonth ym1 = YearMonth.of(d.getYear(),d.getMonth());
                    int daysInMonth1 = ym1.lengthOfMonth() ;
                    sum+=daysInMonth1;
                    d = addMonth(d);
                }
                daysDTOListI = sum;
            }

        }
        else {
            currentStartMonthDate = date;
        }

        if( !checkLeft() ){
            left.setEnabled(false);
        }
        if(!checkRight()){
            right.setEnabled(false);
        }

        int startDay = Integer.parseInt(DStart);
        int endDay = Integer.parseInt(DEnd);
        int currentWeek = 0;
        int currentDay = -sD+2;

        int dim = daysInMonth;
        while (dim%7!=0){dim++;}

        for (int monthday = 0; currentDay <= dim; monthday++){

            if(monthday-sD+2<startDay && currentStartMonthDate.getMonth()==date.getMonth() ||
                currentDay>endDay && currentStartMonthDate.getMonth() ==dateE.getMonth() ||
                currentDay <=0 ||
                currentDay>daysInMonth
            ){
                final View dayV = LayoutInflater
                        .from(getContext())
                        .inflate(R.layout.day, getTableRow(currentWeek), false);
                TextView textView = dayV.findViewById(R.id.day1);
                textView.setBackgroundColor(color(""));
                textView.setText(currentDay <= 0 || currentDay>daysInMonth? "" : String.valueOf(currentDay));
                getTableRow(currentWeek).addView(dayV, monthday%7);
            }else{
                final View dayV = LayoutInflater
                        .from(getContext())
                        .inflate(R.layout.day, getTableRow(currentWeek), false);
                TextView textView = dayV.findViewById(R.id.day1);
                String str = daysDTOList.get(daysDTOListI).getBusyness();
                textView.setBackgroundColor(color(str));
                textView.setText(currentDay > 0 ? String.valueOf(currentDay) : "");
                getTableRow(currentWeek).addView(dayV, monthday%7);
                daysDTOListI++;
            }
            currentDay++;
            currentWeek = (int)(monthday+1)/7;
        }
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
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    public final String month(int i){
        switch (i){
            case 1: return "Январь";
            case 2: return "Февраль";
            case 3:return "Март";
            case 4:return "Апрель";
            case 5:return "Май";
            case 6:return "Июнь";
            case 7:return "Июль";
            case 8:return "Август";
            case 9:return "Сентабрь";
            case 10:return "Октябрь";
            case 11:return "Ноябрь";
            case 12:return "Декабрь";
            default: return "";
        }
    }

    public int color(String b){
        switch (b){
            case "HIGH": return Color.parseColor("#e66760");
            case "MEDIUM": return Color.parseColor("#ffff99");
            case "LOW": return Color.parseColor("#76ff7a");
            default:return Color.parseColor("#FFFFFFFF");
        }

    }

    public TableRow getTableRow(int week){
        switch (week){
            case 0:return tableRow1;
            case 1:return tableRow2;
            case 2:return tableRow3;
            case 3:return tableRow4;
            case 4:return tableRow5;
            case 5:return tableRow6;
            default:return tableRow1;
        }
    }

}
