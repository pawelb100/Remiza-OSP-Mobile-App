package edu.wseiz.remizaosp.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import edu.wseiz.remizaosp.models.Event;

public class EventsDiffUtilCallback extends DiffUtil.Callback {

    private List<Event> oldList;
    private List<Event> newList;

    public EventsDiffUtilCallback(List<Event> oldList, List<Event> newList) {
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
