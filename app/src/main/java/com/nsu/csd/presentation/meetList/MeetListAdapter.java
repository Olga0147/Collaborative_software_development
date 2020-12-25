package com.nsu.csd.presentation.meetList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nsu.csd.R;
import com.nsu.csd.model.MeetingSummaryDto;


import java.util.List;

public class MeetListAdapter extends RecyclerView.Adapter<MeetListItemViewHolder>{

    private List<MeetingSummaryDto> items;
    FragmentActivity f;
    String t;

    public MeetListAdapter(List<MeetingSummaryDto> meetDTOS, FragmentActivity f1, String t1) {
        items = meetDTOS;
        f=f1;
        t=t1;
    }

    void replaceData(final List<MeetingSummaryDto> items) {
        if (null != items) {
            this.items = items;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MeetListItemViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();

        final View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_list_meet, parent, false);

        return new MeetListItemViewHolder(view,f,t);
    }

    @Override
    public void onBindViewHolder(@NonNull final MeetListItemViewHolder holder, final int position) {
        final MeetingSummaryDto item = items.get(position);
        holder.onBind(item);
    }

    @Override
    public int getItemCount() {
        return (null != items) ? items.size() : 0;
    }
}
