package edu.wseiz.remizaosp.utils;

import android.util.Pair;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import edu.wseiz.remizaosp.models.User;

public class UsersInStatusDiffUtilCallback extends DiffUtil.Callback {

    private List<User> oldList;
    private List<User> newList;

    public UsersInStatusDiffUtilCallback(List<User> oldList, List<User> newList) {
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
