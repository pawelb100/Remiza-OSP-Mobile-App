package edu.wseiz.remizaosp.listeners;

import java.util.List;

import edu.wseiz.remizaosp.models.Status;

public interface FetchStatusListListener {
    void onSuccess(List<Status> statuses);
    void onFailed();
}
