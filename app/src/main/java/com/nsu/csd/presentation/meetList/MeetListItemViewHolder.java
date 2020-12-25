package com.nsu.csd.presentation.meetList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nsu.csd.R;
import com.nsu.csd.model.EventSummaryWithIdDTO;
import com.nsu.csd.model.MeetingSummaryDto;
import com.nsu.csd.presentation.detailedEvent.DetailedEventActivity;
import com.nsu.csd.presentation.monthMeet.MonthMeetActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeetListItemViewHolder extends RecyclerView.ViewHolder{

    private TextView time_event_range;
    private TextView titleTextView;
    private MeetingSummaryDto item;
    private FragmentActivity activity;
    private String token;

    public MeetListItemViewHolder(final View itemViewr, FragmentActivity a, String t ) {
        super(itemViewr);
        titleTextView = itemView.findViewById(R.id.title_of_meet);
        time_event_range = itemView.findViewById(R.id.time_of_meet);
        itemView.setOnClickListener(onItemClickListener());
        activity=a;
        token=t;
    }

    @SuppressLint("SetTextI18n")
    void onBind(final MeetingSummaryDto item) {
        this.item = item;
        titleTextView.setText(item.getTitle());

        String Start=item.getStart();
        String YStart = Start.split("-")[0];
        String MStart = Start.split("-")[1];
        String DStart = Start.split("-")[2];

        String End=item.getEnd();
        String YEnd = End.split("-")[0];
        String MEnd = End.split("-")[1];
        String DEnd = End.split("-")[2];

        time_event_range.setText(DStart+"."+MStart + "."+YStart +" - " + DEnd+"."+MEnd + "."+YEnd);
    }

    private View.OnClickListener onItemClickListener() {
        return view -> {
            if (null == item) {
                return;
            }
           //TODO

            Intent mainIntent = new Intent( activity, MonthMeetActivity.class);
            mainIntent.putExtra("meeting_id", item.getId());
            activity.startActivity(mainIntent);
            activity.finish();
        };
    }


    private Date stringToDate(String aDate, String aFormat) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
}
