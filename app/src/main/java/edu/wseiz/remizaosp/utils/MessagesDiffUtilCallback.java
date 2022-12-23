package edu.wseiz.remizaosp.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Message;

public class MessagesDiffUtilCallback extends DiffUtil.Callback {

    private List<Message> oldList;
    private List<Message> newList;

    public MessagesDiffUtilCallback(List<Message> oldList, List<Message> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
