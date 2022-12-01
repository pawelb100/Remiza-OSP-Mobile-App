package edu.wseiz.remizaosp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.models.Status;

public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.ViewHolder>  {

    private final Context context;
    private final List<Status> statusList;
    private final String currentStatusId;
    private final OnItemListClick listener;

    public StatusListAdapter(Context context, List<Status> statusList, String currentStatusId, OnItemListClick listener) {
        this.context = context;
        this.statusList = statusList;
        this.currentStatusId = currentStatusId;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return this.statusList.size();
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
                        .inflate(R.layout.list_item_status, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Status currentItem = statusList.get(position);

        viewHolder.tvStatusName.setText(currentItem.getTitle());

        if (currentItem.getUid().equals(currentStatusId))
            viewHolder.tvIsCurrent.setText("Aktualny");

        viewHolder.parentView.setOnClickListener(v -> {
            listener.onItemClick(currentItem);
        });

        viewHolder.parentView.setOnLongClickListener(v -> {
            listener.onItemLongClick(currentItem, v);
            return true;
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStatusName;
        private final TextView tvIsCurrent;
        private final View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.tvStatusName = view.findViewById(R.id.tvStatusName);
            this.tvIsCurrent = view.findViewById(R.id.tvIsCurrent);
        }

    }

}



