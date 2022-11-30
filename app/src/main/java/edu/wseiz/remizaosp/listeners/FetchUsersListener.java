package edu.wseiz.remizaosp.listeners;

import java.util.List;

import edu.wseiz.remizaosp.models.User;

public interface FetchUsersListener {
    void onSuccess(List<User> users);
    void onFailed();
}
