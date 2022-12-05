package edu.wseiz.remizaosp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Participation;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>  {

    private final Context context;
    private final List<Event> eventList;
    private final OnItemListClick listener;
    private final DateFormat dateFormat;

    public EventListAdapter(Context context, List<Event> eventList, OnItemListClick listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvStreet;
        private final TextView tvRegion;
        private final TextView tvTime;
        private final TextView tvParticipationCount;
        private final View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvStreet = view.findViewById(R.id.tvStreet);
            this.tvRegion = view.findViewById(R.id.tvRegion);
            this.tvTime = view.findViewById(R.id.tvTime);
            this.tvParticipationCount = view.findViewById(R.id.tvParticipantCount);
        }

    }

}



