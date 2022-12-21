package edu.wseiz.remizaosp.listeners;

import java.util.List;

import edu.wseiz.remizaosp.models.User;

public interface FetchUsersByParticipationsListener {
    void onSuccess(List<User> accepted, List<User> rejected);
    void onFailed();
}
