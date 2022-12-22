package edu.wseiz.remizaosp.listeners;

import edu.wseiz.remizaosp.models.User;

public interface FetchCurrentUserListener {
    void onSuccess(User user);
    void onFailed();
}
