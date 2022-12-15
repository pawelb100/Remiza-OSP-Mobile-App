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

import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Status;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.utils.EventsDiffUtilCallback;
import edu.wseiz.remizaosp.utils.UsersInStatusDiffUtilCallback;

public class UsersInStatusListAdapter extends RecyclerView.Adapter<UsersInStatusListAdapter.ViewHolder>  {

    private final Context context;
    private List<User> users;


    public UsersInStatusListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getItemCount() {
        return this.users.size();
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
                        .inflate(R.layout.list_item_users_in_status, parent, false)
        );
    }

    public void updateData(List<User> list) {

        UsersInStatusDiffUtilCallback diffUtilCallback = new UsersInStatusDiffUtilCallback(users, list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);

        users = list;
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        User currentItem = users.get(position);
        viewHolder.tvUserName.setText(currentItem.getName());

        switch (currentItem.getRole()) {
            case Rescuer:
                viewHolder.ivIcon.setImageResource(R.drawable.ic_baseline_person_24);
                break;
            case Driver:
                viewHolder.ivIcon.setImageResource(R.drawable.ic_baseline_drive_eta_24);
                break;
            case Officer:
                viewHolder.ivIcon.setImageResource(R.drawable.ic_baseline_star_24);
                break;
            default:

        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserName;
        private final ImageView ivIcon;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.ivIcon = view.findViewById(R.id.ivIcon);
            this.tvUserName = view.findViewById(R.id.tvUserName);
        }

    }

}



