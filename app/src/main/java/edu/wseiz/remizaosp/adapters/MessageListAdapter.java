package edu.wseiz.remizaosp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.models.Message;
import edu.wseiz.remizaosp.utils.MessagesDiffUtilCallback;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder>  {

    private final Context context;
    private List<Message> messages;
    private final DateFormat dateFormat;

    private String userId;
    private OnItemListClick listener;

    public MessageListAdapter(Context context, List<Message> messages, String userId, OnItemListClick listener) {
        this.context = context;
        this.messages = messages;
        this.dateFormat = new SimpleDateFormat("HH:mm");
        this.userId = userId;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
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
                        .inflate(R.layout.list_item_message, parent, false)
        );
    }

    public String getLastMessageId() {
        return messages.get(0).getId();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Message currentItem = messages.get(position);

        if(currentItem.getUserId().equals(userId)) {

            viewHolder.rightCard.setVisibility(View.VISIBLE);
            viewHolder.leftCard.setVisibility(View.GONE);
            viewHolder.tvMyMessage.setText(currentItem.getText());
            viewHolder.tvMyTime.setText(dateFormat.format(new Date(currentItem.getTimestamp())));
        }
        else
        {
            viewHolder.leftCard.setVisibility(View.VISIBLE);
            viewHolder.rightCard.setVisibility(View.GONE);
            viewHolder.tvAuthor.setText(currentItem.getAuthor());
            viewHolder.tvMessage.setText(currentItem.getText());
            viewHolder.tvTime.setText(dateFormat.format(new Date(currentItem.getTimestamp())));

        }

        viewHolder.parentView.setOnClickListener(v -> listener.onItemClick(currentItem));
        viewHolder.parentView.setOnLongClickListener(v -> {
            listener.onItemLongClick(currentItem, v);
            return false;
        });
    }

    public void updateData(List<Message> newMessages) {

        MessagesDiffUtilCallback diffUtilCallback = new MessagesDiffUtilCallback(messages, newMessages);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);

        messages = newMessages;
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);

        holder.cleanup();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View parentView;

        private final MaterialCardView leftCard;
        private final MaterialCardView rightCard;

        private final TextView tvAuthor;
        private final TextView tvMessage;
        private final TextView tvTime;

        private final TextView tvMyMessage;
        private final TextView tvMyTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.leftCard = view.findViewById(R.id.left_card);
            this.rightCard = view.findViewById(R.id.right_card);
            this.tvAuthor = view.findViewById(R.id.author);
            this.tvMessage = view.findViewById(R.id.text);
            this.tvTime = view.findViewById(R.id.time);
            this.tvMyMessage = view.findViewById(R.id.mytext);
            this.tvMyTime = view.findViewById(R.id.mytime);

        }

        public void cleanup() {
            this.tvAuthor.setText("");
            this.tvMessage.setText("");
            this.tvTime.setText("");
            this.tvMyMessage.setText("");
            this.tvMyTime.setText("");
        }

    }


}



