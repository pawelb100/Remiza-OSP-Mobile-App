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

public class UsersInStatusListAdapter extends RecyclerView.Adapter<UsersInStatusListAdapter.ViewHolder>  {

    private final Context context;
    private final List<String> userNames;


    public UsersInStatusListAdapter(Context context, List<String> userNames) {
        this.context = context;
        this.userNames = userNames;
    }

    @Override
    public int getItemCount() {
        return this.userNames.size();
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

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        String currentItem = userNames.get(position);
        viewHolder.tvUserName.setText(currentItem);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserName;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.tvUserName = view.findViewById(R.id.tvUserName);
        }

    }

}



