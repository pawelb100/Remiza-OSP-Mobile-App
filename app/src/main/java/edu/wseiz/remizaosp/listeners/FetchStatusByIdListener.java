package edu.wseiz.remizaosp.listeners;


import edu.wseiz.remizaosp.models.Status;

public interface FetchStatusByIdListener {
    void onSuccess(Status status);
    void onNoData();
    void onFailed();
}
