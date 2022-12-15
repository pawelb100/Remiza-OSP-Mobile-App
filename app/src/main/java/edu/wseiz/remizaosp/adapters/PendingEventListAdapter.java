package edu.wseiz.remizaosp.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.interfaces.OnPendingEventClick;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Participation;
import edu.wseiz.remizaosp.utils.EventsDiffUtilCallback;

public class PendingEventListAdapter extends RecyclerView.Adapter<PendingEventListAdapter.ViewHolder>  {

    private final Context context;
    private List<Event> eventList;
    private final OnPendingEventClick listener;
    private final String userId;

    public PendingEventListAdapter(Context context, List<Event> eventList, String userId, OnPendingEventClick listener ) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
        this.userId = userId;
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
                        .inflate(R.layout.list_item_event_pending, parent, false)
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
            for(Participation p : currentItem.getParticipationList()) {

                if (p.isParticipating()) {
                    count++;
                }

                if(p.getUserId().equals(userId)) {
                    if (p.isParticipating()) {
                        viewHolder.ibReject.setBackgroundResource(R.drawable.ic_baseline_clear_24);
                        viewHolder.ibAccept.setBackgroundResource(R.drawable.ic_baseline_check_24_colored);
                        viewHolder.ibAccept.setClickable(false);
                        viewHolder.ibReject.setClickable(true);
                    }
                    else {
                        viewHolder.ibReject.setBackgroundResource(R.drawable.ic_baseline_clear_24_colored);
                        viewHolder.ibAccept.setBackgroundResource(R.drawable.ic_baseline_check_24);
                        viewHolder.ibAccept.setClickable(true);
                        viewHolder.ibReject.setClickable(false);
                    }

                }
            }

        viewHolder.tvCount.setText(String.valueOf(count));

        CharSequence charSequence = DateUtils.getRelativeTimeSpanString(currentItem.getTimestamp(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        viewHolder.tvTime.setText(charSequence.toString());

    }

    public void updateData(List<Event> events) {

        EventsDiffUtilCallback diffUtilCallback = new EventsDiffUtilCallback(eventList, events);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);

        eventList = events;
        diffResult.dispatchUpdatesTo(this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvStreet;
        private final TextView tvRegion;
        private final TextView tvTime;
        private final TextView tvCount;

        private final ImageButton ibAccept;
        private final ImageButton ibReject;

        public ViewHolder(@NonNull View view) {
            super(view);

            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvStreet = view.findViewById(R.id.tvStreet);
            this.tvRegion = view.findViewById(R.id.tvRegion);
            this.tvTime = view.findViewById(R.id.tvTime);
            this.tvCount = view.findViewById(R.id.tvCount);

            this.ibAccept = view.findViewById(R.id.ibAccept);
            this.ibReject = view.findViewById(R.id.ibReject);

            view.setOnClickListener(v -> listener.onClick(getAdapterPosition()));
            this.ibAccept.setOnClickListener(v -> listener.onAccept(getAdapterPosition()));
            this.ibReject.setOnClickListener(v -> listener.onReject(getAdapterPosition()));

        }

    }

}



