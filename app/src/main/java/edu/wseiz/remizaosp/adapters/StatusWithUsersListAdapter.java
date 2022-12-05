package edu.wseiz.remizaosp.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.models.Status;
import edu.wseiz.remizaosp.models.User;

public class StatusWithUsersListAdapter extends RecyclerView.Adapter<StatusWithUsersListAdapter.ViewHolder>  {

    private final Context context;
    private final List<Pair<String, List<User>>> statusTitlesWithUsers;

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

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Pair<String, List<User>> currentItem = statusTitlesWithUsers.get(position);

        viewHolder.tvStatusName.setText(currentItem.first);

        viewHolder.rvStatusesWithUsers.setAdapter(new UsersInStatusListAdapter(context, currentItem.second));
        viewHolder.rvStatusesWithUsers.setLayoutManager(new LinearLayoutManager(context));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStatusName;
        private final RecyclerView rvStatusesWithUsers;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.tvStatusName = view.findViewById(R.id.tvStatusName);
            this.rvStatusesWithUsers = view.findViewById(R.id.rvStatusesWithUsers);
        }

    }

}



