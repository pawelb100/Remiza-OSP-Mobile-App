package edu.wseiz.remizaosp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Participation;
import edu.wseiz.remizaosp.utils.EventsDiffUtilCallback;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>  {

    private final Context context;
    private List<Event> eventList;
    private final OnItemListClick listener;
    private final DateFormat dateFormat;

    public EventListAdapter(Context context, List<Event> eventList, OnItemListClick listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
    }

    @Override
    public int getItemCount() {
        return this.eventList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.list_item_event, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Event currentItem = eventList.get(position);

        viewHolder.tvTitle.setText(currentItem.getTitle());
        viewHolder.tvStreet.setText(currentItem.getAddress().getStreet());
        viewHolder.tvRegion.setText(currentItem.getAddress().getRegion());

        if (currentItem.isOngoing())
            viewHolder.ivIcon.setBackgroundResource(R.drawable.ic_baseline_warning_amber_24);
        else
            viewHolder.ivIcon.setBackgroundResource(R.drawable.ic_baseline_calendar_today_24);

        int count = 0;
        if (currentItem.getParticipationList()!=null)
            for(Participation p : currentItem.getParticipationList())
                if (p.isParticipating())
                    count++;

        viewHolder.tvParticipationCount.setText(String.valueOf(count));
        viewHolder.tvTime.setText(dateFormat.format(new Date(currentItem.getTimestamp())));

        viewHolder.parentView.setOnClickListener(v -> {
            listener.onItemClick(currentItem);
        });

        viewHolder.parentView.setOnLongClickListener(v -> {
            listener.onItemLongClick(currentItem, v);
            return true;
        });
    }

    public void updateData(List<Event> events) {

        EventsDiffUtilCallback diffUtilCallback = new EventsDiffUtilCallback(eventList, events);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);

        eventList = events;
        diffResult.dispatchUpdatesTo(this);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivIcon;
        private final TextView tvTitle;
        private final TextView tvStreet;
        private final TextView tvRegion;
        private final TextView tvTime;
        private final TextView tvParticipationCount;
        private final View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.ivIcon = view.findViewById(R.id.ivWarningIcon);
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvStreet = view.findViewById(R.id.tvStreet);
            this.tvRegion = view.findViewById(R.id.tvRegion);
            this.tvTime = view.findViewById(R.id.tvTime);
            this.tvParticipationCount = view.findViewById(R.id.tvParticipantCount);
        }

    }

}



