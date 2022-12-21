package edu.wseiz.remizaosp.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.utils.StatusWithUsersDiffUtilCallback;

public class StatusWithUsersListAdapter extends RecyclerView.Adapter<StatusWithUsersListAdapter.ViewHolder>  {

    private final Context context;
    private List<Pair<String, List<User>>> statusTitlesWithUsers;

    public StatusWithUsersListAdapter(Context context, List<Pair<String, List<User>>> statusTitlesWithUsers) {
        this.context = context;
        this.statusTitlesWithUsers= statusTitlesWithUsers;
    }

    @Override
    public int getItemCount() {
        return this.statusTitlesWithUsers.size();
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
                        .inflate(R.layout.list_item_status_with_users, parent, false)
        );
    }

    public void updateData(List<Pair<String, List<User>>> list) {

        StatusWithUsersDiffUtilCallback diffUtilCallback = new StatusWithUsersDiffUtilCallback(statusTitlesWithUsers, list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);

        statusTitlesWithUsers = list;
        diffResult.dispatchUpdatesTo(this);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Pair<String, List<User>> currentItem = statusTitlesWithUsers.get(position);

        viewHolder.tvStatusName.setText(currentItem.first);

        if (viewHolder.adapter==null) {
            viewHolder.adapter = new UsersListAdapter(context, currentItem.second);
            viewHolder.rvStatusesWithUsers.setAdapter(viewHolder.adapter);
            viewHolder.rvStatusesWithUsers.setLayoutManager(new LinearLayoutManager(context));
        }
        else
            viewHolder.adapter.updateData(currentItem.second);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStatusName;
        private final RecyclerView rvStatusesWithUsers;
        private UsersListAdapter adapter = null;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.tvStatusName = view.findViewById(R.id.tvStatusName);
            this.rvStatusesWithUsers = view.findViewById(R.id.rvStatusesWithUsers);
        }

    }

}



