package com.nsu.csd.presentation.eventList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nsu.csd.R;
import com.nsu.csd.model.EventSummaryWithIdDTO;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListItemViewHolder>{

    private List<EventSummaryWithIdDTO> items;
    FragmentActivity f;
    String t;

    public EventListAdapter(List<EventSummaryWithIdDTO> eventDTOS, FragmentActivity f1, String t1) {
        items = eventDTOS;
        f=f1;
        t=t1;
    }

    void replaceData(final List<EventSummaryWithIdDTO> items) {
        if (null != items) {
            this.items = items;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventListItemViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();

        final View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_list_event, parent, false);

        return new EventListItemViewHolder(view,f,t);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListItemViewHolder holder, final int position) {
        final EventSummaryWithIdDTO item = items.get(position);
        holder.onBind(item);
    }

    @Override
    public int getItemCount() {
        return (null != items) ? items.size() : 0;
    }

}
