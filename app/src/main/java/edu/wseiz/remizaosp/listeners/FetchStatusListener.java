package edu.wseiz.remizaosp.listeners;

import edu.wseiz.remizaosp.models.Status;

public interface FetchStatusListener {
    void onSuccess(Status status);
    void onFailed();
}
