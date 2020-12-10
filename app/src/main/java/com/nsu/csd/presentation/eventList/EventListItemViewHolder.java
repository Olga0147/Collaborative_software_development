package com.nsu.csd.presentation.eventList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nsu.csd.R;
import com.nsu.csd.model.EventSummaryWithIdDTO;
import com.nsu.csd.presentation.detailedEvent.DetailedEventActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventListItemViewHolder extends RecyclerView.ViewHolder{

        private TextView time_event_range;
        private TextView titleTextView;
        private EventSummaryWithIdDTO item;
        private FragmentActivity activity;
        private String token;

        public EventListItemViewHolder(final View itemViewr, FragmentActivity a, String t ) {
            super(itemViewr);
            titleTextView = itemView.findViewById(R.id.time_of_event);
            time_event_range = itemView.findViewById(R.id.title_of_event);
            itemView.setOnClickListener(onItemClickListener());
            activity=a;
            token=t;
        }

        @SuppressLint("SetTextI18n")
        void onBind(final EventSummaryWithIdDTO item) {
            this.item = item;
            titleTextView.setText(item.getTitle());

            String Start=item.getStart().getDateTime();
            String TimeStart =Start.split("T")[1];
            String HourStart = TimeStart.split(":")[0];
            String MinuteStart = TimeStart.split(":")[1];

            String End=item.getEnd().getDateTime();
            String TimeEnd = End.split("T")[1];
            String HourEnd = TimeEnd.split(":")[0];
            String MinuteEnd = TimeEnd.split(":")[1];

            time_event_range.setText(HourStart+":"+MinuteStart + " - " + HourEnd+":"+MinuteEnd);
        }

        private View.OnClickListener onItemClickListener() {
            return view -> {
                if (null == item) {
                    return;
                }
                Intent mainIntent = new Intent( activity, DetailedEventActivity.class);
                mainIntent.putExtra("event_id", item.getId());
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

